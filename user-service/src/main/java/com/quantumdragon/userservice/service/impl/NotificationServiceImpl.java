package com.quantumdragon.userservice.service.impl;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.quantumdragon.userservice.dto.KnowYourCustomerDto;
import com.quantumdragon.userservice.dto.NotificationRequest;
import com.quantumdragon.userservice.dto.StandardResponseDto;
import com.quantumdragon.userservice.entity.PaymentMethod;
import com.quantumdragon.userservice.entity.User;
import com.quantumdragon.userservice.service.NotificationService;

import reactor.core.publisher.Mono;

@Service
public class NotificationServiceImpl implements NotificationService {

        private final WebClient webClient;

        private static final Log logger = LogFactory.getLog(NotificationServiceImpl.class);

        public NotificationServiceImpl(WebClient.Builder webClientBuilder) {
                this.webClient = webClientBuilder.baseUrl("http://localhost:8086").build();
        }

        @Override
        public Mono<String> sendNotification(NotificationRequest notificationRequest) {
                logger.info("Sending notification: " + notificationRequest);
                return this.webClient.post()
                                .uri("/api/notifications/send")
                                .bodyValue(notificationRequest)
                                .retrieve()
                                .bodyToMono(StandardResponseDto.class)
                                .map(StandardResponseDto<String>::getData);
        }

        @Override
        public Mono<String> sendUserRegisterNotification(String userId, User user) {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
                String formattedDateTime = LocalDateTime.now().format(formatter);
                NotificationRequest notificationRequest = new NotificationRequest(
                                userId,
                                "WELCOME",
                                "User Registration",
                                Map.of(
                                                "message",
                                                "Thank you " + user.getFirstName() + " " + user.getLastName()
                                                                + ", for registering with our service. Please complete your KYC to unlock all features.",
                                                "rows", new Object[][] {
                                                                { "First Name:", user.getFirstName() },
                                                                { "Last Name:", user.getLastName() },
                                                                { "Email:", user.getEmail() },
                                                                { "Registered At:", formattedDateTime }
                                                }),
                                "email");
                logger.info("Sending notification: " + notificationRequest);

                return sendNotification(notificationRequest)
                                .doOnSuccess(response -> logger.info("Notification sent successfully: " + response))
                                .doOnError(error -> logger.info("Failed to send notification: " + error.getMessage()));
        }

        public Mono<String> sendUserLoginNotification(String userId, String email) {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
                String formattedDateTime = LocalDateTime.now().format(formatter);
                NotificationRequest notificationRequest = new NotificationRequest(
                                userId,
                                "LOGIN",
                                "User Login",
                                Map.of(
                                                "message",
                                                "A new login was detected on your account. If this wasn't you, please contact support.",
                                                "rows", new Object[][] {
                                                                { "Email:", email },
                                                                { "Logged In At:", formattedDateTime }
                                                }),
                                "email");
                logger.info("Sending notification: " + notificationRequest);

                return sendNotification(notificationRequest)
                                .doOnSuccess(response -> logger.info("Notification sent successfully: " + response))
                                .doOnError(error -> logger.info("Failed to send notification: " + error.getMessage()));
        }

        public Mono<String> sendUserKYCInitNotification(String userId, User user) {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
                String formattedDateTime = LocalDateTime.now().format(formatter);
                NotificationRequest notificationRequest = new NotificationRequest(
                                userId,
                                "KYC_INITIATED",
                                "KYC Process Started",
                                Map.of(
                                                "message",
                                                "Please submit your identification documents to complete the KYC process.",
                                                "rows", new Object[][] {
                                                                { "Email:", user.getEmail() },
                                                                { "Initaited At:", formattedDateTime }
                                                }),
                                "email");
                logger.info("Sending notification: " + notificationRequest);

                return sendNotification(notificationRequest)
                                .doOnSuccess(response -> logger.info("Notification sent successfully: " + response))
                                .doOnError(error -> logger.info("Failed to send notification: " + error.getMessage()));
        }

        public Mono<String> sendUserKYCSubmittedNotification(String userId, KnowYourCustomerDto kyc, User user) {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
                String formattedDateTime = LocalDateTime.now().format(formatter);
                NotificationRequest notificationRequest = new NotificationRequest(
                                userId,
                                "KYC_SUBMITTED",
                                "KYC Documents Received",
                                Map.of(
                                                "message",
                                                "Your KYC documents have been received and are under review. We will notify you once the review is complete.",
                                                "rows", new Object[][] {
                                                                { "Email:", user.getEmail() },
                                                                { "KYC Proof Type:", kyc.getAddressProofType() },
                                                                { "Submitted At:", formattedDateTime }
                                                }),
                                "email");
                logger.info("Sending notification: " + notificationRequest);

                return sendNotification(notificationRequest)
                                .doOnSuccess(response -> logger.info("Notification sent successfully: " + response))
                                .doOnError(error -> logger.info("Failed to send notification: " + error.getMessage()));
        }

        public Mono<String> sendUserKYCApprovedNotification(String userId, User user) {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
                String formattedDateTime = LocalDateTime.now().format(formatter);
                NotificationRequest notificationRequest = new NotificationRequest(
                                userId,
                                "KYC_APPROVED",
                                "KYC Verification Successful",
                                Map.of(
                                                "message",
                                                "Your KYC verification is complete. You now have full access to all account features.",
                                                "rows", new Object[][] {
                                                                { "Email:", user.getEmail() },
                                                                { "KYC Address Proof Type:",
                                                                                user.getKnowYourCustomer()
                                                                                                .getAddressProofType() },
                                                                { "Approved At:", formattedDateTime }
                                                }),
                                "email");
                logger.info("Sending notification: " + notificationRequest);

                return sendNotification(notificationRequest)
                                .doOnSuccess(response -> logger.info("Notification sent successfully: " + response))
                                .doOnError(error -> logger.info("Failed to send notification: " + error.getMessage()));
        }

        public Mono<String> sendUserPaymentMethodNotification(String userId, PaymentMethod paymentMethod) {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
                String formattedDateTime = LocalDateTime.now().format(formatter);
                NotificationRequest notificationRequest = new NotificationRequest(
                                userId,
                                "PAYMENT_METHOD_ADDED",
                                "New Payment Method Added",
                                Map.of(
                                                "message",
                                                "A new credit card has been added to your account. You can now use it for deposits and payments.",
                                                "rows", new Object[][] {
                                                                { "Payment Method Type:",
                                                                                paymentMethod.getMethodType() },
                                                                { "Payment Provider:", paymentMethod.getProvider() },
                                                                { "Payment Added At:", formattedDateTime }
                                                }),
                                "email");
                logger.info("Sending notification: " + notificationRequest);

                return sendNotification(notificationRequest)
                                .doOnSuccess(response -> logger.info("Notification sent successfully: " + response))
                                .doOnError(error -> logger.info("Failed to send notification: " + error.getMessage()));
        }

}