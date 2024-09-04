package com.quantumdragon.userservice.service;


import com.quantumdragon.userservice.dto.PaymentMethodDto;

import java.util.UUID;

public interface PaymentMethodService {

    PaymentMethodDto addPaymentMethod(UUID userId, PaymentMethodDto paymentMethodDTO);

    PaymentMethodDto getPaymentMethod(UUID userId, UUID paymentMethodId);
}

