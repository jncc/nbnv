/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.org.nbn.nbnv.api.model;

/**
 *
 * @author paulbe
 */
public class DatasetResolutionRecordCount {
    private String datasetKey;
    private String label;
    private int count;

    /**
     * @return the datasetKey
     */
    public String getDatasetKey() {
        return datasetKey;
    }

    /**
     * @param datasetKey the datasetKey to set
     */
    public void setDatasetKey(String datasetKey) {
        this.datasetKey = datasetKey;
    }

    /**
     * @return the label
     */
    public String getLabel() {
        return label;
    }

    /**
     * @param label the label to set
     */
    public void setLabel(String label) {
        this.label = label;
    }

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
}
