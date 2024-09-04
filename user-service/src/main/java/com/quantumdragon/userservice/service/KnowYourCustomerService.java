package com.quantumdragon.userservice.service;


import com.quantumdragon.userservice.dto.KnowYourCustomerDto;

import java.util.List;
import java.util.UUID;

public interface KnowYourCustomerService {

    KnowYourCustomerDto retreiveKYC(UUID id);

    String createEmptyKYCForUser(UUID userId);

    KnowYourCustomerDto updateKYC(UUID id, KnowYourCustomerDto dto);

    List<KnowYourCustomerDto> retreiveKYCAll();
}

