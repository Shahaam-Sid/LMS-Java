package com.shahaam.lms.auth;

import java.util.Calendar;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.shahaam.lms.config.JwtService;
import com.shahaam.lms.enums.Roles;
import com.shahaam.lms.exceptions.WorkerNotFoundException;
import com.shahaam.lms.models.Pupil.Admin;
import com.shahaam.lms.repositories.AdminRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final AdminRepository adminRepo;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authManager;

    @Transactional
    public AuthenticationResponseDTO register(RegisterRequestDTO req) {

        Admin admin = mapFromRegisterRequestDTO(req);

        adminRepo.save(admin);

        String jwtToken = jwtService.generateToken(admin);

        return new AuthenticationResponseDTO(
            admin.getAdminID() ,admin.getName(),
            admin.getEmail(), admin.getPhone(), jwtToken
        );

    }

    public AuthenticationResponseDTO authenticate(AuthenticationRequestDTO req) {
        authManager.authenticate(
            new UsernamePasswordAuthenticationToken(req.email(),req.password())
        );

        Admin admin = adminRepo.findByEmail(req.email())
        .orElseThrow(() -> new WorkerNotFoundException(req.email()));

        String jwtToken = jwtService.generateToken(admin);
        
        return new AuthenticationResponseDTO(
            admin.getAdminID() ,admin.getName(),
            admin.getEmail(), admin.getPhone(), jwtToken
        );
    }

    private String generateAdminIDString() {
        StringBuilder sb = new StringBuilder("A");
        int year = Calendar.getInstance().get(Calendar.YEAR);
        String yearString = String.valueOf(year);
        sb.append(yearString.substring(2));

        String lastId = adminRepo.getHighestID("_" + yearString.substring(2) + "%");
        int next = (lastId == null) ? 1 : Integer.parseInt(lastId.substring(6)) + 1;

        sb.append(String.format("%06d", next));
        return sb.toString();
    }
    private Admin mapFromRegisterRequestDTO(RegisterRequestDTO req) {
        return new Admin(
            generateAdminIDString(), req.name(), req.phone(), req.email(),
            req.address(), req.birthYear(), passwordEncoder.encode(req.password()),
            Roles.ADMIN
        );
    }
}