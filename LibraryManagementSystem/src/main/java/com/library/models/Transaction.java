package com.library.models;

import com.library.models.book.AbstractBook;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A class for Transaction object
 * 
 * @author Muhaammad Shahaam Siddiqui
 */
public class Transaction {

    private String transactionID;
    private Member member;
    private AbstractBook book;
    private LocalDate borrowDate;
    private LocalDate dueDate;
    private LocalDate returnDate;
    private double fineAmount;

    public Transaction(String transactionID, Member member,
        AbstractBook book, LocalDate dueDate) {
        
        this(transactionID, member, book, LocalDate.now(), dueDate, null, 0.0);
    }
    public Transaction(String transactionID, Member member,
        AbstractBook book, LocalDate borrowDate, LocalDate dueDate,
        LocalDate returnDate, double fineAmount) {

        setTransactionID(transactionID);
        setMember(member);
        setBook(book);
        setBorrowDate(borrowDate);
        setDueDate(dueDate);
        setReturnDate(returnDate);
        setFineAmount(fineAmount);
    }
    
    // setter
    public final void setTransactionID(String transactionID) throws IllegalArgumentException {
        ValidationUtils.checkNullString(transactionID, "Transaction ID");
        
        this.transactionID = transactionID;
    }
    public final void setMember(Member member) {this.member = member;}
    public final void setBook(AbstractBook book) {this.book = book;}
    public final void setBorrowDate(LocalDate borrowDate) {this.borrowDate = borrowDate;}
    public final void setDueDate(LocalDate dueDate) throws IllegalArgumentException {
        if (dueDate.isBefore(borrowDate))
            throw new IllegalArgumentException("Due Date cannot be before Borrow Date");

        this.dueDate = dueDate;
    }
    public final void setReturnDate(LocalDate returnDate) throws IllegalArgumentException {
        if (returnDate != null && returnDate.isBefore(borrowDate))
            throw new IllegalArgumentException("Return Date cannot be before Borrow Date");

        this.returnDate = returnDate;
    }
    public final void setFineAmount(double fineAmount) throws IllegalArgumentException {
        if (fineAmount < 0) throw new IllegalArgumentException("Fine Amount must be a positive value");

        this.fineAmount = fineAmount;
    }

    // getter
    public final String getTransactionID() {return transactionID;}
    public final Member getMember() {return member;}
    public final AbstractBook getBook() {return book;}
    public final LocalDate getBorrowDate() {return borrowDate;}
    public final LocalDate getDueDate() {return dueDate;}
    public final LocalDate getReturnDate() {return returnDate;}
    public final double getFineAmount() {return fineAmount;} 
    
    /**
     * checks if book is return
     * @return true if returned else false
     */
    public boolean isReturned() {return returnDate != null;}
    /**
     * check if book is not returned within Due date
     * @return true if not returned else false
     */
    public boolean isOverdue() {return !isReturned() && LocalDate.now().isAfter(dueDate);}
    /**
     * get the days overdue
     * @return int number of days
     */
    public int getDaysOverdue() {
        if (!isOverdue()) return 0;
        return (int) (LocalDate.now().toEpochDay() - dueDate.toEpochDay());
    }

    @Override
    public String toString() {
        return "Transaction ID: " + transactionID + "\n" +
        "Member: " + member.getName() + " | " + member.getMemberID() + "\n" +
        "Book: " + book.getTitle() + " | " + book.getISBN() + "\n" +
        "Borrow Date: " + borrowDate + " | Due Date: " + dueDate + "\n" +
        (isReturned() ? "Return Date: " + returnDate : "NOT RETURNED") +
        (fineAmount > 0 ? "Fine Amount: Rs." + fineAmount : "") + "\n";
    }
    @Override
    public boolean equals(Object other) {
        if (this == other) return true;
        if (!(other instanceof Transaction)) return false;
        Transaction otherTransaction = (Transaction) other;

        return (transactionID.equals(otherTransaction.transactionID));
    }
    @Override
    public int hashCode() {
        return Objects.hash(transactionID);
    }
}