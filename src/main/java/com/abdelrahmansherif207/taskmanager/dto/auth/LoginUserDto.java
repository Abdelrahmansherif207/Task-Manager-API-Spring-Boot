package com.abdelrahmansherif207.taskmanager.dto.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class LoginUserDto {
    @NotBlank(message = "Email is required!")
    @Email(message = "Invalid Email Format")
    private String email;

    @NotBlank(message = "Password is required")
    private String password;
}
