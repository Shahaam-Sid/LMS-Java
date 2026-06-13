package com.shahaam.lms.dto.member;

import com.shahaam.lms.enums.MemberStatus;

import jakarta.validation.constraints.Size;

public record MemberUpdateRequestDTO(
    @Size(max = 35, message = "name cannot be null")
    String name,

    @Size(max = 11, min = 11, message = "phone must be exactly 11 digits")
    String phone,

    String email,

    @Size(max = 55, message = "address can be atmost 55 characters")
    String address,

    MemberStatus status
) {} 