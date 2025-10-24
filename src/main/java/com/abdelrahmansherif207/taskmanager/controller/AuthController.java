package com.abdelrahmansherif207.taskmanager.controller;

import com.abdelrahmansherif207.taskmanager.dto.auth.LoginUserDto;
import com.abdelrahmansherif207.taskmanager.dto.auth.RegisterUserDto;
import com.abdelrahmansherif207.taskmanager.dto.auth.VerifyUserDto;
import com.abdelrahmansherif207.taskmanager.entity.User;
import com.abdelrahmansherif207.taskmanager.responses.LoginResponse;
import com.abdelrahmansherif207.taskmanager.service.AuthService;
import com.abdelrahmansherif207.taskmanager.service.JwtService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private final AuthService authService;
    private final JwtService jwtService;

    public AuthController(AuthService authService, JwtService jwtService) {
        this.authService = authService;
        this.jwtService = jwtService;
    }

    @PostMapping("/signup")
    public ResponseEntity<User> registerUser(@RequestBody RegisterUserDto dto) {
        User registedUser =authService.signup(dto);
        return ResponseEntity.ok(registedUser);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginUserDto dto) {
        User authenticatedUser = authService.login(dto);
        String token = jwtService.generateToken(authenticatedUser);
        LoginResponse loginResponse = new LoginResponse(token,jwtService.getExpirationTime());
        return ResponseEntity.ok(loginResponse);
    }


    @PostMapping("/verify")
    public ResponseEntity<String> verify(@RequestBody VerifyUserDto dto) {
        try {
            authService.verifyUser(dto);
            return ResponseEntity.ok("Account Verified Successfully");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/resend-verification-code")
    public ResponseEntity<String> resendVerificationCode(@RequestBody String email) {
        try {
            authService.resendVerificationCode(email);
            return ResponseEntity.ok("Verification code resent successfully");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
