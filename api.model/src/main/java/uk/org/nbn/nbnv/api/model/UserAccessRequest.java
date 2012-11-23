/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.org.nbn.nbnv.api.model;

/**
 *
 * @author Paul Gilbertson
 */
public class UserAccessRequest extends AccessRequest {
    private int userID;

    /**
     * @return the user
     */
    public int getUserID() {
        return userID;
    }

    /**
     * @param user the user to set
     */
    public void setUser(int userID) {
        this.userID = userID;
    }
}
