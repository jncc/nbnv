/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.org.nbn.nbnv.jpa.nbncore;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Paul Gilbertson
 */
@Entity
@Table(name = "User")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "User.findAll", query = "SELECT u FROM User u"),
    @NamedQuery(name = "User.findById", query = "SELECT u FROM User u WHERE u.id = :id"),
    @NamedQuery(name = "User.findByUsername", query = "SELECT u FROM User u WHERE u.username = :username"),
    @NamedQuery(name = "User.findByForename", query = "SELECT u FROM User u WHERE u.forename = :forename"),
    @NamedQuery(name = "User.findBySurname", query = "SELECT u FROM User u WHERE u.surname = :surname"),
    @NamedQuery(name = "User.findByPhone", query = "SELECT u FROM User u WHERE u.phone = :phone"),
    @NamedQuery(name = "User.findByEmail", query = "SELECT u FROM User u WHERE u.email = :email"),
    @NamedQuery(name = "User.findByActive", query = "SELECT u FROM User u WHERE u.active = :active"),
    @NamedQuery(name = "User.findByActivationKey", query = "SELECT u FROM User u WHERE u.activationKey = :activationKey"),
    @NamedQuery(name = "User.findByInvalidEmail", query = "SELECT u FROM User u WHERE u.invalidEmail = :invalidEmail"),
    @NamedQuery(name = "User.findByAllowEmailAlerts", query = "SELECT u FROM User u WHERE u.allowEmailAlerts = :allowEmailAlerts"),
    @NamedQuery(name = "User.findBySubscribedToAdminMails", query = "SELECT u FROM User u WHERE u.subscribedToAdminMails = :subscribedToAdminMails"),
    @NamedQuery(name = "User.findBySubscribedToNBNMarketting", query = "SELECT u FROM User u WHERE u.subscribedToNBNMarketting = :subscribedToNBNMarketting"),
    @NamedQuery(name = "User.findByBannedFromValidation", query = "SELECT u FROM User u WHERE u.bannedFromValidation = :bannedFromValidation"),
    @NamedQuery(name = "User.findByEnglishNameOrder", query = "SELECT u FROM User u WHERE u.englishNameOrder = :englishNameOrder"),
    @NamedQuery(name = "User.findByRegistrationDate", query = "SELECT u FROM User u WHERE u.registrationDate = :registrationDate"),
    @NamedQuery(name = "User.findByLastLoggedIn", query = "SELECT u FROM User u WHERE u.lastLoggedIn = :lastLoggedIn")})
