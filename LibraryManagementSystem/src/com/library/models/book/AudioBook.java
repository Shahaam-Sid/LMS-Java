package com.library.models.book;

import com.library.enums.BookType;
import com.library.interfaces.Searchable;
import java.util.Objects;

public class AudioBook extends AbstractDigitalBook implements Searchable{
    
    protected String narrator;

    public AudioBook(String ISBN, String title, String author, String genre,
        int publishedYear, String downloadURL, String format, String narrator) {

            super(ISBN, title, author, genre, publishedYear, BookType.AUDIOBOOK, downloadURL, format);

            setNarrator(narrator);
    }
    
    // setter
    public final void setNarrator(String narrator) {
        checkNullString(narrator, "Narrator");
        checkLengthString(narrator, 3, 100, "Narrator");

        this.narrator = narrator;
    }
        
    // getter
    public final String getNarrator() {return narrator;}

    // Searchable methods
    @Override
    public boolean matchesQuery(String query) {
        String q = query.toLowerCase();
        return getTitle().toLowerCase().contains(q) ||
        getAuthor().toLowerCase().contains(q) ||
        getISBN().toLowerCase().contains(q) ||
        getGenre().toLowerCase().contains(q) ||
        narrator.toLowerCase().contains(q);
    }

    @Override
    public String toString() {
        return super.toString() + "Narrator: " + narrator + "\n";
    }
    @Override
    public boolean equals(Object other) {
        if (this == other) return true;
        if (!(other instanceof AudioBook)) return false;

        AudioBook otherBook = (AudioBook) other;
        return (ISBN.equals(otherBook.ISBN) && type.equals(otherBook.type) &&
        format.equals(otherBook.format) && narrator.equals(otherBook.narrator) &&
        fileSizeMB == otherBook.fileSizeMB);
    }
    @Override
    public int hashCode() {
        return Objects.hash(ISBN, type, format, narrator, fileSizeMB);
    }
}