package com.shahaam.lms.auth;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record RegisterRequestDTO(
    
    @NotBlank(message = "name cannot be blank")
    @Size(max = 35, min = 3, message = "name must contain 3 -35 characters")
    String name,

    @NotBlank(message = "phone cannot be blank")
    @Size(max = 11, min = 11, message = "phone must be exactly 11 digits")
    String phone,

    @NotBlank(message = "email cannot be blank")
    String email,

    @NotBlank(message = "address cannot be blank")
    @Size(max = 55, message = "address can be atmost 55 characters")
    String address,

    @NotNull(message = "birth_year cannot be null")
    @Min(value = 1910, message = "birthYear cannot be lesser then 1910")
    Integer birthYear,

    @NotBlank(message = "password cannot be blank")
    @Size(min = 8, message = "password cannot be less then 8 characters")
    String password

) {}