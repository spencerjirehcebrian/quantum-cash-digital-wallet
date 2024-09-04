package com.quantumdragon.userservice.mapper;

import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

import com.quantumdragon.userservice.dto.UserProfileResponseDto;
import com.quantumdragon.userservice.dto.UserRegistrationDto;
import com.quantumdragon.userservice.dto.UserResponseDto;
import com.quantumdragon.userservice.dto.UserResponseWithUserProfileDto;
import com.quantumdragon.userservice.dto.UserUpdateDto;
import com.quantumdragon.userservice.entity.User;
import com.quantumdragon.userservice.entity.UserProfile;
import com.quantumdragon.userservice.entity.Role;

import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "status", constant = "ACTIVE")
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "roles", ignore = true)
    @Mapping(target = "knowYourCustomer", ignore = true)
    @Mapping(target = "stripeCustomerId", ignore = true)
    @Mapping(target = "paymentMethods", ignore = true)
    User toUser(UserRegistrationDto userRegistrationDto);

    @Mapping(target = "user", ignore = true)
    @Mapping(target = "userId", ignore = true)
    @Mapping(target = "kycStatus", constant = "PENDING")
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    UserProfile toUserProfile(UserRegistrationDto userRegistrationDto);

    @Mapping(target = "roles", expression = "java(mapRoles(user.getRoles()))")
    UserResponseDto toUserResponseDto(User user);

    @Mapping(target = "roles", expression = "java(mapRoles(user.getRoles()))")
    @Mapping(target = "userProfile", expression = "java(mapUserProfile(userProfile))")
    @Mapping(target = "createdAt", source = "user.createdAt")
    @Mapping(target = "updatedAt", source = "user.updatedAt")
    UserResponseWithUserProfileDto toUserResponseWithUserProfileDto(User user, UserProfile userProfile);

    default UserProfileResponseDto mapUserProfile(UserProfile userProfile) {
        if (userProfile == null) {
            return null;
        }
        return new UserProfileResponseDto(
                userProfile.getAddressLine1(),
                userProfile.getAddressLine2(),
                userProfile.getCity(),
                userProfile.getState(),
                userProfile.getCountry(),
                userProfile.getPostalCode(),
                userProfile.getKycStatus());
    }

    UserProfileResponseDto toUserProfileResponseDto(UserProfile userProfile);

    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "email", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "passwordHash", ignore = true)
    @Mapping(target = "roles", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "knowYourCustomer", ignore = true)
    @Mapping(target = "paymentMethods", ignore = true)
    void updateUserFromDto(UserUpdateDto userUpdateDto, @MappingTarget User user);

    @AfterMapping
    default void handleNullFields(UserUpdateDto userUpdateDto, @MappingTarget User user) {
        if (userUpdateDto.stripeCustomerId() != null) {
            user.setStripeCustomerId(userUpdateDto.stripeCustomerId());
        }
        if (userUpdateDto.firstName() != null) {
            user.setFirstName(userUpdateDto.firstName());
        }
        if (userUpdateDto.lastName() != null) {
            user.setLastName(userUpdateDto.lastName());
        }
        if (userUpdateDto.dateOfBirth() != null) {
            user.setDateOfBirth(userUpdateDto.dateOfBirth());
        }
        if (userUpdateDto.phoneNumber() != null) {
            user.setPhoneNumber(userUpdateDto.phoneNumber());
        }
    }

    default Set<String> mapRoles(Set<Role> roles) {
        if (roles == null) {
            return null;
        }
        return roles.stream()
                .map(role -> role.getRoleName().name())
                .collect(Collectors.toSet());
    }
}
