package com.quantumdragon.userservice.security;

import com.quantumdragon.userservice.entity.Role;
import com.quantumdragon.userservice.entity.User;
import com.quantumdragon.userservice.repository.UserRepository;

import java.util.HashSet;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    private final Log logger = LogFactory.getLog(CustomUserDetailsService.class);

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmailWithRoles(email);
        if (user == null) {
            throw new UsernameNotFoundException("User not found with email: " + email);
        }
        Set<Role> roles = user.getRoles();
        logger.info("Roles retrieved for " + user.getEmail() + " : " + roles);

        Set<GrantedAuthority> grantedAuthorities = new HashSet<>();
        for (Role role : roles) {
            logger.info("Role name: " + role.getRoleName().name());
            grantedAuthorities.add(new SimpleGrantedAuthority(role.getRoleName().name()));
        }
        logger.info("User retrieved: " + user.getEmail());
        logger.info("Granted authorities: " + grantedAuthorities);
        logger.info("Password hash: " + user.getPasswordHash());

        return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPasswordHash(),
                grantedAuthorities);
    }

}
