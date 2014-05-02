/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.org.nbn.nbnv.revoker.model;

import javax.xml.bind.annotation.XmlRootElement;
import uk.org.nbn.nbnv.api.model.*;

/**
 *
 * @author Matt Debont
 */
@XmlRootElement
public class UserAccessRequest extends AccessRequest {
    private User user;

    /**
     * @return the user
     */
    public User getUser() {
        return user;
    }

    /**
     * @param user the user to set
     */
    public void setUser(User user) {
        this.user = user;
    }

}
