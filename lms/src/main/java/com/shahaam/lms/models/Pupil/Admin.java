package com.shahaam.lms.models.Pupil;

import java.util.Collection;
import java.util.List;
import java.util.Objects;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.shahaam.lms.enums.Roles;
import com.shahaam.lms.exceptions.InvalidPasswordException;
import com.shahaam.lms.utils.PasswordUtils;
import com.shahaam.lms.utils.ValidationUtils;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@NoArgsConstructor
@Entity(name = "admins")
@ToString(callSuper = true)
@Getter
public class Admin extends AbstractPupil implements UserDetails {

    public Admin(String adminID ,String name, String phone, String email, String address,
        Integer birthYear, String password, Roles role) {
        
        super(name, phone, email, address, birthYear);
        setAdminID(adminID);
        setPassword(password);
        setRole(role);
    }

    @Id
    @Column(name = "admin_id", length = 9, nullable = false)
    private String adminID;

    @Column(name = "password", nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    private Roles role;

    public void setAdminID(String adminID) throws IllegalArgumentException {
        ValidationUtils.checkID(adminID, "Admin ID");

        this.adminID = adminID;
    }
    public void setPassword(String password) throws InvalidPasswordException {
        PasswordUtils.validate(password);

        this.password = password;
    }
    public void setRole(Roles role) {
        this.role = role;
    }


    public boolean isAdmin() {return true;}

    // Spring Secuirty methods

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.name()));
    }
    @Override
    public String getUsername() {
        return getEmail();
    }
    @Override
    public String getPassword() {
        return password;
    }
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }
    @Override
    public boolean isEnabled() {
        return true;
    }
    

    @Override
    public boolean equals(Object other) {
        if (this == other) return true;
        if (!(other instanceof Admin)) return false;
        Admin otherPupil = (Admin) other; 

        return (adminID.equals(otherPupil.adminID));
    }
    @Override
    public int hashCode() {
        return Objects.hash(adminID);
    }
}