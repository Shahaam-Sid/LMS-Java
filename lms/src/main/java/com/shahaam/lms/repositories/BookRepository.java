package com.shahaam.lms.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.shahaam.lms.models.book.AbstractBook;

public interface BookRepository extends JpaRepository<AbstractBook, String> {

    @Query(value = """
            SELECT * FROM books
                WHERE LOWER(isbn) LIKE LOWER(CONCAT('%', :keyword, '%')) OR
                LOWER(title) LIKE LOWER(CONCAT('%', :keyword, '%')) OR
                LOWER(author) LIKE LOWER(CONCAT('%', :keyword, '%')) OR
                LOWER(genre) LIKE LOWER(CONCAT('%', :keyword, '%')) OR
                LOWER(shelf_location) LIKE LOWER(CONCAT('%', :keyword, '%')) OR
                LOWER(format) LIKE LOWER(CONCAT('%', :keyword, '%')) OR
                LOWER(narrator) LIKE LOWER(CONCAT('%', :keyword, '%'))
            """,
        nativeQuery = true
    )
    List<AbstractBook> searchMatching(@Param("keyword") String keyword);

    @Query(
    value = "SELECT * FROM books WHERE status = 'AVAILABLE' AND (available_copies IS NULL OR available_copies > 0)",
    nativeQuery = true
    )
    List<AbstractBook> findAvailableBooks();
}