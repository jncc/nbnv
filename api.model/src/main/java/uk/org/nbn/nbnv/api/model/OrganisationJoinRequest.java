package uk.org.nbn.nbnv.api.model;

import java.util.Date;

/**
 *
 * @author Matt Debont
 */
public class OrganisationJoinRequest {

    private int id;
    private User user;
    private Organisation organisation;
    private String requestReason;
    private Date requestDate;
    private int responseTypeID;
    private String responseReason;
    private Date responseDate;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Organisation getOrganisation() {
        return organisation;
    }

    public void setOrganisation(Organisation organisation) {
        this.organisation = organisation;
    }

    public String getRequestReason() {
        return requestReason;
    }

    public void setRequestReason(String requestReason) {
        this.requestReason = requestReason;
    }

    public Date getRequestDate() {
        return requestDate;
    }

    public void setRequestDate(Date requestDate) {
        this.requestDate = requestDate;
    }

    public int getResponseTypeID() {
        return responseTypeID;
    }

    public void setResponseTypeID(int responseTypeID) {
        this.responseTypeID = responseTypeID;
    }

    public String getResponseReason() {
        return responseReason;
    }

    public void setResponseReason(String responseReason) {
        this.responseReason = responseReason;
    }

    public Date getResponseDate() {
        return responseDate;
    }

    public void setResponseDate(Date responseDate) {
        this.responseDate = responseDate;
    }

    public void setError() {
        this.responseTypeID = -1;
    }
}
