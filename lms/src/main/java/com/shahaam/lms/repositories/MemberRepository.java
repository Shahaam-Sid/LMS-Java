package com.shahaam.lms.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.shahaam.lms.enums.MemberStatus;
import com.shahaam.lms.models.Pupil.Member;

public interface MemberRepository extends JpaRepository<Member, String> {

    @Query(value = "SELECT MAX(member_id) FROM members WHERE member_id LIKE :keyword",
        nativeQuery = true
    )
    public String getHighestID(@Param("keyword") String keyword);

    public List<Member> findByStatus(MemberStatus status);

    @Query(value = """
            SELECT * FROM members
            WHERE LOWER(member_id) LIKE LOWER(CONCAT('%', :keyword, '%')) OR
            LOWER(name) LIKE LOWER(CONCAT('%', :keyword, '%')) OR
            LOWER(email) LIKE LOWER(CONCAT('%', :keyword, '%')) OR
            LOWER(phone) LIKE LOWER(CONCAT('%', :keyword, '%'))
            """,
        nativeQuery = true)
   public List<Member> searchMatching(@Param("keyword") String keyword);

}