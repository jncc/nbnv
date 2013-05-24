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
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author felix mason
 */
@Entity
@Table(name = "ImportTaxonObservation")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ImportTaxonObservation.findAll", query = "SELECT i FROM ImportTaxonObservation i"),
    @NamedQuery(name = "ImportTaxonObservation.findById", query = "SELECT i FROM ImportTaxonObservation i WHERE i.id = :id"),
    @NamedQuery(name = "ImportTaxonObservation.findByProviderKey", query = "SELECT i FROM ImportTaxonObservation i WHERE i.providerKey = :providerKey"),
    @NamedQuery(name = "ImportTaxonObservation.findByTaxonVersionKey", query = "SELECT i FROM ImportTaxonObservation i WHERE i.taxonVersionKey = :taxonVersionKey"),
    @NamedQuery(name = "ImportTaxonObservation.findByDateStart", query = "SELECT i FROM ImportTaxonObservation i WHERE i.dateStart = :dateStart"),
    @NamedQuery(name = "ImportTaxonObservation.findByDateEnd", query = "SELECT i FROM ImportTaxonObservation i WHERE i.dateEnd = :dateEnd"),
    @NamedQuery(name = "ImportTaxonObservation.findByDateTypeKey", query = "SELECT i FROM ImportTaxonObservation i WHERE i.dateTypeKey = :dateTypeKey"),
    @NamedQuery(name = "ImportTaxonObservation.findByAbsenceRecord", query = "SELECT i FROM ImportTaxonObservation i WHERE i.absenceRecord = :absenceRecord"),
    @NamedQuery(name = "ImportTaxonObservation.findBySensitiveRecord", query = "SELECT i FROM ImportTaxonObservation i WHERE i.sensitiveRecord = :sensitiveRecord")})
public class ImportTaxonObservation implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "providerKey")
    private String providerKey;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 16)
    @Column(name = "taxonVersionKey")
    private String taxonVersionKey;
    @Column(name = "dateStart")
    @Temporal(TemporalType.DATE)
    private Date dateStart;
    @Column(name = "dateEnd")
    @Temporal(TemporalType.DATE)
    private Date dateEnd;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 2)
    @Column(name = "dateTypeKey")
    private String dateTypeKey;
    @Basic(optional = false)
    @NotNull
    @Column(name = "absenceRecord")
    private boolean absenceRecord;
    @Basic(optional = false)
    @NotNull
    @Column(name = "sensitiveRecord")
    private boolean sensitiveRecord;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "importTaxonObservation")
    private Collection<ImportTaxonObservationAttribute> importTaxonObservationAttributeCollection;
    @OneToOne(cascade = CascadeType.ALL, mappedBy = "importTaxonObservation")
    private ImportTaxonObservationPublic importTaxonObservationPublic;
    @JoinColumn(name = "siteID", referencedColumnName = "id")
    @ManyToOne
    private ImportSite siteID;
    @JoinColumn(name = "sampleID", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private ImportSample sampleID;
    @JoinColumn(name = "recorderID", referencedColumnName = "id")
    @ManyToOne
    private ImportRecorder recorderID;
    @JoinColumn(name = "determinerID", referencedColumnName = "id")
    @ManyToOne
    private ImportRecorder determinerID;
    @JoinColumn(name = "featureID", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private ImportFeature featureID;

    public ImportTaxonObservation() {
    }

    public ImportTaxonObservation(Integer id) {
        this.id = id;
    }

    public ImportTaxonObservation(Integer id, String providerKey, String taxonVersionKey, String dateTypeKey, boolean absenceRecord, boolean sensitiveRecord) {
        this.id = id;
        this.providerKey = providerKey;
        this.taxonVersionKey = taxonVersionKey;
        this.dateTypeKey = dateTypeKey;
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

    public String getTaxonVersionKey() {
        return taxonVersionKey;
    }

    public void setTaxonVersionKey(String taxonVersionKey) {
        this.taxonVersionKey = taxonVersionKey;
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

    public String getDateTypeKey() {
        return dateTypeKey;
    }

    public void setDateTypeKey(String dateTypeKey) {
        this.dateTypeKey = dateTypeKey;
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
    public Collection<ImportTaxonObservationAttribute> getImportTaxonObservationAttributeCollection() {
        return importTaxonObservationAttributeCollection;
    }

    public void setImportTaxonObservationAttributeCollection(Collection<ImportTaxonObservationAttribute> importTaxonObservationAttributeCollection) {
        this.importTaxonObservationAttributeCollection = importTaxonObservationAttributeCollection;
    }

    public ImportTaxonObservationPublic getImportTaxonObservationPublic() {
        return importTaxonObservationPublic;
    }

    public void setImportTaxonObservationPublic(ImportTaxonObservationPublic importTaxonObservationPublic) {
        this.importTaxonObservationPublic = importTaxonObservationPublic;
    }

    public ImportSite getSiteID() {
        return siteID;
    }

    public void setSiteID(ImportSite siteID) {
        this.siteID = siteID;
    }

    public ImportSample getSampleID() {
        return sampleID;
    }

    public void setSampleID(ImportSample sampleID) {
        this.sampleID = sampleID;
    }

    public ImportRecorder getRecorderID() {
        return recorderID;
    }

    public void setRecorderID(ImportRecorder recorderID) {
        this.recorderID = recorderID;
    }

    public ImportRecorder getDeterminerID() {
        return determinerID;
    }

    public void setDeterminerID(ImportRecorder determinerID) {
        this.determinerID = determinerID;
    }

    public ImportFeature getFeatureID() {
        return featureID;
    }

    public void setFeatureID(ImportFeature featureID) {
        this.featureID = featureID;
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
        if (!(object instanceof ImportTaxonObservation)) {
            return false;
        }
        ImportTaxonObservation other = (ImportTaxonObservation) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "uk.org.nbn.nbnv.jpa.nbncore.ImportTaxonObservation[ id=" + id + " ]";
    }
    
}
