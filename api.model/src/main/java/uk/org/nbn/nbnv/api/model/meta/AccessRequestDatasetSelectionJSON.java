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
public class AccessRequestDatasetSelectionJSON {
    private int count;
    private List<String> datasets;

    /**
     * @return the count
     */
    public int getCount() {
        return count;
    }

    /**
     * @param count the count to set
     */
    public void setCount(int count) {
        this.count = count;
    }

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
