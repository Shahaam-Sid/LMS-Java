package com.shahaam.lms.interfaces;

import java.util.List;

import com.shahaam.lms.dto.reservation.ReservationResponseDTO;
/**
 * interface for reservable books
 */
public interface ReservingService {

    ReservationResponseDTO addToQueue(String isbn, String memberId);

    public void cancelReservation(String isbn, String memberId);

    public List<ReservationResponseDTO> getQueue(String isbn);
}