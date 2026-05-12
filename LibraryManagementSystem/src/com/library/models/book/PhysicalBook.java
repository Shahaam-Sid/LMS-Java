package com.library.models.book;

import com.library.enums.BookStatus;
import com.library.enums.BookType;
import com.library.interfaces.Borrowable;
import com.library.interfaces.Reservable;
import com.library.interfaces.Searchable;
import com.library.models.Member;
import java.time.LocalDate;
import java.util.LinkedList;
import java.util.Queue;

/**
 * class for PhysicalBook objects
 * 
 * @author Muhammad Shahaam Siddiqui
 */
public class PhysicalBook extends AbstractBook implements Searchable, Borrowable, Reservable {

    private String shelfLocation;
    private int totalCopies;
    private int availableCopies;
    private final Queue<Member> reservationQueue;

    public PhysicalBook(String ISBN, String title, String author, String genre,
        int publishedYear, String shelfLocation, int totalCopies) {
            super(ISBN, title, author, genre, publishedYear, BookType.PHYSICAL);
            setShelfLocation(shelfLocation);
            setTotalCopies(totalCopies);
            setAvailableCopies(totalCopies);
            this.reservationQueue = new LinkedList<>();
        }

    // setter
    public final void setShelfLocation(String shelfLocation) throws IllegalArgumentException {
        checkNullString(shelfLocation, "Shelf Location");
        checkLengthString(shelfLocation, 1, 25, "Shelf Location");

        this.shelfLocation = shelfLocation;
    }
    public final void setTotalCopies(int totalCopies) throws IllegalArgumentException {
        if (totalCopies < 1) throw new IllegalArgumentException("Total Book Copies must be 1 or more");

        this.totalCopies = totalCopies;
    }
    public final void setAvailableCopies(int availableCopies) throws IllegalArgumentException {
        if (availableCopies < 0 || availableCopies > totalCopies) 
            throw new IllegalArgumentException("Available Copies must atleast 0 and lesseror equals to total copies");
    
        this.availableCopies = availableCopies;
    }

    // getter
    public final String getShelfLocation() {return shelfLocation;}
    public final int getTotalCopies() {return totalCopies;}
    public final int getAvailableCopies() {return availableCopies;}

    // Abstract Book Methods
    @Override
    public boolean isAvailable() {return availableCopies > 0;}
    @Override
    public double calculateLateFee(int daysLate) {return daysLate * 5.0;}

    // Searchable methods
    @Override
    public boolean matchesQuery(String query) {
        String q = query.toLowerCase();
        return getTitle().toLowerCase().contains(q) ||
        getAuthor().toLowerCase().contains(q) ||
        getISBN().toLowerCase().contains(q) ||
        getGenre().toLowerCase().contains(q) ||
        shelfLocation.toLowerCase().contains(q);
    }

    // Borrowable methods
    @Override
    public boolean borrow(Member member) {
        if (!isAvailable()) return false;
        availableCopies--;
        if (availableCopies == 0) setStatus(BookStatus.BORROWED);
        return true;
    }
    @Override
    public boolean returnItem(Member member) {
        availableCopies++;
        setStatus(BookStatus.AVAILABLE);

        if (!reservationQueue.isEmpty())
            System.out.println("Notice: " + reservationQueue.peek().getName() + " has a reservation for this book");
        return true;
    }
    @Override
    public int getAvailableCount() {return availableCopies;}
    @Override
    public LocalDate calculateDueDate() {return LocalDate.now().plusDays(14);}

    // Reservable methods
    @Override
    public boolean reserve(Member member) {
        if (reservationQueue.contains(member)) return false;
        return reservationQueue.add(member);
    }
    @Override
    public boolean cancelReservation(Member member) {
        return reservationQueue.remove(member);
    }
    @Override
    public int getQueuePosition(Member member) {
        int pos = 0;
        for (Member m : reservationQueue) {
            pos++;
            if (m.equals(member)) return pos;
        }
        return -1;
    }
    @Override
    public String getQueue() {
        StringBuilder sb = new StringBuilder("Reservation Queue for " + getTitle() + " | " + getISBN() + "\n\n");
        for (Member m : reservationQueue) {
            sb.append(m.toString());
            sb.append("\n");
        }
        return sb.toString();
    }

    @Override
    public String toString() {
        return super.toString() +
        "Shelf: " + shelfLocation + "\n" + 
        "Total Copies: " + totalCopies + "\n" + 
        "Available Copies: " + availableCopies + "\n";
    }
}