package com.quantumdragon.accountservice.security;

import java.util.UUID;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.security.access.expression.SecurityExpressionRoot;
import org.springframework.security.access.expression.method.MethodSecurityExpressionOperations;
import org.springframework.security.core.Authentication;

public class CustomMethodSecurityExpressionRoot extends SecurityExpressionRoot
        implements MethodSecurityExpressionOperations {

    private Object filterObject;
    private Object returnObject;

    private static final Log logger = LogFactory.getLog(CustomMethodSecurityExpressionRoot.class);

    public CustomMethodSecurityExpressionRoot(Authentication authentication) {
        super(authentication);
        logger.info("CustomMethodSecurityExpressionRoot - Authentication: " + authentication);
    }

    public boolean hasUserId(UUID userId) {
        Object principal = this.getPrincipal();
        logger.info("hasUserId check - Requested userId: " + userId + " Principal: " + principal);

        boolean result = false;
        if (principal instanceof String) {
            try {
                UUID principalUuid = UUID.fromString((String) principal);
                result = principalUuid.equals(userId);
            } catch (IllegalArgumentException e) {
                logger.error("Principal is not a valid UUID: " + principal, e);
            }
        } else if (principal instanceof UUID) {
            result = principal.equals(userId);
        }

        logger.info("hasUserId result: " + result);
        return result;
    }

    @Override
    public void setFilterObject(Object filterObject) {
        this.filterObject = filterObject;
    }

    @Override
    public Object getFilterObject() {
        return filterObject;
    }

    @Override
    public void setReturnObject(Object returnObject) {
        this.returnObject = returnObject;
    }

    @Override
    public Object getReturnObject() {
        return returnObject;
    }

    @Override
    public Object getThis() {
        return this;
    }
}
