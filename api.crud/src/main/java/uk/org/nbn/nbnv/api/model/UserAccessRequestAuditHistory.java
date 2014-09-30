/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.org.nbn.nbnv.api.model;

import java.sql.Date;
//import java.util.Date;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author paulbe
 */
@XmlRootElement
public class UserAccessRequestAuditHistory {
    private int id;
    private UserAccessRequest request;
    private User actioner;
    private Date timestamp;
    private String action;

    /**
     * @return the id
     */
    public int getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * @return the request
     */
    public UserAccessRequest getRequest() {
        return request;
    }

    /**
     * @param request the request to set
     */
    public void setRequest(UserAccessRequest request) {
        this.request = request;
    }

    /**
     * @return the actioner
     */
    public User getActioner() {
        return actioner;
    }

    /**
     * @param actioner the actioner to set
     */
    public void setActioner(User actioner) {
        this.actioner = actioner;
    }

    /**
     * @return the timestamp
     */
    public Date getTimestamp() {
        return timestamp;
    }

    /**
     * @param timestamp the timestamp to set
     */
    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    /**
     * @return the action
     */
    public String getAction() {
        return action;
    }

    /**
     * @param action the action to set
     */
    public void setAction(String action) {
        this.action = action;
    }
}
