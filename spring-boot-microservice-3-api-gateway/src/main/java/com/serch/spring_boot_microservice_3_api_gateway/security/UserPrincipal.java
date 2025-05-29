package com.serch.spring_boot_microservice_3_api_gateway.security;

import com.serch.spring_boot_microservice_3_api_gateway.model.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Set;
// Clase que implementa UserDetails de Spring Security para representar un usuario autenticado
@Getter // Genera automáticamente los getters para todos los campos
@NoArgsConstructor // Genera un constructor sin argumentos
@AllArgsConstructor // Genera un constructor con todos los argumentos
@Builder // Permite usar el patrón Builder para crear instancias
public class UserPrincipal implements UserDetails {

    private Long id; // ID único del usuario
    private String username; // Nombre de usuario para autenticación
    transient private String password; // Contraseña (transient para no serializarla)
    transient private User user; // Referencia al objeto User completo (transient para no serializar)
    private Set<GrantedAuthority> authorities; // Roles/permisos del usuario

    // Implementación de UserDetails interface:

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // Devuelve los roles/permisos del usuario
        return authorities;
    }

    @Override
    public String getPassword() {
        // Devuelve la contraseña del usuario
        return password;
    }

    @Override
    public String getUsername() {
        // Devuelve el nombre de usuario
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        // Indica si la cuenta no ha expirado (siempre true en este caso)
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        // Indica si la cuenta no está bloqueada (siempre true en este caso)
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        // Indica si las credenciales no han expirado (siempre true en este caso)
        return true;
    }

    @Override
    public boolean isEnabled() {
        // Indica si la cuenta está habilitada (siempre true en este caso)
        return true;
    }
}