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
    private int feature;
    private String match;
    private String datasetKey;

    /**
     * @return the feature
     */
    public int getFeature() {
        return feature;
    }

    /**
     * @param feature the feature to set
     */
    public void setFeature(int feature) {
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
}
