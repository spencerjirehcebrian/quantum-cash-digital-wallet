package com.quantumdragon.accountservice.config;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import com.quantumdragon.accountservice.security.JwtAuthenticationFilter;

@Configuration
@EnableWebSecurity
// @EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

        @Autowired
        private JwtAuthenticationFilter jwtAuthenticationFilter;

        private static final Log logger = LogFactory.getLog(SecurityConfig.class);

        @Bean
        public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
                logger.info("Configuring HttpSecurity...");

                http
                                .csrf(csrf -> csrf.disable())
                                .authorizeHttpRequests(authorizeRequests -> authorizeRequests
                                                .requestMatchers("/api/accounts/*/deposit").permitAll()
                                                .anyRequest().authenticated())
                                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

                logger.info("HttpSecurity configuration completed.");

                return http.build();
        }

}
