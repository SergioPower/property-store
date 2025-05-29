package com.serch.spring_boot_microservice_3_api_gateway.service;

import com.serch.spring_boot_microservice_3_api_gateway.model.Role;
import com.serch.spring_boot_microservice_3_api_gateway.model.User;

import java.util.Optional;
/**
 * Interfaz que define el contrato para el servicio de gestión de usuarios.
 * Contiene las operaciones básicas para el manejo de usuarios en el sistema.
 */
public interface UserService {

    /**
     * Guarda un nuevo usuario en el sistema.
     * @param user El objeto User a ser guardado
     * @return El usuario guardado con los datos actualizados (incluyendo ID generado)
     */
    User saveUser(User user);

    /**
     * Busca un usuario por su nombre de usuario.
     * @param username El nombre de usuario a buscar
     * @return Un Optional que contiene el usuario si es encontrado, o vacío si no existe
     */
    Optional<User> findByUsername(String username);

    /**
     * Cambia el rol de un usuario existente.
     * @param newRole El nuevo rol a asignar (debe ser un valor del enum Role)
     * @param username El nombre de usuario del usuario a modificar
     */
    void changeRole(Role newRole, String username);
}