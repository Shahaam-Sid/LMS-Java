package com.library.models.book;

import com.library.enums.BookType;
import com.library.interfaces.Borrowable;
import com.library.models.Member;
import java.time.LocalDate;

/**
 * Abstract class for digital books
 * 
 * @author Muhammad Shahaam Siddiqui
 */
public abstract class AbstractDigitalBook extends AbstractBook implements Borrowable{

    private String downloadURL;
    private String format;
    private double fileSizeMB;

    public AbstractDigitalBook(String ISBN, String title, String author, String genre,
        int publishedYear, BookType bookType, String downloadURL, String format, double fileSizeMB) {

            super(ISBN, title, author, genre, publishedYear, bookType);

            setDownloadURL(downloadURL);
            setFormat(format);
            setFileSizeMB(fileSizeMB);
        }

    // setter
    public final void setDownloadURL(String url) throws IllegalArgumentException {
        checkNullString(url, "Download URL");
        checkLengthString(url, 15, 2000, "Download URL");

        this.downloadURL = url;
    }
    public final void setFormat(String format) throws IllegalArgumentException {
        checkNullString(format, "File Format");
        checkLengthString(format, 3, 30, "File Format");

        this.format = format;
    }
    public final void setFileSizeMB(double fileSizeMB) throws IllegalArgumentException {
        if (fileSizeMB <= 0) throw new IllegalArgumentException("File must be bigger then ) mbs");
        
        this.fileSizeMB = fileSizeMB;
    }

    // getter
    public final String getDownloadURL() {return downloadURL;}
    public final String getFormat() {return format;}
    public final double getFileSizeMB() {return fileSizeMB;}

    // Abstract Book methods
    @Override
    public boolean isAvailable() {return true;}
    @Override
    public double calculateLateFee(int daysLate) {return daysLate * 2.0;}

    // Borrowable methods
    @Override
    public boolean borrow(Member member) {return true;}
    @Override
    public boolean returnItem(Member member) {return true;}
    @Override
    public int getAvailableCount() {return Integer.MAX_VALUE;}
    @Override
    public LocalDate calculateDueDate() {return LocalDate.now().plusDays(7);}

    @Override
    public String toString() {
        return "ISBN: " + getISBN() + "\n" +
        "Title: " + getTitle() + "\n" +
        "Author: " + getAuthor() + "\n" +
        "Genre: " + getGenre() + "\n" +
        "Published Year: " + getPublishedYear() + "\n" +
        "Type: " + getType() + "\n" + 
        "Format: " + format + "\n" +
        "File Size (MBs): " + fileSizeMB + "\n";
    }
}