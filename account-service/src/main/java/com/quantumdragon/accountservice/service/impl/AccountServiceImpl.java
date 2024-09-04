package com.quantumdragon.accountservice.service.impl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.quantumdragon.accountservice.dto.*;
import com.quantumdragon.accountservice.entity.Account;
import com.quantumdragon.accountservice.enums.AccountType;
import com.quantumdragon.accountservice.enums.Status;
import com.quantumdragon.accountservice.exception.*;
import com.quantumdragon.accountservice.repository.AccountRepository;
import com.quantumdragon.accountservice.service.AccountService;

import jakarta.transaction.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
public class AccountServiceImpl implements AccountService {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private NotificationServiceImpl notificationService;

    private static final Log logger = LogFactory.getLog(AccountServiceImpl.class);

    @Override
    public AccountResponseDto createAccount(AccountCreateRequestDto request, UUID userId) {
        Account account = new Account();
        account.setUserId(userId);
        account.setAccountType(parseAccountType(request.accountType()));
        account.setBalance(new BigDecimal(0));
        account.setCurrency(request.currency());
        if (request.stripeAccountId() != null && !request.stripeAccountId().isEmpty()) {
            account.setStripeAccountId(request.stripeAccountId());
        } else {
            account.setStripeAccountId(null);
        }
        account = accountRepository.save(account);
        logger.info("Account created: " + account);
        try {
            String notification = notificationService.sendAccountCreateNotification(userId.toString(), account)
                    .block();
            logger.info("Notification sent: " + notification);
        } catch (Exception e) {
            logger.info("Notification error: " + e);
        }
        return toResponse(account);
    }

