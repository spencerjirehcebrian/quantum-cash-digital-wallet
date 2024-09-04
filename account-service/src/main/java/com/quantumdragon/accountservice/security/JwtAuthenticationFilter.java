package com.quantumdragon.accountservice.security;

import com.quantumdragon.accountservice.entity.TokenValidationResponse;
import com.quantumdragon.accountservice.exception.TokenValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final Logger logger = LoggerFactory.getLogger(JwtAuthenticationFilter.class);

    @Autowired
    private JwtTokenValidation jwtTokenValidation;

    @Autowired
    private HandlerExceptionResolver handlerExceptionResolver;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String token = extractToken(request);
        if (isPermittedUrl(request)) {
            // Proceed without triggering authentication logic for permitted URLs
            filterChain.doFilter(request, response);
            return;
        }
        try {
            if (token != null) {
                TokenValidationResponse validationResponse = jwtTokenValidation.validateToken(token);
                if (validationResponse.isValid()) {
                    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                            validationResponse.getUserId(),
                            null,
                            validationResponse.getRoles().stream()
                                    .map(role -> new SimpleGrantedAuthority(role.getRoleName().name()))
                                    .collect(Collectors.toList()));
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                    logger.info("Set authentication for user: " + validationResponse.getUserId());
                } else {
                    throw new AccessDeniedException("Invalid token - Authorization Denied");
                }
            } else {
                throw new AccessDeniedException("No token provided - Authorization Denied");
            }
            filterChain.doFilter(request, response);
        } catch (TokenValidationException e) {
            logger.error("Token validation failed: " + e.getMessage());
            handlerExceptionResolver.resolveException(request, response, null, e);
        } catch (AuthorizationDeniedException e) {
            logger.error("Authorization denied: " + e.getMessage());
            handlerExceptionResolver.resolveException(request, response, null, e);
        } catch (Exception e) {
            logger.error("Unexpected error during authentication: " + e.getMessage());
            handlerExceptionResolver.resolveException(request, response, null,
                    new AccessDeniedException(e.getMessage(), e));
        }
    }

    private String extractToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }

    private boolean isPermittedUrl(HttpServletRequest request) {
        AntPathMatcher pathMatcher = new AntPathMatcher();
        List<String> permittedUrls = Arrays.asList(
                "/api/accounts/*/deposit",
                "/api/register");

        return permittedUrls.stream()
                .anyMatch(urlPattern -> pathMatcher.match(urlPattern, request.getServletPath()));
    }
}