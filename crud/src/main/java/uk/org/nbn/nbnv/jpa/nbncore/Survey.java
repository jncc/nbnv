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
@Table(name = "Survey")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Survey.findAll", query = "SELECT s FROM Survey s"),
    @NamedQuery(name = "Survey.findBySurveyID", query = "SELECT s FROM Survey s WHERE s.surveyID = :surveyID"),
    @NamedQuery(name = "Survey.findBySurveyKey", query = "SELECT s FROM Survey s WHERE s.surveyKey = :surveyKey"),
    @NamedQuery(name = "Survey.findByTitle", query = "SELECT s FROM Survey s WHERE s.title = :title"),
    @NamedQuery(name = "Survey.findByDescription", query = "SELECT s FROM Survey s WHERE s.description = :description"),
    @NamedQuery(name = "Survey.findByGeographicalCoverage", query = "SELECT s FROM Survey s WHERE s.geographicalCoverage = :geographicalCoverage"),
    @NamedQuery(name = "Survey.findByTemporalCoverage", query = "SELECT s FROM Survey s WHERE s.temporalCoverage = :temporalCoverage")})
public class Survey implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "surveyID")
    private Integer surveyID;
    @Basic(optional = false)
    @Column(name = "surveyKey")
    private String surveyKey;
    @Basic(optional = false)
    @Column(name = "title")
    private String title;
    @Column(name = "description")
    private String description;
    @Column(name = "geographicalCoverage")
    private String geographicalCoverage;
    @Column(name = "temporalCoverage")
    private String temporalCoverage;
    @JoinColumn(name = "datasetKey", referencedColumnName = "datasetKey")
    @ManyToOne(optional = false)
    private TaxonDataset datasetKey;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "surveyID")
    private Collection<Sample> sampleCollection;

    public Survey() {
    }

    public Survey(Integer surveyID) {
        this.surveyID = surveyID;
    }

    public Survey(Integer surveyID, String surveyKey, String title) {
        this.surveyID = surveyID;
        this.surveyKey = surveyKey;
        this.title = title;
    }
    
    public Survey(String surveyKey, String title) {
        this.surveyKey = surveyKey;
        this.title = title;
    }

    public Integer getSurveyID() {
        return surveyID;
    }

    public void setSurveyID(Integer surveyID) {
        this.surveyID = surveyID;
    }

    public String getSurveyKey() {
        return surveyKey;
    }

    public void setSurveyKey(String surveyKey) {
        this.surveyKey = surveyKey;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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

    public TaxonDataset getDatasetKey() {
        return datasetKey;
    }

    public void setDatasetKey(TaxonDataset datasetKey) {
        this.datasetKey = datasetKey;
    }

    @XmlTransient
    public Collection<Sample> getSampleCollection() {
        return sampleCollection;
    }

    public void setSampleCollection(Collection<Sample> sampleCollection) {
        this.sampleCollection = sampleCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (surveyID != null ? surveyID.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Survey)) {
            return false;
        }
        Survey other = (Survey) object;
        if ((this.surveyID == null && other.surveyID != null) || (this.surveyID != null && !this.surveyID.equals(other.surveyID))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "uk.org.nbn.nbnv.jpa.nbncore.Survey[ surveyID=" + surveyID + " ]";
    }
    
}
