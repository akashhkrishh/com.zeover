package com.zeogrid.zeover.service;

import com.zeogrid.zeover.exception.EmailNotFoundException;
import com.zeogrid.zeover.model.User;
import com.zeogrid.zeover.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Used by Spring Security for authentication.
     * Supports login with either email or username.
     */
    @Override
    public UserDetails loadUserByUsername(String input) throws UsernameNotFoundException {
        String lookup = input.trim().toLowerCase(); // Ensures whitespace doesn't cause mismatch
        System.out.println("Authenticating user: " + lookup);

        return userRepository.findByEmail(lookup)
                .or(() -> userRepository.findByUsername(lookup))
                .orElseThrow(() ->
                        new UsernameNotFoundException("User not found with email or username: " + input));
    }

    /**
     * Explicit method to load by email (optional).
     */
    public UserDetails loadUserByEmail(String email) throws EmailNotFoundException {
        return userRepository.findByEmail(email.trim().toLowerCase())
                .orElseThrow(() -> new EmailNotFoundException("User not found with email: " + email));
    }

    public List<User> findAllUser() {
        return userRepository.findAll();
    }
}
