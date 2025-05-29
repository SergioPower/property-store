package com.serch.spring_boot_microservice_3_api_gateway.utils;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.util.StringUtils;
/**
 * Clase de utilidad para operaciones relacionadas con la seguridad.
 * Contiene métodos estáticos para trabajar con autoridades, roles y tokens JWT.
 */
public class SecurityUtils {

    // Prefijo estándar que Spring Security usa para los roles (ej. "ROLE_ADMIN")
    public static final String ROLE_PREFIX = "ROLE_";

    // Nombre del header HTTP que contiene el token de autorización
    public static final String AUTH_HEADER = "authorization";

    // Tipo de token utilizado (Bearer token)
    public static final String AUTH_TOKEN_TYPE = "Bearer";

    // Prefijo completo del token (ej. "Bearer eyJhbGciOi...")
    public static final String AUTH_TOKEN_PREFIX = AUTH_TOKEN_TYPE + " ";

    /**
     * Convierte un nombre de rol a una autoridad (GrantedAuthority) con el formato correcto.
     * Asegura que el rol tenga el prefijo requerido por Spring Security.
     *
     * @param role El nombre del rol a convertir (ej. "ADMIN", "USER")
     * @return SimpleGrantedAuthority con el rol formateado correctamente
     */
    public static SimpleGrantedAuthority convertAuthority(String role) {
        // Verifica si el rol ya tiene el prefijo, si no, lo agrega
        String formattedRole = role.startsWith(ROLE_PREFIX) ? role : ROLE_PREFIX + role;

        // Crea y retorna una nueva autoridad con el rol formateado
        return new SimpleGrantedAuthority(formattedRole);
    }

    /**
     * Extrae el token JWT de la cabecera Authorization de una petición HTTP.
     *
     * @param request El objeto HttpServletRequest de la petición entrante
     * @return El token JWT sin el prefijo "Bearer " o null si no es válido
     */
    public static String extractAuthTokenFromRequest(HttpServletRequest request) {
        // Obtiene el valor de la cabecera Authorization
        String bearerToken = request.getHeader(AUTH_HEADER);

        // Verifica que el token exista y tenga el formato correcto
        if (StringUtils.hasLength(bearerToken) && bearerToken.startsWith(AUTH_TOKEN_PREFIX)) {
            // Extrae el token eliminando el prefijo "Bearer " (7 caracteres)
            return bearerToken.substring(7);
        }

        // Retorna null si no hay token válido
        return null;
    }
}