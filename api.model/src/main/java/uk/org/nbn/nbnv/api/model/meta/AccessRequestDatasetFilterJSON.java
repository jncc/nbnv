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
public class AccessRequestDatasetFilterJSON extends AccessRequestFilterJSON {
    private List<String> datasets;

    /**
     * @return the datasets
     */
    public List<String> getDatasets() {
        return datasets;
    }

    /**
     * @param datasets the datasets to set
     */
    public void setDatasets(List<String> datasets) {
        this.datasets = datasets;
    }
}
