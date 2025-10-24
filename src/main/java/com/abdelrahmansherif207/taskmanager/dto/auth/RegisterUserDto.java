package com.abdelrahmansherif207.taskmanager.dto.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class RegisterUserDto {
    @NotBlank
    private String name;

    @NotBlank(message = "Email is required!")
    @Email(message = "Invalid email format")
    private String email;

    @NotBlank(message = "Password is required!")
    @Size(min=6, message = "Password must be at least 6 characters")
    private String password;
}
