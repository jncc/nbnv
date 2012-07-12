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
@Table(name = "Sample")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Sample.findAll", query = "SELECT s FROM Sample s"),
    @NamedQuery(name = "Sample.findBySampleID", query = "SELECT s FROM Sample s WHERE s.sampleID = :sampleID"),
    @NamedQuery(name = "Sample.findBySampleKey", query = "SELECT s FROM Sample s WHERE s.sampleKey = :sampleKey"),
    @NamedQuery(name = "Sample.findByDescription", query = "SELECT s FROM Sample s WHERE s.description = :description"),
    @NamedQuery(name = "Sample.findByGeographicalCoverage", query = "SELECT s FROM Sample s WHERE s.geographicalCoverage = :geographicalCoverage"),
    @NamedQuery(name = "Sample.findByTemporalCoverage", query = "SELECT s FROM Sample s WHERE s.temporalCoverage = :temporalCoverage")})
public class Sample implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "sampleID")
    private Integer sampleID;
    @Basic(optional = false)
    @Column(name = "sampleKey")
    private String sampleKey;
    @Column(name = "description")
    private String description;
    @Column(name = "geographicalCoverage")
    private String geographicalCoverage;
    @Column(name = "temporalCoverage")
    private String temporalCoverage;
    @JoinColumn(name = "surveyID", referencedColumnName = "surveyID")
    @ManyToOne(optional = false)
    private Survey surveyID;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "sampleID")
    private Collection<TaxonObservationPublic> taxonObservationPublicCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "sampleID")
    private Collection<TaxonObservation> taxonObservationCollection;

    public Sample() {
    }

    public Sample(Integer sampleID) {
        this.sampleID = sampleID;
    }

    public Sample(Integer sampleID, String sampleKey) {
        this.sampleID = sampleID;
        this.sampleKey = sampleKey;
    }

    public Integer getSampleID() {
        return sampleID;
    }

    public void setSampleID(Integer sampleID) {
        this.sampleID = sampleID;
    }

    public String getSampleKey() {
        return sampleKey;
    }

    public void setSampleKey(String sampleKey) {
        this.sampleKey = sampleKey;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getGeographicalCoverage() {
        return geographicalCoverage;
    }

    public void setGeographicalCoverage(String geographicalCoverage) {
        this.geographicalCoverage = geographicalCoverage;
    }

    public String getTemporalCoverage() {
        return temporalCoverage;
    }

    public void setTemporalCoverage(String temporalCoverage) {
        this.temporalCoverage = temporalCoverage;
    }

    public Survey getSurveyID() {
        return surveyID;
    }

    public void setSurveyID(Survey surveyID) {
        this.surveyID = surveyID;
    }

    @XmlTransient
    public Collection<TaxonObservationPublic> getTaxonObservationPublicCollection() {
        return taxonObservationPublicCollection;
    }

    public void setTaxonObservationPublicCollection(Collection<TaxonObservationPublic> taxonObservationPublicCollection) {
        this.taxonObservationPublicCollection = taxonObservationPublicCollection;
    }

    @XmlTransient
    public Collection<TaxonObservation> getTaxonObservationCollection() {
        return taxonObservationCollection;
    }

    public void setTaxonObservationCollection(Collection<TaxonObservation> taxonObservationCollection) {
        this.taxonObservationCollection = taxonObservationCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (sampleID != null ? sampleID.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Sample)) {
            return false;
        }
        Sample other = (Sample) object;
        if ((this.sampleID == null && other.sampleID != null) || (this.sampleID != null && !this.sampleID.equals(other.sampleID))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "uk.org.nbn.nbnv.jpa.nbncore.Sample[ sampleID=" + sampleID + " ]";
    }
    
}
