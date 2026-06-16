package com.shahaam.lms.dto.reservation;

import com.shahaam.lms.enums.ReservationStatus;

public record ReservationResponseDTO(
    String memberId,
    String isbn,
    Integer queuePosition,
    ReservationStatus status
) {}