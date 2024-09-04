package com.quantumdragon.userservice.repository;

import com.quantumdragon.userservice.entity.User;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {
    boolean existsByEmail(String email);

    @EntityGraph(attributePaths = "roles")
    User findByEmail(String email);

    @Query("SELECT u FROM User u LEFT JOIN FETCH u.roles WHERE u.email = :email")
    User findByEmailWithRoles(@Param("email") String email);

    // @Query(value = "SELECT u.*, r.id as role_id, r.name as role_name " +
    // "FROM user_service.users u " +
    // "LEFT JOIN user_service.user_roles ur ON u.id = ur.user_id " +
    // "LEFT JOIN user_service.roles r ON ur.role_id = r.id " +
    // "WHERE u.email = :email", nativeQuery = true)
    // @EntityGraph(attributePaths = { "roles" })
    // User findUserByEmailWithRoles(@Param("email") String email);
}
