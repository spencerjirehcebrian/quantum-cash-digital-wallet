package com.quantumdragon.userservice.service;

import com.quantumdragon.userservice.dto.*;
import com.quantumdragon.userservice.enums.Status;
import jakarta.transaction.Transactional;
import org.springframework.security.core.AuthenticationException;

import java.util.List;
import java.util.UUID;

public interface UserService {

    UserResponseWithUserProfileDto save(UserRegistrationDto userDto);

    UserResponseWithUserProfileDto saveAdmin(UserRegistrationDto userDto);

    UserResponseDto update(UUID userId, UserUpdateDto userDto);

    UserResponseWithUserProfileDto retrieveById(UUID id);

    String retrieveEmailById(UUID id);

    @Transactional
    List<UserResponseWithUserProfileDto> retrieveAll();

    void delete(UUID id);

    @Transactional
    AuthResponseDto authenticate(UserLoginDto loginDto) throws AuthenticationException;

    void logout(String token);

    String updatePassword(UUID userId, String newPassword);

    UserResponseDto updateEmail(UUID userId, UserEmailUpdateDto newEmail);

    Status checkUserStatus(UUID userId);

    ApproveKYCResponseDto approveKYC(UUID userId, String token);

    void rejectKYC(UUID userId);
}