    @Override
    public AccountResponseDto getAccount(UUID accountId) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new ResourceNotFoundException("Account not found"));
        logger.info("Account retrieved: " + account);
        return toResponse(account);
    }

    @Override
    public List<AccountResponseDto> getUserAccounts(UUID userId) {
        if (userId == null) {
            throw new InvalidInputException("User ID cannot be null");
        } else if (accountRepository.findByUserId(userId).isEmpty()) {
            throw new ResourceNotFoundException("User has no accounts or does not exist");
        }

        return accountRepository.findByUserId(userId).stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public AccountResponseDto updateAccount(UUID accountId, AccountUpdateRequestDto request) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new ResourceNotFoundException("Account not found"));
        logger.info("Account updated: " + account);
        account.setAccountType(parseAccountType(request.accountType()));
        account.setBalance(request.balance());
        account = accountRepository.save(account);
        return toResponse(account);
    }

    @Override
    public AccountResponseDto depositToAccount(UUID accountId, AccountDepositRequestDto depositAmount) {
        validateAmount(depositAmount.depositAmount());
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new ResourceNotFoundException("Account not found"));
        logger.info("Account before deposit: " + account);
        account.setBalance(account.getBalance().add(depositAmount.depositAmount()));
        account = accountRepository.save(account);
        logger.info("Account after deposit: " + account);
        try {
            String notification = notificationService
                    .sendAccountDepositNotification(account.getUserId().toString(), account,
                            depositAmount.depositAmount())
                    .block();
            logger.info("Notification sent: " + notification);
        } catch (Exception e) {
            logger.info("Notification error: " + e);
        }
        return toResponse(account);
    }

    @Override
    public AccountResponseDto withdrawFromAccount(UUID accountId, AccountWithdrawRequestDto withdrawRequest) {
        validateAmount(withdrawRequest.withdrawAmount());
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new ResourceNotFoundException("Account not found"));
        logger.info("Account before withdrawal: " + account);

        BigDecimal currentBalance = account.getBalance();
        BigDecimal withdrawAmount = withdrawRequest.withdrawAmount();

        if (currentBalance.compareTo(withdrawAmount) < 0) {
            throw new RuntimeException("Insufficient funds in the account");
        }

        account.setBalance(currentBalance.subtract(withdrawAmount));
        account = accountRepository.save(account);
        logger.info("Account after withdrawal: " + account);
        try {
            String notification = notificationService
                    .sendAccountWithdrawNotification(account.getUserId().toString(), account, withdrawAmount)
                    .block();
            logger.info("Notification sent: " + notification);
        } catch (Exception e) {
            logger.info("Notification error: " + e);
        }
        return toResponse(account);
    }

    @Override
    public AccountTransferResponseDto transferBetweenAccounts(UUID sourceAccountId,
            AccountTransferRequestDto transferRequest) {
        validateAmount(transferRequest.transferAmount());

        UUID destinationAccountId = transferRequest.toAccountId();

        Account sourceAccount = accountRepository.findById(sourceAccountId)
                .orElseThrow(() -> new ResourceNotFoundException("Source account not found"));
        Account destinationAccount = accountRepository.findById(destinationAccountId)
                .orElseThrow(() -> new ResourceNotFoundException("Destination account not found"));

        logger.info("Source account before transfer: " + sourceAccount);
        logger.info("Destination account before transfer: " + destinationAccount);

        BigDecimal transferAmount = transferRequest.transferAmount();
        BigDecimal sourceBalance = sourceAccount.getBalance();

        if (sourceBalance.compareTo(transferAmount) < 0) {
            throw new RuntimeException("Insufficient funds in the source account");
        }

        sourceAccount.setBalance(sourceBalance.subtract(transferAmount));
        destinationAccount.setBalance(destinationAccount.getBalance().add(transferAmount));

        sourceAccount = accountRepository.save(sourceAccount);
        destinationAccount = accountRepository.save(destinationAccount);

        logger.info("Source account after transfer: " + sourceAccount);
        logger.info("Destination account after transfer: " + destinationAccount);

        try {
            if (sourceAccount.getUserId() == destinationAccount.getUserId()) {
                String sourceNotification = notificationService
                        .sendAccountTransferNotification(sourceAccount.getUserId().toString(), sourceAccount,
                                destinationAccount,
                                transferAmount)
                        .block();
                logger.info("Source account notification sent: " + sourceNotification);

            } else {
                String sourceNotification = notificationService
                        .sendAccountTransferNotification(sourceAccount.getUserId().toString(), sourceAccount,
                                destinationAccount,
                                transferAmount)
                        .block();
                logger.info("Source account notification sent: " + sourceNotification);

                String destinationNotification = notificationService
                        .sendAccountTransferNotification(destinationAccount.getUserId().toString(), sourceAccount,
                                destinationAccount, transferAmount)
                        .block();
                logger.info("Destination account notification sent: " + destinationNotification);
            }
        } catch (Exception e) {
            logger.info("Notification error: " + e);
        }

        return toTransferResponse(sourceAccount, destinationAccount);
    }

    private void validateAmount(BigDecimal amount) {
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Amount must be greater than zero");
        }
    }

    @Override
    public void deleteAccount(UUID accountId) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new ResourceNotFoundException("Account not found"));
        logger.info("Account status set to inactive: " + account);
        account.setStatus(Status.INACTIVE);
        accountRepository.save(account);
    }

    @Override
    public AccountBalanceResponseDto getAccountBalance(UUID accountId) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new ResourceNotFoundException("Account not found"));
        logger.info("Account balance retrieved: " + account);
        return new AccountBalanceResponseDto(account.getBalance());
    }

    @Override
    public AccountType parseAccountType(String accountTypeStr) {
        try {
            return AccountType.valueOf(accountTypeStr.toUpperCase());
        } catch (IllegalArgumentException e) {
            logger.error("Invalid account type: " + accountTypeStr);
            throw new InvalidAccountTypeException("Invalid account type: " + accountTypeStr);
        }
    }

    private AccountResponseDto toResponse(Account account) {
        return new AccountResponseDto(
                account.getId(),
                account.getUserId(),
                account.getAccountType().toString(),
                account.getBalance(),
                account.getCurrency(),
                account.getStatus());
    }

    private AccountTransferResponseDto toTransferResponse(Account sourceAccount, Account destinationAccount) {
        return new AccountTransferResponseDto(toResponse(sourceAccount), toResponse(destinationAccount));
    }
}
