package com.shahaam.lms.models.book;

import java.time.Year;
import java.util.Objects;

import com.shahaam.lms.enums.BookStatus;
import com.shahaam.lms.exceptions.InvalidISBNException;
import com.shahaam.lms.utils.ValidationUtils;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorColumn;
import jakarta.persistence.DiscriminatorType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity(name="books")
@Inheritance(strategy=InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name="type", discriminatorType=DiscriminatorType.STRING)
@NoArgsConstructor
@ToString(callSuper=true)
public abstract class AbstractBook {
    @Id
    @Column(name = "isbn",length=13, nullable = false)
    protected String ISBN;

    @Column(name = "title" ,length=50, nullable = false)
    protected String title;

    @Column(name = "author", length=30, nullable = false)
    protected String author;

    @Column(name = "genre", length=20, nullable = false)
    protected String genre;

    @Column(name = "published_year", columnDefinition = "SMALLINT", nullable = false)
    protected Integer publishedYear;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    protected BookStatus status;

    public AbstractBook(String ISBN, String title, String author, String genre,
        Integer publishedYear) {
            setISBN(ISBN);
            setTitle(title);
            setAuthor(author);
            setGenre(genre);
            setPublishedYear(publishedYear);
            setStatus(BookStatus.AVAILABLE);
        }
    public AbstractBook(String ISBN, String title, String author, String genre,
        BookStatus status, Integer publishedYear) {
            setISBN(ISBN);
            setTitle(title);
            setAuthor(author);
            setGenre(genre);
            setPublishedYear(publishedYear);
            setStatus(status);
        }

    // setter
    public void setISBN(String ISBN) throws IllegalArgumentException {
        ValidationUtils.checkNullString(ISBN, "ISBN");
        if (!ValidationUtils.isValidISBN(ISBN)) throw new InvalidISBNException(ISBN);
        this.ISBN = ISBN;
    }
    public void setTitle(String title) throws IllegalArgumentException {
        ValidationUtils.checkNullString(title, "Book Title");
        ValidationUtils.checkLengthString(title, 3, 50, "Title");

        this.title = title;
    }
    public void setAuthor(String author) throws IllegalArgumentException {
        ValidationUtils.checkNullString(author, "Author");
        ValidationUtils.checkLengthString(author, 3, 30, "Author");

        this.author = author;
    }
    public void setGenre(String genre) throws IllegalArgumentException {
        ValidationUtils.checkNullString(genre, "Genre");
        ValidationUtils.checkLengthString(genre, 3, 20, "Genre");
    
        this.genre = genre;
    }
    public void setPublishedYear(Integer year) throws IllegalArgumentException {
        if (year != null && (year < -699 || year > Year.now().getValue()))
            throw new IllegalArgumentException("Invalid Year of Publish");

        this.publishedYear = year;
    }
    public void setStatus(BookStatus status) {this.status = status;}

    // getter
    public String getISBN() {return ISBN;}
    public String getTitle() {return title;}
    public String getAuthor() {return author;}
    public String getGenre() {return genre;}
    public Integer getPublishedYear() {return publishedYear;}
    public String getStatus() {return status.name();}

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
    public abstract Double calculateLateFee(int daysLate);

    @Override
    public boolean equals(Object other) {
        if (this == other) return true;
        if (!(other instanceof AbstractBook)) return false;

        AbstractBook otherBook = (AbstractBook) other;
        return (ISBN.equals(otherBook.ISBN));
    }
    @Override
    public int hashCode() {
        return Objects.hash(ISBN);
    }
}