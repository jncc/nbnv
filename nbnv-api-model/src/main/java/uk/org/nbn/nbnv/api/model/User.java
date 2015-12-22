package uk.org.nbn.nbnv.api.model;

import java.util.Date;
import javax.xml.bind.annotation.XmlRootElement;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;

/**
 * A simple class to hold the details of a nbn user
 * @author Christopher Johnson
 */
@XmlRootElement
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
    
    @NotEmpty
    @Email
    private String email;
    
    @NotEmpty
    private String phone;
    
    @NotEmpty
    private String password;
    private Date registrationDate;
    
    private boolean allowEmailAlerts;
    private boolean subscribedToAdminMails;
    private boolean subscribedToNBNMarketting;
    private boolean active;

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
    
        public boolean isAllowEmailAlerts() {
        return allowEmailAlerts;
    }

    public void setAllowEmailAlerts(boolean allowEmailAlerts) {
        this.allowEmailAlerts = allowEmailAlerts;
    }

    public boolean isSubscribedToAdminMails() {
        return subscribedToAdminMails;
    }

    public void setSubscribedToAdminMails(boolean subscribedToAdminMails) {
        this.subscribedToAdminMails = subscribedToAdminMails;
    }

    public boolean isSubscribedToNBNMarketting() {
        return subscribedToNBNMarketting;
    }

    public void setSubscribedToNBNMarketting(boolean subscribedToNBNMarketting) {
        this.subscribedToNBNMarketting = subscribedToNBNMarketting;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
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
