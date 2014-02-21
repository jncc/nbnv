package uk.org.nbn.nbnv.api.model;

import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Paul Gilbertson
 */
@XmlRootElement
public class OrganisationMembership {

    private User user;
    private Organisation organisation;
    private Role role;

    public enum Role {
        member,
        administrator,
        lead
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

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }
}
