/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.org.nbn.nbnv.api.model.meta;

import java.util.List;

/**
 *
 * @author Paul Gilbertson
 */
public class AccessRequestJSON {
    private String sensitive;
    private List<AccessRequestFilterJSON> filters;
    private AccessRequestDatasetSelectionJSON datasetselection;
    private AccessRequestRequestJSON request;

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
     * @return the filters
     */
    public List<AccessRequestFilterJSON> getFilters() {
        return filters;
    }

    /**
     * @param filters the filters to set
     */
    public void setFilters(List<AccessRequestFilterJSON> filters) {
        this.filters = filters;
    }

    /**
     * @return the datasetselection
     */
    public AccessRequestDatasetSelectionJSON getDatasetselection() {
        return datasetselection;
    }

    /**
     * @param datasetselection the datasetselection to set
     */
    public void setDatasetselection(AccessRequestDatasetSelectionJSON datasetselection) {
        this.datasetselection = datasetselection;
    }

    /**
     * @return the request
     */
    public AccessRequestRequestJSON getRequest() {
        return request;
    }

    /**
     * @param request the request to set
     */
    public void setRequest(AccessRequestRequestJSON request) {
        this.request = request;
    }
}
