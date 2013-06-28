/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.org.nbn.nbnv.jpa.nbncore;

import java.io.Serializable;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Paul Gilbertson
 */
@Entity
@Table(name = "UserOrganisationMembership")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "UserOrganisationMembership.findAll", query = "SELECT u FROM UserOrganisationMembership u"),
    @NamedQuery(name = "UserOrganisationMembership.findByUserID", query = "SELECT u FROM UserOrganisationMembership u WHERE u.userOrganisationMembershipPK.userID = :userID"),
    @NamedQuery(name = "UserOrganisationMembership.findByOrganisationID", query = "SELECT u FROM UserOrganisationMembership u WHERE u.userOrganisationMembershipPK.organisationID = :organisationID")})
public class UserOrganisationMembership implements Serializable {
    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected UserOrganisationMembershipPK userOrganisationMembershipPK;
    @JoinColumn(name = "organisationRoleID", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private UserOrganisationRole userOrganisationRole;
    @JoinColumn(name = "userID", referencedColumnName = "id", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private User user;
    @JoinColumn(name = "organisationID", referencedColumnName = "id", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Organisation organisation;

    public UserOrganisationMembership() {
    }

    public UserOrganisationMembership(UserOrganisationMembershipPK userOrganisationMembershipPK) {
        this.userOrganisationMembershipPK = userOrganisationMembershipPK;
    }

    public UserOrganisationMembership(int userID, int organisationID) {
        this.userOrganisationMembershipPK = new UserOrganisationMembershipPK(userID, organisationID);
    }

    public UserOrganisationMembershipPK getUserOrganisationMembershipPK() {
        return userOrganisationMembershipPK;
    }

    public void setUserOrganisationMembershipPK(UserOrganisationMembershipPK userOrganisationMembershipPK) {
        this.userOrganisationMembershipPK = userOrganisationMembershipPK;
    }

    public UserOrganisationRole getUserOrganisationRole() {
        return userOrganisationRole;
    }

    public void setUserOrganisationRole(UserOrganisationRole userOrganisationRole) {
        this.userOrganisationRole = userOrganisationRole;
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

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (userOrganisationMembershipPK != null ? userOrganisationMembershipPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof UserOrganisationMembership)) {
            return false;
        }
        UserOrganisationMembership other = (UserOrganisationMembership) object;
        if ((this.userOrganisationMembershipPK == null && other.userOrganisationMembershipPK != null) || (this.userOrganisationMembershipPK != null && !this.userOrganisationMembershipPK.equals(other.userOrganisationMembershipPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "uk.org.nbn.nbnv.jpa.nbncore.UserOrganisationMembership[ userOrganisationMembershipPK=" + userOrganisationMembershipPK + " ]";
    }
    
}
