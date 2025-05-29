package com.serch.spring_boot_microservice_3_api_gateway.service;

import com.serch.spring_boot_microservice_3_api_gateway.model.Role;
import com.serch.spring_boot_microservice_3_api_gateway.model.User;
import com.serch.spring_boot_microservice_3_api_gateway.repository.UserRepository;
import com.serch.spring_boot_microservice_3_api_gateway.security.jwt.JwtProvider;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
@Service // Indica que esta clase es un componente de servicio de Spring
public class UserServiceImpl implements UserService {

    @Autowired // Inyección automática del repositorio de usuarios
    private UserRepository userRepository;

    @Autowired // Inyección automática del codificador de contraseñas
    private PasswordEncoder passwordEncoder;

    @Autowired // Inyección para generar token
    private JwtProvider jwtProvider;

    @Override
    public User saveUser(User user){
        // Codifica la contraseña antes de guardarla
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        // Asigna el rol USER por defecto al nuevo usuario
        user.setRole(Role.USER);
        // Establece la fecha y hora actual como fecha de creación
        user.setFechaCreacion(LocalDateTime.now());
        // Crea un usuario con sus propiedades
        User userCreated = userRepository.save(user);

        String jwt = jwtProvider.generateToken(userCreated);
        userCreated.setToken(jwt);

        return userCreated;
    }

    @Override
    public Optional<User> findByUsername(String username){
        // Busca un usuario por su nombre de usuario
        return userRepository.findByUsername(username);
    }

    @Transactional // Asegura que la operación se ejecute dentro de una transacción
    @Override
    public void changeRole(Role newRole, String username){
        // Actualiza el rol del usuario en la base de datos
        userRepository.updateUserRole(username, newRole);
    }
}