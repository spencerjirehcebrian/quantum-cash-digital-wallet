package com.quantumdragon.userservice.service.impl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.quantumdragon.userservice.dto.PaymentMethodDto;
import com.quantumdragon.userservice.entity.PaymentMethod;
import com.quantumdragon.userservice.entity.User;
import com.quantumdragon.userservice.mapper.PaymentMethodMapper;
import com.quantumdragon.userservice.repository.PaymentMethodRepository;
import com.quantumdragon.userservice.repository.UserRepository;
import com.quantumdragon.userservice.service.PaymentMethodService;

import jakarta.transaction.Transactional;
import java.util.Optional;
import java.util.UUID;

@Service
public class PaymentMethodServiceImpl implements PaymentMethodService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PaymentMethodRepository paymentMethodRepository;

    @Autowired
    private PaymentMethodMapper paymentMethodMapper;

    @Autowired
    private NotificationServiceImpl notificationService;

    private static final Log logger = LogFactory.getLog(PaymentMethodServiceImpl.class);

    @Override
    @Transactional
    public PaymentMethodDto addPaymentMethod(UUID userId, PaymentMethodDto paymentMethodDTO) {
        if (userRepository.existsById(userId)) {
            PaymentMethod paymentMethod = paymentMethodMapper.toEntity(paymentMethodDTO);
            paymentMethod.setUserId(userId);
            PaymentMethod savedPaymentMethod = paymentMethodRepository.save(paymentMethod);
            try {
                String notification = notificationService
                        .sendUserPaymentMethodNotification(userId.toString(),
                                paymentMethod)
                        .block();
                logger.info("Notification sent: " + notification);
            } catch (Exception e) {
                logger.error("Error sending notification: " + e.getMessage());
            }
            return paymentMethodMapper.toDto(savedPaymentMethod);
        } else {
            throw new RuntimeException("User not found");
        }
    }

    @Override
    @Transactional
    public PaymentMethodDto getPaymentMethod(UUID userId, UUID paymentMethodId) {
        Optional<User> userOptional = userRepository.findById(userId);

        if (userOptional.isPresent()) {
            User user = userOptional.get();
            Optional<PaymentMethod> paymentMethodOptional = paymentMethodRepository.findById(paymentMethodId);

            if (paymentMethodOptional.isPresent()) {
                PaymentMethod paymentMethod = paymentMethodOptional.get();

                if (paymentMethod.getUserId().equals(user.getId())) {
                    return paymentMethodMapper.toDto(paymentMethod);
                } else {
                    throw new RuntimeException("Payment method does not belong to the user");
                }
            } else {
                throw new RuntimeException("Payment method not found");
            }
        } else {
            throw new RuntimeException("User not found");
        }
    }
}
