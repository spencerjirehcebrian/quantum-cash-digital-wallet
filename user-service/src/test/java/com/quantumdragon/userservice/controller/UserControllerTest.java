// package com.quantumdragon.userservice.controller;

// import com.quantumdragon.userservice.dto.*;
// import com.quantumdragon.userservice.enums.Status;
// import com.quantumdragon.userservice.service.impl.UserServiceImpl;
// import org.junit.jupiter.api.BeforeEach;
// import org.junit.jupiter.api.Test;
// import org.mockito.InjectMocks;
// import org.mockito.Mock;
// import org.mockito.MockitoAnnotations;
// import org.springframework.security.core.AuthenticationException;

// import java.time.LocalDate;
// import java.time.LocalDateTime;
// import java.time.ZonedDateTime;
// import java.util.Arrays;
// import java.util.List;
// import java.util.UUID;

// import static org.junit.jupiter.api.Assertions.*;
// import static org.mockito.Mockito.*;

// class UserControllerTest {

//         @Mock
//         private UserServiceImpl userService;

//         @InjectMocks
//         private UserController userController;

//         @BeforeEach
//         void setUp() {
//                 MockitoAnnotations.openMocks(this);
//         }

//         @Test
//         void testRegister() {
//                 UserRegistrationDto registrationDto = new UserRegistrationDto("John", "Doe", "john@example.com",
//                                 "password", LocalDate.of(1990, 1, 1), "1234567890");
//                 UserResponseDto responseDto = new UserResponseDto(UUID.randomUUID(), "John", "Doe", "john@example.com",
//                                 "1234567890", Status.ACTIVE, ZonedDateTime.now(), ZonedDateTime.now());

//                 when(userService.save(any(UserRegistrationDto.class))).thenReturn(responseDto);

//                 StandardResponseDto<UserResponseDto> result = userController.register(registrationDto);

//                 assertTrue(result.isSuccess());
//                 assertEquals("User registered successfully", result.getMessage());
//                 assertEquals(responseDto, result.getData());
//                 verify(userService).save(registrationDto);
//         }

//         @Test
//         void testRetrieveById() {
//                 UUID userId = UUID.randomUUID();
//                 UserResponseDto responseDto = new UserResponseDto(userId, "John", "Doe", "john@example.com",
//                                 "1234567890", Status.ACTIVE, ZonedDateTime.now(), ZonedDateTime.now());

//                 when(userService.retrieveById(userId)).thenReturn(responseDto);

//                 StandardResponseDto<UserResponseDto> result = userController.retrieveById(userId);

//                 assertTrue(result.isSuccess());
//                 assertEquals("User retrieved successfully", result.getMessage());
//                 assertEquals(responseDto, result.getData());
//                 verify(userService).retrieveById(userId);
//         }

//         @Test
//         void testRetrieveAll() {
//                 List<UserResponseDto> users = Arrays.asList(
//                                 new UserResponseDto(UUID.randomUUID(), "John", "Doe", "john@example.com", "1234567890",
//                                                 Status.ACTIVE, ZonedDateTime.now(), ZonedDateTime.now()),
//                                 new UserResponseDto(UUID.randomUUID(), "Jane", "Doe", "jane@example.com", "0987654321",
//                                                 Status.ACTIVE, ZonedDateTime.now(), ZonedDateTime.now()));

//                 when(userService.retrieveAll()).thenReturn(users);

//                 StandardResponseDto<List<UserResponseDto>> result = userController.retrieveAll();

//                 assertTrue(result.isSuccess());
//                 assertEquals("All users retrieved successfully", result.getMessage());
//                 assertEquals(users, result.getData());
//                 verify(userService).retrieveAll();
//         }

//         @Test
//         void testCreateAuthenticationToken() throws AuthenticationException {
//                 UserLoginDto loginDto = new UserLoginDto("john@example.com", "password");
//                 AuthResponseDto authResponseDto = new AuthResponseDto(UUID.randomUUID(), "token",
//                                 LocalDateTime.now().plusHours(1));

