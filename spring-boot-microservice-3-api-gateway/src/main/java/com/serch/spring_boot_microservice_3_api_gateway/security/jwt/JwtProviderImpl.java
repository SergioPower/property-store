package com.serch.spring_boot_microservice_3_api_gateway.security.jwt;

import com.serch.spring_boot_microservice_3_api_gateway.model.User;
import com.serch.spring_boot_microservice_3_api_gateway.security.UserPrincipal;
import com.serch.spring_boot_microservice_3_api_gateway.utils.SecurityUtils;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Arrays;
import java.util.Date;
import java.util.Set;
import java.util.stream.Collectors;
/**
 * Implementación del proveedor de JWT (JSON Web Tokens) para autenticación.
 * Se encarga de generar, validar y extraer información de tokens JWT.
 */
@Component
public class JwtProviderImpl implements JwtProvider {

    // Clave secreta para firmar los JWT (inyectada desde application.properties)
    @Value("${app.jwt.secret}")
    private String JWT_SECRET;

    // Tiempo de expiración del token en milisegundos (inyectado desde application.properties)
    @Value("${app.jwt.expiration-in-ms}")
    private Long JWT_EXPIRATION_IN_MS;

    /**
     * Genera un token JWT firmado para un usuario autenticado.
     * @param auth Objeto UserPrincipal con los detalles del usuario
     * @return String que representa el token JWT firmado
     */
    @Override
    public String generateToken(UserPrincipal auth) {
        // Convierte las autoridades/roles a string separado por comas
        String authorities = auth.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        // Convierte el secreto a clave de firma HMAC
        Key key = Keys.hmacShaKeyFor(JWT_SECRET.getBytes(StandardCharsets.UTF_8));

        // Construye el token JWT
        return Jwts.builder()
                .setSubject(auth.getUsername())  // Nombre de usuario como sujeto
                .claim("roles", authorities)    // Roles como claim personalizado
                .claim("userId", auth.getId())  // ID de usuario como claim
                .setExpiration(new Date(System.currentTimeMillis() + JWT_EXPIRATION_IN_MS))  // Fecha expiración
                .signWith(key, SignatureAlgorithm.HS512)  // Firma con algoritmo HS512
                .compact();  // Convierte a string
    }

    /**
     * Obtiene la autenticación a partir del token JWT en la request.
     * @param request HttpServletRequest que contiene el token
     * @return Objeto Authentication o null si no es válido
     */
    @Override
    public Authentication getAuthentication(HttpServletRequest request) {
        Claims claims = extractClaims(request);

        if (claims == null) {
            return null;
        }

        // Extrae información del token
        String username = claims.getSubject();
        Long userId = claims.get("userId", Long.class);

        // Convierte los roles a autoridades GrantedAuthority
        Set<GrantedAuthority> authorities = Arrays.stream(claims.get("roles").toString().split(","))
                .map(SecurityUtils::convertAuthority)
                .collect(Collectors.toSet());

        // Construye UserDetails con la información del token
        UserDetails userDetails = UserPrincipal.builder()
                .username(username)
                .authorities(authorities)
                .id(userId)
                .build();

        if (username == null) {
            return null;
        }

        // Retorna el objeto Authentication para Spring Security
        return new UsernamePasswordAuthenticationToken(userDetails, null, authorities);
    }
    @Override
    public String generateToken(User user){
        Key key = Keys.hmacShaKeyFor(JWT_SECRET.getBytes(StandardCharsets.UTF_8));

        return Jwts.builder()
                .setSubject(user.getUsername())
                .claim("roles", user.getRole())
                .claim("userId", user.getId())
                .setExpiration(new Date(System.currentTimeMillis() + JWT_EXPIRATION_IN_MS))
                .signWith(key, SignatureAlgorithm.HS512)
                .compact();
    }

    /**
     * Verifica si el token JWT en la request es válido.
     * @param request HttpServletRequest que contiene el token
     * @return true si el token es válido, false en caso contrario
     */
    @Override
    public boolean isTokenValid(HttpServletRequest request) {
        Claims claims = extractClaims(request);

        if (claims == null) {
            return false;
        }

        // Verifica si el token ha expirado
        if (claims.getExpiration().before(new Date())) {
            return false;
        }

        return true;
    }

    /**
     * Extrae los claims (información) del token JWT.
     * @param request HttpServletRequest que contiene el token
     * @return Objeto Claims o null si el token es inválido
     */
    private Claims extractClaims(HttpServletRequest request) {
        // Extrae el token de las cabeceras
        String token = SecurityUtils.extractAuthTokenFromRequest(request);

        if (token == null) {
            return null;
        }

        // Convierte el secreto a clave de firma
        Key key = Keys.hmacShaKeyFor(JWT_SECRET.getBytes(StandardCharsets.UTF_8));

        // Parsea y valida el token
        return Jwts.parserBuilder()
                .setSigningKey(key)  // Usa la clave para verificar la firma
                .build()
                .parseClaimsJws(token)  // Parsea el token firmado
                .getBody();  // Obtiene los claims (información contenida)
    }
}