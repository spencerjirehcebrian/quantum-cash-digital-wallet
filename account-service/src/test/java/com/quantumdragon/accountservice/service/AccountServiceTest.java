// package com.quantumdragon.accountservice.service;

// import com.quantumdragon.accountservice.dto.*;
// import com.quantumdragon.accountservice.entity.Account;
// import com.quantumdragon.accountservice.enums.AccountType;
// import com.quantumdragon.accountservice.enums.Status;
// import com.quantumdragon.accountservice.exception.*;
// import com.quantumdragon.accountservice.repository.AccountRepository;
// import org.junit.jupiter.api.BeforeEach;
// import org.junit.jupiter.api.Test;
// import org.mockito.InjectMocks;
// import org.mockito.Mock;
// import org.mockito.MockitoAnnotations;

// import java.math.BigDecimal;
// import java.util.*;

// import static org.junit.jupiter.api.Assertions.*;
// import static org.mockito.Mockito.*;

// class AccountServiceTest {

// @Mock
// private AccountRepository accountRepository;

// @InjectMocks
// private AccountService accountService;

// @BeforeEach
// void setUp() {
// MockitoAnnotations.openMocks(this);
// }

// @Test
// void createAccount_Success() {
// UUID userId = UUID.randomUUID();
// AccountCreateRequestDto request = new AccountCreateRequestDto(userId,
// "SAVINGS",
// new BigDecimal("1000.00"),
// "USD");

// Account savedAccount = new Account();
// savedAccount.setId(UUID.randomUUID());
// savedAccount.setUserId(userId);
// savedAccount.setAccountType(AccountType.SAVINGS);
// savedAccount.setBalance(new BigDecimal("1000.00"));
// savedAccount.setCurrency("USD");
// savedAccount.setStatus(Status.ACTIVE);

// when(accountRepository.save(any(Account.class))).thenReturn(savedAccount);

// AccountResponseDto response = accountService.createAccount(request);

// assertNotNull(response);
// assertEquals(savedAccount.getId(), response.id());
// assertEquals(userId, response.userId());
// assertEquals("SAVINGS", response.accountType());
// assertEquals(new BigDecimal("1000.00"), response.balance());
// assertEquals("USD", response.currency());
// assertEquals(Status.ACTIVE, response.status());

// verify(accountRepository).save(any(Account.class));
// }

// @Test
// void createAccount_InvalidInput() {
// AccountCreateRequestDto request = new AccountCreateRequestDto(null, null,
// null, null);

// assertThrows(InvalidInputException.class, () ->
// accountService.createAccount(request));
// }

// @Test
// void getAccount_Success() {
// UUID accountId = UUID.randomUUID();
// Account account = new Account();
// account.setId(accountId);
// account.setUserId(UUID.randomUUID());
// account.setAccountType(AccountType.CHECKING);
// account.setBalance(new BigDecimal("500.00"));
// account.setCurrency("EUR");
// account.setStatus(Status.ACTIVE);

// when(accountRepository.findById(accountId)).thenReturn(Optional.of(account));

// AccountResponseDto response = accountService.getAccount(accountId);

// assertNotNull(response);
// assertEquals(accountId, response.id());
// assertEquals("CHECKING", response.accountType());
// assertEquals(new BigDecimal("500.00"), response.balance());
// assertEquals("EUR", response.currency());
// assertEquals(Status.ACTIVE, response.status());
// }

// @Test
// void getAccount_NotFound() {
// UUID accountId = UUID.randomUUID();
// when(accountRepository.findById(accountId)).thenReturn(Optional.empty());

// assertThrows(ResourceNotFoundException.class, () ->
// accountService.getAccount(accountId));
// }

// @Test
// void getUserAccounts_Success() {
// UUID userId = UUID.randomUUID();
// List<Account> accounts = Arrays.asList(
// createAccount(UUID.randomUUID(), userId, AccountType.SAVINGS, new
// BigDecimal("1000.00"), "USD"),
// createAccount(UUID.randomUUID(), userId, AccountType.CHECKING, new
// BigDecimal("500.00"), "USD"));

// when(accountRepository.findByUserId(userId)).thenReturn(accounts);

// List<AccountResponseDto> response = accountService.getUserAccounts(userId);

// assertEquals(2, response.size());
// assertEquals("SAVINGS", response.get(0).accountType());
// assertEquals("CHECKING", response.get(1).accountType());
// }

// @Test
// void updateAccount_Success() {
// UUID accountId = UUID.randomUUID();
// AccountUpdateRequestDto request = new AccountUpdateRequestDto("CREDIT", new
// BigDecimal("2000.00"));

// Account existingAccount = createAccount(accountId, UUID.randomUUID(),
// AccountType.SAVINGS,
// new BigDecimal("1000.00"), "USD");
// when(accountRepository.findById(accountId)).thenReturn(Optional.of(existingAccount));
// when(accountRepository.save(any(Account.class))).thenAnswer(invocation ->
// invocation.getArgument(0));

// AccountResponseDto response = accountService.updateAccount(accountId,
// request);

// assertNotNull(response);
// assertEquals(accountId, response.id());
// assertEquals("CREDIT", response.accountType());
// assertEquals(new BigDecimal("2000.00"), response.balance());
// }

// @Test
// void updateAccount_NotFound() {
// UUID accountId = UUID.randomUUID();
// AccountUpdateRequestDto request = new AccountUpdateRequestDto("CREDIT", new
// BigDecimal("2000.00"));

// when(accountRepository.findById(accountId)).thenReturn(Optional.empty());

// assertThrows(ResourceNotFoundException.class, () ->
// accountService.updateAccount(accountId, request));
// }

// @Test
// void deleteAccount_Success() {
// UUID accountId = UUID.randomUUID();
// Account account = createAccount(accountId, UUID.randomUUID(),
// AccountType.SAVINGS, new BigDecimal("1000.00"),
// "USD");

// when(accountRepository.findById(accountId)).thenReturn(Optional.of(account));
// when(accountRepository.save(any(Account.class))).thenAnswer(invocation ->
// invocation.getArgument(0));

// accountService.deleteAccount(accountId);

// assertEquals(Status.INACTIVE, account.getStatus());
// verify(accountRepository).save(account);
// }

// @Test
// void getAccountBalance_Success() {
// UUID accountId = UUID.randomUUID();
// Account account = createAccount(accountId, UUID.randomUUID(),
// AccountType.SAVINGS, new BigDecimal("1000.00"),
// "USD");

// when(accountRepository.findById(accountId)).thenReturn(Optional.of(account));

// AccountBalanceResponseDto response =
// accountService.getAccountBalance(accountId);

// assertEquals(new BigDecimal("1000.00"), response.balance());
// }

// private Account createAccount(UUID id, UUID userId, AccountType type,
// BigDecimal balance, String currency) {
// Account account = new Account();
// account.setId(id);
// account.setUserId(userId);
// account.setAccountType(type);
// account.setBalance(balance);
// account.setCurrency(currency);
// account.setStatus(Status.ACTIVE);
// return account;
// }
// }