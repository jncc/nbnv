/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package nbn.common.dataset;

import nbn.common.organisation.Organisation;

/**
 *
 * @author Administrator
 */
public abstract class GISLayeredDataset extends Dataset {
    private String gisLayerID;
    GISLayeredDataset(String key, String title, Organisation provider) {
        super(key,title,provider);
    }
    /**
     * @return the gisLayerID
     */
    public String getGisLayerID() {
        return gisLayerID;
    }

    /**
     * @param gisLayerID the gisLayerID to set
     */
    void setGisLayerID(String gisLayerID) {
        this.gisLayerID = gisLayerID;
    }
}
