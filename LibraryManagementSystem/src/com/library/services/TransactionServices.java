package com.library.services;

import com.library.exceptions.BookNotAvailableException;
import com.library.exceptions.BookNotFoundException;
import com.library.exceptions.MemberLimitExceededException;
import com.library.exceptions.MemberNotFoundException;
import com.library.interfaces.Borrowable;
import com.library.models.Member;
import com.library.models.Transaction;
import com.library.models.book.AbstractBook;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Class for TransactionServices
 * 
 * @author Muhammad Shahaam Siddiqui
 */
public class TransactionServices {

    private List<Transaction> transactions = new ArrayList<>();

    private BookServices bookService;
    private MemberServices memberService;

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
     */
    public Transaction borrowBook(String isbn, String memberID) throws BookNotFoundException,
    MemberNotFoundException, MemberLimitExceededException, UnsupportedOperationException,
    BookNotAvailableException {
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

        transactions.add(t);
        member.addTransaction(t);

        return t;
    }
    /**
     * closes transaction
     * @param isbn of book to return
     * @param memberId of member who returns
     * @return transaction
     * @throws MemberNotFoundException if member id not found
     * @throws BookNotFoundException if isbn not found
     * @throws RuntimeException no active borrow
     */
    public Transaction returnBook(String isbn, String memberId) throws MemberNotFoundException,
    BookNotFoundException, RuntimeException {
        Member member = memberService.getMember(memberId);
        AbstractBook book = bookService.getBook(isbn);
        
        Transaction active = transactions.stream()
                .filter(t -> !t.isReturned()
                        && t.getBook().getISBN().equals(isbn)
                        && t.getMember().getMemberID().equals(memberId))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("No active borrow record found."));

        ((Borrowable) book).returnItem(member);

        active.setReturnDate(LocalDate.now());

        if (active.getDaysOverdue() > 0) {
            double fine = book.calculateLateFee(active.getDaysOverdue());
            active.setFineAmount(fine);
        }

        member.removeTransaction(active);
        return active;
    }
    /**
     * returns a list of overdues
     * @return list of overdue transactions
     */
    public List<Transaction> getOverdueTransactions() {
        List<Transaction> overdue = new ArrayList<>();
        for (Transaction t : transactions) {
            if (t.isOverdue()) overdue.add(t);
        }

        return overdue;
    }
    /**
     * returns list of all transactions
     * @return list of transactions
     */
    public List<Transaction> getAllTransactions() {
        return new ArrayList<>(transactions);
    }
}