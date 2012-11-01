/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.org.nbn.nbnv.importer.spatial.ui.model;

import java.io.Serializable;

/**
 *
 * @author Paul Gilbertson
 */
public class Metadata implements Serializable {
    private String title = "";
    private String description = "";
    private String methods = "";
    private String purpose = "";
    private String geographic = "";
    private String temporal = "";
    private String quality = "";
    private String info = "";
    private int organisationID = -1;
    private String datasetID = "";

    /**
     * @return the title
     */
    public String getTitle() {
        return title;
    }

    /**
     * @param title the title to set
     */
    public void setTitle(String title) {
        if (title != null) {
            this.title = title.trim();
        } else {
            this.title = "";
        }
    }

    /**
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * @param description the description to set
     */
    public void setDescription(String description) {
        if (description != null) {
            this.description = description.trim();
        } else {
            this.description = "";
        }
    }

    /**
     * @return the methods
     */
    public String getMethods() {
        return methods;
    }

    /**
     * @param methods the methods to set
     */
    public void setMethods(String methods) {
        if (methods != null) {
            this.methods = methods.trim();
        } else {
            this.methods = "";
        }
    }

    /**
     * @return the purpose
     */
    public String getPurpose() {
        return purpose;
    }

    /**
     * @param purpose the purpose to set
     */
    public void setPurpose(String purpose) {
        if (purpose != null) {
            this.purpose = purpose.trim();
        } else {
            this.purpose = "";
        }
    }

    /**
     * @return the geographic
     */
    public String getGeographic() {
        return geographic;
    }

    /**
     * @param geographic the geographic to set
     */
    public void setGeographic(String geographic) {
        if (geographic != null) {
            this.geographic = geographic.trim();
        } else {
            this.geographic = "";
        }
    }

    /**
     * @return the temporal
     */
    public String getTemporal() {
        return temporal;
    }

    /**
     * @param temporal the temporal to set
     */
    public void setTemporal(String temporal) {
        if (temporal != null) {
            this.temporal = temporal.trim();
        } else {
            this.temporal = "";
        }
    }

    /**
     * @return the quality
     */
    public String getQuality() {
        return quality;
    }

    /**
     * @param quality the quality to set
     */
    public void setQuality(String quality) {
        if (quality != null) {
            this.quality = quality.trim();
        } else {
            this.quality = "";
        }
    }

    /**
     * @return the info
     */
    public String getInfo() {
        return info;
    }

    /**
     * @param info the info to set
     */
    public void setInfo(String info) {
        if (info != null) {
            this.info = info.trim();
        } else {
            this.info = "";
        }
    }

    /**
     * @return the organisationID
     */
    public int getOrganisationID() {
        return organisationID;
    }

    /**
     * @param organisationID the organisationID to set
     */
    public void setOrganisationID(int organisationID) {
        this.organisationID = organisationID;
    }

    public String getDatasetID() {
        return datasetID;
    }

    public void setDatasetID(String datasetID) {
        this.datasetID = datasetID;
    }
}
