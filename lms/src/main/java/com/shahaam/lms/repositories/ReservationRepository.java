package com.shahaam.lms.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.shahaam.lms.enums.ReservationStatus;
import com.shahaam.lms.models.reservation.Reservation;
import com.shahaam.lms.models.reservation.ReservationId;

public interface ReservationRepository extends JpaRepository<Reservation, ReservationId> {

    List<Reservation> findByIdIsbnAndStatusOrderByQueuePositionAsc(
            String isbn, ReservationStatus status);

    boolean existsByIdIsbnAndIdMemberId(String isbn, String memberId);

    Optional<Reservation> findFirstByIdIsbnAndStatusOrderByQueuePositionAsc(
            String isbn, ReservationStatus status);

    List<Reservation> findByIdMemberIdOrderByReservedAtDesc(String memberId);

    @Query("SELECT COUNT(r) FROM Reservation r " +
           "WHERE r.id.isbn = :isbn AND r.status = 'PENDING'")
    long countPendingByIsbn(@Param("isbn") String isbn);

    @Modifying
    @Query("UPDATE Reservation r SET r.queuePosition = r.queuePosition - 1 " +
           "WHERE r.id.isbn = :isbn AND r.status = 'PENDING' " +
           "AND r.queuePosition > :removedPosition")
    void shiftQueuePositionsDown(@Param("isbn") String isbn,
                                  @Param("removedPosition") int removedPosition);
    
}