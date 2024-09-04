package com.quantumdragon.accountservice.config;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.expression.method.MethodSecurityExpressionHandler;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.GlobalMethodSecurityConfiguration;

import com.quantumdragon.accountservice.security.CustomMethodSecurityExpressionHandler;

@SuppressWarnings("deprecation")
@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class MethodSecurityConfig extends GlobalMethodSecurityConfiguration {

    private static final Log logger = LogFactory.getLog(MethodSecurityConfig.class);

    @Override
    protected MethodSecurityExpressionHandler createExpressionHandler() {
        logger.info("MethodSecurityExpressionHandler created");
        return new CustomMethodSecurityExpressionHandler();
    }
}
