package com.quantumdragon.userservice.dto;

import lombok.Data;

@Data
public class CreateCustomerResponse {
    private String customerId;
    private String email;
    private String name;
    private String phone;
    private String addressLine1;
    private String addressLine2;
    private String city;
    private String state;
    private String postalCode;
    private String country;
    private String status;
    private String message;
}
