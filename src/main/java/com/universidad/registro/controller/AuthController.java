package com.universidad.registro.controller;


import com.universidad.registro.dto.AuthDTO.JwtResponse;
import com.universidad.registro.dto.AuthDTO.LoginRequest;
import com.universidad.registro.dto.AuthDTO.MessageResponse;
import com.universidad.registro.dto.AuthDTO.SignupRequest;
import com.universidad.registro.model.Rol;
import com.universidad.registro.model.Usuario;
import com.universidad.registro.repository.RolRepository;
import com.universidad.registro.repository.UsuarioRepository;
import com.universidad.registro.security.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;


import javax.validation.Valid;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Controlador para manejar las operaciones de autenticación y registro de usuarios.
 * Proporciona endpoints para iniciar sesión, registrarse y obtener información de sesión.
 */
// Este controlador maneja las solicitudes relacionadas con la autenticación y el registro de usuarios en la aplicación.
@CrossOrigin(origins = "*", maxAge = 3600) // Permite solicitudes de cualquier origen (CORS)
@RestController
@RequestMapping("/api/auth") // Configura el controlador para manejar solicitudes HTTP en la ruta "/api/auth"
public class AuthController {
    
    // Inyecta el AuthenticationManager para manejar la autenticación de usuarios
    // Este componente es responsable de autenticar a los usuarios utilizando el nombre de usuario y la contraseña proporcionados.
    @Autowired
    AuthenticationManager authenticationManager;

    // Inyecta el repositorio de usuarios para acceder a los datos de los usuarios en la base de datos
    // Este repositorio proporciona métodos para buscar, guardar y verificar la existencia de usuarios en la base de datos.
    @Autowired
    UsuarioRepository usuarioRepository;

    // Inyecta el repositorio de roles para acceder a los datos de los roles en la base de datos
    // Este repositorio proporciona métodos para buscar roles por su nombre y verificar la existencia de roles en la base de datos.
    @Autowired
    RolRepository rolRepository;

    // Inyecta el PasswordEncoder para codificar las contraseñas de los usuarios
    // Este componente es responsable de codificar las contraseñas antes de almacenarlas en la base de datos,
    @Autowired
    PasswordEncoder encoder;

    // Inyecta el JwtUtils para manejar la generación y validación de tokens JWT
    // Este componente es responsable de crear y validar tokens JWT que se utilizan para autenticar a los usuarios en la aplicación.
    @Autowired
    JwtUtils jwtUtils;

    /**
     * Endpoint para autenticar a un usuario.
     * @param loginRequest Contiene el nombre de usuario y la contraseña del usuario.
     * @return Un objeto JwtResponse que contiene el token JWT y la información del usuario autenticado.
     */
    // Este endpoint maneja las solicitudes POST para autenticar a un usuario.
    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {

        // Verifica si el nombre de usuario y la contraseña son válidos
        // Si la autenticación es exitosa, se genera un token JWT y se devuelve al cliente.
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

        // Establece la autenticación en el contexto de seguridad de Spring
        // Esto permite que la información de autenticación esté disponible en el resto de la aplicación
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);
        
        // Obtiene los detalles del usuario autenticado
        // y sus roles (authorities) para incluirlos en la respuesta.
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();        
        List<String> roles = userDetails.getAuthorities().stream()
                .map(item -> item.getAuthority())
                .collect(Collectors.toList());
       
