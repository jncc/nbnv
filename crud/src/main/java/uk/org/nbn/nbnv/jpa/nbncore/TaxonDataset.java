/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.org.nbn.nbnv.jpa.nbncore;

import java.io.Serializable;
import java.util.Collection;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Paul Gilbertson
 */
@Entity
@Table(name = "TaxonDataset")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "TaxonDataset.findAll", query = "SELECT t FROM TaxonDataset t"),
    @NamedQuery(name = "TaxonDataset.findByDatasetKey", query = "SELECT t FROM TaxonDataset t WHERE t.datasetKey = :datasetKey"),
    @NamedQuery(name = "TaxonDataset.findByAllowRecordValidation", query = "SELECT t FROM TaxonDataset t WHERE t.allowRecordValidation = :allowRecordValidation")})
public class TaxonDataset implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 8)
    @Column(name = "datasetKey")
    private String datasetKey;
    @Basic(optional = false)
    @NotNull
    @Column(name = "allowRecordValidation")
    private boolean allowRecordValidation;
    @JoinColumn(name = "publicResolutionID", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Resolution resolution;
    @JoinColumn(name = "datasetKey", referencedColumnName = "key", insertable = false, updatable = false)
    @OneToOne(optional = false)
    private Dataset dataset;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "taxonDataset")
    private Collection<TaxonObservationDownloadStatistics> taxonObservationDownloadStatisticsCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "taxonDataset")
    private Collection<Survey> surveyCollection;
    @OneToMany(mappedBy = "taxonDataset")
    private Collection<TaxonObservationFilterElement> taxonObservationFilterElementCollection;

    public TaxonDataset() {
    }

    public TaxonDataset(String datasetKey) {
        this.datasetKey = datasetKey;
    }

    public TaxonDataset(String datasetKey, boolean allowRecordValidation) {
        this.datasetKey = datasetKey;
        this.allowRecordValidation = allowRecordValidation;
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

    // manually renamed to *public* resolution for clarity

    public Resolution getPublicResolution() {
        return resolution;
    }

    public void setPublicResolution(Resolution resolution) {
        this.resolution = resolution;
    }

    public Dataset getDataset() {
        return dataset;
    }

    public void setDataset(Dataset dataset) {
        this.dataset = dataset;
    }

    @XmlTransient
    public Collection<TaxonObservationDownloadStatistics> getTaxonObservationDownloadStatisticsCollection() {
        return taxonObservationDownloadStatisticsCollection;
    }

    public void setTaxonObservationDownloadStatisticsCollection(Collection<TaxonObservationDownloadStatistics> taxonObservationDownloadStatisticsCollection) {
        this.taxonObservationDownloadStatisticsCollection = taxonObservationDownloadStatisticsCollection;
    }

    @XmlTransient
    public Collection<Survey> getSurveyCollection() {
        return surveyCollection;
    }

    public void setSurveyCollection(Collection<Survey> surveyCollection) {
        this.surveyCollection = surveyCollection;
    }

    @XmlTransient
    public Collection<TaxonObservationFilterElement> getTaxonObservationFilterElementCollection() {
        return taxonObservationFilterElementCollection;
    }

    public void setTaxonObservationFilterElementCollection(Collection<TaxonObservationFilterElement> taxonObservationFilterElementCollection) {
        this.taxonObservationFilterElementCollection = taxonObservationFilterElementCollection;
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
