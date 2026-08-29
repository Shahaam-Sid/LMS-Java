package com.shahaam.lms.dto.book;

import com.shahaam.lms.enums.BookStatus;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;

public record BookUpdateRequestDTO(

    BookStatus status,

    @Size(max=25)
    String shelfLocation,

    @Min(value=1, message="Total copies must positive value")
    Integer totalCopies,

    @Min(value=0, message="Available copies must greater then 0")
    Integer availableCopies
) {}