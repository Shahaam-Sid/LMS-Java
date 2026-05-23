package com.library.models.book;

import java.util.Objects;

import com.library.enums.BookType;
import com.library.models.ValidationUtils;

/**
 * class for AudioBook objects
 * 
 * @author Muhammad Shahaam Siddiqui
 */
public class AudioBook extends AbstractDigitalBook{
    
    private String narrator;

    public AudioBook(String ISBN, String title, String author, String genre,
        int publishedYear, String downloadURL, String format, double fileSizeMB, String narrator) {

            super(ISBN, title, author, genre, publishedYear, BookType.AUDIOBOOK, downloadURL, format, fileSizeMB);

            setNarrator(narrator);
    }
    
    // setter
    public final void setNarrator(String narrator) throws IllegalArgumentException {
        ValidationUtils.checkNullString(narrator, "Narrator");
        ValidationUtils.checkLengthString(narrator, 3, 100, "Narrator");

        this.narrator = narrator;
    }
        
    // getter
    public final String getNarrator() {return narrator;}

    @Override
    public String toString() {
        return super.toString() + "Narrator: " + narrator + "\n";
    }
    @Override
    public boolean equals(Object other) {
        if (this == other) return true;
        if (!(other instanceof AudioBook)) return false;

        AudioBook otherBook = (AudioBook) other;
        return (getISBN().equals(otherBook.getISBN()) && getType().equals(otherBook.getType()) &&
        getFormat().equals(otherBook.getFormat()) && getNarrator().equals(otherBook.getNarrator()) &&
        getFileSizeMB() == otherBook.getFileSizeMB());
    }
    @Override
    public int hashCode() {
        return Objects.hash(getISBN(), getType(), getFormat(), getNarrator(), getFileSizeMB());
    }
}