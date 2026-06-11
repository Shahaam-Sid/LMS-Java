package com.shahaam.lms.models.book;

import com.shahaam.lms.enums.BookStatus;
import com.shahaam.lms.utils.ValidationUtils;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@NoArgsConstructor
@Getter
@ToString(callSuper=true)
@Entity
@DiscriminatorValue("AUDIOBOOK")
public class AudioBook extends AbstractDigitalBook{
    
    @Column(name = "narrator", length=100)
    private String narrator;

    public AudioBook(String ISBN, String title, String author, String genre,
        Integer publishedYear, String downloadURL, String format, Double fileSizeMB, String narrator) {

            super(ISBN, title, author, genre, publishedYear, downloadURL, format, fileSizeMB);

            setNarrator(narrator);
    }

    public AudioBook(String ISBN, String title, String author, String genre, BookStatus status,
        Integer publishedYear, String downloadURL, String format, Double fileSizeMB, String narrator) {

            super(ISBN, title, author, genre, status, publishedYear, downloadURL, format, fileSizeMB);

            setNarrator(narrator);
    }
    
    // setter
    public final void setNarrator(String narrator) throws IllegalArgumentException {
        ValidationUtils.checkNullString(narrator, "Narrator");
        ValidationUtils.checkLengthString(narrator, 3, 100, "Narrator");

        this.narrator = narrator;
    }
}