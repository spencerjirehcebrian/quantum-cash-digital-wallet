// package com.quantumdragon.userservice.service.impl;

// import com.quantumdragon.userservice.dto.*;
// import com.quantumdragon.userservice.entity.BlacklistedToken;
// import com.quantumdragon.userservice.entity.User;
// import com.quantumdragon.userservice.enums.Status;
// import com.quantumdragon.userservice.exception.EmailAlreadyExistsException;
// import com.quantumdragon.userservice.repository.BlacklistedTokenRepository;
// import com.quantumdragon.userservice.repository.UserRepository;
// import com.quantumdragon.userservice.security.CustomUserDetailsService;
// import com.quantumdragon.userservice.security.JwtTokenUtil;
// import org.junit.jupiter.api.BeforeEach;
// import org.junit.jupiter.api.Test;
// import org.mockito.InjectMocks;
// import org.mockito.Mock;
// import org.mockito.MockitoAnnotations;
// import org.springframework.security.authentication.AuthenticationManager;
// import org.springframework.security.core.AuthenticationException;
// import org.springframework.security.core.userdetails.UserDetails;
// import org.springframework.security.crypto.password.PasswordEncoder;

// import java.time.LocalDate;
// import java.time.LocalDateTime;
// import java.time.ZonedDateTime;
// import java.util.Arrays;
// import java.util.List;
// import java.util.Optional;
// import java.util.UUID;

// import static org.junit.jupiter.api.Assertions.*;
// import static org.mockito.Mockito.*;

// class UserServiceImplTest {

// @Mock
// private UserRepository userRepository;

// @Mock
// private PasswordEncoder passwordEncoder;

// @Mock
// private AuthenticationManager authenticationManager;

// @Mock
// private JwtTokenUtil jwtTokenUtil;

// @Mock
// private CustomUserDetailsService customUserDetailsService;

// @Mock
// private BlacklistedTokenRepository blacklistedTokenRepository;

// @InjectMocks
// private UserServiceImpl userService;

// @BeforeEach
// void setUp() {
// MockitoAnnotations.openMocks(this);
// }

// @Test
// void testSave() {
// UserRegistrationDto registrationDto = new UserRegistrationDto("John", "Doe",
// "john@example.com", "password",
// LocalDate.of(1990, 1, 1),
// "1234567890");
// User savedUser = new User();
// savedUser.setId(UUID.randomUUID());
// savedUser.setFirstName("John");
// savedUser.setLastName("Doe");
// savedUser.setEmail("john@example.com");
// savedUser.setPhoneNumber("1234567890");
// savedUser.setStatus(Status.ACTIVE);
// savedUser.setCreatedAt(ZonedDateTime.now());
// savedUser.setUpdatedAt(ZonedDateTime.now());

// when(userRepository.existsByEmail(registrationDto.email())).thenReturn(false);
// when(passwordEncoder.encode(registrationDto.password())).thenReturn("encodedPassword");
// when(userRepository.save(any(User.class))).thenReturn(savedUser);

// UserResponseDto result = userService.save(registrationDto);

// assertNotNull(result);
// assertEquals(savedUser.getId(), result.id());
// assertEquals(savedUser.getFirstName(), result.firstName());
// assertEquals(savedUser.getLastName(), result.lastName());
// assertEquals(savedUser.getEmail(), result.email());
// assertEquals(savedUser.getPhoneNumber(), result.phoneNumber());
// assertEquals(savedUser.getStatus(), result.status());

// verify(userRepository).existsByEmail(registrationDto.email());
// verify(passwordEncoder).encode(registrationDto.password());
// verify(userRepository).save(any(User.class));
// }

// @Test
// void testSaveEmailAlreadyExists() {
// UserRegistrationDto registrationDto = new UserRegistrationDto("John", "Doe",
// "john@example.com", "password",
// LocalDate.of(1990, 1, 1),
// "1234567890");

// when(userRepository.existsByEmail(registrationDto.email())).thenReturn(true);

