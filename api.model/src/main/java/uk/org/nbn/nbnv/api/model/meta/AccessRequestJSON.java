/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.org.nbn.nbnv.api.model.meta;

/**
 *
 * @author Paul Gilbertson
 */
public class AccessRequestJSON {
    private String sensitive;
    private AccessRequestSpatialFilterJSON spatial;
    private AccessRequestTaxonFilterJSON taxon;
    private AccessRequestYearFilterJSON year;
    private AccessRequestDatasetFilterJSON dataset;
    private AccessRequestReasonJSON reason;
    private AccessRequestTimeLimitJSON time;

    /**
     * @return the sensitive
     */
    public String getSensitive() {
        return sensitive;
    }

    /**
     * @param sensitive the sensitive to set
     */
    public void setSensitive(String sensitive) {
        this.sensitive = sensitive;
    }

    /**
     * @return the spatial
     */
    public AccessRequestSpatialFilterJSON getSpatial() {
        return spatial;
    }

    /**
     * @param spatial the spatial to set
     */
    public void setSpatial(AccessRequestSpatialFilterJSON spatial) {
        this.spatial = spatial;
    }

    /**
     * @return the taxon
     */
    public AccessRequestTaxonFilterJSON getTaxon() {
        return taxon;
    }

    /**
     * @param taxon the taxon to set
     */
    public void setTaxon(AccessRequestTaxonFilterJSON taxon) {
        this.taxon = taxon;
    }

    /**
     * @return the year
     */
    public AccessRequestYearFilterJSON getYear() {
        return year;
    }

    /**
     * @param year the year to set
     */
    public void setYear(AccessRequestYearFilterJSON year) {
        this.year = year;
    }

    /**
     * @return the dataset
     */
    public AccessRequestDatasetFilterJSON getDataset() {
        return dataset;
    }

    /**
     * @param dataset the dataset to set
     */
    public void setDataset(AccessRequestDatasetFilterJSON dataset) {
        this.dataset = dataset;
    }

    /**
     * @return the reason
     */
    public AccessRequestReasonJSON getReason() {
        return reason;
    }

    /**
     * @param reason the reason to set
     */
    public void setReason(AccessRequestReasonJSON reason) {
        this.reason = reason;
    }

    /**
     * @return the time
     */
    public AccessRequestTimeLimitJSON getTime() {
        return time;
    }

    /**
     * @param time the time to set
     */
    public void setTime(AccessRequestTimeLimitJSON time) {
        this.time = time;
    }
}
