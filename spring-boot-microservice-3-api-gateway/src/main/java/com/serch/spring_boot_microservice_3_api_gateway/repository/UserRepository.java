package com.serch.spring_boot_microservice_3_api_gateway.repository;

import com.serch.spring_boot_microservice_3_api_gateway.model.Role;
import com.serch.spring_boot_microservice_3_api_gateway.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;


// Interfaz que extiende JpaRepository para operaciones CRUD con la entidad User
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Consulta derivada del nombre del método
     * @param username Busca un usuario por su nombre de usuario (username)}
     * @return un Optional para manejar casos donde el usuario no existe
     */
    Optional<User> findByUsername(String username);

    // Consulta JPQL personalizada para actualizar el rol de un usuario
    @Modifying // Indica que esta consulta modificará datos (requiere transacción)
    @Query("update User set role = :role where username = :username") // JPQL update statement
    void updateUserRole(
            @Param("username") String username, // Parámetro para el nombre de usuario
            @Param("role") Role role // Parámetro para el nuevo rol
    );
}