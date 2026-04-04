package com.library.models;

import com.library.interfaces.Pupil;

/**
 * An Abstract class for individuals
 * 
 * @author Muhammad Shahaam Siddiqui
 */
public abstract class AbstractPupil implements Pupil{

    protected String name;
    protected String phone;
    protected String email;
    protected String address;
    protected int age;

    public AbstractPupil(String name, String phone, String email, String address, int age) {
        setName(name);
        setPhone(phone);
        setEmail(email);
        setAddress(address);
        setAge(age);
    }

    // Setters
    public final void setName(String name) throws IllegalArgumentException {
        checkNullString(name, "Name");
        checkLengthString(name, 5, 35, "Name");

        for (char c : name.toUpperCase().toCharArray()) {
            
            if ((c < 'A' || c > 'Z') && c != ' ') 
                throw new IllegalArgumentException("Name must only contain Alphabets");
        }
        
        this.name = name;
    }
    public final void setPhone(String phone) throws IllegalArgumentException {
        checkNullString(phone, "Phone Number");
        checkLengthString(phone, 11, 11, "Phone Number");

        for (char c : phone.toCharArray()) {
            if (c < '0' || c > '9')
                throw new IllegalArgumentException("Number must only contain Numeric Characters");
        }
        this.phone = phone;
    }
    public final void setEmail(String email) throws IllegalArgumentException {
        checkNullString(email, "Email Address");
        
        String regex = "^[a-zA-Z0-9._%+\\-]+@[a-zA-Z0-9.\\-]+\\.[a-zA-Z]{2,}$";
        
        if (!email.matches(regex))
            throw new IllegalArgumentException("Invalid email format");
        this.email = email;
    }
    public final void setAddress(String address) {
        checkNullString(address, "Address");
        checkLengthString(address, 5, 55, "Address");

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
    /**
     * method to check and validate ID
     * @param id
     * @param field field, Member ID or Worker ID
     */
    protected static void checkID(String id, String field) {
        checkNullString(id, field);
        checkLengthString(id, 9, 9, field);

        for (char c : id.toCharArray())
            if (!Character.isLetterOrDigit(c))
                throw new IllegalArgumentException("Invalid " + field);
    }
}