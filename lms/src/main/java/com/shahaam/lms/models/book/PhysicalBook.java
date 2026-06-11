package com.shahaam.lms.models.book;

import com.shahaam.lms.enums.BookStatus;
import com.shahaam.lms.interfaces.Borrowable;
import com.shahaam.lms.utils.ValidationUtils;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.validation.constraints.Min;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * class for PhysicalBook objects
 * 
 * @author Muhammad Shahaam Siddiqui
 */
@Entity
@DiscriminatorValue("PHYSICAL")
@NoArgsConstructor
@Getter
@ToString(callSuper=true)
public class PhysicalBook extends AbstractBook implements Borrowable {

    @Column(name = "shelf_location", length=25)
    private String shelfLocation;

    @Column(name = "total_copies")
    @Min(value=1, message="Total copies must greater then 0")
    private Integer totalCopies;

    @Column(name = "available_copies")
    @Min(value=0, message="Available copies must positive value")
    private Integer availableCopies;

    public PhysicalBook(String ISBN, String title, String author, String genre,
        Integer publishedYear, String shelfLocation, Integer totalCopies) {
            super(ISBN, title, author, genre, publishedYear);
            setShelfLocation(shelfLocation);
            setTotalCopies(totalCopies);
            setAvailableCopies(totalCopies);
        }
    public PhysicalBook(String ISBN, String title, String author, String genre, BookStatus status,
        Integer publishedYear, String shelfLocation, Integer availableCopies, Integer totalCopies) {
            super(ISBN, title, author, genre, status, publishedYear);
            setShelfLocation(shelfLocation);
            setTotalCopies(totalCopies);
            setAvailableCopies(availableCopies);
        }

        // setter
    public final void setShelfLocation(String shelfLocation) throws IllegalArgumentException {
        ValidationUtils.checkNullString(shelfLocation, "Shelf Location");
        ValidationUtils.checkLengthString(shelfLocation, 1, 25, "Shelf Location");

        this.shelfLocation = shelfLocation;
    }
    public final void setTotalCopies(Integer totalCopies) throws IllegalArgumentException {
        if (totalCopies < 1) throw new IllegalArgumentException("Total Book Copies must be 1 or more");

        this.totalCopies = totalCopies;
    }
    public final void setAvailableCopies(Integer availableCopies) throws IllegalArgumentException {
        if (availableCopies < 0 || availableCopies > totalCopies) 
            throw new IllegalArgumentException("Available Copies must atleast 0 and lesser or equals to total copies");
    
        this.availableCopies = availableCopies;
    }

    // Borrowable methods
    @Override
    public boolean isAvailable() {return (status == BookStatus.AVAILABLE) && (availableCopies > 0);}

    @Override
    public Integer getAvailableCount() {return availableCopies;}
}