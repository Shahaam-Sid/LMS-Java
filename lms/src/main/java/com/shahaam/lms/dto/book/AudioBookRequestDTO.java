package com.shahaam.lms.dto.book;

import com.shahaam.lms.enums.BookType;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public record AudioBookRequestDTO(

    @NotBlank(message="ISBN cannot be blank")
    @Size(max = 13, min  = 10, message = "isbn must be 10 or 13 characters") 
    String ISBN,

    @NotBlank(message="Title cannot be blank")
    @Size(max=50)
    String title,

    @NotBlank(message="Author cannot be blank")
    @Size(max=30)
    String author,

    @NotBlank(message="Genre cannot be blank")
    @Size(max=20)
    String genre,

    @NotNull
    @Max(value = 9999, message = "Invalid Year")
    @Min(value = -1000, message = "Invalid Year")
    Integer publishedYear,

    @NotBlank(message="download URL cannot be blank")
    @Size(max=2000)
    String downloadURL,

    @NotBlank(message="Format cannot be blank")
    @Size(max=30)
    String format,

    @NotNull
    @Digits(integer = 4, fraction = 2)
    @Positive(message = "file size must be positive")
    double fileSizeMB,

    @NotBlank(message = "narrator cannot be null")
    @Size(max=100)
    String narrator,

    @NotNull
    BookType type
) implements BookRequestDTO {}