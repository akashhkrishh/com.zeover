package com.zeogrid.zeover.controller;

import com.zeogrid.zeover.payload.request.LoginRequest;
import com.zeogrid.zeover.payload.request.RegisterRequest;
import com.zeogrid.zeover.payload.response.GlobalResponse;
import com.zeogrid.zeover.payload.response.TokenResponse;
import com.zeogrid.zeover.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public  AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<GlobalResponse<TokenResponse>> register(@Valid @RequestBody RegisterRequest registerRequest) {
        return authService.authRegister(registerRequest);
    }

    @PostMapping("/login")
    public ResponseEntity<GlobalResponse<TokenResponse>> login(@Valid @RequestBody LoginRequest loginRequest) {
        return authService.authLogin(loginRequest);
    }

}
