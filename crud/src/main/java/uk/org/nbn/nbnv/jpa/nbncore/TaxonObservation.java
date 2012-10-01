/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.org.nbn.nbnv.jpa.nbncore;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
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
@Table(name = "TaxonObservation")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "TaxonObservation.findAll", query = "SELECT t FROM TaxonObservation t"),
    @NamedQuery(name = "TaxonObservation.findById", query = "SELECT t FROM TaxonObservation t WHERE t.id = :id"),
    @NamedQuery(name = "TaxonObservation.findByProviderKey", query = "SELECT t FROM TaxonObservation t WHERE t.providerKey = :providerKey"),
    @NamedQuery(name = "TaxonObservation.findByDateStart", query = "SELECT t FROM TaxonObservation t WHERE t.dateStart = :dateStart"),
    @NamedQuery(name = "TaxonObservation.findByDateEnd", query = "SELECT t FROM TaxonObservation t WHERE t.dateEnd = :dateEnd"),
    @NamedQuery(name = "TaxonObservation.findByAbsenceRecord", query = "SELECT t FROM TaxonObservation t WHERE t.absenceRecord = :absenceRecord"),
    @NamedQuery(name = "TaxonObservation.findBySensitiveRecord", query = "SELECT t FROM TaxonObservation t WHERE t.sensitiveRecord = :sensitiveRecord")})
public class TaxonObservation implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "providerKey")
    private String providerKey;
    @Column(name = "dateStart")
    @Temporal(TemporalType.DATE)
    private Date dateStart;
    @Column(name = "dateEnd")
    @Temporal(TemporalType.DATE)
    private Date dateEnd;
    @Basic(optional = false)
    @NotNull
    @Column(name = "absenceRecord")
    private boolean absenceRecord;
    @Basic(optional = false)
    @NotNull
    @Column(name = "sensitiveRecord")
    private boolean sensitiveRecord;
    @ManyToMany(mappedBy = "taxonObservationCollection")
    private Collection<Organisation> organisationCollection;
    @JoinTable(name = "UserTaxonObservationAccess", joinColumns = {
        @JoinColumn(name = "observationID", referencedColumnName = "id")}, inverseJoinColumns = {
        @JoinColumn(name = "userID", referencedColumnName = "id")})
    @ManyToMany
    private Collection<User> userCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "taxonObservation")
    private Collection<TaxonObservationAttribute> taxonObservationAttributeCollection;
    @OneToOne(cascade = CascadeType.ALL, mappedBy = "taxonObservation")
    private TaxonObservationPublic taxonObservationPublic;
    @JoinColumn(name = "taxonVersionKey", referencedColumnName = "taxonVersionKey")
    @ManyToOne(optional = false)
    private Taxon taxonVersionKey;
    @JoinColumn(name = "siteID", referencedColumnName = "id")
    @ManyToOne
    private Site siteID;
    @JoinColumn(name = "sampleID", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Sample sampleID;
    @JoinColumn(name = "determinerID", referencedColumnName = "id")
    @ManyToOne
    private Recorder determinerID;
    @JoinColumn(name = "recorderID", referencedColumnName = "id")
    @ManyToOne
    private Recorder recorderID;
    @JoinColumn(name = "featureID", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Feature featureID;
    @JoinColumn(name = "dateTypeKey", referencedColumnName = "key")
    @ManyToOne(optional = false)
    private DateType dateTypeKey;

    public TaxonObservation() {
    }

    public TaxonObservation(Integer id) {
        this.id = id;
    }

    public TaxonObservation(Integer id, String providerKey, boolean absenceRecord, boolean sensitiveRecord) {
        this.id = id;
        this.providerKey = providerKey;
        this.absenceRecord = absenceRecord;
        this.sensitiveRecord = sensitiveRecord;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getProviderKey() {
        return providerKey;
    }

    public void setProviderKey(String providerKey) {
        this.providerKey = providerKey;
    }

    public Date getDateStart() {
        return dateStart;
    }

    public void setDateStart(Date dateStart) {
        this.dateStart = dateStart;
    }

    public Date getDateEnd() {
        return dateEnd;
    }

    public void setDateEnd(Date dateEnd) {
        this.dateEnd = dateEnd;
    }

    public boolean getAbsenceRecord() {
        return absenceRecord;
    }

    public void setAbsenceRecord(boolean absenceRecord) {
        this.absenceRecord = absenceRecord;
    }

    public boolean getSensitiveRecord() {
        return sensitiveRecord;
    }

    public void setSensitiveRecord(boolean sensitiveRecord) {
        this.sensitiveRecord = sensitiveRecord;
    }

    @XmlTransient
    public Collection<Organisation> getOrganisationCollection() {
        return organisationCollection;
    }

    public void setOrganisationCollection(Collection<Organisation> organisationCollection) {
        this.organisationCollection = organisationCollection;
    }

    @XmlTransient
    public Collection<User> getUserCollection() {
        return userCollection;
    }

    public void setUserCollection(Collection<User> userCollection) {
        this.userCollection = userCollection;
    }

    @XmlTransient
    public Collection<TaxonObservationAttribute> getTaxonObservationAttributeCollection() {
        return taxonObservationAttributeCollection;
    }

    public void setTaxonObservationAttributeCollection(Collection<TaxonObservationAttribute> taxonObservationAttributeCollection) {
        this.taxonObservationAttributeCollection = taxonObservationAttributeCollection;
    }

    public TaxonObservationPublic getTaxonObservationPublic() {
        return taxonObservationPublic;
    }

    public void setTaxonObservationPublic(TaxonObservationPublic taxonObservationPublic) {
        this.taxonObservationPublic = taxonObservationPublic;
    }

    public Taxon getTaxonVersionKey() {
        return taxonVersionKey;
    }

    public void setTaxonVersionKey(Taxon taxonVersionKey) {
        this.taxonVersionKey = taxonVersionKey;
    }

    public Site getSiteID() {
        return siteID;
    }

    public void setSiteID(Site siteID) {
        this.siteID = siteID;
    }

    public Sample getSampleID() {
        return sampleID;
    }

    public void setSampleID(Sample sampleID) {
        this.sampleID = sampleID;
    }

    public Recorder getDeterminerID() {
        return determinerID;
    }

    public void setDeterminerID(Recorder determinerID) {
        this.determinerID = determinerID;
    }

    public Recorder getRecorderID() {
        return recorderID;
    }

    public void setRecorderID(Recorder recorderID) {
        this.recorderID = recorderID;
    }

    public Feature getFeatureID() {
        return featureID;
    }

    public void setFeatureID(Feature featureID) {
        this.featureID = featureID;
    }

    public DateType getDateTypeKey() {
        return dateTypeKey;
    }

    public void setDateTypeKey(DateType dateTypeKey) {
        this.dateTypeKey = dateTypeKey;
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
        if (!(object instanceof TaxonObservation)) {
            return false;
        }
        TaxonObservation other = (TaxonObservation) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "uk.org.nbn.nbnv.jpa.nbncore.TaxonObservation[ id=" + id + " ]";
    }
    
}
