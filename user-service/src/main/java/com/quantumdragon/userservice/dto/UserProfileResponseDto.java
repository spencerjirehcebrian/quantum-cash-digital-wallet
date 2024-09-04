package com.quantumdragon.userservice.dto;

import com.quantumdragon.userservice.enums.KycStatus;

public record UserProfileResponseDto(
                String addressLine1,
                String addressLine2,
                String city,
                String state,
                String country,
                String postalCode,
                KycStatus kycStatus) {

}
