package com.shahaam.lms.controller;

import java.net.URI;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.shahaam.lms.dto.loan.LoanRequestDTO;
import com.shahaam.lms.dto.loan.LoanResponseDTO;
import com.shahaam.lms.services.LoanService;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/loans")
@Validated
public class LoanController {

    private final LoanService ls;

    @GetMapping
    public ResponseEntity<List<LoanResponseDTO>> getAllLoans() {
        return ResponseEntity.ok(ls.getAllLoans());
    }
    @GetMapping("/{id}")
    public ResponseEntity<LoanResponseDTO> getLoan(@PathVariable @Size(max=36, min=36) String id) {
        return ResponseEntity.ok(ls.getLoan(id));
    }
    @GetMapping("/active")
    public ResponseEntity<List<LoanResponseDTO>> getActiveLoans() {
        return ResponseEntity.ok(ls.getActiveLoans());
    }
    @GetMapping("/active/overdue")
    public ResponseEntity<List<LoanResponseDTO>> getOverdueLoans() {
        return ResponseEntity.ok(ls.getOverdueLoans());
    }

    @PostMapping
    public ResponseEntity<LoanResponseDTO> borrowBook(@RequestBody @Valid LoanRequestDTO req) {
        LoanResponseDTO loan = ls.addLoan(req);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
        .buildAndExpand(loan.loanID()).toUri();
        return ResponseEntity.created(location).body(loan);
    }
    
    @PatchMapping("/{id}")
    public ResponseEntity<LoanResponseDTO> returnBook(@PathVariable @Size(max=36, min=36) String id) {
        return ResponseEntity.ok(ls.completeLoan(id));
    }
}