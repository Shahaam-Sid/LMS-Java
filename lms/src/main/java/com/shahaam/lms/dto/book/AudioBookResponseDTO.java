package com.shahaam.lms.dto.book;

import com.shahaam.lms.enums.BookStatus;
import com.shahaam.lms.enums.BookType;

public record AudioBookResponseDTO(
    String ISBN,
    String title,
    String author,
    String genre,
    Integer publishedYear,
    BookStatus status,
    String Narrator,
    BookType type
) implements BookResponseDTO {}