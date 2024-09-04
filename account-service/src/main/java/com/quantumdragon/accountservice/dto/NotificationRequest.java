package com.quantumdragon.accountservice.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import java.util.Map;

public record NotificationRequest(

        @NotBlank(message = "User ID is mandatory") String userId,

        @NotBlank(message = "Type is mandatory") String type,

        @NotBlank(message = "Subject is mandatory") String subject,

        @NotEmpty(message = "Content cannot be empty") Map<String, Object> content,

        @NotBlank(message = "Channel is mandatory") String channel

) {

}
