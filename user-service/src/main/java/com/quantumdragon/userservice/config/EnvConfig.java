package com.quantumdragon.userservice.config;

import org.springframework.context.annotation.Configuration;

@Configuration
public class EnvConfig {
    public static String getEnv(String key) {
        return System.getenv(key);
    }

    public static String getEnv(String key, String defaultValue) {
        String value = System.getenv(key);
        return (value != null) ? value : defaultValue;
    }
}