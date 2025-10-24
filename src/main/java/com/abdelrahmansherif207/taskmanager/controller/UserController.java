package com.abdelrahmansherif207.taskmanager.controller;

import com.abdelrahmansherif207.taskmanager.entity.User;
import com.abdelrahmansherif207.taskmanager.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequestMapping("/users")
@RestController
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/me")
    public ResponseEntity<User> getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User CurrentUser = (User) authentication.getPrincipal();
        return ResponseEntity.ok(CurrentUser);
    }

    @GetMapping("/")
    public ResponseEntity<List<User>> getAllUsers() {
        List <User> users = userService.findAll();
        return ResponseEntity.ok(users);
    }
}
