package com.zeogrid.zeover.controller;

import com.zeogrid.zeover.model.User;
import com.zeogrid.zeover.service.UserService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/public/loader")
    public List<User> findAll() {
        return userService.findAllUser();
    }

}
