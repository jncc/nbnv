/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.org.nbn.nbnv.jpa.nbncore;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author Paul Gilbertson
 */
@Embeddable
public class TaxonObservationDownloadStatisticsPK implements Serializable {
    @Basic(optional = false)
    @NotNull
    @Column(name = "filterID")
    private int filterID;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 8)
    @Column(name = "datasetKey")
    private String datasetKey;

    public TaxonObservationDownloadStatisticsPK() {
    }

    public TaxonObservationDownloadStatisticsPK(int filterID, String datasetKey) {
        this.filterID = filterID;
        this.datasetKey = datasetKey;
    }

    public int getFilterID() {
        return filterID;
    }

    public void setFilterID(int filterID) {
        this.filterID = filterID;
    }

    public String getDatasetKey() {
        return datasetKey;
    }

    public void setDatasetKey(String datasetKey) {
        this.datasetKey = datasetKey;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) filterID;
        hash += (datasetKey != null ? datasetKey.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TaxonObservationDownloadStatisticsPK)) {
            return false;
        }
        TaxonObservationDownloadStatisticsPK other = (TaxonObservationDownloadStatisticsPK) object;
        if (this.filterID != other.filterID) {
            return false;
        }
        if ((this.datasetKey == null && other.datasetKey != null) || (this.datasetKey != null && !this.datasetKey.equals(other.datasetKey))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "uk.org.nbn.nbnv.jpa.nbncore.TaxonObservationDownloadStatisticsPK[ filterID=" + filterID + ", datasetKey=" + datasetKey + " ]";
    }
    
}