// assertThrows(EmailAlreadyExistsException.class, () ->
// userService.save(registrationDto));

// verify(userRepository).existsByEmail(registrationDto.email());
// verify(userRepository, never()).save(any(User.class));
// }

// @Test
// void testUpdate() {
// UUID userId = UUID.randomUUID();
// UserUpdateDto updateDto = new UserUpdateDto("John", "Doe", LocalDate.of(1990,
// 1, 1),
// "1234567890");
// User existingUser = new User();
// existingUser.setId(userId);

// when(userRepository.findById(userId)).thenReturn(Optional.of(existingUser));
// when(userRepository.save(any(User.class))).thenReturn(existingUser);

// UserResponseDto result = userService.update(userId, updateDto);

// assertNotNull(result);
// assertEquals(existingUser.getId(), result.id());
// assertEquals(updateDto.firstName(), result.firstName());
// assertEquals(updateDto.lastName(), result.lastName());
// assertEquals(updateDto.phoneNumber(), result.phoneNumber());

// verify(userRepository).findById(userId);
// verify(userRepository).save(any(User.class));
// }

// @Test
// void testRetrieveById() {
// UUID userId = UUID.randomUUID();
// User user = new User();
// user.setId(userId);
// user.setFirstName("John");
// user.setLastName("Doe");
// user.setEmail("john@example.com");
// user.setPhoneNumber("1234567890");
// user.setStatus(Status.ACTIVE);
// user.setCreatedAt(ZonedDateTime.now());
// user.setUpdatedAt(ZonedDateTime.now());

// when(userRepository.findById(userId)).thenReturn(Optional.of(user));

// UserResponseDto result = userService.retrieveById(userId);

// assertNotNull(result);
// assertEquals(user.getId(), result.id());
// assertEquals(user.getFirstName(), result.firstName());
// assertEquals(user.getLastName(), result.lastName());
// assertEquals(user.getEmail(), result.email());
// assertEquals(user.getPhoneNumber(), result.phoneNumber());
// assertEquals(user.getStatus(), result.status());

// verify(userRepository).findById(userId);
// }

// @Test
// void testRetrieveAll() {
// List<User> users = Arrays.asList(
// new User(UUID.randomUUID(), "john@example.com", "password", "John", "Doe",
// LocalDate.of(1990, 1, 1),
// "1234567890", Status.ACTIVE, ZonedDateTime.now(), ZonedDateTime.now()),
// new User(UUID.randomUUID(), "jane@example.com", "password", "Jane", "Doe",
// LocalDate.of(1991, 2, 2),
// "0987654321", Status.ACTIVE, ZonedDateTime.now(), ZonedDateTime.now()));

// when(userRepository.findAll()).thenReturn(users);

// List<UserResponseDto> result = userService.retrieveAll();

// assertNotNull(result);
// assertEquals(2, result.size());
// assertEquals(users.get(0).getId(), result.get(0).id());
// assertEquals(users.get(1).getId(), result.get(1).id());

// verify(userRepository).findAll();
// }

// @Test
// void testDelete() {
// UUID userId = UUID.randomUUID();
// User user = new User();
// user.setId(userId);
// user.setStatus(Status.ACTIVE);

// when(userRepository.findById(userId)).thenReturn(Optional.of(user));
// when(userRepository.save(any(User.class))).thenReturn(user);

// userService.delete(userId);

// assertEquals(Status.INACTIVE, user.getStatus());
// verify(userRepository).findById(userId);
// verify(userRepository).save(user);
// }

// @Test
// void testAuthenticate() throws AuthenticationException {
// UserLoginDto loginDto = new UserLoginDto("john@example.com", "password");
// User user = new User();
// user.setId(UUID.randomUUID());
// user.setEmail("john@example.com");

// UserDetails userDetails = mock(UserDetails.class);
// when(customUserDetailsService.loadUserByUsername(loginDto.email())).thenReturn(userDetails);
// when(jwtTokenUtil.generateToken(userDetails.getUsername())).thenReturn("token");
// when(jwtTokenUtil.getExpirationDateFromToken("token")).thenReturn(LocalDateTime.now().plusHours(1));
// when(userRepository.findByEmail(loginDto.email())).thenReturn(user);

