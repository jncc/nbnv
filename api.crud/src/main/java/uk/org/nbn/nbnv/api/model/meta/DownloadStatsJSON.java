/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.org.nbn.nbnv.api.model.meta;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Matt Debont
 */
@XmlRootElement
public class DownloadStatsJSON {
    private RequestDatasetFilterJSON dataset;
    private String startDate = "";
    private String endDate = "";
    private List<Integer> filterID = new ArrayList<Integer>();
    private List<Integer> userID = new ArrayList<Integer>();
    private List<Integer> organisationID = new ArrayList<Integer>();
    private List<Integer> purposeID = new ArrayList<Integer>();

    public RequestDatasetFilterJSON getDataset() {
        return dataset;
    }

    public void setDataset(RequestDatasetFilterJSON dataset) {
        this.dataset = dataset;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public List<Integer> getFilterID() {
        return filterID;
    }

    public void setFilterID(List<Integer> filterID) {
        this.filterID = filterID;
    }

    public List<Integer> getUserID() {
        return userID;
    }

    public void setUserID(List<Integer> userID) {
        this.userID = userID;
    }

    public List<Integer> getOrganisationID() {
        return organisationID;
    }

    public void setOrganisationID(List<Integer> organisationID) {
        this.organisationID = organisationID;
    }

    public List<Integer> getPurposeID() {
        return purposeID;
    }

    public void setPurposeID(List<Integer> purposeID) {
        this.purposeID = purposeID;
    }
}
