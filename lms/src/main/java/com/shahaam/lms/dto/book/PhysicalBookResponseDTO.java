package com.shahaam.lms.dto.book;

import com.shahaam.lms.enums.BookStatus;
import com.shahaam.lms.enums.BookType;

public record PhysicalBookResponseDTO(
    String ISBN,
    String title,
    String author,
    String genre,
    Integer publishedYear,
    BookStatus status,
    String shelfLocation,
    BookType type
) implements BookResponseDTO {}