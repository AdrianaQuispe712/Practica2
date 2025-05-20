package com.universidad.registro.security;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;


import com.universidad.registro.service.UserDetailsServiceImpl;


import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

// Este filtro se encarga de interceptar las solicitudes HTTP y verificar si contienen un token JWT válido en el encabezado de autorización.
// Si el token es válido, se establece la autenticación del usuario en el contexto de seguridad de Spring.
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    // Inyecta las dependencias necesarias para la autenticación JWT
    @Autowired
    private JwtUtils jwtUtils;

    // Inyecta el servicio de detalles del usuario para cargar la información del usuario
    // a partir del nombre de usuario extraído del token JWT
    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    // Logger para registrar información y errores
    // Utiliza SLF4J para registrar mensajes de información y errors
    // en la consola o en un archivo de registro, según la configuración del logger
    private static final Logger logger = LoggerFactory.getLogger(JwtAuthenticationFilter.class);

    // Método que se ejecuta para cada solicitud HTTP
    // Este método se encarga de verificar el token JWT y establecer la autenticación del usuario en el contexto de seguridad
    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain)
            throws ServletException, IOException {
        try {
            // Extrae el token JWT del encabezado de autorización de la solicitud HTTP
            // y lo valida utilizando el método parseJwt
            String jwt = parseJwt(request);
            if (jwt != null && jwtUtils.validateJwtToken(jwt)) {
                String username = jwtUtils.getUserNameFromJwtToken(jwt);

                // Si el token es válido, carga los detalles del usuario utilizando el nombre de usuario extraído del token
                // y establece la autenticación del usuario en el contexto de seguridad de Spring
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                // Establece la autenticación en el contexto de seguridad de Spring
                // para que esté disponible en el resto de la aplicación
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        } catch (Exception e) {
            logger.error("No se puede establecer la autenticación del usuario: {}", e);
        }

        // Continúa con la cadena de filtros para que la solicitud sea procesada por el siguiente filtro o controlador
        // en la cadena de filtros de Spring Security
        filterChain.doFilter(request, response);
    }

    // Método para extraer el token JWT del encabezado de autorización de la solicitud HTTP
    // Verifica si el encabezado tiene el prefijo "Bearer " y extrae el token
    private String parseJwt(HttpServletRequest request) { 
        // Obtener el encabezado de autorización de la solicitud HTTP
        String headerAuth = request.getHeader("Authorization"); // Obtener el encabezado de autorización

        // Verificar si el encabezado no está vacío y si comienza con el prefijo "Bearer "
        // Si el encabezado tiene el prefijo "Bearer ", extraer el token JWT del encabezado
        if (StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer ")) { 
            return headerAuth.substring(7); 
        }


        return null;
    }
}