public class User implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 40)
    @Column(name = "username")
    private String username;
    @Basic(optional = false)
    @NotNull
    @Lob
    @Column(name = "password_sha1")
    private byte[] passwordSha1;
    @Basic(optional = false)
    @NotNull
    @Lob
    @Column(name = "password_md5_sha1")
    private byte[] passwordMd5Sha1;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 25)
    @Column(name = "forename")
    private String forename;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 25)
    @Column(name = "surname")
    private String surname;
    // @Pattern(regexp="^\\(?(\\d{3})\\)?[- ]?(\\d{3})[- ]?(\\d{4})$", message="Invalid phone/fax format, should be as xxx-xxx-xxxx")//if the field contains phone or fax number consider using this annotation to enforce field validation
    @Size(max = 15)
    @Column(name = "phone")
    private String phone;
    // @Pattern(regexp="[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?", message="Invalid email")//if the field contains email address consider using this annotation to enforce field validation
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 70)
    @Column(name = "email")
    private String email;
    @Basic(optional = false)
    @NotNull
    @Column(name = "active")
    private boolean active;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 12)
    @Column(name = "activationKey")
    private String activationKey;
    @Basic(optional = false)
    @NotNull
    @Column(name = "invalidEmail")
    private boolean invalidEmail;
    @Basic(optional = false)
    @NotNull
    @Column(name = "allowEmailAlerts")
    private boolean allowEmailAlerts;
    @Basic(optional = false)
    @NotNull
    @Column(name = "subscribedToAdminMails")
    private boolean subscribedToAdminMails;
    @Basic(optional = false)
    @NotNull
    @Column(name = "subscribedToNBNMarketting")
    private boolean subscribedToNBNMarketting;
    @Basic(optional = false)
    @NotNull
    @Column(name = "bannedFromValidation")
    private boolean bannedFromValidation;
    @Basic(optional = false)
    @NotNull
    @Column(name = "englishNameOrder")
    private boolean englishNameOrder;
    @Column(name = "registrationDate")
    @Temporal(TemporalType.TIMESTAMP)
    private Date registrationDate;
    @Column(name = "lastLoggedIn")
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastLoggedIn;
    @ManyToMany(mappedBy = "userCollection")
    private Collection<TaxonObservation> taxonObservationCollection;
    @ManyToMany(mappedBy = "userCollection")
    private Collection<Dataset> datasetCollection;
    @JoinColumn(name = "userTypeID", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private UserType userTypeID;
    @OneToMany(mappedBy = "userID")
    private Collection<TaxonObservationDownload> taxonObservationDownloadCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "userID")
    private Collection<UserAccessRequest> userAccessRequestCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "user")
    private Collection<UserOrganisationMembership> userOrganisationMembershipCollection;

    public User() {
    }

    public User(Integer id) {
        this.id = id;
    }

    public User(Integer id, String username, byte[] passwordSha1, byte[] passwordMd5Sha1, String forename, String surname, String email, boolean active, String activationKey, boolean invalidEmail, boolean allowEmailAlerts, boolean subscribedToAdminMails, boolean subscribedToNBNMarketting, boolean bannedFromValidation, boolean englishNameOrder) {
        this.id = id;
        this.username = username;
        this.passwordSha1 = passwordSha1;
        this.passwordMd5Sha1 = passwordMd5Sha1;
        this.forename = forename;
        this.surname = surname;
        this.email = email;
        this.active = active;
        this.activationKey = activationKey;
        this.invalidEmail = invalidEmail;
        this.allowEmailAlerts = allowEmailAlerts;
        this.subscribedToAdminMails = subscribedToAdminMails;
        this.subscribedToNBNMarketting = subscribedToNBNMarketting;
        this.bannedFromValidation = bannedFromValidation;
        this.englishNameOrder = englishNameOrder;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public byte[] getPasswordSha1() {
        return passwordSha1;
    }

    public void setPasswordSha1(byte[] passwordSha1) {
        this.passwordSha1 = passwordSha1;
    }

    public byte[] getPasswordMd5Sha1() {
        return passwordMd5Sha1;
    }

    public void setPasswordMd5Sha1(byte[] passwordMd5Sha1) {
        this.passwordMd5Sha1 = passwordMd5Sha1;
    }

    public String getForename() {
        return forename;
    }

    public void setForename(String forename) {
        this.forename = forename;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean getActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public String getActivationKey() {
        return activationKey;
    }

    public void setActivationKey(String activationKey) {
        this.activationKey = activationKey;
    }

    public boolean getInvalidEmail() {
        return invalidEmail;
    }

    public void setInvalidEmail(boolean invalidEmail) {
        this.invalidEmail = invalidEmail;
    }

    public boolean getAllowEmailAlerts() {
        return allowEmailAlerts;
    }

    public void setAllowEmailAlerts(boolean allowEmailAlerts) {
        this.allowEmailAlerts = allowEmailAlerts;
    }

    public boolean getSubscribedToAdminMails() {
        return subscribedToAdminMails;
    }

    public void setSubscribedToAdminMails(boolean subscribedToAdminMails) {
        this.subscribedToAdminMails = subscribedToAdminMails;
    }

    public boolean getSubscribedToNBNMarketting() {
        return subscribedToNBNMarketting;
    }

    public void setSubscribedToNBNMarketting(boolean subscribedToNBNMarketting) {
        this.subscribedToNBNMarketting = subscribedToNBNMarketting;
    }

    public boolean getBannedFromValidation() {
        return bannedFromValidation;
    }

    public void setBannedFromValidation(boolean bannedFromValidation) {
        this.bannedFromValidation = bannedFromValidation;
    }

    public boolean getEnglishNameOrder() {
        return englishNameOrder;
    }

    public void setEnglishNameOrder(boolean englishNameOrder) {
        this.englishNameOrder = englishNameOrder;
    }

    public Date getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(Date registrationDate) {
        this.registrationDate = registrationDate;
    }

    public Date getLastLoggedIn() {
        return lastLoggedIn;
    }

    public void setLastLoggedIn(Date lastLoggedIn) {
        this.lastLoggedIn = lastLoggedIn;
    }

    @XmlTransient
    public Collection<TaxonObservation> getTaxonObservationCollection() {
        return taxonObservationCollection;
    }

    public void setTaxonObservationCollection(Collection<TaxonObservation> taxonObservationCollection) {
        this.taxonObservationCollection = taxonObservationCollection;
    }

    @XmlTransient
    public Collection<Dataset> getDatasetCollection() {
        return datasetCollection;
    }

    public void setDatasetCollection(Collection<Dataset> datasetCollection) {
        this.datasetCollection = datasetCollection;
    }

    public UserType getUserTypeID() {
        return userTypeID;
    }

    public void setUserTypeID(UserType userTypeID) {
        this.userTypeID = userTypeID;
    }

    @XmlTransient
    public Collection<TaxonObservationDownload> getTaxonObservationDownloadCollection() {
        return taxonObservationDownloadCollection;
    }

    public void setTaxonObservationDownloadCollection(Collection<TaxonObservationDownload> taxonObservationDownloadCollection) {
        this.taxonObservationDownloadCollection = taxonObservationDownloadCollection;
    }

    @XmlTransient
    public Collection<UserAccessRequest> getUserAccessRequestCollection() {
        return userAccessRequestCollection;
    }

    public void setUserAccessRequestCollection(Collection<UserAccessRequest> userAccessRequestCollection) {
        this.userAccessRequestCollection = userAccessRequestCollection;
    }

    @XmlTransient
    public Collection<UserOrganisationMembership> getUserOrganisationMembershipCollection() {
        return userOrganisationMembershipCollection;
    }

    public void setUserOrganisationMembershipCollection(Collection<UserOrganisationMembership> userOrganisationMembershipCollection) {
        this.userOrganisationMembershipCollection = userOrganisationMembershipCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof User)) {
            return false;
        }
        User other = (User) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "uk.org.nbn.nbnv.jpa.nbncore.User[ id=" + id + " ]";
    }
    
}
