package com.shahaam.lms.auth;

public record AuthenticationResponseDTO(
    String adminID,
    String name,
    String email,
    String phone,
    String token
) {}