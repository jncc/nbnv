/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.org.nbn.nbnv.api.model.meta;

import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Matt Debont
 */
@XmlRootElement
public class OrganisationJoinRequestJSON {
    private int id;
    private int userID;
    private int organisationID;
    private String reason;
    private int responseType;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
    
    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public int getOrganisationID() {
        return organisationID;
    }

    public void setOrganisationID(int organisationID) {
        this.organisationID = organisationID;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public int getResponseType() {
        return responseType;
    }

    public void setResponseType(int responseType) {
        this.responseType = responseType;
    }
}
