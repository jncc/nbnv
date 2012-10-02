/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.org.nbn.nbnv.jpa.nbncore;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Paul Gilbertson
 */
@Entity
@Table(name = "TaxonObservationDownloadStatistics")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "TaxonObservationDownloadStatistics.findAll", query = "SELECT t FROM TaxonObservationDownloadStatistics t"),
    @NamedQuery(name = "TaxonObservationDownloadStatistics.findByFilterID", query = "SELECT t FROM TaxonObservationDownloadStatistics t WHERE t.taxonObservationDownloadStatisticsPK.filterID = :filterID"),
    @NamedQuery(name = "TaxonObservationDownloadStatistics.findByDatasetKey", query = "SELECT t FROM TaxonObservationDownloadStatistics t WHERE t.taxonObservationDownloadStatisticsPK.datasetKey = :datasetKey"),
    @NamedQuery(name = "TaxonObservationDownloadStatistics.findByRecordCount", query = "SELECT t FROM TaxonObservationDownloadStatistics t WHERE t.recordCount = :recordCount")})
public class TaxonObservationDownloadStatistics implements Serializable {
    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected TaxonObservationDownloadStatisticsPK taxonObservationDownloadStatisticsPK;
    @Basic(optional = false)
    @NotNull
    @Column(name = "recordCount")
    private int recordCount;
    @JoinColumn(name = "filterID", referencedColumnName = "filterID", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private TaxonObservationDownload taxonObservationDownload;
    @JoinColumn(name = "datasetKey", referencedColumnName = "datasetKey", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private TaxonDataset taxonDataset;

    public TaxonObservationDownloadStatistics() {
    }

    public TaxonObservationDownloadStatistics(TaxonObservationDownloadStatisticsPK taxonObservationDownloadStatisticsPK) {
        this.taxonObservationDownloadStatisticsPK = taxonObservationDownloadStatisticsPK;
    }

    public TaxonObservationDownloadStatistics(TaxonObservationDownloadStatisticsPK taxonObservationDownloadStatisticsPK, int recordCount) {
        this.taxonObservationDownloadStatisticsPK = taxonObservationDownloadStatisticsPK;
        this.recordCount = recordCount;
    }

    public TaxonObservationDownloadStatistics(int filterID, String datasetKey) {
        this.taxonObservationDownloadStatisticsPK = new TaxonObservationDownloadStatisticsPK(filterID, datasetKey);
    }

    public TaxonObservationDownloadStatisticsPK getTaxonObservationDownloadStatisticsPK() {
        return taxonObservationDownloadStatisticsPK;
    }

    public void setTaxonObservationDownloadStatisticsPK(TaxonObservationDownloadStatisticsPK taxonObservationDownloadStatisticsPK) {
        this.taxonObservationDownloadStatisticsPK = taxonObservationDownloadStatisticsPK;
    }

    public int getRecordCount() {
        return recordCount;
    }

    public void setRecordCount(int recordCount) {
        this.recordCount = recordCount;
    }

    public TaxonObservationDownload getTaxonObservationDownload() {
        return taxonObservationDownload;
    }

    public void setTaxonObservationDownload(TaxonObservationDownload taxonObservationDownload) {
        this.taxonObservationDownload = taxonObservationDownload;
    }

    public TaxonDataset getTaxonDataset() {
        return taxonDataset;
    }

    public void setTaxonDataset(TaxonDataset taxonDataset) {
        this.taxonDataset = taxonDataset;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (taxonObservationDownloadStatisticsPK != null ? taxonObservationDownloadStatisticsPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TaxonObservationDownloadStatistics)) {
            return false;
        }
        TaxonObservationDownloadStatistics other = (TaxonObservationDownloadStatistics) object;
        if ((this.taxonObservationDownloadStatisticsPK == null && other.taxonObservationDownloadStatisticsPK != null) || (this.taxonObservationDownloadStatisticsPK != null && !this.taxonObservationDownloadStatisticsPK.equals(other.taxonObservationDownloadStatisticsPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "uk.org.nbn.nbnv.jpa.nbncore.TaxonObservationDownloadStatistics[ taxonObservationDownloadStatisticsPK=" + taxonObservationDownloadStatisticsPK + " ]";
    }
    
}
