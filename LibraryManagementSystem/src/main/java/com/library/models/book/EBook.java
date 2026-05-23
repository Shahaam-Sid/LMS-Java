package com.library.models.book;

import java.util.Objects;

import com.library.enums.BookType;

/**
 * class for EBook objects
 * 
 * @author Muhammad Shahaam Siddiqui
 */
public class EBook extends AbstractDigitalBook {
    
    public EBook(String ISBN, String title, String author, String genre,
        int publishedYear, String downloadURL, String format, double fileSizeMB) {

            super(ISBN, title, author, genre, publishedYear, BookType.EBOOK, downloadURL, format, fileSizeMB);
        }

    @Override
    public boolean equals(Object other) {
        if (this == other) return true;
        if (!(other instanceof EBook)) return false;

        EBook otherBook = (EBook) other;
        return (getISBN().equals(otherBook.getISBN()) && getType().equals(otherBook.getType()) &&
        getFormat().equals(otherBook.getFormat()) && getFileSizeMB() == otherBook.getFileSizeMB());
    }
    @Override
    public int hashCode() {
        return Objects.hash(getISBN(), getType(), getFormat(), getFileSizeMB());
    }
}