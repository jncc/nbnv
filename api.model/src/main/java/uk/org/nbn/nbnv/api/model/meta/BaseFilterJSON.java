/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.org.nbn.nbnv.api.model.meta;

import java.util.List;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Matt Debont
 */
@XmlRootElement
public abstract class BaseFilterJSON {
    protected String sensitive;
    protected String polygon = "";
    protected RequestSpatialFilterJSON spatial;
    protected RequestTaxonFilterJSON taxon;
    protected RequestYearFilterJSON year;
    protected RequestDatasetFilterJSON dataset;
    protected List<Integer> verification;

    public String getSensitive() {
        return sensitive;
    }

    public void setSensitive(String sensitive) {
        this.sensitive = sensitive;
    }

    public String getPolygon() {
        return polygon;
    }

    public void setPolygon(String polygon) {
        this.polygon = polygon;
    }

    public RequestSpatialFilterJSON getSpatial() {
        return spatial;
    }

    public void setSpatial(RequestSpatialFilterJSON spatial) {
        this.spatial = spatial;
    }

    public RequestTaxonFilterJSON getTaxon() {
        return taxon;
    }

    public void setTaxon(RequestTaxonFilterJSON taxon) {
        this.taxon = taxon;
    }

    public RequestYearFilterJSON getYear() {
        return year;
    }

    public void setYear(RequestYearFilterJSON year) {
        this.year = year;
    }

    public RequestDatasetFilterJSON getDataset() {
        return dataset;
    }

    public void setDataset(RequestDatasetFilterJSON dataset) {
        this.dataset = dataset;
    }

    public List<Integer> getVerification() {
        return verification;
    }

    public void setVerification(List<Integer> verification) {
        this.verification = verification;
    }
    
    public abstract BaseFilterJSON toBaseFilter();
}
