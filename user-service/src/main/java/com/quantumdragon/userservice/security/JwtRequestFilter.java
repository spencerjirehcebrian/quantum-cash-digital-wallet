package com.quantumdragon.userservice.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import com.quantumdragon.userservice.exception.JwtAuthenticationException;

import java.io.IOException;

@Component
public class JwtRequestFilter extends OncePerRequestFilter {

    private final CustomUserDetailsService customUserDetailsService;
    private final JwtTokenUtil jwtTokenUtil;
    private final HandlerExceptionResolver handlerExceptionResolver;

    public JwtRequestFilter(CustomUserDetailsService customUserDetailsService, JwtTokenUtil jwtTokenUtil,
            HandlerExceptionResolver handlerExceptionResolver) {
        this.customUserDetailsService = customUserDetailsService;
        this.jwtTokenUtil = jwtTokenUtil;
        this.handlerExceptionResolver = handlerExceptionResolver;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {

        final String requestTokenHeader = request.getHeader("Authorization");

        String username = null;
        String jwtToken = null;

        if (requestTokenHeader == null || !requestTokenHeader.startsWith("Bearer ")) {
            logger.debug("Authentication header " + requestTokenHeader);
            logger.warn("Authentication header is null/non-bearer");
            chain.doFilter(request, response);
            return;
        }
        try {
            jwtToken = requestTokenHeader.substring(7);
            username = jwtTokenUtil.getUsernameFromToken(jwtToken);
            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                UserDetails userDetails = this.customUserDetailsService.loadUserByUsername(username);
                if (jwtTokenUtil.validateToken(jwtToken, userDetails.getUsername())) {
                    UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                            userDetails, null, userDetails.getAuthorities());
                    usernamePasswordAuthenticationToken
                            .setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
                    logger.info("Token validated - User authenticated: " + username);
                } else {
                    logger.info("Invalid Token - User not authenticated or has logged out");
                    throw new JwtAuthenticationException("User not authenticated or has logged out");
                }
            }
            chain.doFilter(request, response);
        } catch (JwtAuthenticationException e) {
            logger.error("JwtAuthenticationException: " + e.getMessage());
            handlerExceptionResolver.resolveException(request, response, null, e);
        } catch (Exception e) {
            logger.error("Cannot set user authentication: " + e.getMessage());
            handlerExceptionResolver.resolveException(request, response, null,
                    new JwtAuthenticationException("Authentication error"));
        }
    }
}
