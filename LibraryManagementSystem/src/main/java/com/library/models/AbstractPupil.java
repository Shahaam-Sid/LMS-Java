package com.library.models;

import com.library.interfaces.Pupil;

/**
 * An Abstract class for individuals
 * 
 * @author Muhammad Shahaam Siddiqui
 */
public abstract class AbstractPupil implements Pupil{

    private String name;
    private String phone;
    private String email;
    private String address;
    private int age;

    public AbstractPupil(String name, String phone, String email, String address, int age) {
        setName(name);
        setPhone(phone);
        setEmail(email);
        setAddress(address);
        setAge(age);
    }

    // Setters
    public final void setName(String name) throws IllegalArgumentException {
        ValidationUtils.checkNullString(name, "Name");
        ValidationUtils.checkLengthString(name, 5, 35, "Name");

        for (char c : name.toUpperCase().toCharArray()) {
            
            if ((c < 'A' || c > 'Z') && c != ' ') 
                throw new IllegalArgumentException("Name must only contain Alphabets");
        }
        
        this.name = name;
    }
    public final void setPhone(String phone) throws IllegalArgumentException {
        ValidationUtils.checkNullString(phone, "Phone Number");
        ValidationUtils.checkLengthString(phone, 11, 11, "Phone Number");

        for (char c : phone.toCharArray()) {
            if (c < '0' || c > '9')
                throw new IllegalArgumentException("Number must only contain Numeric Characters");
        }
        this.phone = phone;
    }
    public final void setEmail(String email) throws IllegalArgumentException {
        ValidationUtils.checkNullString(email, "Email Address");
        
        String regex = "^[a-zA-Z0-9._%+\\-]+@[a-zA-Z0-9.\\-]+\\.[a-zA-Z]{2,}$";
        
        if (!email.matches(regex))
            throw new IllegalArgumentException("Invalid email format");
        this.email = email;
    }
    public final void setAddress(String address) {
        ValidationUtils.checkNullString(address, "Address");
        ValidationUtils.checkLengthString(address, 5, 55, "Address");

        this.address = address;
    }
    public final void setAge(int age) throws IllegalArgumentException {
        if (age < 10 || age > 100) throw new IllegalArgumentException("Age not in acceptable range");
        this.age = age;
    }

    // Getters
    public String getName() {return name;}
    public String getPhone() {return phone;}
    public String getEmail() {return email;}
    public String getAddress() {return address;}
    public int getAge() {return age;}

    @Override
    public String toString() {
        return "Name: " + name  + "\n" + 
            "Phone: " + phone + "\n" +
            "Email: " + email + "\n" +
            "Address: " + address + "\n" +
            "Age: " + age + "\n";
    }
}
// => Change age field to contain year of birth in Members and Workers