package com.quantumdragon.accountservice.service.impl;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.quantumdragon.accountservice.dto.*;
import com.quantumdragon.accountservice.entity.*;
import com.quantumdragon.accountservice.service.NotificationService;

import reactor.core.publisher.Mono;

@Service
public class NotificationServiceImpl implements NotificationService {

        private final WebClient webClient;

        private static final Log logger = LogFactory.getLog(NotificationServiceImpl.class);

        @Value("${notification.service.url}")
        private String customerServiceUrl;

        public NotificationServiceImpl(WebClient.Builder webClientBuilder) {
                this.webClient = webClientBuilder.baseUrl("http://localhost:8086").build();
        }

        public Mono<String> sendNotification(NotificationRequest notificationRequest) {
                logger.info("Sending notification: " + notificationRequest);
                return this.webClient.post()
                                .uri("/api/notifications/send")
                                .bodyValue(notificationRequest)
                                .retrieve()
                                .bodyToMono(StandardResponseDto.class)
                                .map(StandardResponseDto<String>::getData);
        }

        public Mono<String> sendAccountCreateNotification(String userId, Account account) {

                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
                String formattedDateTime = LocalDateTime.now().format(formatter);
                NotificationRequest notificationRequest = new NotificationRequest(
                                userId,
                                "ACCOUNT_CREATED",
                                "New " + account.getAccountType().toString().toLowerCase() + " Account Opened",
                                Map.of(
                                                "message",
                                                "Your new " + account.getAccountType().toString().toLowerCase()
                                                                + "  account has been successfully created. Start paying today!",
                                                "rows", new Object[][] {
                                                                { "Account Account Type:", account.getAccountType() },
                                                                { "Balance:", account.getBalance() },
                                                                { "Currency:", account.getCurrency() },
                                                                { "Created At:", formattedDateTime }
                                                }),
                                "email");
                logger.info("Sending notification: " + notificationRequest);

                return sendNotification(notificationRequest)
                                .doOnSuccess(response -> logger.info("Notification sent successfully: " + response))
                                .doOnError(error -> logger.info("Failed to send notification: " + error.getMessage()));
        }

        public Mono<String> sendAccountDepositNotification(String userId, Account account, BigDecimal amount) {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
                String formattedDateTime = LocalDateTime.now().format(formatter);
                NotificationRequest notificationRequest = new NotificationRequest(
                                userId,
                                "DEPOSIT_SUCCESSFUL",
                                "Deposit Successful",
                                Map.of(
                                                "message",
                                                "Your deposit of " + amount + " " + account.getCurrency()
                                                                + " has been successfully processed and added to your account.",
                                                "rows", new Object[][] {
                                                                { "Account Account Type:", account.getAccountType() },
                                                                { "Deposit Amount:", amount },
                                                                { "Current Balance:", account.getBalance() },
                                                                { "Currency:", account.getCurrency() },
                                                                { "Transaction Occured At:", formattedDateTime }
                                                }),
                                "email");
                logger.info("Sending notification: " + notificationRequest);

                return sendNotification(notificationRequest)
                                .doOnSuccess(response -> logger.info("Notification sent successfully: " + response))
                                .doOnError(error -> logger.info("Failed to send notification: " + error.getMessage()));
        }

        public Mono<String> sendAccountWithdrawNotification(String userId, Account account, BigDecimal amount) {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
                String formattedDateTime = LocalDateTime.now().format(formatter);
                NotificationRequest notificationRequest = new NotificationRequest(
                                userId,
                                "WITHDRAW_SUCCESSFUL",
                                "Withdraw Successful",
                                Map.of(
                                                "message",
                                                "Your withdrawal of " + amount + " "
                                                                + account.getCurrency()
                                                                + " has been successfully processed and taken from your account.",
                                                "rows", new Object[][] {
                                                                { "Account Account Type:", account.getAccountType() },
                                                                { "Withdrawal Amount:", amount },
                                                                { "Current Balance:", account.getBalance() },
                                                                { "Currency:", account.getCurrency() },
                                                                { "Transaction Occured At:", formattedDateTime }
                                                }),
                                "email");
                logger.info("Sending notification: " + notificationRequest);

                return sendNotification(notificationRequest)
                                .doOnSuccess(response -> logger.info("Notification sent successfully: " + response))
                                .doOnError(error -> logger.info("Failed to send notification: " + error.getMessage()));
        }

        public Mono<String> sendAccountTransferNotification(String userId, Account sourceAccount,
                        Account destinationAccount, BigDecimal amount) {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
                String formattedDateTime = LocalDateTime.now().format(formatter);
                NotificationRequest notificationRequest = new NotificationRequest(
                                userId,
                                "TRANSFER_SUCCESSFUL",
                                "Internal Transfer Successful",
                                Map.of(
                                                "message",
                                                "Your transfer of " + amount + " " + sourceAccount.getCurrency()
                                                                + " has been processed.",
                                                "rows", new Object[][] {
                                                                { "Source Account Type:",
                                                                                sourceAccount.getAccountType() },
                                                                { "Transfer Amount:", amount },
                                                                { "Source Current Balance:",
                                                                                sourceAccount.getBalance() },
                                                                { "Destination Account Account Type:",
                                                                                destinationAccount.getAccountType() },
                                                                { "Destination Balance:",
                                                                                destinationAccount.getBalance() },
                                                                { "Currency:", destinationAccount.getCurrency() },
                                                                { "Transaction Occured At:", formattedDateTime }
                                                }),
                                "email");
                logger.info("Sending notification: " + notificationRequest);

                return sendNotification(notificationRequest)
                                .doOnSuccess(response -> logger.info("Notification sent successfully: " + response))
                                .doOnError(error -> logger.info("Failed to send notification: " + error.getMessage()));
        }

}