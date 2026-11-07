package com.shahaam.lms.dto.member;

import com.shahaam.lms.enums.MemberStatus;

public record MemberResponseDTO(
    
    String memberID,
    String name,
    MemberStatus status
) {}