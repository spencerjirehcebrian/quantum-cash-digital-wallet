package com.quantumdragon.accountservice.security;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.quantumdragon.accountservice.entity.TokenValidationRequest;
import com.quantumdragon.accountservice.entity.TokenValidationResponse;
import com.quantumdragon.accountservice.exception.TokenValidationException;

import org.springframework.stereotype.Service;

@Service
public class JwtTokenValidation {
    @Autowired
    private RestTemplate restTemplate;

    @Value("${auth.service.url}")
    private String authServiceUrl;

    private static final Log logger = LogFactory.getLog(JwtTokenValidation.class);

    public TokenValidationResponse validateToken(String token) {
        TokenValidationRequest request = new TokenValidationRequest(token);
        try {
            ResponseEntity<TokenValidationResponse> response = restTemplate.postForEntity(
                    authServiceUrl + "/api/auth/validate-token",
                    request,
                    TokenValidationResponse.class);
            logger.info("Token validation response: " + response.getBody());
            return response.getBody();
        } catch (HttpStatusCodeException e) {
            logger.error("HTTP Status Code Error: " + e.getStatusCode());
            logger.error("Response Body: " + e.getResponseBodyAsString());
            throw new TokenValidationException("Token validation failed: " + e.getStatusCode(), e);
        } catch (RestClientException e) {
            logger.error("RestClientException: " + e.getMessage());
            throw new TokenValidationException("Token validation failed due to client error", e);
        } catch (Exception e) {
            logger.error("Unexpected error during token validation", e);
            throw new TokenValidationException("Unexpected error during token validation", e);
        }
    }
}
