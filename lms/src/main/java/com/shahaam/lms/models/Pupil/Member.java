package com.shahaam.lms.models.Pupil;

import java.util.Objects;

import com.shahaam.lms.enums.MemberStatus;
import com.shahaam.lms.utils.ValidationUtils;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity(name = "members")
@NoArgsConstructor
@ToString(callSuper = true)
public class Member extends AbstractPupil {

    @Id
    @Column(name = "member_id", length = 9, nullable = false)
    private String memberID;

    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    private MemberStatus status;

    @Column(name = "count_borrows", nullable = false, columnDefinition = "TINYINT UNSIGNED")
    private Integer countBorrows;

    public static int MAX_BORROW_LIMIT = 3;

    public Member(String memberID, String name, String phone,
        String email, String address, Integer birthYear) {
        super(name, phone, email, address, birthYear);
        setMemberID(memberID);
        setStatus(MemberStatus.ACTIVE);
        setCountBorrows(0);
    }
    public Member(String memberID, String name, String phone, String email, String address,
        Integer birthYear, MemberStatus status, Integer countOfBorrows) {
        super(name, phone, email, address, birthYear);
        setMemberID(memberID);
        setStatus(status);
        setCountBorrows(countOfBorrows);
    }

    // setter
    public void setMemberID(String memberID) throws IllegalArgumentException {
        ValidationUtils.checkID(memberID, "Member ID");

        this.memberID = memberID;
    }
    public void setStatus(MemberStatus status) {this.status = status;}
    public void setCountBorrows(Integer count) {
        if (count != null && (count < 0 || count > MAX_BORROW_LIMIT))
            throw new IllegalArgumentException("Borrow Count cannot be more then " + MAX_BORROW_LIMIT);

        this.countBorrows = count;
    }

    public String getMemberID() {return memberID;}
    public String getStatus() {return status.name();}
    public Integer getCountBorrows() {return countBorrows;}

    public boolean canBorrow() {
        return status == MemberStatus.ACTIVE && countBorrows < MAX_BORROW_LIMIT;
    }

    @Override
    public boolean isAdmin() {return false;}

    @Override
    public boolean equals(Object other) {
        if (this == other) return true;
        if (!(other instanceof Member)) return false;
        Member otherPupil = (Member) other; 

        return (memberID.equals(otherPupil.memberID));
    }
    @Override
    public int hashCode() {
        return Objects.hash(memberID);
    }
}