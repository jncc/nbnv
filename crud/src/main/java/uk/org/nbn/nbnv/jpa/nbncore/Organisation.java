/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.org.nbn.nbnv.jpa.nbncore;

import java.io.Serializable;
import java.util.Collection;
import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Administrator
 */
@Entity
@Table(name = "Organisation")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Organisation.findAll", query = "SELECT o FROM Organisation o"),
    @NamedQuery(name = "Organisation.findByOrganisationID", query = "SELECT o FROM Organisation o WHERE o.organisationID = :organisationID"),
    @NamedQuery(name = "Organisation.findByAllowPublicRegistration", query = "SELECT o FROM Organisation o WHERE o.allowPublicRegistration = :allowPublicRegistration"),
    @NamedQuery(name = "Organisation.findByAbbreviation", query = "SELECT o FROM Organisation o WHERE o.abbreviation = :abbreviation"),
    @NamedQuery(name = "Organisation.findByPostcode", query = "SELECT o FROM Organisation o WHERE o.postcode = :postcode"),
    @NamedQuery(name = "Organisation.findByWebsite", query = "SELECT o FROM Organisation o WHERE o.website = :website"),
    @NamedQuery(name = "Organisation.findByContactEmail", query = "SELECT o FROM Organisation o WHERE o.contactEmail = :contactEmail"),
    @NamedQuery(name = "Organisation.findByOrganisationName", query = "SELECT o FROM Organisation o WHERE o.organisationName = :organisationName"),
    @NamedQuery(name = "Organisation.findByContactName", query = "SELECT o FROM Organisation o WHERE o.contactName = :contactName"),
    @NamedQuery(name = "Organisation.findByAddress", query = "SELECT o FROM Organisation o WHERE o.address = :address"),
    @NamedQuery(name = "Organisation.findByLogoURL", query = "SELECT o FROM Organisation o WHERE o.logoURL = :logoURL"),
    @NamedQuery(name = "Organisation.findBySummary", query = "SELECT o FROM Organisation o WHERE o.summary = :summary"),
    @NamedQuery(name = "Organisation.findByPhone", query = "SELECT o FROM Organisation o WHERE o.phone = :phone")})
public class Organisation implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "organisationID")
    private Integer organisationID;
    @Basic(optional = false)
    @Column(name = "allowPublicRegistration")
    private boolean allowPublicRegistration;
    @Column(name = "abbreviation")
    private String abbreviation;
    @Column(name = "postcode")
    private String postcode;
    @Column(name = "website")
    private String website;
    @Column(name = "contactEmail")
    private String contactEmail;
    @Column(name = "organisationName")
    private String organisationName;
    @Column(name = "contactName")
    private String contactName;
    @Column(name = "address")
    private String address;
    @Column(name = "logo")
    private String logo;
    @Column(name = "logoSmall")
    private String logoSmall;
    @Column(name = "summary")
    private String summary;
    @Column(name = "phone")
    private String phone;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "datasetProvider")
    private Collection<Dataset> datasetCollection;

    public Organisation() {
    }

    public Organisation(Integer organisationID) {
        this.organisationID = organisationID;
    }

    public Organisation(Integer organisationID, boolean allowPublicRegistration) {
        this.organisationID = organisationID;
        this.allowPublicRegistration = allowPublicRegistration;
    }

    public Integer getOrganisationID() {
        return organisationID;
    }

    public void setOrganisationID(Integer organisationID) {
        this.organisationID = organisationID;
    }

    public boolean getAllowPublicRegistration() {
        return allowPublicRegistration;
    }

    public void setAllowPublicRegistration(boolean allowPublicRegistration) {
        this.allowPublicRegistration = allowPublicRegistration;
    }

    public String getAbbreviation() {
        return abbreviation;
    }

    public void setAbbreviation(String abbreviation) {
        this.abbreviation = abbreviation;
    }

    public String getPostcode() {
        return postcode;
    }

    public void setPostcode(String postcode) {
        this.postcode = postcode;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getContactEmail() {
        return contactEmail;
    }

    public void setContactEmail(String contactEmail) {
        this.contactEmail = contactEmail;
    }

    public String getOrganisationName() {
        return organisationName;
    }

    public void setOrganisationName(String organisationName) {
        this.organisationName = organisationName;
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getLogoSmall() {
        return logoSmall;
    }

    public void setLogoSmall(String logoSmall) {
        this.logoSmall = logoSmall;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    @XmlTransient
    public Collection<Dataset> getDatasetCollection() {
        return datasetCollection;
    }

    public void setDatasetCollection(Collection<Dataset> datasetCollection) {
        this.datasetCollection = datasetCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (organisationID != null ? organisationID.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Organisation)) {
            return false;
        }
        Organisation other = (Organisation) object;
        if ((this.organisationID == null && other.organisationID != null) || (this.organisationID != null && !this.organisationID.equals(other.organisationID))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "uk.org.nbn.nbnv.jpa.nbncore.Organisation[ organisationID=" + organisationID + " ]";
    }
    
}
