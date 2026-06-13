package com.shahaam.lms.dto.loan;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record LoanRequestDTO(

    @NotBlank(message = "member id cannot be blank")
    @Size(max = 9, min = 9, message = "member id should be exaclty 9 characters")
    String member_id,

    @NotBlank(message = "isbn cannot be blank")
    @Size(max = 13, min  = 10, message = "isbn must be 10 or 13 characters")
    String isbn
) {}