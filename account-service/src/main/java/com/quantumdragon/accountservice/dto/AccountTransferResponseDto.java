package com.quantumdragon.accountservice.dto;

import lombok.Data;

@Data
public class AccountTransferResponseDto {
    private AccountResponseDto sourceAccount;
    private AccountResponseDto destinationAccount;

    public AccountTransferResponseDto(AccountResponseDto sourceAccount, AccountResponseDto destinationAccount) {
        this.sourceAccount = sourceAccount;
        this.destinationAccount = destinationAccount;
    }
}
