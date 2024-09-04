package com.quantumdragon.userservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class KnowYourCustomerDto {
    private String idType;
    private String idNumber;
    private ZonedDateTime idExpiryDate;
    private String addressProofType;
    private String addressProofDocument;
    private String idProofDocument;
}