// AuthResponseDto result = userService.authenticate(loginDto);

// assertNotNull(result);
// assertEquals(user.getId(), result.userId());
// assertEquals("token", result.token());
// assertNotNull(result.expiresAt());

// verify(authenticationManager).authenticate(any());
// verify(customUserDetailsService).loadUserByUsername(loginDto.email());
// verify(jwtTokenUtil).generateToken(userDetails.getUsername());
// verify(jwtTokenUtil).getExpirationDateFromToken("token");
// verify(userRepository).findByEmail(loginDto.email());
// }

// @Test
// void testLogout() {
// String token = "token";

// userService.logout(token);

// verify(blacklistedTokenRepository).save(any(BlacklistedToken.class));
// }

// @Test
// void testUpdatePassword() {
// UUID userId = UUID.randomUUID();
// String newPassword = "newPassword";
// User user = new User();
// user.setId(userId);

// when(userRepository.findById(userId)).thenReturn(Optional.of(user));
// when(passwordEncoder.encode(newPassword)).thenReturn("encodedPassword");
// when(userRepository.save(any(User.class))).thenReturn(user);

// userService.updatePassword(userId, newPassword);

// assertEquals("encodedPassword", user.getPasswordHash());

// verify(userRepository).findById(userId);
// verify(passwordEncoder).encode(newPassword);
// verify(userRepository).save(user);
// }

// @Test
// void testCheckUserStatus() {
// UUID userId = UUID.randomUUID();
// User user = new User();
// user.setId(userId);
// user.setStatus(Status.ACTIVE);

// when(userRepository.findById(userId)).thenReturn(Optional.of(user));

// Status result = userService.checkUserStatus(userId);

// assertEquals(Status.ACTIVE, result);

// verify(userRepository).findById(userId);
// }

// @Test
// void testSaveUserNotFound() {
// UUID userId = UUID.randomUUID();
// UserUpdateDto updateDto = new UserUpdateDto("John", "Doe", LocalDate.of(1990,
// 1, 1),
// "1234567890");

// when(userRepository.findById(userId)).thenReturn(Optional.empty());

// assertThrows(IllegalArgumentException.class, () -> userService.update(userId,
// updateDto));

// verify(userRepository).findById(userId);
// verify(userRepository, never()).save(any(User.class));
// }

// @Test
// void testRetrieveByIdUserNotFound() {
// UUID userId = UUID.randomUUID();

// when(userRepository.findById(userId)).thenReturn(Optional.empty());

// assertThrows(IllegalArgumentException.class, () ->
// userService.retrieveById(userId));

// verify(userRepository).findById(userId);
// }

// @Test
// void testDeleteUserNotFound() {
// UUID userId = UUID.randomUUID();

// when(userRepository.findById(userId)).thenReturn(Optional.empty());

// assertThrows(IllegalArgumentException.class, () ->
// userService.delete(userId));

// verify(userRepository).findById(userId);
// verify(userRepository, never()).save(any(User.class));
// }

// @Test
// void testUpdatePasswordUserNotFound() {
// UUID userId = UUID.randomUUID();
// String newPassword = "newPassword";

// when(userRepository.findById(userId)).thenReturn(Optional.empty());

// assertThrows(IllegalArgumentException.class, () ->
// userService.updatePassword(userId, newPassword));

// verify(userRepository).findById(userId);
// verify(passwordEncoder, never()).encode(anyString());
// verify(userRepository, never()).save(any(User.class));
// }

// @Test
// void testCheckUserStatusUserNotFound() {
// UUID userId = UUID.randomUUID();

// when(userRepository.findById(userId)).thenReturn(Optional.empty());

// assertThrows(IllegalArgumentException.class, () ->
// userService.checkUserStatus(userId));

// verify(userRepository).findById(userId);
// }
// }
