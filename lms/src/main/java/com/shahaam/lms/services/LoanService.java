package com.shahaam.lms.services;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.shahaam.lms.dto.loan.LoanRequestDTO;
import com.shahaam.lms.dto.loan.LoanResponseDTO;
import com.shahaam.lms.enums.BookStatus;
import com.shahaam.lms.exceptions.BookAlreadyBorrowedException;
import com.shahaam.lms.exceptions.BookNotAvailableException;
import com.shahaam.lms.exceptions.BookNotFoundException;
import com.shahaam.lms.exceptions.MemberLimitExceededException;
import com.shahaam.lms.exceptions.MemberNotFoundException;
import com.shahaam.lms.exceptions.NoActiveBorrowRecordFoundException;
import com.shahaam.lms.exceptions.NoBorrowRecordFoundException;
import com.shahaam.lms.models.Loan;
import com.shahaam.lms.models.Pupil.Member;
import com.shahaam.lms.models.book.AbstractBook;
import com.shahaam.lms.models.book.PhysicalBook;
import com.shahaam.lms.repositories.BookRepository;
import com.shahaam.lms.repositories.LoanRepository;
import com.shahaam.lms.repositories.MemberRepository;

@Service
public class LoanService {

    private final LoanRepository loanRepo;
    private final BookRepository bookRepo;
    private final MemberRepository memberRepo;
    
    public LoanService(LoanRepository loanRepo, BookRepository bookRepo, MemberRepository memberRepo) {
        this.loanRepo = loanRepo;
        this.bookRepo = bookRepo;
        this.memberRepo = memberRepo;
    }

    @Transactional(readOnly = true)
    public LoanResponseDTO getLoan(String loanID) {
        Loan loan = loanRepo.findById(loanID).orElseThrow(() -> new NoBorrowRecordFoundException(loanID));
        return mapToResponseDTO(loan);
    }
    @Transactional
    public LoanResponseDTO addLoan(LoanRequestDTO req) {
        AbstractBook book = bookRepo.findById(req.isbn())
        .orElseThrow(() -> new BookNotFoundException(req.isbn()));

        Member member = memberRepo.findById(req.member_id())
        .orElseThrow(() -> new MemberNotFoundException(req.member_id()));

        if (loanRepo.existsByBookAndMemberAndReturnDateIsNull(book, member))
            throw new BookAlreadyBorrowedException(req.isbn(), req.member_id());

        if (!member.canBorrow()) throw new MemberLimitExceededException(req.member_id());

        if (!book.isAvailable()) throw new BookNotAvailableException(req.isbn());

        LocalDate dueDate;

        if (book instanceof PhysicalBook pb) {
            pb.decrementAvailableCopies();
            if (pb.getAvailableCopies() == 0) pb.setStatus(BookStatus.BORROWED);
            dueDate = LocalDate.now().plusDays(14);
        } else {dueDate = LocalDate.now().plusDays(7);}

        Loan loan = new Loan(UUID.randomUUID().toString(), member, book, dueDate);

        return mapToResponseDTO(loanRepo.save(loan));
    }
    @Transactional
    public LoanResponseDTO completeLoan(String loanID) {
        Loan loan = loanRepo.findById(loanID)
        .orElseThrow(() -> new NoBorrowRecordFoundException(loanID));

        if (loan.getReturnDate() != null)
            throw new NoActiveBorrowRecordFoundException(
                loan.getBook().getISBN(),
                loan.getMember().getMemberID()
            );

        AbstractBook book = loan.getBook();

        if (book instanceof PhysicalBook pb) {
            pb.incrementAvailableCopies();
            pb.setStatus(BookStatus.AVAILABLE);
        }

        loan.setReturnDate(LocalDate.now());

        if (loan.getDaysOverdue() > 0) {
            Double fine = book.calculateLateFee(loan.getDaysOverdue());
            loan.setFineAmount(fine);
        }

        return mapToResponseDTO(loan);
    }
    @Transactional(readOnly = true)
    public List<LoanResponseDTO> getAllLoans() {
        return loanRepo.findAll().stream().map(this::mapToResponseDTO).toList();
    }
    @Transactional(readOnly = true)
    public List<LoanResponseDTO> getActiveLoans() {
        return loanRepo.findByReturnDateIsNull().stream().map(this::mapToResponseDTO).toList();
    }
    @Transactional(readOnly = true)
    public List<LoanResponseDTO> getOverdueLoans() {
        return loanRepo.findByReturnDateIsNullAndDueDateBefore(LocalDate.now())
        .stream().map(this::mapToResponseDTO).toList();
    }

    // helper methods
    private LoanResponseDTO mapToResponseDTO(Loan loan) {

        return new LoanResponseDTO(
            loan.getLoanID(), loan.getMember().getMemberID(), loan.getBook().getISBN(),
            dateToString(loan.getBorrowDate()), dateToString(loan.getDueDate()),
            (loan.getReturnDate() == null) ? null : dateToString(loan.getReturnDate()),
            (loan.getFineAmount() == null) ? null : loan.getFineAmount()
        );
    }

    private static LocalDate stringtoDate(String date) {
        return LocalDate.parse(date, DateTimeFormatter.ofPattern("dd-MM-yyyy"));
    }
    private static String dateToString(LocalDate date) {
        return date.format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));
    }    
}