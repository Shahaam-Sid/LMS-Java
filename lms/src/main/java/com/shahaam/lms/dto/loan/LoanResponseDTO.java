package com.shahaam.lms.dto.loan;

public record LoanResponseDTO(
    
    String loanID,
    String memberID,
    String ISBN,
    String borrowDate,
    String dueDate,
    String returnDate,
    Double fineAmount
) {}