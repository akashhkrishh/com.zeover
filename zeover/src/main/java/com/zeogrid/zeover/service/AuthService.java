package com.zeogrid.zeover.service;

import com.zeogrid.zeover.exception.UserAlreadyRegisterException;
import com.zeogrid.zeover.model.Role;
import com.zeogrid.zeover.model.User;
import com.zeogrid.zeover.payload.request.LoginRequest;
import com.zeogrid.zeover.payload.request.RegisterRequest;
import com.zeogrid.zeover.payload.response.GlobalResponse;
import com.zeogrid.zeover.payload.response.TokenResponse;
import com.zeogrid.zeover.repository.RoleRepository;
import com.zeogrid.zeover.repository.UserRepository;
import jakarta.validation.Valid;
import org.springframework.beans.BeanUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;
    private final AuthenticationManager authenticationManager;
    private final JWTService jwtService;

    public AuthService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder,
                       RoleRepository roleRepository,
                       AuthenticationManager authenticationManager,
                       JWTService jwtService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }

    public ResponseEntity<GlobalResponse<TokenResponse>> authRegister(@Valid RegisterRequest registerRequest) {
        String username = registerRequest.getUsername().trim().toLowerCase();
        String email = registerRequest.getEmail().trim().toLowerCase();

        if (userRepository.existsByEmail(email) || userRepository.existsByUsername(username)) {
            throw new UserAlreadyRegisterException("User is already registered in database");
        }

        Role userRole = roleRepository.findByName(com.zeogrid.zeover.enums.RoleType.ROLE_USER)
                .orElseThrow(() -> new RuntimeException("Role not found"));

        User newUser = new User();
        BeanUtils.copyProperties(registerRequest, newUser);
        newUser.setUsername(username);
        newUser.setEmail(email);
        newUser.setRoles(Set.of(userRole));
        newUser.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        newUser.setEnabled(true);
        userRepository.save(newUser);

        String token = jwtService.generateToken(newUser.getId());
        TokenResponse tokenResponse = new TokenResponse(token, newUser.getEmail());

        return ResponseEntity.ok(new GlobalResponse<>(tokenResponse, null, null, true));
    }

    public ResponseEntity<GlobalResponse<TokenResponse>> authLogin(@Valid LoginRequest loginRequest) {
        String input = loginRequest.getEmail().trim().toLowerCase(); // Could be username or email
        Optional<User> userOpt = userRepository.findByEmail(input)
                .or(() -> userRepository.findByUsername(input));

        if (userOpt.isEmpty()) {
            return ResponseEntity.status(401).body(
                    new GlobalResponse<>(null, null, "User not found", false)
            );
        }

        // Don't encode password here, Spring Security does this internally
        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        input,
                        loginRequest.getPassword()
                )
        );

        User user = (User) auth.getPrincipal();
        String token = jwtService.generateToken(user.getId());

        TokenResponse tokenResponse = new TokenResponse(token, user.getEmail());
        return ResponseEntity.ok(new GlobalResponse<>(tokenResponse, null, null, true));
    }
}
