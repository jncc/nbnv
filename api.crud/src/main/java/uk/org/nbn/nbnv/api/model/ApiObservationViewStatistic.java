/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.org.nbn.nbnv.api.model;

import java.text.SimpleDateFormat;
import java.util.Date;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Matt Debont
 */
@XmlRootElement
public class ApiObservationViewStatistic {
    private int viewID;
    private int userID;
    private String forename;
    private String surname;
    private String email;
    private String ip;
    private String datasetKey;
    private String filterText;
    private Date viewTime;
    private String viewTimeString;
    private int viewed;
    private int recordCount;
    private int totalDatasetRecords;

    public int getViewID() {
        return viewID;
    }

    public void setViewID(int viewID) {
        this.viewID = viewID;
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

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getDatasetKey() {
        return datasetKey;
    }

    public void setDatasetKey(String datasetKey) {
        this.datasetKey = datasetKey;
    }

    public String getFilterText() {
        return filterText;
    }

    public void setFilterText(String filterText) {
        this.filterText = filterText;
    }

    public Date getViewTime() {
        return viewTime;
    }

    public void setViewTime(Date viewTime) {
        this.viewTime = viewTime;
        this.viewTimeString = new SimpleDateFormat("EEE MMM dd yyyy HH:mm:ss '('zzz')'").format(viewTime);
    }

    public String getViewTimeString() {
        return viewTimeString;
    }

    public void setViewTimeString(String viewTimeString) {
        this.viewTimeString = viewTimeString;
    }

    public int getViewed() {
        return viewed;
    }

    public void setViewed(int viewed) {
        this.viewed = viewed;
    }

    public int getRecordCount() {
        return recordCount;
    }

    public void setRecordCount(int recordCount) {
        this.recordCount = recordCount;
    }

    public int getTotalDatasetRecords() {
        return totalDatasetRecords;
    }

    public void setTotalDatasetRecords(int totalDatasetRecords) {
        this.totalDatasetRecords = totalDatasetRecords;
    }
}
