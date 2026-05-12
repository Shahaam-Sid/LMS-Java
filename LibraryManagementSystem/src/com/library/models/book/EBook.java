package com.library.models.book;

import com.library.enums.BookType;
import com.library.interfaces.Searchable;
import java.util.Objects;

/**
 * class for EBook objects
 * 
 * @author Muhammad Shahaam Siddiqui
 */
public class EBook extends AbstractDigitalBook implements Searchable {
    
    public EBook(String ISBN, String title, String author, String genre,
        int publishedYear, String downloadURL, String format, double fileSizeMB) {

            super(ISBN, title, author, genre, publishedYear, BookType.EBOOK, downloadURL, format, fileSizeMB);
        }

    // Searchable methods
    @Override
    public boolean matchesQuery(String query) {
        String q = query.toLowerCase();
        return getTitle().toLowerCase().contains(q) ||
        getAuthor().toLowerCase().contains(q) ||
        getISBN().toLowerCase().contains(q) ||
        getGenre().toLowerCase().contains(q) ||
        getFormat().toLowerCase().contains(q);
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