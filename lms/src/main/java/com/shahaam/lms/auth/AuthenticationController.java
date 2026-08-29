package com.shahaam.lms.auth;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

/**
 * Admin Controller Class'
 * 
 * @author Muhammad Shahaam Siddiqui
 */
@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authenticationService;
 
    /**
     * Post Request to register Admin
     * @param RegisterRequestDTO req
     * @return ResponseEntity<AuthenticationResponseDTO>
     */
    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponseDTO> register(
        @RequestBody @Valid RegisterRequestDTO req
    ) {
        return ResponseEntity.ok(authenticationService.register(req));
    }

    /**
     * Post Request to authenticate Admin
     * @param AuthenticationRequestDTO req
     * @return ResponseEntity<AuthenticationResponseDTO>
     */
    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponseDTO> authenticate(
        @RequestBody @Valid AuthenticationRequestDTO req
    ) {
        return ResponseEntity.ok(authenticationService.authenticate(req));
    }
}