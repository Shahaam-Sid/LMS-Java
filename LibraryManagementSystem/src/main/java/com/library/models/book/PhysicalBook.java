package com.library.models.book;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

import com.library.db.DBConnection;
import com.library.db.DBUtility;
import com.library.enums.BookStatus;
import com.library.enums.BookType;
import com.library.exceptions.DatabaseException;
import com.library.interfaces.Borrowable;
import com.library.interfaces.Reservable;
import com.library.models.Member;
import com.library.models.ValidationUtils;
import com.library.services.MemberServices;

/**
 * class for PhysicalBook objects
 * 
 * @author Muhammad Shahaam Siddiqui
 */
public class PhysicalBook extends AbstractBook implements Borrowable, Reservable {

    private String shelfLocation;
    private int totalCopies;
    private int availableCopies;

    public PhysicalBook(String ISBN, String title, String author, String genre,
        int publishedYear, String shelfLocation, int totalCopies) {
            super(ISBN, title, author, genre, publishedYear, BookType.PHYSICAL);
            setShelfLocation(shelfLocation);
            setTotalCopies(totalCopies);
            setAvailableCopies(totalCopies);
        }

    // setter
    public final void setShelfLocation(String shelfLocation) throws IllegalArgumentException {
        ValidationUtils.checkNullString(shelfLocation, "Shelf Location");
        ValidationUtils.checkLengthString(shelfLocation, 1, 25, "Shelf Location");

        this.shelfLocation = shelfLocation;
    }
    public final void setTotalCopies(int totalCopies) throws IllegalArgumentException {
        if (totalCopies < 1) throw new IllegalArgumentException("Total Book Copies must be 1 or more");

        this.totalCopies = totalCopies;
    }
    public final void setAvailableCopies(int availableCopies) throws IllegalArgumentException {
        if (availableCopies < 0 || availableCopies > totalCopies) 
            throw new IllegalArgumentException("Available Copies must atleast 0 and lesser or equals to total copies");
    
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

        if (!isReservationQueueEmpty())
            System.out.println("Notice: " + peakReservationQueue().getName() + " has a reservation for this book");
        return true;
    }
    @Override
    public int getAvailableCount() {return availableCopies;}
    @Override
    public LocalDate calculateDueDate() {return LocalDate.now().plusDays(14);}
    /**
     * Checks if reservation queue is empty
     * @return true if empty else false
     */
    public boolean isReservationQueueEmpty() {return DBUtility.isEmpty("reservations");}
    // Reservable methods
    
