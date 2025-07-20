package com.zeogrid.zeover.utils;

import com.zeogrid.zeover.enums.RoleType;
import com.zeogrid.zeover.model.Role;
import com.zeogrid.zeover.model.User;
import com.zeogrid.zeover.repository.RoleRepository;
import com.zeogrid.zeover.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import java.util.Set;

@Component
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        Role adminRole = roleRepository.save(new Role(null, RoleType.ROLE_ADMIN));
        Role userRole = roleRepository.save(new Role(null, RoleType.ROLE_USER));
        Role moderatorRole = roleRepository.save(new Role(null, RoleType.ROLE_MODERATOR));
        Role superadminRole = roleRepository.save(new Role(null, RoleType.ROLE_SUPER_ADMIN));

        User admin = User.builder()
                .username("admin")
                .email("admin@app.com")
                .enabled(true)
                .roles(Set.of(adminRole))
                .password(passwordEncoder.encode("admin123"))
                .build();

        User user = User.builder()
                .username("user")
                .email("user@app.com")
                .enabled(true)
                .roles(Set.of(userRole))
                .password(passwordEncoder.encode("user123"))
                .build();


        User moderator = User.builder()
                .username("moderator")
                .email("moderator@app.com")
                .enabled(true)
                .roles(Set.of(moderatorRole))
                .password(passwordEncoder.encode("moderator123"))
                .build();

        User superAdmin = User.builder()
                .username("superadmin")
                .email("superadmin@app.com")
                .enabled(true)
                .roles(Set.of(superadminRole))
                .password(passwordEncoder.encode("superadmin123"))
                .build();

        userRepository.save(admin);
        userRepository.save(user);
        userRepository.save(moderator);
        userRepository.save(superAdmin);

    }
}
