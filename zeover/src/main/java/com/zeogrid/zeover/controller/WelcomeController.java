package com.zeogrid.zeover.controller;

import com.zeogrid.zeover.payload.response.GlobalResponse;
import com.zeogrid.zeover.payload.response.WelcomeResponse;
import com.zeogrid.zeover.service.WelcomeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class WelcomeController {

    private final WelcomeService welcomeService;
    public WelcomeController(WelcomeService welcomeService) {
        this.welcomeService = welcomeService;
    }

    @GetMapping("/welcome")
    public ResponseEntity<GlobalResponse<WelcomeResponse>> welcome() {
        return welcomeService.welcome();
    }
}
