package com.example.social_media.security;

import com.example.social_media.user.User;
import com.example.social_media.user.UserDaoService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserDaoService userDaoService;

    public CustomUserDetailsService(UserDaoService userDaoService) {
        this.userDaoService = userDaoService;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userDaoService.findByEmail(email);
        if (user == null) {
            throw new UsernameNotFoundException("User not found with email: " + email);
        }
        return new CustomUserDetails(user);
    }
} 