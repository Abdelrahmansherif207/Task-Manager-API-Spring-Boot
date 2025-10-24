package com.abdelrahmansherif207.taskmanager.service;

import com.abdelrahmansherif207.taskmanager.dto.auth.LoginUserDto;
import com.abdelrahmansherif207.taskmanager.dto.auth.RegisterUserDto;
import com.abdelrahmansherif207.taskmanager.dto.auth.VerifyUserDto;
import com.abdelrahmansherif207.taskmanager.entity.User;
import com.abdelrahmansherif207.taskmanager.repository.UserRepository;
import jakarta.mail.MessagingException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;

@Service

public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final EmailService emailService;

    // Constructor
    public AuthService(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            AuthenticationManager authenticationManager,
            EmailService emailService
    )
    {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.emailService = emailService;
    }

    // Signup
    public User signup(RegisterUserDto dto) {
        // Check if email already exists
        if (userRepository.findByEmail(dto.getEmail()).isPresent()) {
            throw new RuntimeException("Email already in use!");
        }

        // Create new user
        User user = new User(
            dto.getName(),
            dto.getEmail(),
            passwordEncoder.encode(dto.getPassword())
        );

        // Generate and set verification code (6-digit number)
        String verificationCode = generateVerificationCode();
        user.setVerificationCode(verificationCode);
        
        // Set verification code expiration (24 hours from now)
        user.setVerificationCodeExpiration(
            LocalDateTime.now().plusHours(24).toString()
        );

        // Save user first to get the ID
        User savedUser = userRepository.save(user);

        try {
            // Send verification email
            emailService.sendEmail(
                savedUser.getEmail(),
                "Verify your email",
                "Your verification code is: " + verificationCode
            );
        } catch (Exception e) {
            // Log the error but don't fail the signup process
            System.err.println("Failed to send verification email: " + e.getMessage());
        }

        return savedUser;
    }

    // Login
    public User login (LoginUserDto dto) {
        User user = userRepository.findByEmail(dto.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found!"));

        if (!user.isEnabled()) {
            throw new RuntimeException("Account not verified. Please verify your email.");
        }
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        dto.getEmail(),
                        dto.getPassword()
                )
        );
        return user;
    }

    // Verify user
    public void verifyUser(VerifyUserDto dto) {
        Optional<User> optionalUser = userRepository.findByEmail(dto.getEmail());
        if (optionalUser.isEmpty()) {
            throw new RuntimeException("User not found!");
        }
        
        User user = optionalUser.get();
        
        // Check if verification code exists
        if (user.getVerificationCode() == null || user.getVerificationCodeExpiration() == null) {
            throw new RuntimeException("No verification code found for this user!");
        }
        
        // Check if verification code has expired
        LocalDateTime expirationTime = LocalDateTime.parse(user.getVerificationCodeExpiration());
        if (LocalDateTime.now().isAfter(expirationTime)) {
            throw new RuntimeException("Verification code expired!");
        }
        
        // Verify the code matches
        if (!user.getVerificationCode().equals(dto.getVerificationCode())) {
            throw new RuntimeException("Invalid verification code!");
        }
        
        // Enable the user and clear verification data
        user.setEnabled(true);
        user.setVerificationCode(null);
        user.setVerificationCodeExpiration(null);
        userRepository.save(user);
    }

    public void resendVerificationCode(String email) {
        Optional<User> optionalUser = userRepository.findByEmail(email);
        if(optionalUser.isPresent()) {
            User user = optionalUser.get();
            if(user.isEnabled()) {
                throw new RuntimeException("Account is already verified!");
            }
            user.setVerificationCode(generateVerificationCode());
            user.setVerificationCodeExpiration(LocalDateTime.now().plusHours(24).toString()); // ADD THIS
            sendVerificationEmail(user);
            userRepository.save(user);
        } else  {
            throw new RuntimeException("User not found!");
        }
    }

    private void sendVerificationEmail(User user) { 
        String subject = "Account Verification";
        String verificationCode = "VERIFICATION CODE " + user.getVerificationCode();
        String htmlMessage = "<html>"
                + "<body style=\"font-family: Arial, sans-serif;\">"
                + "<div style=\"background-color: #f5f5f5; padding: 20px;\">"
                + "<h2 style=\"color: #333;\">Welcome to our app!</h2>"
                + "<p style=\"font-size: 16px;\">Please enter the verification code below to continue:</p>"
                + "<div style=\"background-color: #fff; padding: 20px; border-radius: 5px; box-shadow: 0 0 10px rgba(0,0,0,0.1);\">"
                + "<h3 style=\"color: #333;\">Verification Code:</h3>"
                + "<p style=\"font-size: 18px; font-weight: bold; color: #007bff;\">" + verificationCode + "</p>"
                + "</div>"
                + "</div>"
                + "</body>"
                + "</html>";

        try {
            emailService.sendEmail(user.getEmail(), subject, htmlMessage);
        } catch (MessagingException e) {
            // Handle email sending exception
            e.printStackTrace();
        }
    }
    private String generateVerificationCode() {
        Random random = new Random();
        int code = random.nextInt(900000) + 100000;
        return String.valueOf(code);
    }
}
