package com.universidad.registro.config;

import com.universidad.registro.security.JwtAuthenticationEntryPoint;
import com.universidad.registro.security.JwtAuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.session.jdbc.config.annotation.web.http.EnableJdbcHttpSession;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
@EnableJdbcHttpSession
/** 
 * Esta clase configura la seguridad de la aplicación utilizando Spring Security.
 * Se encarga de definir las reglas de autorización y autenticación para los endpoints de la API.
 */
public class SecurityConfig {
    /**
     * Este bean se encarga de manejar las excepciones de autenticación no autorizada.
     * Se utiliza para devolver una respuesta adecuada cuando un usuario no autenticado intenta acceder a un recurso protegido.
     */
    private final JwtAuthenticationEntryPoint unauthorizedHandler;

    public SecurityConfig(JwtAuthenticationEntryPoint unauthorizedHandler) {
        this.unauthorizedHandler = unauthorizedHandler;
    }

    /**
     * Este bean se encarga de gestionar la autenticación de los usuarios.
     * Se utiliza para autenticar las credenciales de los usuarios al iniciar sesión.
     */        
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    /**
     * Este bean se encarga de codificar las contraseñas de los usuarios.
     * Se utiliza para almacenar las contraseñas de forma segura en la base de datos.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


    /**
     * Este bean se encarga de gestionar la autenticación mediante JWT (JSON Web Token).
     * Se utiliza para validar y procesar los tokens JWT en las solicitudes de los usuarios.
     */
    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter() {
        return new JwtAuthenticationFilter();
    }

    /**
     * Este bean se encarga de configurar la cadena de filtros de seguridad de Spring Security.
     * Se utiliza para definir las reglas de autorización y autenticación para los endpoints de la API.
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .cors(cors -> cors.disable())
            .csrf(csrf -> csrf.disable())
            .exceptionHandling(exception -> exception.authenticationEntryPoint(unauthorizedHandler))
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/api/auth/**", "/api/public/**", "/swagger-ui/**", "/v3/api-docs/**").permitAll()
                .requestMatchers("/api/admin/**").hasRole("ADMIN")
                .requestMatchers("/api/docentes/**").hasAnyRole("ADMIN", "DOCENTE")
                .requestMatchers("/api/estudiantes/**").hasAnyRole("ADMIN", "DOCENTE", "ESTUDIANTE")
                .anyRequest().authenticated()
            );

        // Usar el bean jwtAuthenticationFilter directamente
        http.addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
