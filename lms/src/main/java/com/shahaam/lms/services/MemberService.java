package com.shahaam.lms.services;

import java.util.Calendar;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.shahaam.lms.dto.member.MemberRequestDTO;
import com.shahaam.lms.dto.member.MemberResponseDTO;
import com.shahaam.lms.dto.member.MemberUpdateRequestDTO;
import com.shahaam.lms.enums.MemberStatus;
import com.shahaam.lms.exceptions.MemberNotFoundException;
import com.shahaam.lms.models.Pupil.Member;
import com.shahaam.lms.repositories.MemberRepository;

@Service
public class MemberService {

    private MemberRepository memberRepo;

    public MemberService(MemberRepository memberRepo) {
        this.memberRepo = memberRepo;
    }

    @Transactional(readOnly = true)
    public long getMembersCount() {
        return memberRepo.count();
    }
    @Transactional
    public MemberResponseDTO addMember(MemberRequestDTO req) {
        return mapToResponseDTO(memberRepo.save(mapFromRequestDTO(req)));
    }
    @Transactional(readOnly = true)
    public boolean doesMemberExist(String id) {
        return memberRepo.existsById(id);
    }
    @Transactional(readOnly = true)
    public MemberResponseDTO getMember(String id) {
        return mapToResponseDTO(getMemberAsObj(id));
    }
    @Transactional(readOnly = true)
    public List<MemberResponseDTO> getAllMembers() {
        return memberRepo.findAll().stream().map(this::mapToResponseDTO).toList();
    }
    @Transactional
    public void removeMember(String id) {
        if (!doesMemberExist(id)) throw new MemberNotFoundException(id);

        memberRepo.deleteById(id);
    }
    @Transactional(readOnly = true)
    public boolean isMemberActive(String id) {
        Member member = getMemberAsObj(id);
        return (member.getStatus() == "ACTIVE");
    }
    @Transactional(readOnly = true)
    public List<MemberResponseDTO> getActiveMembers() {
        return memberRepo.findByStatus(MemberStatus.ACTIVE)
        .stream().map(this::mapToResponseDTO).toList();
    }
    @Transactional(readOnly = true)
    public List<MemberResponseDTO> searchKeyword(String keyword) {
        return memberRepo.searchMatching(keyword).stream().map(this::mapToResponseDTO).toList();
    }
    @Transactional
    public MemberResponseDTO updateMember(String id, MemberUpdateRequestDTO req) {
        Member member = getMemberAsObj(id);

        if (req.name() != null) {member.setName(req.name());}
        if (req.phone() != null) {member.setPhone(req.phone());}
        if (req.email() != null) {member.setEmail(req.email());}
        if (req.address() != null) {member.setAddress(req.address());}
        if (req.status() != null) {member.setStatus(req.status());}

        return mapToResponseDTO(member);
    }
 
    // helper method

    @Transactional(readOnly = true)
    private Member getMemberAsObj(String id) {
        return memberRepo.findById(id).orElseThrow(() -> new MemberNotFoundException(id));
    }

    private String generateMemberIDString() {
        StringBuilder sb = new StringBuilder("M");
        int year = Calendar.getInstance().get(Calendar.YEAR);
        String yearString = String.valueOf(year);
        sb.append(yearString.substring(2));

        String lastId = memberRepo.getHighestID("_" + yearString.substring(2) + "%");
        int next = (lastId == null) ? 1 : Integer.parseInt(lastId.substring(6)) + 1;

        sb.append(String.format("%06d", next));
        return sb.toString();
    }
    private Member mapFromRequestDTO(MemberRequestDTO req) {
        return new Member(
            generateMemberIDString() ,req.name(), req.phone(), req.email(),
            req.address(), req.birthYear()
        );
    }
    private MemberResponseDTO mapToResponseDTO(Member member) {
        return new MemberResponseDTO(
            member.getMemberID(), member.getName(), member.getPhone(),
            member.getEmail(), MemberStatus.valueOf(member.getStatus())
        );
    }
}