/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.org.nbn.nbnv.importer.ui.model;

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
    private String use = "";
    private String access = "";
    private String datasetAdminName = "";
    private String datasetAdminPhone = "";
    private String datasetAdminEmail = "";
    private String geographicalRes = "10km2";
    private String recordAtts = "N/A";
    private String recorderNames = "N/A";
    private String organisationName = "";
    private String organisationWebsite = "";
    private int datasetAdminID = -1;
    private int organisationID = -1;

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
     * @return the use
     */
    public String getUse() {
        return use;
    }

    /**
     * @param use the use to set
     */
    public void setUse(String use) {
        if (use != null) {
            this.use = use.trim();
        } else {
            this.use = "";
        }
    }

    /**
     * @return the access
     */
    public String getAccess() {
        return access;
    }

    /**
     * @param access the access to set
     */
    public void setAccess(String access) {
        if (access != null) {
            this.access = access.trim();
        } else {
            this.access = "";
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

    public String getDatasetAdminName() {
        return datasetAdminName;
    }

    public void setDatasetAdminName(String datasetAdminName) {
        if (datasetAdminName != null) {
            this.datasetAdminName = datasetAdminName.trim();
        } else {
            this.datasetAdminName = "";
        }
    }

    public String getDatasetAdminPhone() {
        return datasetAdminPhone;
    }

    public void setDatasetAdminPhone(String datasetAdminPhone) {
        if (datasetAdminPhone != null) {
            this.datasetAdminPhone = datasetAdminPhone.trim();
        } else {
            this.datasetAdminPhone = "";
        }
    }

    public String getDatasetAdminEmail() {
        return datasetAdminEmail;
    }

    public void setDatasetAdminEmail(String datasetAdminEmail) {
        if (datasetAdminEmail != null) {
            this.datasetAdminEmail = datasetAdminEmail.trim();
        } else {
            this.datasetAdminEmail = "";
        }
    }

    public int getDatasetAdminID() {
        return datasetAdminID;
    }

    public void setDatasetAdminID(int datasetAdminID) {
        this.datasetAdminID = datasetAdminID;
    }

    public String getGeographicalRes() {
        return geographicalRes;
    }

    public void setGeographicalRes(String geographicalRes) {
        this.geographicalRes = geographicalRes;
    }

    public String getRecordAtts() {
        return recordAtts;
    }

    public void setRecordAtts(String recordAtts) {
        this.recordAtts = recordAtts;
    }

    public String getRecorderNames() {
        return recorderNames;
    }

    public void setRecorderNames(String recorderNames) {
        this.recorderNames = recorderNames;
    }

    public String getOrganisationName() {
        return organisationName;
    }

    public void setOrganisationName(String organisationName) {
        this.organisationName = organisationName;
    }

    public String getOrganisationWebsite() {
        return organisationWebsite;
    }

    public void setOrganisationWebsite(String organisationWebsite) {
        this.organisationWebsite = organisationWebsite;
    }
}
