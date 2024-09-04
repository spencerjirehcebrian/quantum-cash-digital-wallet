package com.quantumdragon.userservice.service;


import com.quantumdragon.userservice.dto.KnowYourCustomerDto;
import com.quantumdragon.userservice.dto.NotificationRequest;
import com.quantumdragon.userservice.entity.PaymentMethod;
import com.quantumdragon.userservice.entity.User;
import reactor.core.publisher.Mono;

public interface NotificationService {

    Mono<String> sendNotification(NotificationRequest notificationRequest);

    Mono<String> sendUserRegisterNotification(String userId, User user);

    Mono<String> sendUserLoginNotification(String userId, String email);

    Mono<String> sendUserKYCInitNotification(String userId, User user);

    Mono<String> sendUserKYCSubmittedNotification(String userId, KnowYourCustomerDto kyc, User user);

    Mono<String> sendUserKYCApprovedNotification(String userId, User user);

    Mono<String> sendUserPaymentMethodNotification(String userId, PaymentMethod paymentMethod);
}

