/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.org.nbn.nbnv.jpa.nbncore;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Administrator
 */
@Entity
@Table(name = "TaxonObservation")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "TaxonObservation.findAll", query = "SELECT t FROM TaxonObservation t"),
    @NamedQuery(name = "TaxonObservation.findByObservationID", query = "SELECT t FROM TaxonObservation t WHERE t.observationID = :observationID"),
    @NamedQuery(name = "TaxonObservation.findByObservationKey", query = "SELECT t FROM TaxonObservation t WHERE t.observationKey = :observationKey"),
    @NamedQuery(name = "TaxonObservation.findByDateStart", query = "SELECT t FROM TaxonObservation t WHERE t.dateStart = :dateStart"),
    @NamedQuery(name = "TaxonObservation.findByDateEnd", query = "SELECT t FROM TaxonObservation t WHERE t.dateEnd = :dateEnd"),
    @NamedQuery(name = "TaxonObservation.findByAbsenceRecord", query = "SELECT t FROM TaxonObservation t WHERE t.absenceRecord = :absenceRecord"),
    @NamedQuery(name = "TaxonObservation.findBySensitiveRecord", query = "SELECT t FROM TaxonObservation t WHERE t.sensitiveRecord = :sensitiveRecord")})
public class TaxonObservation implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "observationID")
    private Integer observationID;
    @Basic(optional = false)
    @Column(name = "observationKey")
    private String observationKey;
    @Column(name = "dateStart")
    @Temporal(TemporalType.DATE)
    private Date dateStart;
    @Column(name = "dateEnd")
    @Temporal(TemporalType.DATE)
    private Date dateEnd;
    @Basic(optional = false)
    @Column(name = "absenceRecord")
    private boolean absenceRecord;
    @Basic(optional = false)
    @Column(name = "sensitiveRecord")
    private boolean sensitiveRecord;
    @JoinColumn(name = "taxonVersionKey", referencedColumnName = "taxonVersionKey")
    @ManyToOne(optional = false)
    private Taxon taxonVersionKey;
    @JoinColumn(name = "siteID", referencedColumnName = "siteID")
    @ManyToOne(optional = false)
    private Site siteID;
    @JoinColumn(name = "sampleID", referencedColumnName = "sampleID")
    @ManyToOne(optional = false)
    private Sample sampleID;
    @JoinColumn(name = "determinerID", referencedColumnName = "recorderID")
    @ManyToOne
    private Recorder determinerID;
    @JoinColumn(name = "recorderID", referencedColumnName = "recorderID")
    @ManyToOne
    private Recorder recorderID;
    @JoinColumn(name = "featureID", referencedColumnName = "featureID")
    @ManyToOne(optional = false)
    private Feature featureID;
    @JoinColumn(name = "dateType", referencedColumnName = "dateTypeKey")
    @ManyToOne(optional = false)
    private DateType dateType;

    public TaxonObservation() {
    }

    public TaxonObservation(Integer observationID) {
        this.observationID = observationID;
    }

    public TaxonObservation(Integer observationID, String observationKey, boolean absenceRecord, boolean sensitiveRecord) {
        this.observationID = observationID;
        this.observationKey = observationKey;
        this.absenceRecord = absenceRecord;
        this.sensitiveRecord = sensitiveRecord;
    }

    public Integer getObservationID() {
        return observationID;
    }

    public void setObservationID(Integer observationID) {
        this.observationID = observationID;
    }

    public String getObservationKey() {
        return observationKey;
    }

    public void setObservationKey(String observationKey) {
        this.observationKey = observationKey;
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

    public DateType getDateType() {
        return dateType;
    }

    public void setDateType(DateType dateType) {
        this.dateType = dateType;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (observationID != null ? observationID.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TaxonObservation)) {
            return false;
        }
        TaxonObservation other = (TaxonObservation) object;
        if ((this.observationID == null && other.observationID != null) || (this.observationID != null && !this.observationID.equals(other.observationID))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "uk.org.nbn.nbnv.jpa.nbncore.TaxonObservation[ observationID=" + observationID + " ]";
    }
    
}
