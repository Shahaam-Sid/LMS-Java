package com.shahaam.lms.models.reservation;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Embeddable
public class ReservationId implements Serializable {

    @Column(name = "isbn", nullable = false, length = 13)
    private String isbn;

    @Column(name = "member_id", nullable = false, length = 9)
    private String memberId;
}