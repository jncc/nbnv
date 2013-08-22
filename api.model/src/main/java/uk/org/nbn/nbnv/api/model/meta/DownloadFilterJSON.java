/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.org.nbn.nbnv.api.model.meta;

/**
 *
 * @author Paul Gilbertson
 */
public class DownloadFilterJSON {
    private String sensitive;
    private String includeAttributes = "false";
    private String polygon = "";
    private RequestSpatialFilterJSON spatial;
    private RequestTaxonFilterJSON taxon;
    private RequestYearFilterJSON year;
    private RequestDatasetFilterJSON dataset;
    private RequestReasonJSON reason;

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

    public String getIncludeAttributes() {
        return includeAttributes;
    }

    public void setIncludeAttributes(String includeAttributes) {
        this.includeAttributes = includeAttributes;
    }

    public String getPolygon() {
        return polygon;
    }

    public void setPolygon(String polygon) {
        this.polygon = polygon;
    }
    
    /**
     * @return the spatial
     */
    public RequestSpatialFilterJSON getSpatial() {
        return spatial;
    }

    /**
     * @param spatial the spatial to set
     */
    public void setSpatial(RequestSpatialFilterJSON spatial) {
        this.spatial = spatial;
    }

    /**
     * @return the taxon
     */
    public RequestTaxonFilterJSON getTaxon() {
        return taxon;
    }

    /**
     * @param taxon the taxon to set
     */
    public void setTaxon(RequestTaxonFilterJSON taxon) {
        this.taxon = taxon;
    }

    /**
     * @return the year
     */
    public RequestYearFilterJSON getYear() {
        return year;
    }

    /**
     * @param year the year to set
     */
    public void setYear(RequestYearFilterJSON year) {
        this.year = year;
    }

    /**
     * @return the dataset
     */
    public RequestDatasetFilterJSON getDataset() {
        return dataset;
    }

    /**
     * @param dataset the dataset to set
     */
    public void setDataset(RequestDatasetFilterJSON dataset) {
        this.dataset = dataset;
    }

    /**
     * @return the reason
     */
    public RequestReasonJSON getReason() {
        return reason;
    }

    /**
     * @param reason the reason to set
     */
    public void setReason(RequestReasonJSON reason) {
        this.reason = reason;
    }
}