        // Busca el usuario en la base de datos utilizando el nombre de usuario
        // y lanza una excepción si no se encuentra.        
        Usuario usuario = usuarioRepository.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("Error: Usuario no encontrado."));

        // Devuelve una respuesta HTTP 200 OK con el token JWT y la información del usuario autenticado.
        // La respuesta incluye el ID del usuario, el nombre de usuario, el correo electrónico y los roles del usuario.
        return ResponseEntity.ok(new JwtResponse(jwt, 
                                                 usuario.getId(), 
                                                 userDetails.getUsername(), 
                                                 usuario.getEmail(), 
                                                 new HashSet<>(roles)));
    }

    /**
     * Endpoint para registrar un nuevo usuario.
     * @param signUpRequest Contiene la información del nuevo usuario a registrar.
     * @return Un objeto MessageResponse que indica el resultado del registro.
     */
    // Este endpoint maneja las solicitudes POST para registrar un nuevo usuario.
    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {
        if (usuarioRepository.existsByUsername(signUpRequest.getUsername())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: El nombre de usuario ya está en uso."));
        }

        // Verifica si el correo electrónico ya está en uso
        // Si el correo electrónico ya está registrado, devuelve un error.
        if (usuarioRepository.existsByEmail(signUpRequest.getEmail())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: El email ya está en uso."));
        }


        // Crear nuevo usuario
        Usuario usuario = new Usuario();
        usuario.setUsername(signUpRequest.getUsername());
        usuario.setEmail(signUpRequest.getEmail());
        usuario.setPassword(encoder.encode(signUpRequest.getPassword()));
        usuario.setNombre(signUpRequest.getNombre());
        usuario.setApellido(signUpRequest.getApellido());

        
        Set<String> strRoles = signUpRequest.getRoles();
        Set<Rol> roles = new HashSet<>();

        // Verifica si se han proporcionado roles en la solicitud de registro
        // Si no se proporcionan roles, asigna el rol de estudiante por defecto.
        if (strRoles == null) {
            Rol estudianteRol = rolRepository.findByNombre(Rol.NombreRol.ROL_ESTUDIANTE)
                    .orElseThrow(() -> new RuntimeException("Error: Rol no encontrado."));
            roles.add(estudianteRol);
        } else {
            strRoles.forEach(role -> {
                switch (role) {
                case "admin":
                    Rol adminRol = rolRepository.findByNombre(Rol.NombreRol.ROL_ADMIN)
                            .orElseThrow(() -> new RuntimeException("Error: Rol no encontrado."));
                    roles.add(adminRol);
                    break;
                case "docente":
                    Rol docRol = rolRepository.findByNombre(Rol.NombreRol.ROL_DOCENTE)
                            .orElseThrow(() -> new RuntimeException("Error: Rol no encontrado."));
                    roles.add(docRol);
                    break;
                default:
                    Rol estudianteRol = rolRepository.findByNombre(Rol.NombreRol.ROL_ESTUDIANTE)
                            .orElseThrow(() -> new RuntimeException("Error: Rol no encontrado."));
                    roles.add(estudianteRol);
                }
            });
        }


        usuario.setRoles(roles);
        usuarioRepository.save(usuario);


        return ResponseEntity.ok(new MessageResponse("Usuario registrado exitosamente!"));
    }
    
    /**
     * Endpoint para obtener información de la sesión del usuario autenticado.
     * @return Un objeto JwtResponse que contiene la información del usuario autenticado.
     */
    // Este endpoint maneja las solicitudes GET para obtener información de la sesión del usuario autenticado.  
    @GetMapping("/session-info")
    public ResponseEntity<?> getSessionInfo() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated() && !(auth.getPrincipal().equals("anonymousUser"))) {
            UserDetails userDetails = (UserDetails) auth.getPrincipal();
            Usuario usuario = usuarioRepository.findByUsername(userDetails.getUsername())
                    .orElseThrow(() -> new RuntimeException("Error: Usuario no encontrado."));
            
            // Obtiene los roles del usuario autenticado y los convierte a un conjunto de cadenas
            // para incluirlos en la respuesta.        
            Set<String> roles = userDetails.getAuthorities().stream()
                    .map(item -> item.getAuthority())
                    .collect(Collectors.toSet());
            
            // Devuelve una respuesta HTTP 200 OK con la información del usuario autenticado.
            // La respuesta incluye el ID del usuario, el nombre de usuario, el correo electrónico y los roles del usuario.
            return ResponseEntity.ok(new JwtResponse(
                null, // No se envía un nuevo token
                usuario.getId(),
                userDetails.getUsername(),
                usuario.getEmail(),
                roles
            ));
        }
        
        return ResponseEntity.ok(new MessageResponse("No hay sesión activa"));
    }
    
    /**
     * Endpoint para cerrar la sesión del usuario autenticado.
     * @return Un objeto MessageResponse que indica el resultado del cierre de sesión.
     */
    // Este endpoint maneja las solicitudes POST para cerrar la sesión del usuario autenticado.
    @PostMapping("/logout")
    public ResponseEntity<?> logoutUser() {
        SecurityContextHolder.clearContext();
        return ResponseEntity.ok(new MessageResponse("Sesión cerrada exitosamente!"));
    }
}
