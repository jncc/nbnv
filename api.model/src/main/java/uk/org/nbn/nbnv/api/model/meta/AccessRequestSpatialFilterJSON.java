/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.org.nbn.nbnv.api.model.meta;

/**
 *
 * @author paulbe
 */
public class AccessRequestSpatialFilterJSON extends AccessRequestFilterJSON {
    private String feature = "";
    private String match = "overlap";
    private String dataset = "";

    /**
     * @return the feature
     */
    public String getFeature() {
        return feature;
    }

    /**
     * @param feature the feature to set
     */
    public void setFeature(String feature) {
        this.feature = feature;
    }

    /**
     * @return the match
     */
    public String getMatch() {
        return match;
    }

    /**
     * @param match the match to set
     */
    public void setMatch(String match) {
        this.match = match;
    }

    /**
     * @return the dataset
     */
    public String getDataset() {
        return dataset;
    }

    /**
     * @param dataset the dataset to set
     */
    public void setDataset(String dataset) {
        this.dataset = dataset;
    }
}
