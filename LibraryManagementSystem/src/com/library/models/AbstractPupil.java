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
    public void setName(String name) throws IllegalArgumentException {
        checkNullString(name, "Name");
        checkLengthString(name, 5, 35, name);

        for (char c : name.toUpperCase().toCharArray()) {
            
            if ((c < 'A' || c > 'Z') && c != ' ') 
                throw new IllegalArgumentException("Name must only contain Alphabets");
        }
        
        this.name = name;
    }
    public void setPhone(String phone) throws IllegalArgumentException {
        checkNullString(phone, "Phone Number");
        checkLengthString(phone, 11, 11, "Phone Number");

        for (char c : name.toCharArray()) {
            if (c < '0' || c > '9')
                throw new IllegalArgumentException("Number must only contain Numeric Characters");
        }
        this.phone = phone;
    }
    public void setEmail(String email) throws IllegalArgumentException {
        checkNullString(email, "Email Address");
        
        String regex = "^[a-zA-Z0-9._%+\\-]+@[a-zA-Z0-9.\\-]+\\.[a-zA-Z]{2,}$";
        
        if (!email.matches(regex))
            throw new IllegalArgumentException("Invalid email format");
        this.email = email;
    }
    public void setAddress(String address) {
        checkNullString(address, "Address");
        checkLengthString(address, 5, 55, address);

        this.address = address;
    }
    public void setAge(int age) throws IllegalArgumentException {
        if (age < 10 || age > 100) throw new IllegalArgumentException("Age not in acceptable range");
        this.age = age;
    }

    // Getters
    public String getName() {return name;}
    public String getPhone() {return phone;}
    public String getEmail() {return email;}
    public String getAddress() {return address;}
    public int getAge() {return age;}

    /**
     * a static helper method to check for empty strings
     * @param string 
     * @param field field of information
     * @throws IllegalArgumentException if string is null or length is 0 or only contains whitespaces
     */
    protected static void checkNullString(String string, String field) throws IllegalArgumentException {
        if (string == null || string.trim().length() == 0)
            throw new IllegalArgumentException("Invalid " + field);
    }
    /**
     * a static helper method to check to validate length of strings
     * @param string
     * @param lengthMin minimun acceptable length
     * @param lengthMax maximum acceptable length
     * @param field field of information
     * @throws IllegalArgumentException if length is not in acceptable range
     */
    protected static void checkLengthString(String string, int lengthMin, int lengthMax, String field) throws IllegalArgumentException {
        if (string.length() < lengthMin || string.length() > lengthMax)
            throw new IllegalArgumentException("Allowed Length for " + field + ": " + lengthMin + " - " + lengthMax);
    }
}