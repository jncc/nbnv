/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.org.nbn.nbnv.api.model;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * @author Matt Debont
 */
public class DownloadReport {
    private String filterID;
    private String datasetKey;
    
    private int userID;
    private String forename;
    private String surname;
    private String email;
    
    private int purposeID;
    private String purpose;
    private String reason;
    private String filterJSON;
    private String filterText;
    
    private int organisationID;
    private String organisationName;
    
    private Date downloadTime;
    private String downloadTimeString;

    private int recordCount;
    private int totalRecords;
    private int totalDownloaded;

    public String getFilterID() {
        return filterID;
    }

    public void setFilterID(String filterID) {
        this.filterID = filterID;
    }

    public String getDatasetKey() {
        return datasetKey;
    }

    public void setDatasetKey(String datasetKey) {
        this.datasetKey = datasetKey;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public String getForename() {
        return forename;
    }

    public void setForename(String forename) {
        this.forename = forename;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getPurposeID() {
        return purposeID;
    }

    public void setPurposeID(int purposeID) {
        this.purposeID = purposeID;
    }

    public String getPurpose() {
        return purpose;
    }

    public void setPurpose(String purpose) {
        this.purpose = purpose;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getFilterJSON() {
        return filterJSON;
    }

    public void setFilterJSON(String filterJSON) {
        this.filterJSON = filterJSON;
    }

    public String getFilterText() {
        return filterText;
    }

    public void setFilterText(String filterText) {
        this.filterText = filterText;
    }

    public int getOrganisationID() {
        return organisationID;
    }

    public void setOrganisationID(int organisationID) {
        this.organisationID = organisationID;
    }

    public String getOrganisationName() {
        return organisationName;
    }

    public void setOrganisationName(String organisationName) {
        this.organisationName = organisationName;
    }

    public Date getDownloadTime() {
        return downloadTime;
    }

    public void setDownloadTime(Date downloadTime) {
        this.downloadTime = downloadTime;
        this.downloadTimeString = new SimpleDateFormat("EEE MMM dd yyyy HH:mm:ss '('zzz')'").format(downloadTime);
    }
    
    public String getDownloadTimeString() {
        return downloadTimeString;
    }

    public void setDownloadTimeString(String downloadTimeString) {
        this.downloadTimeString = downloadTimeString;
    }

    public int getRecordCount() {
        return recordCount;
    }

    public void setRecordCount(int recordCount) {
        this.recordCount = recordCount;
    }

    public int getTotalRecords() {
        return totalRecords;
    }

    public void setTotalRecords(int totalRecords) {
        this.totalRecords = totalRecords;
    }

    public int getTotalDownloaded() {
        return totalDownloaded;
    }

    public void setTotalDownloaded(int totalDownloaded) {
        this.totalDownloaded = totalDownloaded;
    }
}

