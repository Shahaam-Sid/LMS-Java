package com.shahaam.lms.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.shahaam.lms.models.Pupil.Admin;

public interface AdminRepository extends JpaRepository<Admin, String> {

    Optional<Admin> findByEmail(String email);

    @Query(value = "SELECT MAX(admin_id) FROM admins WHERE admin_id LIKE :keyword",
        nativeQuery = true
    )
    public String getHighestID(@Param("keyword") String keyword);

}
