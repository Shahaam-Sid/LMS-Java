package com.shahaam.lms.enums;

public enum ReservationStatus {
    PENDING,      // waiting in queue
    NOTIFIED,     // book is available, member has been told
    FULFILLED,    // member borrowed the book
    CANCELLED 
}
