/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.org.nbn.nbnv.jpa.nbncore;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;

/**
 *
 * @author Paul Gilbertson
 */
@Embeddable
public class UserOrganisationMembershipPK implements Serializable {
    @Basic(optional = false)
    @NotNull
    @Column(name = "userID")
    private int userID;
    @Basic(optional = false)
    @NotNull
    @Column(name = "organisationID")
    private int organisationID;

    public UserOrganisationMembershipPK() {
    }

    public UserOrganisationMembershipPK(int userID, int organisationID) {
        this.userID = userID;
        this.organisationID = organisationID;
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

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) userID;
        hash += (int) organisationID;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof UserOrganisationMembershipPK)) {
            return false;
        }
        UserOrganisationMembershipPK other = (UserOrganisationMembershipPK) object;
        if (this.userID != other.userID) {
            return false;
        }
        if (this.organisationID != other.organisationID) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "uk.org.nbn.nbnv.jpa.nbncore.UserOrganisationMembershipPK[ userID=" + userID + ", organisationID=" + organisationID + " ]";
    }
    
}
