package com.quantumdragon.userservice.repository;

import com.quantumdragon.userservice.entity.UserProfile;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserProfileRepository extends JpaRepository<UserProfile, UUID> {
    List<UserProfile> findAllByUserIdIn(List<UUID> userIds);
}
