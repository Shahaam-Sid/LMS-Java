package com.library.models.book;

import com.library.enums.BookType;
import com.library.interfaces.Searchable;
import java.util.Objects;

public class EBook extends AbstractDigitalBook implements Searchable {
    
    public EBook(String ISBN, String title, String author, String genre,
        int publishedYear, String downloadURL, String format) {

            super(ISBN, title, author, genre, publishedYear, BookType.EBOOK, downloadURL, format);
        }

    // Searchable methods
    @Override
    public boolean matchesQuery(String query) {
        String q = query.toLowerCase();
        return getTitle().toLowerCase().contains(q) ||
        getAuthor().toLowerCase().contains(q) ||
        getISBN().toLowerCase().contains(q) ||
        getGenre().toLowerCase().contains(q) ||
        format.toLowerCase().contains(q);
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) return true;
        if (!(other instanceof EBook)) return false;

        EBook otherBook = (EBook) other;
        return (ISBN.equals(otherBook.ISBN) && type.equals(otherBook.type) &&
        format.equals(otherBook.format) && fileSizeMB == otherBook.fileSizeMB);
    }
    @Override
    public int hashCode() {
        return Objects.hash(ISBN, type, format, fileSizeMB);
    }
}