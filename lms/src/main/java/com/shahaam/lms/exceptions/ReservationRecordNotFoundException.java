package com.shahaam.lms.exceptions;

public class ReservationRecordNotFoundException extends RuntimeException {
    public ReservationRecordNotFoundException(String isbn) {
        super("No Reservation Record Found for book " + isbn);
    }
    public  ReservationRecordNotFoundException() {
        super("No such Reservation Record Found");
    }
}