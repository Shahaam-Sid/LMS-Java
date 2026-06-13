package com.shahaam.lms.dto.member;

import com.shahaam.lms.enums.MemberStatus;

public record MemberResponseDTO(
    
    String memberID,
    String name,
    String phone,
    String email,
    MemberStatus status
) {}