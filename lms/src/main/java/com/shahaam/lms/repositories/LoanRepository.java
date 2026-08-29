package com.shahaam.lms.repositories;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.shahaam.lms.models.Loan;
import com.shahaam.lms.models.Pupil.Member;
import com.shahaam.lms.models.book.AbstractBook;

public interface LoanRepository extends JpaRepository<Loan, String> {

    boolean existsByBookAndMemberAndReturnDateIsNull(AbstractBook book, Member member);

    List<Loan> findByReturnDateIsNullAndDueDateBefore(LocalDate date);

    List<Loan> findByReturnDateIsNull();
}