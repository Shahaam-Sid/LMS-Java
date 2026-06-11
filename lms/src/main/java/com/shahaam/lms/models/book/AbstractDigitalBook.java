package com.shahaam.lms.models.book;

import com.shahaam.lms.enums.BookStatus;
import com.shahaam.lms.interfaces.Borrowable;
import com.shahaam.lms.utils.ValidationUtils;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * Abstract class for digital books
 * 
 * @author Muhammad Shahaam Siddiqui
 */
@NoArgsConstructor
@Getter
@ToString(callSuper=true)
@MappedSuperclass
public abstract class AbstractDigitalBook extends AbstractBook implements Borrowable{

    @Column(name = "download_url", length=2000)
    private String downloadURL;

    @Column(name = "format", length=30)
    private String format;

    private Double fileSizeMB;

    public AbstractDigitalBook(String ISBN, String title, String author, String genre,
        Integer publishedYear, String downloadURL, String format, Double fileSizeMB) {

            super(ISBN, title, author, genre, publishedYear);

            setDownloadURL(downloadURL);
            setFormat(format);
            setFileSizeMB(fileSizeMB);
        }
    public AbstractDigitalBook(String ISBN, String title, String author, String genre, BookStatus status,
        Integer publishedYear, String downloadURL, String format, Double fileSizeMB) {

            super(ISBN, title, author, genre, status, publishedYear);

            setDownloadURL(downloadURL);
            setFormat(format);
            setFileSizeMB(fileSizeMB);
        }

    // setter
    public final void setDownloadURL(String url) throws IllegalArgumentException {
        ValidationUtils.checkNullString(url, "Download URL");
        ValidationUtils.checkLengthString(url, 15, 2000, "Download URL");

        this.downloadURL = url;
    }
    public final void setFormat(String format) throws IllegalArgumentException {
        ValidationUtils.checkNullString(format, "File Format");
        ValidationUtils.checkLengthString(format, 3, 30, "File Format");

        this.format = format;
    }
    public final void setFileSizeMB(Double fileSizeMB) throws IllegalArgumentException {
        if (fileSizeMB <= 0) throw new IllegalArgumentException("File must be bigger then ) mbs");
        
        this.fileSizeMB = fileSizeMB;
    }

    // Abstract Book methods
    @Override
    public boolean isAvailable() {return true;}
    @Override
    public Integer getAvailableCount() {return Integer.MAX_VALUE;}
}