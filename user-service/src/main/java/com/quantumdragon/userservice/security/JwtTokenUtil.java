package com.quantumdragon.userservice.security;

import com.quantumdragon.userservice.entity.Role;
import com.quantumdragon.userservice.enums.RoleName;
import com.quantumdragon.userservice.repository.BlacklistedTokenRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

@Component
public class JwtTokenUtil {

    private static final Log logger = LogFactory.getLog(JwtTokenUtil.class);

    @Value("${JWT_SECRET}")
    private String secret;

    @Value("${JWT_EXPIRATION}")
    private long expiration;

    @Autowired
    private BlacklistedTokenRepository blacklistedTokenRepository;

    private Key getSigningKey() {
        byte[] keyBytes = secret.getBytes();
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String generateToken(String username, UUID userId, Set<Role> roles) {
        logger.info("Generating token for user: " + username);
        Set<String> roleNames = roles.stream()
                .map(Role::getRoleName)
                .map(Enum::name)
                .collect(Collectors.toSet());
        return Jwts.builder()
                .setSubject(username)
                .claim("user_id", userId)
                .claim("roles", roleNames)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public LocalDateTime getExpirationDateFromToken(String token) {
        return getClaimFromToken(token, Claims::getExpiration).toInstant().atZone(ZoneId.systemDefault())
                .toLocalDateTime();
    }

    public String getUsernameFromToken(String token) {
        return getClaimFromToken(token, Claims::getSubject);
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(getSigningKey()).build().parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public Set<Role> getRolesFromToken(String token) {
        Claims claims = Jwts.parserBuilder().setSigningKey(getSigningKey()).build().parseClaimsJws(token).getBody();
        Object rolesObject = claims.get("roles");

        if (!(rolesObject instanceof List<?>)) {
            throw new IllegalArgumentException("Roles claim is not a List");
        }

        List<?> rolesList = (List<?>) rolesObject;

        return rolesList.stream()
                .filter(role -> role instanceof String)
                .map(role -> (String) role)
                .map(RoleName::valueOf)
                .map(roleName -> new Role(null, null, roleName)) // Create a Role instance with the RoleName
                .collect(Collectors.toSet());
    }

    public UUID getUserIdFromToken(String token) {
        Claims claims = Jwts.parserBuilder().setSigningKey(getSigningKey()).build().parseClaimsJws(token).getBody();
        String userIdString = claims.get("user_id").toString();
        return UUID.fromString(userIdString);
    }

    public boolean validateToken(String token, String username) {
        final String tokenUsername = getUsernameFromToken(token);
        return (username.equals(tokenUsername) && !isTokenExpired(token) && !isTokenBlacklisted(token));
    }

    private boolean isTokenBlacklisted(String token) {
        return blacklistedTokenRepository.findByToken(token).isPresent();
    }

    private Claims getAllClaimsFromToken(String token) {
        return Jwts.parserBuilder().setSigningKey(getSigningKey()).build().parseClaimsJws(token).getBody();
    }

    private <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }

    private boolean isTokenExpired(String token) {
        final Date expiration = getClaimFromToken(token, Claims::getExpiration);
        return expiration.before(new Date());
    }
}
