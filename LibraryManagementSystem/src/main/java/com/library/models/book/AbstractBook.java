package com.library.models.book;

import com.library.enums.BookStatus;
import com.library.enums.BookType;
import java.time.Year;
import java.util.Objects;

/**
 * Abstract Class for Book
 * 
 * @author Muhammad Shahaam Siddiqui
 */
public abstract class AbstractBook {
    private String ISBN;
    private String title;
    private String author;
    private String genre;
    private int publishedYear;
    private BookStatus status;
    private BookType type;

    public AbstractBook(String ISBN, String title, String author, String genre,
        int publishedYear, BookType type) {
            setISBN(ISBN);
            setTitle(title);
            setAuthor(author);
            setGenre(genre);
            setPublishedYear(publishedYear);
            setStatus(BookStatus.AVAILABLE);
            setType(type);
        }

    // abstract methods
    /**
     * checks if book is available
     * @return true if available else false
     */
    public abstract boolean isAvailable();
    /**
     * calculates late fee
     * @param daysLate int number of days
     * @return double late fee
     */
    public abstract double calculateLateFee(int daysLate);

    // setter
    public final void setISBN(String ISBN) throws IllegalArgumentException {
        checkNullString(ISBN, "ISBN");
        if (!isValidISBN(ISBN)) throw new IllegalArgumentException(ISBN + " Invalid ISBN Format");
        this.ISBN = ISBN;
    }
    public final void setTitle(String title) throws IllegalArgumentException {
        checkNullString(title, "Book Title");
        checkLengthString(title, 3, 50, "Title");

        this.title = title;
    }
    public final void setAuthor(String author) throws IllegalArgumentException {
        checkNullString(author, "Author");
        checkLengthString(author, 3, 50, "Author");

        this.author = author;
    }
    public final void setGenre(String genre) throws IllegalArgumentException {
        checkNullString(genre, "Genre");
        checkLengthString(genre, 3, 20, "Genre");
    
        this.genre = genre;
    }
    public final void setPublishedYear(int year) throws IllegalArgumentException {
        if (year < -699 || year > Year.now().getValue())
            throw new IllegalArgumentException("Invalid Year of Publish");

        this.publishedYear = year;
    }
    public final void setStatus(BookStatus status) {this.status = status;}
    public final void setType(BookType type) {this.type = type;}

    // getter
    public String getISBN() {return ISBN;}
    public String getTitle() {return title;}
    public String getAuthor() {return author;}
    public String getGenre() {return genre;}
    public int getPublishedYear() {return publishedYear;}
    public String getStatus() {return status.name();}
    public String getType() {return type.name();}

    @Override
    public String toString() {
        return "ISBN: " + ISBN + "\n" +
        "Title: " + title + "\n" +
        "Author: " + author + "\n" +
        "Genre: " + genre + "\n" +
        "Published Year: " + publishedYear + "\n" +
        "Status: " + status.name() + "\n" +
        "Type: " + type.name() + "\n";
    }
    @Override
    public boolean equals(Object other) {
        if (this == other) return true;
        if (!(other instanceof AbstractBook)) return false;

        AbstractBook otherBook = (AbstractBook) other;
        return (ISBN.equals(otherBook.ISBN) && type.equals(otherBook.type));
    }
    @Override
    public int hashCode() {
        return Objects.hash(ISBN, type);
    }

    // helper method
    /**
     * a static helper method to check for empty strings
     * @param string 
     * @param field field of information
     * @throws IllegalArgumentException if string is null or length is 0 or only contains whitespaces
     */
    protected static void checkNullString(String string, String field) throws IllegalArgumentException {
        if (string == null || string.trim().length() == 0)
            throw new IllegalArgumentException("Invalid " + field);
    }
    /**
     * a static helper method to check to validate length of strings
     * @param string
     * @param lengthMin minimun acceptable length
     * @param lengthMax maximum acceptable length
     * @param field field of information
     * @throws IllegalArgumentException if length is not in acceptable range
     */
    protected static void checkLengthString(String string, int lengthMin, int lengthMax, String field) throws IllegalArgumentException {
        if (string.length() < lengthMin || string.length() > lengthMax)
            throw new IllegalArgumentException("Allowed Length for " + field + ": " + lengthMin + " - " + lengthMax);
    }
    protected static boolean isValidISBN(String isbn) {
        int n = isbn.length();

        if (n == 10) {
            return isValidISBN10(isbn);
        } else if (n == 13) {
            return isValidISBN13(isbn);
        }
        return false;
    }
    private static boolean isValidISBN10(String isbn) {
        int sum = 0;
        for (int i = 0; i < 9; i++) {
            int digit = isbn.charAt(i) - '0';
            if (digit < 0 || digit > 9)
                return false;
            sum += digit * (10 - i);
        }
        char last = isbn.charAt(9);
        if (last != 'X' && (last < '0' || last > '9'))
            return false;
        sum += (last == 'X') ? 10 : (last - '0');
        return sum % 11 == 0;
    }
    private static boolean isValidISBN13(String isbn) {
        int sum = 0;
        for (int i = 0; i < 12; i++) {
            int digit = isbn.charAt(i) - '0';
            if (digit < 0 || digit > 9)
                return false;
            sum += (i % 2 == 0) ? digit : digit * 3; // alternating weights 1 and 3
        }
        int checkDigit = isbn.charAt(12) - '0';
        if (checkDigit < 0 || checkDigit > 9)
            return false;
        return (sum + checkDigit) % 10 == 0;
    }
}