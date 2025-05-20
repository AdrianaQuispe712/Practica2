package com.universidad.registro.security;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;


import java.io.IOException;


@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {


    private static final Logger logger = LoggerFactory.getLogger(JwtAuthenticationEntryPoint.class);

    // Este método se invoca cuando un usuario no autenticado intenta acceder a un recurso protegido
    // y se lanza una excepción de autenticación.
    @Override
    public void commence(jakarta.servlet.http.HttpServletRequest request,
            jakarta.servlet.http.HttpServletResponse response, AuthenticationException authException)
            throws IOException, jakarta.servlet.ServletException {
        logger.error("Error de autenticación: {}", authException.getMessage());
        response.sendError(jakarta.servlet.http.HttpServletResponse.SC_UNAUTHORIZED, "Error: No autorizado");
    }
}
