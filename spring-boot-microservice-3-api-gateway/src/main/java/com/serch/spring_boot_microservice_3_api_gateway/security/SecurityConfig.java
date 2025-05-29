package com.serch.spring_boot_microservice_3_api_gateway.security;

import com.serch.spring_boot_microservice_3_api_gateway.model.Role;
import com.serch.spring_boot_microservice_3_api_gateway.security.jwt.JwtAuthorizationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    // Inyección del servicio personalizado de detalles de usuario
    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    // Inyección del codificador de contraseñas
    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * Configura y provee el AuthenticationManager como un bean de Spring.
     *
     * @param authenticationConfiguration Configuración de autenticación proporcionada por Spring
     * @return AuthenticationManager configurado
     * @throws Exception Si ocurre un error durante la configuración
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    /**
     * Configura la cadena de filtros de seguridad para la aplicación.
     *
     * @param http Configuración de seguridad HTTP
     * @return SecurityFilterChain configurado
     * @throws Exception Si ocurre un error durante la configuración
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        // Configuración del AuthenticationManager usando el UserDetailsService personalizado
        AuthenticationManagerBuilder auth = http.getSharedObject(AuthenticationManagerBuilder.class);
        auth.userDetailsService(customUserDetailsService) // Establece el servicio personalizado de usuarios
                .passwordEncoder(passwordEncoder);             // Establece el codificador de contraseñas
        AuthenticationManager authenticationManager = auth.build();

        // Configuración principal de seguridad
        http
                // Deshabilita CSRF (Cross-Site Request Forgery) - común en APIs REST stateless
                .csrf(csrf -> csrf.disable())
                // Configuración de autorización de peticiones
                .authorizeHttpRequests(authz -> authz
                        // Permite acceso público a los endpoints de autenticación
                        .requestMatchers("/api/authentication/sign-in", "/api/authentication/sign-up").permitAll()
                        .requestMatchers(HttpMethod.GET,"/gateway/inmueble").permitAll()
                        .requestMatchers("/gateway/inmueble/**").hasRole(Role.ADMIN.name())
                        // Todas las demás peticiones requieren autenticación
                        .anyRequest().authenticated()
                )
                // Establece el AuthenticationManager configurado
                .authenticationManager(authenticationManager)
                .addFilterBefore(jwtAuthorizationFilter(), UsernamePasswordAuthenticationFilter.class)
                // Configura la política de sesión como stateless (sin estado)
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                );

        return http.build();
    }

    @Bean
    public JwtAuthorizationFilter jwtAuthorizationFilter(){
        return new JwtAuthorizationFilter();
    }

}