package com.quantumdragon.userservice.service.impl;

import com.quantumdragon.userservice.dto.KnowYourCustomerDto;
import com.quantumdragon.userservice.entity.KnowYourCustomer;
import com.quantumdragon.userservice.entity.User;
import com.quantumdragon.userservice.mapper.KnowYourCustomerMapper;
import com.quantumdragon.userservice.repository.KnowYourCustomerRepository;
import com.quantumdragon.userservice.repository.UserRepository;
import com.quantumdragon.userservice.service.KnowYourCustomerService;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class KnowYourCustomerServiceImpl implements KnowYourCustomerService {

    @Autowired
    private KnowYourCustomerRepository kycRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private NotificationServiceImpl notificationService;

    private static final Log logger = LogFactory.getLog(KnowYourCustomer.class);

    private final KnowYourCustomerMapper mapper = KnowYourCustomerMapper.INSTANCE;

    @Override
    public KnowYourCustomerDto retreiveKYC(UUID id) {
        return kycRepository.findById(id)
                .map(mapper::toDto).orElseThrow(
                        () -> new IllegalArgumentException("KYC not found"));
    }

    @Override
    public String createEmptyKYCForUser(UUID userId) {
        logger.info("Creating empty KYC for user: " + userId);
        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            logger.info("User found: " + user.getEmail());
            KnowYourCustomer emptyKYC = new KnowYourCustomer();
            emptyKYC.setUser(user);
            emptyKYC.setAddressProofDocument(null);
            emptyKYC.setIdProofDocument(null);
            emptyKYC = kycRepository.save(emptyKYC);
            user.setKnowYourCustomer(emptyKYC);
            userRepository.save(user);
            try {
                String notification = notificationService
                        .sendUserKYCInitNotification(user.getId().toString(),
                                user)
                        .block();
                logger.info("Notification sent: " + notification);
            } catch (Exception e) {
                logger.info("Notification error: " + e);
            }
            return emptyKYC.getUserId().toString();
        } else {
            throw new IllegalArgumentException("User not found");
        }
    }

    @Override
    public KnowYourCustomerDto updateKYC(UUID id, KnowYourCustomerDto dto) {

        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        KnowYourCustomerDto kyc = kycRepository.findById(id)
                .map(existingEntity -> {
                    KnowYourCustomer updatedEntity = mapper.toEntity(dto);
                    updatedEntity.setUserId(id);
                    kycRepository.save(updatedEntity);
                    return mapper.toDto(updatedEntity);
                }).orElseThrow(
                        () -> new IllegalArgumentException("KYC not found"));
        try {
            String notification = notificationService
                    .sendUserKYCSubmittedNotification(id.toString(),
                            dto, user)
                    .block();
            logger.info("Notification sent: " + notification);
        } catch (Exception e) {
            logger.info("Notification error: " + e);
        }
        return kyc;
    }

    @Override
    public List<KnowYourCustomerDto> retreiveKYCAll() {
        return kycRepository.findAll().stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }
}
