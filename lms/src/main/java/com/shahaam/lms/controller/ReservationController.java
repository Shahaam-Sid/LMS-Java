package com.shahaam.lms.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.shahaam.lms.dto.reservation.ReservationRequestDTO;
import com.shahaam.lms.dto.reservation.ReservationResponseDTO;
import com.shahaam.lms.services.ReservationService;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@RestController
@RequestMapping("/api/v1/reservations")
public class ReservationController {

    private final ReservationService rs;

    public ReservationController(ReservationService rs) {
        this.rs = rs;
    }

    @PostMapping
    public ResponseEntity<ReservationResponseDTO> reserve(
        @RequestBody @Valid ReservationRequestDTO req
    ) {
        ReservationResponseDTO output = rs.addToQueue(req.isbn(), req.memberId());

        return ResponseEntity.status(HttpStatus.CREATED).body(output);
    }

    @GetMapping("/book/{isbn}")
    public ResponseEntity<List<ReservationResponseDTO>> getQueue(
        @PathVariable
        @NotBlank(message = "isbn cannot be blank")
        @Size(max = 13, min  = 10, message = "isbn must be 10 or 13 characters")
        String isbn
    ) {return ResponseEntity.ok(rs.getQueue(isbn));}

    @GetMapping("/member/{id}")
    public ResponseEntity<List<ReservationResponseDTO>> getReservationforMember(
        @PathVariable
        @NotBlank(message = "member id cannot be blank")
        @Size(max = 9, min = 9, message = "member id should be exaclty 9 characters")
        String id
    ) {return ResponseEntity.ok(rs.getReservationsForMember(id));}
}