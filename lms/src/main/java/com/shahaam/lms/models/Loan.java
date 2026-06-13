package com.shahaam.lms.models;

import java.time.LocalDate;
import java.util.Objects;

import com.shahaam.lms.models.Pupil.Member;
import com.shahaam.lms.models.book.AbstractBook;
import com.shahaam.lms.utils.ValidationUtils;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity(name = "loans")
@NoArgsConstructor
@Getter
@ToString
public class Loan {

    @Id
    @Column(name = "loan_id", length = 36)
    private String loanID;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(
        name = "member_id", nullable = false,
        foreignKey = @ForeignKey(
            name = "fk_transaction_member",
            foreignKeyDefinition = "FOREIGN KEY (member_id) REFERENCES members(member_id) ON DELETE RESTRICT ON UPDATE CASCADE"
        )
    )
    private Member member;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(
        name = "isbn", nullable = false,
        foreignKey = @ForeignKey(
            name = "fk_transaction_book",
            foreignKeyDefinition = "FOREIGN KEY (isbn) REFERENCES books(isbn) ON DELETE RESTRICT ON UPDATE CASCADE"
        )
    )
    private AbstractBook book;

    @Column(nullable = false, name = "borrow_date")
    private LocalDate borrowDate;

    @Column(nullable = false, name = "due_date")
    private LocalDate dueDate;

    @Column(nullable = true, name = "return_date")
    private LocalDate returnDate;

    @Column(nullable = true, name = "fine_amount")
    private Double fineAmount;

    public Loan(String loanID, Member member,
        AbstractBook book, LocalDate dueDate) {
        
        this(loanID, member, book, LocalDate.now(), dueDate, null, null);
    }
    public Loan(String loanID, Member member,
        AbstractBook book, LocalDate borrowDate, LocalDate dueDate,
        LocalDate returnDate, Double fineAmount) {

        setLoanID(loanID);
        setMember(member);
        setBook(book);
        setBorrowDate(borrowDate);
        setDueDate(dueDate);
        setReturnDate(returnDate);
        setFineAmount(fineAmount);
    }
    
    // setter
    public void setLoanID(String loanID) throws IllegalArgumentException {
        ValidationUtils.checkNullString(loanID, "Loan ID");
        
        this.loanID = loanID;
    }
    public void setMember(Member member) {this.member = member;}
    public void setBook(AbstractBook book) {this.book = book;}
    public void setBorrowDate(LocalDate borrowDate) {this.borrowDate = borrowDate;}
    public void setDueDate(LocalDate dueDate) throws IllegalArgumentException {
        if (dueDate.isBefore(borrowDate))
            throw new IllegalArgumentException("Due Date cannot be before Borrow Date");

        this.dueDate = dueDate;
    }
    public void setReturnDate(LocalDate returnDate) throws IllegalArgumentException {
        if (returnDate != null && returnDate.isBefore(borrowDate))
            throw new IllegalArgumentException("Return Date cannot be before Borrow Date");

        this.returnDate = returnDate;
    }
    public void setFineAmount(Double fineAmount) throws IllegalArgumentException {
        if (fineAmount != null && fineAmount < 0) throw new IllegalArgumentException("Fine Amount must be a positive value");

        this.fineAmount = fineAmount;
    }

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
    public boolean equals(Object other) {
        if (this == other) return true;
        if (!(other instanceof Loan)) return false;
        Loan otherTransaction = (Loan) other;

        return (loanID.equals(otherTransaction.loanID));
    }
    @Override
    public int hashCode() {
        return Objects.hash(loanID);
    }
}