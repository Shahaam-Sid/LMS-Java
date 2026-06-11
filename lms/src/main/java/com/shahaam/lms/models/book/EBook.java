package com.shahaam.lms.models.book;

import com.shahaam.lms.enums.BookStatus;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * EBook class for EBook objects
 * 
 * @author Muhammad Shahaam Siddiqui
 */
@NoArgsConstructor
@ToString(callSuper=true)
@Entity
@DiscriminatorValue("EBOOK")
public class EBook extends AbstractDigitalBook {

    public EBook(String ISBN, String title, String author, String genre,
        Integer publishedYear, String downloadURL, String format, Double fileSizeMB) {

            super(ISBN, title, author, genre, publishedYear, downloadURL, format, fileSizeMB);
        }
    public EBook(String ISBN, String title, String author, String genre, BookStatus status,
        Integer publishedYear, String downloadURL, String format, Double fileSizeMB) {

            super(ISBN, title, author, genre, status, publishedYear, downloadURL, format, fileSizeMB);
        }
}  