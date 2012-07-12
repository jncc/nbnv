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
@Table(name = "TaxonDataset")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "TaxonDataset.findAll", query = "SELECT t FROM TaxonDataset t"),
    @NamedQuery(name = "TaxonDataset.findByDatasetKey", query = "SELECT t FROM TaxonDataset t WHERE t.datasetKey = :datasetKey"),
    @NamedQuery(name = "TaxonDataset.findByAllowRecordValidation", query = "SELECT t FROM TaxonDataset t WHERE t.allowRecordValidation = :allowRecordValidation"),
    @NamedQuery(name = "TaxonDataset.findByRecordCount", query = "SELECT t FROM TaxonDataset t WHERE t.recordCount = :recordCount")})
public class TaxonDataset implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "datasetKey")
    private String datasetKey;
    @Basic(optional = false)
    @Column(name = "allowRecordValidation")
    private boolean allowRecordValidation;
    @Basic(optional = false)
    @Column(name = "recordCount")
    private int recordCount;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "datasetKey")
    private Collection<Survey> surveyCollection;
    @JoinColumn(name = "publicResolution", referencedColumnName = "resolutionID")
    @ManyToOne(optional = false)
    private Resolution publicResolution;
    @JoinColumn(name = "maxResolution", referencedColumnName = "resolutionID")
    @ManyToOne(optional = false)
    private Resolution maxResolution;
    @JoinColumn(name = "datasetKey", referencedColumnName = "datasetKey", insertable = false, updatable = false)
    @OneToOne(optional = false)
    private Dataset dataset;

    public TaxonDataset() {
    }

    public TaxonDataset(String datasetKey) {
        this.datasetKey = datasetKey;
    }

    public TaxonDataset(String datasetKey, boolean allowRecordValidation, int recordCount) {
        this.datasetKey = datasetKey;
        this.allowRecordValidation = allowRecordValidation;
        this.recordCount = recordCount;
    }

    public String getDatasetKey() {
        return datasetKey;
    }

    public void setDatasetKey(String datasetKey) {
        this.datasetKey = datasetKey;
    }

    public boolean getAllowRecordValidation() {
        return allowRecordValidation;
    }

    public void setAllowRecordValidation(boolean allowRecordValidation) {
        this.allowRecordValidation = allowRecordValidation;
    }

    public int getRecordCount() {
        return recordCount;
    }

    public void setRecordCount(int recordCount) {
        this.recordCount = recordCount;
    }

    @XmlTransient
    public Collection<Survey> getSurveyCollection() {
        return surveyCollection;
    }

    public void setSurveyCollection(Collection<Survey> surveyCollection) {
        this.surveyCollection = surveyCollection;
    }

    public Resolution getPublicResolution() {
        return publicResolution;
    }

    public void setPublicResolution(Resolution publicResolution) {
        this.publicResolution = publicResolution;
    }

    public Resolution getMaxResolution() {
        return maxResolution;
    }

    public void setMaxResolution(Resolution maxResolution) {
        this.maxResolution = maxResolution;
    }

    public Dataset getDataset() {
        return dataset;
    }

    public void setDataset(Dataset dataset) {
        this.dataset = dataset;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (datasetKey != null ? datasetKey.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TaxonDataset)) {
            return false;
        }
        TaxonDataset other = (TaxonDataset) object;
        if ((this.datasetKey == null && other.datasetKey != null) || (this.datasetKey != null && !this.datasetKey.equals(other.datasetKey))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "uk.org.nbn.nbnv.jpa.nbncore.TaxonDataset[ datasetKey=" + datasetKey + " ]";
    }
    
}
