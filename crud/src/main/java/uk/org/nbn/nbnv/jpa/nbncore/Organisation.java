/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.org.nbn.nbnv.jpa.nbncore;

import java.io.Serializable;
import java.util.Collection;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Paul Gilbertson
 */
@Entity
@Table(name = "Organisation")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Organisation.findAll", query = "SELECT o FROM Organisation o"),
    @NamedQuery(name = "Organisation.findAllNameSort", query = "SELECT o FROM Organisation o ORDER BY o.name"),
    @NamedQuery(name = "Organisation.findById", query = "SELECT o FROM Organisation o WHERE o.id = :id"),
    @NamedQuery(name = "Organisation.findByName", query = "SELECT o FROM Organisation o WHERE o.name = :name"),
    @NamedQuery(name = "Organisation.findByAbbreviation", query = "SELECT o FROM Organisation o WHERE o.abbreviation = :abbreviation"),
    @NamedQuery(name = "Organisation.findBySummary", query = "SELECT o FROM Organisation o WHERE o.summary = :summary"),
    @NamedQuery(name = "Organisation.findByAddress", query = "SELECT o FROM Organisation o WHERE o.address = :address"),
    @NamedQuery(name = "Organisation.findByPostcode", query = "SELECT o FROM Organisation o WHERE o.postcode = :postcode"),
    @NamedQuery(name = "Organisation.findByPhone", query = "SELECT o FROM Organisation o WHERE o.phone = :phone"),
    @NamedQuery(name = "Organisation.findByWebsite", query = "SELECT o FROM Organisation o WHERE o.website = :website"),
    @NamedQuery(name = "Organisation.findByContactName", query = "SELECT o FROM Organisation o WHERE o.contactName = :contactName"),
    @NamedQuery(name = "Organisation.findByContactEmail", query = "SELECT o FROM Organisation o WHERE o.contactEmail = :contactEmail"),
    @NamedQuery(name = "Organisation.findByAllowPublicRegistration", query = "SELECT o FROM Organisation o WHERE o.allowPublicRegistration = :allowPublicRegistration")})
public class Organisation implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 200)
    @Column(name = "name")
    private String name;
    @Size(max = 10)
    @Column(name = "abbreviation")
    private String abbreviation;
    @Size(max = 2147483647)
    @Column(name = "summary")
    private String summary;
    @Size(max = 200)
    @Column(name = "address")
    private String address;
    @Size(max = 10)
    @Column(name = "postcode")
    private String postcode;
    // @Pattern(regexp="^\\(?(\\d{3})\\)?[- ]?(\\d{3})[- ]?(\\d{4})$", message="Invalid phone/fax format, should be as xxx-xxx-xxxx")//if the field contains phone or fax number consider using this annotation to enforce field validation
    @Size(max = 50)
    @Column(name = "phone")
    private String phone;
    @Size(max = 100)
    @Column(name = "website")
    private String website;
    @Size(max = 120)
    @Column(name = "contactName")
    private String contactName;
    @Size(max = 100)
    @Column(name = "contactEmail")
    private String contactEmail;
    @Basic(optional = false)
    @NotNull
    @Column(name = "allowPublicRegistration")
    private boolean allowPublicRegistration;
    @Lob
    @Column(name = "logoSmall")
    private byte[] logoSmall;
    @Lob
    @Column(name = "logo")
    private byte[] logo;
    @JoinTable(name = "OrganisationTaxonObservationAccess", joinColumns = {
        @JoinColumn(name = "organisationID", referencedColumnName = "id")}, inverseJoinColumns = {
        @JoinColumn(name = "observationID", referencedColumnName = "id")})
    @ManyToMany
    private Collection<TaxonObservation> taxonObservationCollection;
    @OneToMany(mappedBy = "organisation")
    private Collection<TaxonObservationDownload> taxonObservationDownloadCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "organisation")
    private Collection<OrganisationAccessRequest> organisationAccessRequestCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "organisation")
    private Collection<Dataset> datasetCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "organisation")
    private Collection<UserOrganisationMembership> userOrganisationMembershipCollection;
    @Transient
    private String organisationAdmin;

    public Organisation() {
    }

    public Organisation(Integer id) {
        this.id = id;
    }

    public Organisation(Integer id, String name, boolean allowPublicRegistration) {
        this.id = id;
        this.name = name;
        this.allowPublicRegistration = allowPublicRegistration;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAbbreviation() {
        return abbreviation;
    }

    public void setAbbreviation(String abbreviation) {
        this.abbreviation = abbreviation;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPostcode() {
        return postcode;
    }

    public void setPostcode(String postcode) {
        this.postcode = postcode;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public String getContactEmail() {
        return contactEmail;
    }

    public void setContactEmail(String contactEmail) {
        this.contactEmail = contactEmail;
    }

    public boolean getAllowPublicRegistration() {
        return allowPublicRegistration;
    }

    public void setAllowPublicRegistration(boolean allowPublicRegistration) {
        this.allowPublicRegistration = allowPublicRegistration;
    }

    public byte[] getLogoSmall() {
        return logoSmall;
    }

    public void setLogoSmall(byte[] logoSmall) {
        this.logoSmall = logoSmall;
    }

    public byte[] getLogo() {
        return logo;
    }

    public void setLogo(byte[] logo) {
        this.logo = logo;
    }

    @XmlTransient
    public Collection<TaxonObservation> getTaxonObservationCollection() {
        return taxonObservationCollection;
    }

    public void setTaxonObservationCollection(Collection<TaxonObservation> taxonObservationCollection) {
        this.taxonObservationCollection = taxonObservationCollection;
    }

    @XmlTransient
    public Collection<TaxonObservationDownload> getTaxonObservationDownloadCollection() {
        return taxonObservationDownloadCollection;
    }

    public void setTaxonObservationDownloadCollection(Collection<TaxonObservationDownload> taxonObservationDownloadCollection) {
        this.taxonObservationDownloadCollection = taxonObservationDownloadCollection;
    }

    @XmlTransient
    public Collection<OrganisationAccessRequest> getOrganisationAccessRequestCollection() {
        return organisationAccessRequestCollection;
    }

    public void setOrganisationAccessRequestCollection(Collection<OrganisationAccessRequest> organisationAccessRequestCollection) {
        this.organisationAccessRequestCollection = organisationAccessRequestCollection;
    }

    @XmlTransient
    public Collection<Dataset> getDatasetCollection() {
        return datasetCollection;
    }

    public void setDatasetCollection(Collection<Dataset> datasetCollection) {
        this.datasetCollection = datasetCollection;
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
        if (!(object instanceof Organisation)) {
            return false;
        }
        Organisation other = (Organisation) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "uk.org.nbn.nbnv.jpa.nbncore.Organisation[ id=" + id + " ]";
    }

    @Transient
    public String getOrganisationAdmin() {
        return organisationAdmin;
    }

    @Transient
    public void setOrganisationAdmin(String organisationAdmin) {
        this.organisationAdmin = organisationAdmin;
    }
}
