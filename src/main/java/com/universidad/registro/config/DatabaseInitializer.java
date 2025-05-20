package com.universidad.registro.config;


import com.universidad.registro.model.Rol;
import com.universidad.registro.model.Usuario;
import com.universidad.registro.repository.RolRepository;
import com.universidad.registro.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;


import java.util.HashSet;
import java.util.Set;


@Component
// Esta clase se encarga de inicializar la base de datos con roles y un usuario administrador por defecto
// al iniciar la aplicación. Implementa CommandLineRunner para ejecutar código al inicio de la aplicación.
public class DatabaseInitializer implements CommandLineRunner {


    @Autowired
    private RolRepository rolRepository;
    
    @Autowired
    private UsuarioRepository usuarioRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;

    // Este método se ejecuta al iniciar la aplicación y se encarga de inicializar los roles y el usuario administrador
    // por defecto si no existen en la base de datos.
    @Override
    public void run(String... args) throws Exception {
        // Inicializar roles si no existen
        inicializarRoles();
        
        // Crear usuario administrador por defecto si no existe
        crearAdminPorDefecto();
    }

    // Método para inicializar los roles en la base de datos
    // Si no existen, se crean los roles: ROL_ADMIN, ROL_DOCENTE y ROL_ESTUDIANTE
    private void inicializarRoles() {
        if (rolRepository.count() == 0) {
            Rol rolAdmin = new Rol();
            rolAdmin.setNombre(Rol.NombreRol.ROL_ADMIN);
            rolRepository.save(rolAdmin);
            
            Rol rolDocente = new Rol();
            rolDocente.setNombre(Rol.NombreRol.ROL_DOCENTE);
            rolRepository.save(rolDocente);
            
            Rol rolEstudiante = new Rol();
            rolEstudiante.setNombre(Rol.NombreRol.ROL_ESTUDIANTE);
            rolRepository.save(rolEstudiante);
            
            System.out.println("Roles inicializados en la base de datos");
        }
    }
    
    // Método para crear un usuario administrador por defecto si no existe
    // Este usuario tendrá el username "admin", password "admin123" y el rol de administrador
    private void crearAdminPorDefecto() {
        if (!usuarioRepository.existsByUsername("admin")) {
            Usuario admin = new Usuario();
            admin.setUsername("admin");
            admin.setPassword(passwordEncoder.encode("admin123"));
            admin.setEmail("admin@universidad.com");
            admin.setNombre("Administrador");
            admin.setApellido("Sistema");
            admin.setActivo(true);
            
            Set<Rol> roles = new HashSet<>();
            Rol rolAdmin = rolRepository.findByNombre(Rol.NombreRol.ROL_ADMIN)
                    .orElseThrow(() -> new RuntimeException("Error: Rol no encontrado."));
            roles.add(rolAdmin);
            admin.setRoles(roles);
            
            usuarioRepository.save(admin);
            
            System.out.println("Usuario administrador creado: admin / admin123");
        }
    }
}
