package uk.org.nbn.nbnv.api.model;

import java.util.Date;
import javax.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;

/**
 * A simple class to hold the details of a nbn user
 * @author Christopher Johnson
 */
public class User {
    public static final int PUBLIC_USER_ID = 1;
    public static final User PUBLIC_USER;
    
    static {
        PUBLIC_USER = new User();
        PUBLIC_USER.setId(PUBLIC_USER_ID);
    }
    
    private int id;
    @NotEmpty
    private String forename;
    @NotEmpty
    private String surname;
    @NotEmpty
    private String username;
    
    @Email
    private String email;
    
    @NotEmpty
    private String phone;
    
    @NotEmpty
    private String password;
    private Date registrationDate;

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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
