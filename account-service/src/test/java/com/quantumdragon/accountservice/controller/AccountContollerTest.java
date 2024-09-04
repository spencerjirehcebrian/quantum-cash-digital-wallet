// package com.quantumdragon.accountservice.controller;

// import com.quantumdragon.accountservice.dto.*;
// import com.quantumdragon.accountservice.enums.Status;
// import com.quantumdragon.accountservice.service.AccountService;
// import org.junit.jupiter.api.BeforeEach;
// import org.junit.jupiter.api.Test;
// import org.mockito.InjectMocks;
// import org.mockito.Mock;
// import org.mockito.MockitoAnnotations;
// import org.springframework.http.HttpStatus;
// import org.springframework.http.ResponseEntity;

// import java.math.BigDecimal;
// import java.util.Arrays;
// import java.util.List;
// import java.util.UUID;

// import static org.junit.jupiter.api.Assertions.*;
// import static org.mockito.Mockito.*;

// class AccountControllerTest {

//     @Mock
//     private AccountService accountService;

//     @InjectMocks
//     private AccountController accountController;

//     @BeforeEach
//     void setUp() {
//         MockitoAnnotations.openMocks(this);
//     }

//     @Test
//     void createAccount_Success() {
//         UUID userId = UUID.randomUUID();
//         AccountCreateRequestDto request = new AccountCreateRequestDto(userId, "SAVINGS", new BigDecimal("1000.00"),
//                 "USD");
//         AccountResponseDto expectedResponse = new AccountResponseDto(UUID.randomUUID(), userId, "SAVINGS",
//                 new BigDecimal("1000.00"), "USD", Status.ACTIVE);

//         when(accountService.createAccount(request)).thenReturn(expectedResponse);

//         ResponseEntity<StandardResponseDto<AccountResponseDto>> response = accountController.createAccount(request);

//         assertEquals(HttpStatus.OK, response.getStatusCode());
//         assertTrue(response.getBody().isSuccess());
//         assertEquals("Account created successfully", response.getBody().getMessage());
//         assertEquals(expectedResponse, response.getBody().getData());
//     }

//     @Test
//     void getAccount_Success() {
//         UUID accountId = UUID.randomUUID();
//         AccountResponseDto expectedResponse = new AccountResponseDto(accountId, UUID.randomUUID(), "SAVINGS",
//                 new BigDecimal("1000.00"), "USD", Status.ACTIVE);

//         when(accountService.getAccount(accountId)).thenReturn(expectedResponse);

//         ResponseEntity<StandardResponseDto<AccountResponseDto>> response = accountController.getAccount(accountId);

//         assertEquals(HttpStatus.OK, response.getStatusCode());
//         assertTrue(response.getBody().isSuccess());
//         assertEquals("Account retrieved successfully", response.getBody().getMessage());
//         assertEquals(expectedResponse, response.getBody().getData());
//     }

//     @Test
//     void getUserAccounts_Success() {
//         UUID userId = UUID.randomUUID();
//         List<AccountResponseDto> expectedResponse = Arrays.asList(
//                 new AccountResponseDto(UUID.randomUUID(), userId, "SAVINGS", new BigDecimal("1000.00"), "USD",
//                         Status.ACTIVE),
//                 new AccountResponseDto(UUID.randomUUID(), userId, "CHECKING", new BigDecimal("500.00"), "USD",
//                         Status.ACTIVE));

//         when(accountService.getUserAccounts(userId)).thenReturn(expectedResponse);

//         ResponseEntity<StandardResponseDto<List<AccountResponseDto>>> response = accountController
//                 .getUserAccounts(userId);

//         assertEquals(HttpStatus.OK, response.getStatusCode());
//         assertTrue(response.getBody().isSuccess());
//         assertEquals("Accounts retrieved successfully", response.getBody().getMessage());
//         assertEquals(expectedResponse, response.getBody().getData());
//     }

//     @Test
//     void updateAccount_Success() {
//         UUID accountId = UUID.randomUUID();
//         AccountUpdateRequestDto request = new AccountUpdateRequestDto("CHECKING", new BigDecimal("1500.00"));
//         AccountResponseDto expectedResponse = new AccountResponseDto(accountId, UUID.randomUUID(), "CHECKING",
//                 new BigDecimal("1500.00"), "USD", Status.ACTIVE);

//         when(accountService.updateAccount(accountId, request)).thenReturn(expectedResponse);

//         ResponseEntity<StandardResponseDto<AccountResponseDto>> response = accountController.updateAccount(accountId,
//                 request);

//         assertEquals(HttpStatus.OK, response.getStatusCode());
//         assertTrue(response.getBody().isSuccess());
//         assertEquals("Account updated successfully", response.getBody().getMessage());
//         assertEquals(expectedResponse, response.getBody().getData());
//     }

//     @Test
//     void deleteAccount_Success() {
//         UUID accountId = UUID.randomUUID();

//         ResponseEntity<StandardResponseDto<Void>> response = accountController.deleteAccount(accountId);

//         assertEquals(HttpStatus.OK, response.getStatusCode());
//         assertTrue(response.getBody().isSuccess());
//         assertEquals("Account marked as inactive", response.getBody().getMessage());
//         assertNull(response.getBody().getData());

//         verify(accountService).deleteAccount(accountId);
//     }

//     @Test
//     void getAccountBalance_Success() {
//         UUID accountId = UUID.randomUUID();
//         AccountBalanceResponseDto expectedResponse = new AccountBalanceResponseDto(new BigDecimal("1000.00"));

//         when(accountService.getAccountBalance(accountId)).thenReturn(expectedResponse);

//         ResponseEntity<StandardResponseDto<AccountBalanceResponseDto>> response = accountController
//                 .getAccountBalance(accountId);

//         assertEquals(HttpStatus.OK, response.getStatusCode());
//         assertTrue(response.getBody().isSuccess());
//         assertEquals("Account balance retrieved successfully", response.getBody().getMessage());
//         assertEquals(expectedResponse, response.getBody().getData());
//     }

// }