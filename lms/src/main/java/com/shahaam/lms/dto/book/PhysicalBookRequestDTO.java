package com.shahaam.lms.dto.book;

import com.shahaam.lms.enums.BookType;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record PhysicalBookRequestDTO(

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

    @NotBlank(message="shelfLocation cannot be blank")
    @Size(max=25)
    String shelfLocation,

    @NotNull
    @Min(value=1, message="Total copies must greater then 0")
    Integer totalCopies,

    @NotNull
    BookType type

) implements BookRequestDTO {}