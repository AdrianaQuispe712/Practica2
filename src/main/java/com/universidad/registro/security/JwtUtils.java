package com.universidad.registro.security;


import io.jsonwebtoken.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import io.jsonwebtoken.security.SignatureException;


import java.util.Date;


@Component
public class JwtUtils {
    private static final Logger logger = LoggerFactory.getLogger(JwtUtils.class);

    // Se utiliza para firmar el JWT y verificar su integridad
    // Se inyecta desde el archivo de propiedades de la aplicación (application.properties o application.yml)
    // Se espera que sea una cadena secreta que solo el servidor conoce
    @Value("${app.jwtSecret}")
    private String jwtSecret;

    // Se utiliza para establecer la fecha de expiración del JWT
    // Se inyecta desde el archivo de propiedades de la aplicación (application.properties o application.yml)
    // Se espera que sea un número entero que representa la cantidad de milisegundos antes de que el token expire
    // Se utiliza para establecer la fecha de expiración del JWT
    @Value("${app.jwtExpirationMs}")
    private int jwtExpirationMs;

    // Este método se utiliza para extraer el token JWT del encabezado de autorización de la solicitud HTTP
    // El token JWT se espera que esté en el formato "Bearer <token>"
    // Si el encabezado de autorización no está presente o no tiene el formato correcto, se devuelve null
    public String generateJwtToken(Authentication authentication) {
        UserDetails userPrincipal = (UserDetails) authentication.getPrincipal();

        // Se utiliza para generar un nuevo token JWT utilizando la información del usuario autenticado
        // Se establece el sujeto del token como el nombre de usuario del usuario autenticado
        return Jwts.builder()
                .setSubject((userPrincipal.getUsername()))
                .setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime() + jwtExpirationMs))
                .signWith(new javax.crypto.spec.SecretKeySpec(jwtSecret.getBytes(), SignatureAlgorithm.HS512.getJcaName()), SignatureAlgorithm.HS512)
                .compact();
    }

    // Este método se utiliza para extraer el token JWT del encabezado de autorización de la solicitud HTTP
    // El token JWT se espera que esté en el formato "Bearer <token>"
    // Si el encabezado de autorización no está presente o no tiene el formato correcto, se devuelve null
    public String getUserNameFromJwtToken(String token) {
        return Jwts.parserBuilder().setSigningKey(jwtSecret.getBytes()).build().parseClaimsJws(token).getBody().getSubject();
    }

    
    public boolean validateJwtToken(String authToken) { // Este método se utiliza para validar el token JWT
        // Se utiliza para verificar la firma del token y asegurarse de que no haya sido modificado
        // Se verifica si el token ha expirado y si es válido
        // Si el token es válido, se devuelve true; de lo contrario, se devuelve false
        try {
            Jwts.parserBuilder().setSigningKey(jwtSecret.getBytes()).build().parseClaimsJws(authToken);
            return true;
        } catch (SignatureException e) {
            logger.error("Invalid JWT signature: {}", e.getMessage());
        } catch (MalformedJwtException e) {
            logger.error("Invalid JWT token: {}", e.getMessage());
        } catch (ExpiredJwtException e) {
            logger.error("JWT token is expired: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            logger.error("JWT token is unsupported: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            logger.error("JWT claims string is empty: {}", e.getMessage());
        }


        return false;
    }
}
