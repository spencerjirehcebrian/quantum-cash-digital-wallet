package com.quantumdragon.accountservice.service;


import com.quantumdragon.accountservice.dto.NotificationRequest;
import com.quantumdragon.accountservice.entity.Account;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;

public interface NotificationService {

    Mono<String> sendNotification(NotificationRequest notificationRequest);

    Mono<String> sendAccountCreateNotification(String userId, Account account);

    Mono<String> sendAccountDepositNotification(String userId, Account account, BigDecimal amount);

    Mono<String> sendAccountWithdrawNotification(String userId, Account account, BigDecimal amount);

    Mono<String> sendAccountTransferNotification(String userId, Account sourceAccount,
                                                 Account destinationAccount, BigDecimal amount);
}