    @Override
    public boolean reserve(Member member) throws DatabaseException {
        int output = 0;
        try (Connection conn = DBConnection.getConnection()) {
            try (PreparedStatement ps = conn.prepareStatement("SELECT * FROM reservations WHERE member_id = ? AND isbn = ?")) {
                ps.setString(1, member.getMemberID());
                ps.setString(2, getISBN());
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) return false;
                }
            }
            String sql = """
                    INSERT INTO reservations (isbn, member_id, notified, position)
                    VALUES (?, ?, 0, (
                        SELECT COALESCE(MAX(position), 0) + 1
                        FROM reservations AS br
                        WHERE br.isbn = ?
                    ))
                    """;
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, getISBN());
                ps.setString(2, member.getMemberID());
                ps.setString(3, getISBN());

                output = ps.executeUpdate();
            }
        } catch (SQLException e) {
            throw new DatabaseException(e.getErrorCode(), e);
        }

        return output == 1;
    }
    /**
     * returns first in reservation queue
     * @return Member object
     * @throws DatabaseException Error From Database
     * @throws RuntimeException if Queue is Empty
     */
    public Member peakReservationQueue() throws DatabaseException, RuntimeException {
        String sql = """
                SELECT member_id, position
                FROM reservations
                WHERE isbn = ?
                ORDER BY position ASC
                LIMIT 1
                """;
        try (Connection conn = DBConnection.getConnection();
        PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, getISBN());
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return MemberServices.mapMemberFromDB(rs);
            }
        } catch (SQLException e) {
            throw new DatabaseException(e.getErrorCode(), e);
        }
        throw new RuntimeException("No Record found");
    }
    @Override
    public boolean cancelReservation(Member member) throws DatabaseException {
        int rowsAffected = 0;
        try (Connection conn = DBConnection.getConnection()) {
            try (PreparedStatement ps = conn.prepareStatement("SELECT * FROM reservations WHERE member_id = ? AND isbn = ?")) {
                ps.setString(1, member.getMemberID());
                ps.setString(2, getISBN());
                try (ResultSet rs = ps.executeQuery()) {
                    if (!rs.next()) return false;
                }
            }
            int targetPosition = getQueuePosition(member);
            if (targetPosition == -1) return false;
            try {
                conn.setAutoCommit(false);
                String sql1 = """
                        DELETE FROM reservations
                        WHERE isbn = ? AND member_id = ?;
                        """;
                try (PreparedStatement ps = conn.prepareStatement(sql1)) {
                    ps.setString(1, getISBN());
                    ps.setString(2, member.getMemberID());
                    rowsAffected += ps.executeUpdate();
                }
                String sql2 = """
                        UPDATE reservations
                        SET position = position - 1
                        WHERE isbn = ? AND position > ?;
                        """;
                try (PreparedStatement ps = conn.prepareStatement(sql2)) {
                    ps.setString(1, getISBN());
                    ps.setInt(2, targetPosition);
                    rowsAffected += ps.executeUpdate();
                }               
            } catch (SQLException e) {
                conn.rollback();
                throw e;
            } finally {
                if (rowsAffected == 2) conn.commit();
                else conn.rollback();
                conn.setAutoCommit(true);
            }
        } catch (SQLException e) {
            throw new DatabaseException(e.getErrorCode(), e);
        }
        return rowsAffected == 2;
    }
    @Override
    public int getQueuePosition(Member member) throws DatabaseException {
        try (Connection conn = DBConnection.getConnection();
        PreparedStatement ps = conn.prepareStatement("SELECT position FROM reservations WHERE member_id = ? AND isbn = ?")) {
            ps.setString(1, member.getMemberID());
            ps.setString(2, getISBN());
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getInt(1);
            }
        } catch (SQLException e) {
            throw new DatabaseException(e.getErrorCode(), e);
        }
        return -1;
    }
    @Override
    public String getQueue() throws DatabaseException {
        StringBuilder sb = new StringBuilder("Reservation Queue for " + getTitle() + " | " + getISBN() + "\n\n");

        try (Connection conn = DBConnection.getConnection();
        PreparedStatement ps = conn.prepareStatement("SELECT member_id FROM reservations WHERE isbn = ?")) {
            ps.setString(1, getISBN());
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    sb.append(MemberServices.mapMemberFromDB(rs).toString());
                    sb.append("\n");
                }
            }
        } catch (SQLException e) {
            throw new DatabaseException(e.getErrorCode(), e);
        }
        return sb.toString();
    }
    /**
     * Notifies Next Member in Queue
     * @throws DatabaseException from Database
     */
    public void notifyNextInQueue() throws DatabaseException, RuntimeException {
        Member member = peakReservationQueue();
        try (Connection conn = DBConnection.getConnection();
        PreparedStatement ps = conn.prepareStatement("""
                UPDATE reservations
                SET notified = 1
                WHERE isbn = ? AND position = 1;
                """)) {

            ps.setString(1, getISBN());
            int output = ps.executeUpdate();
            if (output == 1) System.out.println("Member " + member.getMemberID() + " is next in Reservation Queue for Book " + getISBN());
        } catch (SQLException e) {
            throw new DatabaseException(e.getErrorCode(), e);
        }
    }

    @Override
    public String toString() {
        return super.toString() +
        "Shelf: " + shelfLocation + "\n" + 
        "Total Copies: " + totalCopies + "\n" + 
        "Available Copies: " + availableCopies + "\n";
    }
}