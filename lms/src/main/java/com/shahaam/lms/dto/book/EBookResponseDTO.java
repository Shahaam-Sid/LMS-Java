package com.shahaam.lms.dto.book;

import com.shahaam.lms.enums.BookStatus;
import com.shahaam.lms.enums.BookType;

public record EBookResponseDTO(
    String ISBN,
    String title,
    String author,
    String genre,
    Integer publishedYear,
    BookStatus status,
    BookType type
) implements BookResponseDTO {}