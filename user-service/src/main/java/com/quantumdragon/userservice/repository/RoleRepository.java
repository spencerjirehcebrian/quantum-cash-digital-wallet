package com.quantumdragon.userservice.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.quantumdragon.userservice.entity.Role;
import com.quantumdragon.userservice.enums.RoleName;

public interface RoleRepository extends JpaRepository<Role, UUID> {
    Optional<Role> findByRoleName(RoleName roleName);

    List<Role> findByUserId(UUID userId);
}
