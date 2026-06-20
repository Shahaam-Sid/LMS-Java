package com.shahaam.lms.auth;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record AuthenticationRequestDTO(
    
    @NotBlank(message = "email cannot be blank")
    String email,

    @NotBlank(message = "password cannot be blank")
    @Size(min = 8, message = "password cannot be less then 8 characters")
    String password
) {}