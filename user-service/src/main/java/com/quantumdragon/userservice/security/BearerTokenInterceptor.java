package com.quantumdragon.userservice.security;

import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;
import java.io.IOException;

@Component
public class BearerTokenInterceptor implements ClientHttpRequestInterceptor {

    private String token;

    public void setToken(String token) {
        this.token = token;
    }

    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution)
            throws IOException {
        request.getHeaders().set("Authorization", "Bearer " + this.token);
        return execution.execute(request, body);
    }
}