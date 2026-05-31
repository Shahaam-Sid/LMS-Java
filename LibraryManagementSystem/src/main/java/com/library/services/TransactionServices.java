package com.library.services;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.library.db.DBConnection;
import com.library.exceptions.BookAlreadyBorrowedException;
import com.library.exceptions.BookNotAvailableException;
import com.library.exceptions.BookNotFoundException;
import com.library.exceptions.ChangesNotSavedException;
import com.library.exceptions.DatabaseException;
import com.library.exceptions.MemberLimitExceededException;
import com.library.exceptions.MemberNotFoundException;
import com.library.exceptions.NoActiveBorrowRecordFoundException;
import com.library.interfaces.Borrowable;
import com.library.models.Member;
import com.library.models.Transaction;
import com.library.models.book.AbstractBook;
import com.library.models.book.PhysicalBook;

/**
 * Class for TransactionServices
 * 
 * @author Muhammad Shahaam Siddiqui
 */
public class TransactionServices {

    private final BookServices bookService;
    private final MemberServices memberService;

    public TransactionServices(BookServices bookService, MemberServices memberService) {
        this.bookService = bookService;
        this.memberService = memberService;
    }

    /**
     * creates and return transaction
     * @param isbn of book to borrow
     * @param memberID of member who borrows
     * @return Transaction
     * @throws BookNotFoundException if isbn not found
     * @throws MemberNotFoundException if member id not found
     * @throws MemberLimitExceededException if borrow limit of member exceeds limit
     * @throws UnsupportedOperationException if book not borrowable
     * @throws BookNotAvailableException if book not currently available
     * @throws BookAlreadyBorrowedException if book is already Borrowed by the Member
     * @throws ChangesNotSavedException Unexpected Error from Database
     * @throws DatabaseException from Database
     */
    public Transaction borrowBook(String isbn, String memberID) throws BookNotFoundException,
    MemberNotFoundException, MemberLimitExceededException, UnsupportedOperationException,
    BookNotAvailableException,BookAlreadyBorrowedException,DatabaseException, ChangesNotSavedException {
        AbstractBook book = bookService.getBook(isbn);
        Member member = memberService.getMember(memberID);

        if (!member.canBorrow())
            throw new MemberLimitExceededException(memberID);

        if (!(book instanceof Borrowable))
            throw new UnsupportedOperationException("This Item cannot be borrowed");

        Borrowable borrowable = (Borrowable) book;

        if (!borrowable.isAvailable())
            throw new BookNotAvailableException(book.getTitle());

        borrowable.borrow(member);

        LocalDate dueDate = borrowable.calculateDueDate();
        Transaction t = new Transaction(UUID.randomUUID().toString(), member, book, dueDate);
    
        int rowsAffected = 0;
        try (Connection conn = DBConnection.getConnection()) {
            try (PreparedStatement ps = conn.prepareStatement("SELECT * FROM transactions WHERE member_id = ? AND isbn = ?")) {
                ps.setString(1, member.getMemberID());
                ps.setString(2, book.getISBN());
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) throw new BookAlreadyBorrowedException(book.getTitle(), member.getMemberID());
                }
            }
            try {
                conn.setAutoCommit(false);
                
                if (book.getType().equals("PHYSICAL")) {
                    try (PreparedStatement ps = conn.prepareStatement("UPDATE books SET available_copies = ?, book_status = ? WHERE isbn = ?")) {
                        ps.setInt(1, ((PhysicalBook) book).getAvailableCopies());
                        ps.setString(2, book.getStatus());
                        ps.setString(3, book.getISBN());

                        rowsAffected += ps.executeUpdate();
                    }
                }
                String sql = """
                INSERT INTO transactions (transaction_id, member_id, isbn, borrow_date, due_date)
                VALUES (?, ?, ?, ?, ?)
                """;
                try (PreparedStatement ps = conn.prepareStatement(sql)) {
                    ps.setString(1, t.getTransactionID());
                    ps.setString(2, t.getMember().getMemberID());
                    ps.setString(3, t.getBook().getISBN());
                    ps.setDate(4, Date.valueOf(t.getBorrowDate()));
                    ps.setDate(5, Date.valueOf(t.getDueDate()));

                    rowsAffected += ps.executeUpdate();
                }    
            } catch (SQLException e) {
                conn.rollback();
                throw e;
            } finally {
                if (rowsAffected == 2 || rowsAffected == 1) conn.commit();
                else conn.rollback();
                conn.setAutoCommit(true);
            }
        } catch (SQLException e) {
            throw new DatabaseException(e.getErrorCode(), e);
        }

        if (rowsAffected == 2) return t;
        else throw new ChangesNotSavedException();
    }
    /**
     * closes transaction
     * @param isbn of book to return
     * @param memberId of member who returns
     * @return transaction
     * @throws MemberNotFoundException if member id not found
     * @throws BookNotFoundException if isbn not found
     * @throws NoActiveBorrowRecordFound no active borrow
     * @throws ChangesNotSavedException an error occured
     * @throws DatabaseException from Database
     */
    public Transaction returnBook(String isbn, String memberId) throws MemberNotFoundException,
    BookNotFoundException, DatabaseException, NoActiveBorrowRecordFoundException,
    ChangesNotSavedException, DatabaseException {
        Member member = memberService.getMember(memberId);
        AbstractBook book = bookService.getBook(isbn);
        Transaction active = null;
        try (Connection conn = DBConnection.getConnection()) {
            String sql = """
                    SELECT * FROM transactions
                    WHERE return_date IS NULL
                    AND member_id = ?
                    AND isbn = ?
                    """;
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, member.getMemberID());
                ps.setString(2, book.getISBN());
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) active = mapTransactionFromDB(rs);
                }
            }
            
        } catch (SQLException e) {
            throw new DatabaseException(e.getErrorCode(), e);
        }
        if (active == null) throw new NoActiveBorrowRecordFoundException(book.getTitle(), member.getMemberID()); // fix this

        ((Borrowable) book).returnItem(member);

        active.setReturnDate(LocalDate.now());

        if (active.getDaysOverdue() > 0) {
            double fine = book.calculateLateFee(active.getDaysOverdue());
            active.setFineAmount(fine);
        }

        int rowsAffected = 0;
        try (Connection conn = DBConnection.getConnection()) {
            conn.setAutoCommit(false);
            try{
                try (PreparedStatement ps = conn.prepareStatement("UPDATE books SET available_copies = ?, book_status = ? WHERE isbn = ?")) {
                    ps.setInt(1, ((PhysicalBook) book).getAvailableCopies());
                    ps.setString(2, book.getStatus());
                    ps.setString(3, book.getISBN());

                    rowsAffected += ps.executeUpdate();
                }
                try (PreparedStatement ps = conn.prepareStatement("UPDATE transactions SET return_date = ?, fine_amount = ? WHERE transaction_id = ?")) {
                    ps.setDate(1, Date.valueOf(active.getReturnDate()));
                    ps.setDouble(2, active.getFineAmount());
                    ps.setString(3, active.getTransactionID());

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
        if (rowsAffected == 2) return active;
        else throw new ChangesNotSavedException();
    }
    /**
     * returns a list of overdues
     * @return list of overdue transactions
     * @throws DatabaseException from Database
     */
    public List<Transaction> getOverdueTransactions() throws DatabaseException {
        List<Transaction> overdue = new ArrayList<>();

        String sql = """
                SELECT * FROM transactions
                WHERE return_date IS NULL
                AND due_date < CURDATE()
                """;
        try (Connection conn = DBConnection.getConnection();
        PreparedStatement ps = conn.prepareStatement(sql);
        ResultSet rs = ps.executeQuery()) {
            while (rs.next()) overdue.add(mapTransactionFromDB(rs));
        } catch (SQLException e) {
            throw new DatabaseException(e.getErrorCode(), e);
        }
        return overdue;
    }
    /**
     * returns list of all transactions
     * @return list of transactions
     *  @throws DatabaseException from Database
     */
    public List<Transaction> getAllTransactions() throws DatabaseException {
        List<Transaction> result = new ArrayList<>();

        try (Connection conn = DBConnection.getConnection();
        PreparedStatement ps = conn.prepareStatement("SELECT * FROM transactions");
        ResultSet rs = ps.executeQuery()) {
            while (rs.next()) result.add(mapTransactionFromDB(rs));
        } catch (SQLException e) {
            throw new DatabaseException(e.getErrorCode(), e);
        }
        return result;
    }

    public static Transaction mapTransactionFromDB(ResultSet rs) throws IllegalArgumentException, DatabaseException {
        if (rs == null) throw new IllegalArgumentException("Invalid Respone from Database");
        try {
            Date returnDateinSQL = rs.getDate("return_date");
            LocalDate returnDate = (returnDateinSQL != null) ? returnDateinSQL.toLocalDate() : null;

            MemberServices ms = new MemberServices();
            BookServices bs = new BookServices();

            Member member = ms.getMember(rs.getString("member_id"));
            AbstractBook book = bs.getBook(rs.getString("isbn"));



            Transaction t = new Transaction(rs.getString("transaction_id"),
            member, book, rs.getDate("borrow_date").toLocalDate(),
            rs.getDate("due_date").toLocalDate(), returnDate, rs.getDouble("fine_amount"));

            return t;
        } catch (SQLException e) {
            throw new DatabaseException(e.getErrorCode(), e);
        }
    }
}