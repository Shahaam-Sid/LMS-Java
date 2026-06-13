package com.shahaam.lms.controller;

import java.net.URI;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.shahaam.lms.dto.member.MemberRequestDTO;
import com.shahaam.lms.dto.member.MemberResponseDTO;
import com.shahaam.lms.dto.member.MemberUpdateRequestDTO;
import com.shahaam.lms.services.MemberService;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@RestController
@RequestMapping("/api/v1/members")
@Validated
public class MemberController{

    MemberService ms;

    public MemberController(MemberService ms) {
        this.ms = ms;
    }

    @GetMapping
    public ResponseEntity<List<MemberResponseDTO>> getAllMembers() {
        List<MemberResponseDTO> members = ms.getAllMembers();
        return ResponseEntity.ok(members);
    }
    @GetMapping("/{id}")
    public ResponseEntity<MemberResponseDTO> getMember(
        @PathVariable @NotBlank @Size(min=9, max=9, message="Invalid member id") String id
    ) {
        return ResponseEntity.ok(ms.getMember(id));
    }
    @GetMapping("/search")
    public ResponseEntity<List<MemberResponseDTO>> searchMembers(@RequestParam @NotBlank String q) {
        List<MemberResponseDTO> results = ms.searchKeyword(q);
        return ResponseEntity.ok(results);
    }
    @GetMapping("/active")
    public ResponseEntity<List<MemberResponseDTO>> getActiveMembers() {
        List<MemberResponseDTO> output = ms.getActiveMembers();
        return ResponseEntity.ok(output);
    }

    @PostMapping
    public ResponseEntity<MemberResponseDTO> addMember(@RequestBody @Valid MemberRequestDTO req) {
        MemberResponseDTO output = ms.addMember(req);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
        .path("/" + output.memberID()).buildAndExpand(output.memberID()).toUri();
        return ResponseEntity.created(location).body(output);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<MemberResponseDTO> updateMember(
        @PathVariable @NotBlank @Size(min=9, max=9, message="Invalid member id") String id,
        @RequestBody @Valid MemberUpdateRequestDTO req
    ) {
        return ResponseEntity.ok(ms.updateMember(id, req));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMember(
        @PathVariable @NotBlank @Size(min=9, max=9, message="Invalid member id") String id
    ) {
        ms.removeMember(id);
        return ResponseEntity.noContent().build();
    }
}