package uk.org.nbn.nbnv.api.model;

import java.util.Date;

/**
 * A simple class to hold the details of a nbn user
 * @author Christopher Johnson
 */
public class User {
    public static final int PUBLIC_USER_ID = 0;
    public static final User PUBLIC_USER = new User();
    
    private int id;
    private String forename, surname, username, email, phone;
    private Date registrationDate;
    
    /*Constructor for public user*/
    private User() {
        this.id=PUBLIC_USER_ID;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getForename() {
        return forename;
    }

    public void setForename(String forename) {
        this.forename = forename;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Date getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(Date registrationDate) {
        this.registrationDate = registrationDate;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
    
    @Override public boolean equals(Object o) {
        if(o instanceof User) {
            return id == ((User)o).id;
        }
        return false;
    }

    @Override public int hashCode() {
        return this.id;
    }
}
