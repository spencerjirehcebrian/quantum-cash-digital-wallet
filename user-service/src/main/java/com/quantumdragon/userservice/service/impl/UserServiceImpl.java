package com.quantumdragon.userservice.service.impl;

import com.quantumdragon.userservice.dto.*;
import com.quantumdragon.userservice.enums.KycStatus;
import com.quantumdragon.userservice.enums.RoleName;
import com.quantumdragon.userservice.enums.Status;
import com.quantumdragon.userservice.entity.BlacklistedToken;
import com.quantumdragon.userservice.entity.Role;
import com.quantumdragon.userservice.entity.User;
import com.quantumdragon.userservice.entity.UserProfile;
import com.quantumdragon.userservice.exception.*;
import com.quantumdragon.userservice.mapper.UserMapper;
import com.quantumdragon.userservice.repository.BlacklistedTokenRepository;
import com.quantumdragon.userservice.repository.RoleRepository;
import com.quantumdragon.userservice.repository.UserProfileRepository;
import com.quantumdragon.userservice.repository.UserRepository;
import com.quantumdragon.userservice.security.BearerTokenInterceptor;
import com.quantumdragon.userservice.security.CustomUserDetailsService;
import com.quantumdragon.userservice.security.JwtTokenUtil;
import com.quantumdragon.userservice.service.UserService;

