package com.serch.spring_boot_microservice_3_api_gateway.security;

import com.serch.spring_boot_microservice_3_api_gateway.model.User;
import com.serch.spring_boot_microservice_3_api_gateway.service.UserService;
import com.serch.spring_boot_microservice_3_api_gateway.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service // Implementación personalizada de UserDetailsService que se utiliza para cargar detalles del usuario durante la autenticación
public class CustomUserDetailsService implements UserDetailsService {

    // Inyección del servicio de usuario para buscar información de usuarios
    @Autowired
    private UserService userService;

    // Método principal que carga un usuario por su nombre de usuario
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Busca el usuario en la base de datos a través del UserService
        User user = userService.findByUsername(username)
                // Si no se encuentra, lanza una excepción
                .orElseThrow(() -> new UsernameNotFoundException("El usuario no fue encontrado: "+username));

        // Convierte el rol del usuario en una autoridad (GrantedAuthority)
        Set<GrantedAuthority> authorities = Set.of(SecurityUtils.convertAuthority(user.getRole().name()));

        // NOTA: Actualmente el método retorna null, lo cual es incorrecto.
        // Debería retornar una implementación de UserDetails con los datos del usuario
        return UserPrincipal.builder()
                .user(user)
                .id(user.getId())
                .username(username)
                .password(user.getPassword())
                .authorities(authorities)
                .build();
    }
}