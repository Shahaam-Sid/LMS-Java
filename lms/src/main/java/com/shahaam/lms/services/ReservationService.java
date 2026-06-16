package com.shahaam.lms.services;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.shahaam.lms.dto.reservation.ReservationResponseDTO;
import com.shahaam.lms.enums.ReservationStatus;
import com.shahaam.lms.exceptions.BookAlreadyBorrowedException;
import com.shahaam.lms.exceptions.BookNotFoundException;
import com.shahaam.lms.exceptions.MemberNotFoundException;
import com.shahaam.lms.exceptions.ReservationRecordNotFoundException;
import com.shahaam.lms.interfaces.ReservingService;
import com.shahaam.lms.models.Pupil.Member;
import com.shahaam.lms.models.book.AbstractBook;
import com.shahaam.lms.models.reservation.Reservation;
import com.shahaam.lms.models.reservation.ReservationId;
import com.shahaam.lms.repositories.BookRepository;
import com.shahaam.lms.repositories.MemberRepository;
import com.shahaam.lms.repositories.ReservationRepository;


@Service
public class ReservationService implements ReservingService {
    private final ReservationRepository reservationRepository;
    private ReservationRepository reservationRepo;
    private MemberRepository memberRepo;
    private BookRepository bookRepo;
    
    public ReservationService(ReservationRepository reservationRepo, MemberRepository memberRepo,
            BookRepository bookRepo, ReservationRepository reservationRepository) {
        this.reservationRepo = reservationRepo;
        this.memberRepo = memberRepo;
        this.bookRepo = bookRepo;
        this.reservationRepository = reservationRepository;
    }

    @Override
    @Transactional
    public ReservationResponseDTO addToQueue(String isbn, String memberId) {
        if (reservationRepository.existsByIdIsbnAndIdMemberId(isbn, memberId))
            throw new BookAlreadyBorrowedException(isbn, memberId);

        AbstractBook book = bookRepo.findById(isbn).orElseThrow(() -> new BookNotFoundException(isbn));

        Member member = memberRepo.findById(memberId).orElseThrow(() -> new MemberNotFoundException(memberId));

        int nextPosition = (int) reservationRepo.countPendingByIsbn(isbn) + 1;

        Reservation reservation = new Reservation(book, member, nextPosition);

        return mapToResponseDTO(reservationRepo.save(reservation));
    }

    @Transactional
    public void notifyNextInQueue(String isbn) {

        Reservation reservation = reservationRepo
        .findFirstByIdIsbnAndStatusOrderByQueuePositionAsc(isbn, ReservationStatus.PENDING)
        .orElseThrow(() -> new ReservationRecordNotFoundException(isbn));

        reservation.setStatus(ReservationStatus.NOTIFIED);
    }

    @Override
    @Transactional
    public void cancelReservation(String isbn, String memberId) {
        Reservation reservation = reservationRepo.findById(new ReservationId(isbn, memberId))
        .orElseThrow(() -> new ReservationRecordNotFoundException(isbn));

        int cancelledPosition = reservation.getQueuePosition();

        reservation.setStatus(ReservationStatus.CANCELLED);

        reservationRepo.shiftQueuePositionsDown(isbn, cancelledPosition);
    }

    @Transactional
    private void markedFulfilled(String isbn, String memberId) {
        ReservationId id = new ReservationId(isbn, memberId);
        Reservation reservation = reservationRepo.findById(id)
        .orElseThrow(() -> new ReservationRecordNotFoundException());

        reservation.setStatus(ReservationStatus.FULFILLED);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ReservationResponseDTO> getQueue(String isbn) {
        return reservationRepo.findByIdIsbnAndStatusOrderByQueuePositionAsc(
            isbn, ReservationStatus.PENDING).stream().map(this::mapToResponseDTO).toList();
    }

    /** Get all reservations for a member */
    @Transactional(readOnly = true)
    public List<ReservationResponseDTO> getReservationsForMember(String memberId) {
        return reservationRepo.findByIdMemberIdOrderByReservedAtDesc(memberId)
        .stream().map(this::mapToResponseDTO).toList();
    }

    // helper method
    private ReservationResponseDTO mapToResponseDTO(Reservation reservation) {
        return new ReservationResponseDTO(
            reservation.getMember().getMemberID(),
            reservation.getBook().getISBN(),
            reservation.getQueuePosition(),
            ReservationStatus.valueOf(reservation.getStatus())
        );
    }    
}