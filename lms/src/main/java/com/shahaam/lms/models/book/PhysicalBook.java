package com.shahaam.lms.models.book;

import com.shahaam.lms.enums.BookStatus;
import com.shahaam.lms.interfaces.Borrowable;
import com.shahaam.lms.utils.ValidationUtils;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
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
    private Integer totalCopies;

    @Column(name = "available_copies")
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
    public void setShelfLocation(String shelfLocation) throws IllegalArgumentException {
        ValidationUtils.checkNullString(shelfLocation, "Shelf Location");
        ValidationUtils.checkLengthString(shelfLocation, 1, 25, "Shelf Location");

        this.shelfLocation = shelfLocation;
    }
    public void setTotalCopies(Integer totalCopies) throws IllegalArgumentException {
        if (totalCopies != null && totalCopies < 1) throw new IllegalArgumentException("Total Book Copies must be 1 or more");

        this.totalCopies = totalCopies;
    } // Total Copies to be set not null before setting available copies
    public void setAvailableCopies(Integer availableCopies) throws IllegalArgumentException {
        if (totalCopies == null) {
            this.availableCopies = null;
            return;
        }
        if (availableCopies != null && (availableCopies < 0 || availableCopies > totalCopies)) 
            throw new IllegalArgumentException("Available Copies must atleast 0 and lesser or equals to total copies");
    
        this.availableCopies = availableCopies;
    }

    public void decrementAvailableCopies() {
        if (availableCopies == 0)
            throw new IllegalArgumentException("Available copies cannot be decremented, is 0");
        availableCopies--;
    }
    public void incrementAvailableCopies() {
        if (availableCopies == totalCopies)
            throw new IllegalArgumentException("Available copies cannot be greater then Total copies");
        availableCopies++;
    }

    // Borrowable methods
    @Override
    public boolean isAvailable() {return (status == BookStatus.AVAILABLE) && (availableCopies > 0);}
    @Override
    public Double calculateLateFee(int daysLate) {return daysLate * 5.0;}
    @Override
    public Integer getAvailableCount() {return availableCopies;}
}