package com.library.models.book;

import com.library.enums.BookType;
import com.library.interfaces.Borrowable;
import com.library.models.Member;
import java.time.LocalDate;

public abstract class AbstractDigitalBook extends AbstractBook implements Borrowable{

    protected String downloadURL;
    protected String format;
    protected double fileSizeMB;

    public AbstractDigitalBook(String ISBN, String title, String author, String genre,
        int publishedYear, BookType bookType, String downloadURL, String format) {

            super(ISBN, title, author, genre, publishedYear, bookType);

            setDownloadURL(downloadURL);
            setFormat(format);
        }

    // setter
    public final void setDownloadURL(String url) {
        checkNullString(url, "Download URL");
        checkLengthString(url, 15, 2000, "Download URL");

        this.downloadURL = url;
    }
    public final void setFormat(String format) {
        checkNullString(format, "File Format");
        checkLengthString(format, 3, 30, "File Format");

        this.format = format;
    }

    // getter
    public final String getDownloadURL() {return downloadURL;}
    public final String getFormat() {return format;}

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
        return "ISBN: " + ISBN + "\n" +
        "Title: " + title + "\n" +
        "Author: " + author + "\n" +
        "Genre: " + genre + "\n" +
        "Published Year: " + publishedYear + "\n" +
        "Type: " + type.name() + "\n" + 
        "Format: " + format + "\n" +
        "File Size (MBs): " + fileSizeMB + "\n";
    }
}