package com.quantumdragon.accountservice.service;

import com.quantumdragon.accountservice.dto.*;
import com.quantumdragon.accountservice.enums.AccountType;
import com.quantumdragon.accountservice.exception.InvalidAccountTypeException;

import java.util.List;
import java.util.UUID;

public interface AccountService {

    AccountResponseDto createAccount(AccountCreateRequestDto request, UUID userId);

    AccountResponseDto getAccount(UUID accountId);

    List<AccountResponseDto> getUserAccounts(UUID userId);

    AccountResponseDto updateAccount(UUID accountId, AccountUpdateRequestDto request);

    AccountResponseDto depositToAccount(UUID accountId, AccountDepositRequestDto depositAmount);

    AccountResponseDto withdrawFromAccount(UUID accountId, AccountWithdrawRequestDto withdrawRequest);

    AccountTransferResponseDto transferBetweenAccounts(UUID sourceAccountId, AccountTransferRequestDto transferRequest);

    void deleteAccount(UUID accountId);

    AccountBalanceResponseDto getAccountBalance(UUID accountId);

    AccountType parseAccountType(String accountTypeStr) throws InvalidAccountTypeException;

}
