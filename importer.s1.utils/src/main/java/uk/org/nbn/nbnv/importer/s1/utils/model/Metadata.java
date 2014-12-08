/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.org.nbn.nbnv.importer.s1.utils.model;

import java.io.Serializable;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;

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
    private int organisationID = -1;
    private String datasetAdminName = "";
    private String datasetAdminPhone = "";
    private String datasetAdminEmail = "";
    private String geographicalRes = "";
    private String recordAtts = "";
    private String recorderNames = "";
    private int datasetAdminID = -1;    
    private String datasetID = "";

    public Metadata() {
        
    }
    
    public Metadata(Map<String, String> map) {
        this.title = getStringFromMap("title", map);
        this.description = getStringFromMap("description", map);
        this.methods = getStringFromMap("methods", map);
        this.purpose = getStringFromMap("purpose", map);
        this.geographic = getStringFromMap("geographic", map);
        this.temporal = getStringFromMap("temporal", map);
        this.quality = getStringFromMap("quality", map);
        this.info = getStringFromMap("info", map);
        this.use = getStringFromMap("use", map);
        this.access = getStringFromMap("access", map);
        this.organisationID = getIntFromMap("organisationID", map);
        this.datasetAdminName = getStringFromMap("datasetAdminName", map);
        this.datasetAdminPhone = getStringFromMap("datasetAdminPhone", map);
        this.datasetAdminEmail = getStringFromMap("datasetAdminEmail", map);
        this.geographicalRes = getStringFromMap("geographicalRes", map);
        this.recordAtts = getStringFromMap("recordAtts", map);
        this.recorderNames = getStringFromMap("recorderNames", map);
        this.datasetAdminID = getIntFromMap("datasetAdminID", map);
        this.datasetID = getStringFromMap("datasetID", map);        
    }
    
    private String getStringFromMap(String key, Map<String, String> map) {
        if (map.containsKey(key) && !StringUtils.isBlank(map.get(key))) {
            return map.get(key);
        }
        return "";
    }
    
    private int getIntFromMap(String key, Map<String, String> map) {
        if (map.containsKey(key) && !StringUtils.isBlank(map.get(key))) {
            return Integer.parseInt(map.get(key));
        }
        return -1;
    }
    
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getMethods() {
        return methods;
    }

    public void setMethods(String methods) {
        this.methods = methods;
    }

    public String getPurpose() {
        return purpose;
    }

    public void setPurpose(String purpose) {
        this.purpose = purpose;
    }

    public String getGeographic() {
        return geographic;
    }

    public void setGeographic(String geographic) {
        this.geographic = geographic;
    }

    public String getTemporal() {
        return temporal;
    }

    public void setTemporal(String temporal) {
        this.temporal = temporal;
    }

    public String getQuality() {
        return quality;
    }

    public void setQuality(String quality) {
        this.quality = quality;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getUse() {
        return use;
    }

    public void setUse(String use) {
        this.use = use;
    }

    public String getAccess() {
        return access;
    }

    public void setAccess(String access) {
        this.access = access;
    }

    public int getOrganisationID() {
        return organisationID;
    }

    public void setOrganisationID(int organisationID) {
        this.organisationID = organisationID;
    }

    public String getDatasetAdminName() {
        return datasetAdminName;
    }

    public void setDatasetAdminName(String datasetAdminName) {
        this.datasetAdminName = datasetAdminName;
    }

    public String getDatasetAdminPhone() {
        return datasetAdminPhone;
    }

    public void setDatasetAdminPhone(String datasetAdminPhone) {
        this.datasetAdminPhone = datasetAdminPhone;
    }

    public String getDatasetAdminEmail() {
        return datasetAdminEmail;
    }

    public void setDatasetAdminEmail(String datasetAdminEmail) {
        this.datasetAdminEmail = datasetAdminEmail;
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

    public int getDatasetAdminID() {
        return datasetAdminID;
    }

    public void setDatasetAdminID(int datasetAdminID) {
        this.datasetAdminID = datasetAdminID;
    }

    public String getDatasetID() {
        return datasetID;
    }

    public void setDatasetID(String datasetID) {
        this.datasetID = datasetID;
    }
}