//                 when(userService.authenticate(loginDto)).thenReturn(authResponseDto);

//                 StandardResponseDto<AuthResponseDto> result = userController.createAuthenticationToken(loginDto);

//                 assertTrue(result.isSuccess());
//                 assertEquals("Authentication successful", result.getMessage());
//                 assertEquals(authResponseDto, result.getData());
//                 verify(userService).authenticate(loginDto);
//         }

//         @Test
//         void testUpdateUser() {
//                 UUID userId = UUID.randomUUID();
//                 UserUpdateDto updateDto = new UserUpdateDto("John", "Doe", LocalDate.of(1990, 1, 1),
//                                 "1234567890");
//                 UserResponseDto responseDto = new UserResponseDto(userId, "John", "Doe", "john@example.com",
//                                 "1234567890", Status.ACTIVE, ZonedDateTime.now(), ZonedDateTime.now());

//                 when(userService.update(userId, updateDto)).thenReturn(responseDto);

//                 StandardResponseDto<UserResponseDto> result = userController.updateUser(userId, updateDto);

//                 assertTrue(result.isSuccess());
//                 assertEquals("User updated successfully", result.getMessage());
//                 assertEquals(responseDto, result.getData());
//                 verify(userService).update(userId, updateDto);
//         }

//         @Test
//         void testUpdatePassword() {
//                 UUID userId = UUID.randomUUID();
//                 UserPasswordUpdateDto passwordUpdateDto = new UserPasswordUpdateDto("newPassword");

//                 doNothing().when(userService).updatePassword(userId, passwordUpdateDto.newPassword());

//                 StandardResponseDto<String> result = userController.updatePassword(userId, passwordUpdateDto);

//                 assertTrue(result.isSuccess());
//                 assertEquals("Password updated successfully", result.getMessage());
//                 assertEquals("Password updated successfully", result.getData());
//                 verify(userService).updatePassword(userId, passwordUpdateDto.newPassword());
//         }

//         @Test
//         void testCheckUserStatus() {
//                 UUID userId = UUID.randomUUID();
//                 Status status = Status.ACTIVE;

//                 when(userService.checkUserStatus(userId)).thenReturn(status);

//                 StandardResponseDto<Status> result = userController.checkUserStatus(userId);

//                 assertTrue(result.isSuccess());
//                 assertEquals("User status retrieved successfully", result.getMessage());
//                 assertEquals(status, result.getData());
//                 verify(userService).checkUserStatus(userId);
//         }

//         @Test
//         void testDeleteUser() {
//                 UUID userId = UUID.randomUUID();
//                 String authHeader = "Bearer token";

//                 doNothing().when(userService).logout("token");
//                 doNothing().when(userService).delete(userId);

//                 StandardResponseDto<String> result = userController.deleteUser(userId, authHeader);

//                 assertTrue(result.isSuccess());
//                 assertEquals("User deleted successfully", result.getMessage());
//                 assertEquals("User deleted successfully", result.getData());
//                 verify(userService).logout("token");
//                 verify(userService).delete(userId);
//         }

//         @Test
//         void testLogout() {
//                 String authHeader = "Bearer token";

//                 doNothing().when(userService).logout("token");

//                 StandardResponseDto<String> result = userController.logout(authHeader);

//                 assertTrue(result.isSuccess());
//                 assertEquals("Logout successful", result.getMessage());
//                 assertEquals("Logout successful", result.getData());
//                 verify(userService).logout("token");
//         }

//         // @Test
//         // void testHandleEmailAlreadyExistsException() {
//         // EmailAlreadyExistsException ex = new EmailAlreadyExistsException("Email
//         // already exists");
//         // UserController.CustomExceptionHandler handler = new
//         // UserController.CustomExceptionHandler();

//         // ResponseEntity<StandardResponseDto<String>> response =
//         // handler.handleEmailAlreadyExistsException(ex,
//         // null);

//         // assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
//         // assertFalse(response.getBody().isSuccess());
//         // assertEquals("Email already exists", response.getBody().getMessage());
//         // assertNull(response.getBody().getData());
//         // }

// }