import jakarta.transaction.Transactional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.kafka.common.errors.ResourceNotFoundException;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenUtil jwtTokenUtil;
    private final CustomUserDetailsService customUserDetailsService;
    private final BlacklistedTokenRepository blacklistedTokenRepository;
    private final UserMapper userMapper;
    private final RoleRepository roleRepository;
    private final UserProfileRepository userProfileRepository;
    private final RestTemplate restTemplate;
    private final BearerTokenInterceptor bearerTokenInterceptor;
    private final NotificationServiceImpl notificationService;

    @Value("${payment_gateway.service.url}")
    private String customerServiceUrl;

    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder,
            AuthenticationManager authenticationManager, JwtTokenUtil jwtTokenUtil,
            CustomUserDetailsService customUserDetailsService,
            BlacklistedTokenRepository blacklistedTokenRepository, UserMapper userMapper,
            RoleRepository roleRepository, UserProfileRepository userProfileRepository, RestTemplate restTemplate,
            BearerTokenInterceptor bearerTokenInterceptor, NotificationServiceImpl notificationService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtTokenUtil = jwtTokenUtil;
        this.customUserDetailsService = customUserDetailsService;
        this.blacklistedTokenRepository = blacklistedTokenRepository;
        this.userMapper = userMapper;
        this.roleRepository = roleRepository;
        this.userProfileRepository = userProfileRepository;
        this.restTemplate = restTemplate;
        this.bearerTokenInterceptor = bearerTokenInterceptor;
        this.notificationService = notificationService;
    }

    private static final Log logger = LogFactory.getLog(UserServiceImpl.class);

    @Override
    public UserResponseWithUserProfileDto save(UserRegistrationDto userDto) {
        if (userRepository.existsByEmail(userDto.email())) {
            throw new EmailAlreadyExistsException("Email already exists");
        }
        if (userDto.email() == null || userDto.passwordHash() == null) {
            throw new InvalidInputException("Email and password are required");
        }
        User user = userMapper.toUser(userDto);
        user.setPasswordHash(passwordEncoder.encode(userDto.passwordHash()));
        User savedUser = userRepository.save(user);
        logger.info("User registered: " + savedUser.getId());

        UserProfile savedUserProfile = userMapper.toUserProfile(userDto);
        savedUserProfile.setUser(savedUser);
        userProfileRepository.save(savedUserProfile);
        logger.info("User registered: " + savedUser.getEmail());

        Role userRole = new Role();
        userRole.setUserId(savedUser.getId());
        userRole.setRoleName(RoleName.ROLE_USER);
        roleRepository.save(userRole);
        logger.info("Assigned " + userRole.getRoleName() + " to user: " + savedUser.getEmail());

        Set<Role> roles = new HashSet<>();
        roles.add(userRole);
        savedUser.setRoles(roles);

        try {
            String notification = notificationService
                    .sendUserRegisterNotification(savedUser.getId().toString(), savedUser)
                    .block();
            logger.info("Notification sent: " + notification);
        } catch (Exception e) {
            logger.error("Error sending notification: " + e.getMessage());
        }
        return userMapper.toUserResponseWithUserProfileDto(savedUser, savedUserProfile);
    }

    @Override
    public UserResponseWithUserProfileDto saveAdmin(UserRegistrationDto userDto) {
        if (userRepository.existsByEmail(userDto.email())) {
            throw new EmailAlreadyExistsException("Email already exists");
        }
        if (userDto.email() == null || userDto.passwordHash() == null) {
            throw new InvalidInputException("Email and password are required");
        }
        User user = userMapper.toUser(userDto);
        user.setPasswordHash(passwordEncoder.encode(userDto.passwordHash()));
        User savedUser = userRepository.save(user);
        logger.info("User registered: " + savedUser.getId());

        UserProfile savedUserProfile = userMapper.toUserProfile(userDto);
        savedUserProfile.setUser(savedUser);
        userProfileRepository.save(savedUserProfile);
        logger.info("User registered: " + savedUser.getEmail());

        Role userRole = new Role();
        userRole.setUserId(savedUser.getId());
        userRole.setRoleName(RoleName.ROLE_ADMIN);
        roleRepository.save(userRole);
        logger.info("Assigned " + userRole.getRoleName() + " to user: " + savedUser.getEmail());

        Set<Role> roles = new HashSet<>();
        roles.add(userRole);
        savedUser.setRoles(roles);

        return userMapper.toUserResponseWithUserProfileDto(savedUser, savedUserProfile);
    }

    @Override
    public UserResponseDto update(UUID userId, UserUpdateDto userDto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        userMapper.updateUserFromDto(userDto, user);
        User updatedUser = userRepository.save(user);
        logger.info("User updated: " + updatedUser.getEmail());
        return userMapper.toUserResponseDto(updatedUser);
    }

    @Override
    public UserResponseWithUserProfileDto retrieveById(UUID id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        UserProfile userProfile = userProfileRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        logger.info("User retrieved: " + user.getEmail());
        return userMapper.toUserResponseWithUserProfileDto(user, userProfile);
    }

    @Override
    public String retrieveEmailById(UUID id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        logger.info("User retrieved: " + user.getEmail());
        return user.getEmail();
    }

    @Transactional
    @Override
    public List<UserResponseWithUserProfileDto> retrieveAll() {
        List<User> users = userRepository.findAll();
        List<UserProfile> profiles = userProfileRepository.findAllByUserIdIn(
                users.stream().map(User::getId).collect(Collectors.toList()));

        Map<UUID, UserProfile> profileMap = profiles.stream()
                .collect(Collectors.toMap(profile -> profile.getUser().getId(), profile -> profile));

        return users.stream()
                .map(user -> userMapper.toUserResponseWithUserProfileDto(user, profileMap.get(user.getId())))
                .collect(Collectors.toList());
    }

    @Override
    public void delete(UUID id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        if (user.getStatus() == Status.INACTIVE) {
            throw new IllegalArgumentException("User is already inactive");
        }
        user.setStatus(Status.INACTIVE);
        userRepository.save(user);
        logger.info("User set to INACTIVE with ID: " + id);
    }

    @Transactional
    @Override
    public AuthResponseDto authenticate(UserLoginDto loginDto) throws AuthenticationException {
        if (loginDto.email() == null || loginDto.password() == null) {
            throw new InvalidInputException("Email and password are required");
        }

        User user = userRepository.findByEmail(loginDto.email());
        if (user == null) {
            throw new ResourceNotFoundException("User not found");
        }

        authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(loginDto.email(), loginDto.password()));

        final UserDetails userDetails = customUserDetailsService.loadUserByUsername(loginDto.email());
        final String token = jwtTokenUtil.generateToken(userDetails.getUsername(), user.getId(), user.getRoles());
        final LocalDateTime expiresAt = jwtTokenUtil.getExpirationDateFromToken(token);
        List<Role> roles = roleRepository.findByUserId(user.getId());
        List<String> roleNames = roles.stream()
                .map(role -> role.getRoleName().name())
                .collect(Collectors.toList());

        logger.info("User logged in: " + user.getEmail());

        try {
            String notification = notificationService.sendUserLoginNotification(user.getId().toString(),
                    user.getEmail())
                    .block();
            logger.info("Notification sent: " + notification);
        } catch (Exception e) {
            logger.error("Error sending notification: " + e.getMessage());
        }

        return new AuthResponseDto(user.getId(), token, expiresAt, roleNames);
    }

    @Override
    public void logout(String token) {
        BlacklistedToken blacklistedToken = new BlacklistedToken();
        blacklistedToken.setToken(token);
        blacklistedTokenRepository.save(blacklistedToken);
        logger.info("User logged out and token blacklisted");
    }

    @Override
    public String updatePassword(UUID userId, String newPassword) {
        if (newPassword == null) {
            throw new InvalidInputException("Email is required");
        }
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        user.setPasswordHash(passwordEncoder.encode(newPassword));
        userRepository.save(user);
        logger.info("User password updated");
        return "User password updated";
    }

    @Override
    public UserResponseDto updateEmail(UUID userId, UserEmailUpdateDto newEmail) {

        if (newEmail.email() == null) {
            throw new InvalidInputException("Email is required");
        }

        if (userRepository.existsByEmail(newEmail.email())) {
            throw new EmailAlreadyExistsException("Email already exists");
        }
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        user.setEmail(newEmail.email());
        User updatedUser = userRepository.save(user);
        logger.info("User email updated from " + user.getEmail() + " to " + newEmail.email());
        return userMapper.toUserResponseDto(updatedUser);
    }

    @Override
    public Status checkUserStatus(UUID userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        logger.info("Status retrieved for user: " + user.getEmail());
        return user.getStatus();
    }

    @Override
    public ApproveKYCResponseDto approveKYC(UUID userId, String token) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        UserProfile userProfile = userProfileRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User Profile not found"));
        if (user.getStripeCustomerId() != null) {
            throw new ResourceNotFoundException("Stripe customer ID already exists");
        }
        userProfile.setKycStatus(KycStatus.APPROVED);
        userProfileRepository.save(userProfile);
        logger.info("KYC " + userProfile.getKycStatus() + " for user: " + userProfile.getUser().getEmail());

        bearerTokenInterceptor.setToken(token);

        restTemplate.getInterceptors().add(bearerTokenInterceptor);

        CreateCustomerRequest request = new CreateCustomerRequest();
        request.setEmail(user.getEmail());
        request.setName(user.getFirstName() + " " + user.getLastName());
        request.setPhone(user.getPhoneNumber());
        request.setAddressLine1(userProfile.getAddressLine1());
        request.setAddressLine2(userProfile.getAddressLine2());
        request.setCity(userProfile.getCity());
        request.setState(userProfile.getState());
        request.setPostalCode(userProfile.getPostalCode());
        request.setCountry(userProfile.getCountry());

        try {
            ResponseEntity<CreateCustomerResponse> response = restTemplate.postForEntity(
                    customerServiceUrl + "/api/payments/customer", request, CreateCustomerResponse.class);

            if (response.getStatusCode() == HttpStatus.OK) {
                logger.info("Customer created successfully: " + response.getBody());
                user.setStripeCustomerId(response.getBody().getCustomerId());
                User updatedUser = userRepository.save(user);
                logger.info("User stripe customer id updated: " + updatedUser.getStripeCustomerId());

                try {
                    String notification = notificationService
                            .sendUserKYCApprovedNotification(updatedUser.getId().toString(),
                                    updatedUser)
                            .block();
                    logger.info("Notification sent: " + notification);
                } catch (Exception e) {
                    logger.error("Error sending notification: " + e.getMessage());
                }

                return new ApproveKYCResponseDto(response.getBody().getCustomerId(), updatedUser.getStripeCustomerId());
            } else {
                logger.error("Failed to create customer. Code: " + response.getStatusCode());
                throw new RuntimeException("Failed to create customer. Status code: " + response.getStatusCode());
            }
        } catch (RestClientException e) {
            logger.error("Exception occurred while creating customer", e);
            throw new RuntimeException("Failed to create customer", e);
        }
    }

    @Override
    public void rejectKYC(UUID userId) {
        UserProfile userProfile = userProfileRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        userProfile.setKycStatus(KycStatus.REJECTED);
        userProfileRepository.save(userProfile);
        logger.info("KYC " + userProfile.getKycStatus() + " for user: " + userProfile.getUser().getEmail());
    }
}
