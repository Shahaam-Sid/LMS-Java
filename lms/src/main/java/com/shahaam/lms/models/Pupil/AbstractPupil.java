package com.shahaam.lms.models.Pupil;

import java.util.Calendar;

import com.shahaam.lms.utils.ValidationUtils;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@MappedSuperclass
@NoArgsConstructor
@Getter
@ToString
public abstract class AbstractPupil {

    @Column(name = "name", length = 35, nullable = false)
    private String name;

    @Column(name = "phone", length = 11, nullable = false)
    private String phone;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "address", length = 55, nullable = false)
    private String address;

    @Column(name = "birth_year", columnDefinition = "SMALLINT", nullable = false)
    private Integer birthYear;

    public AbstractPupil(String name, String phone, String email, String address, Integer birthYear) {
        setName(name);
        setPhone(phone);
        setEmail(email);
        setAddress(address);
        setBirthYear(birthYear);
    }

    // Setters
    public void setName(String name) throws IllegalArgumentException {
        ValidationUtils.checkNullString(name, "Name");
        ValidationUtils.checkLengthString(name, 5, 35, "Name");

        for (char c : name.toUpperCase().toCharArray()) {
            
            if ((c < 'A' || c > 'Z') && c != ' ') 
                throw new IllegalArgumentException("Name must only contain Alphabets");
        }
        
        this.name = name;
    }
    public void setPhone(String phone) throws IllegalArgumentException {
        ValidationUtils.checkNullString(phone, "Phone Number");
        ValidationUtils.checkLengthString(phone, 11, 11, "Phone Number");

        for (char c : phone.toCharArray()) {
            if (c < '0' || c > '9')
                throw new IllegalArgumentException("Number must only contain Numeric Characters");
        }
        this.phone = phone;
    }
    public void setEmail(String email) throws IllegalArgumentException {
        ValidationUtils.checkNullString(email, "Email Address");
        
        String regex = "^[a-zA-Z0-9._%+\\-]+@[a-zA-Z0-9.\\-]+\\.[a-zA-Z]{2,}$";
        
        if (!email.matches(regex))
            throw new IllegalArgumentException("Invalid email format");
        this.email = email;
    }
    public void setAddress(String address) {
        ValidationUtils.checkNullString(address, "Address");
        ValidationUtils.checkLengthString(address, 5, 55, "Address");

        this.address = address;
    }
    public void setBirthYear(Integer birthYear) throws IllegalArgumentException {
        if (birthYear != null &&
           (birthYear < 1910 || birthYear > Calendar.getInstance().get(Calendar.YEAR))) 
            throw new IllegalArgumentException("Year of Birth not in acceptable range");
            
        this.birthYear = birthYear;
    }

    public abstract boolean isAdmin();
}