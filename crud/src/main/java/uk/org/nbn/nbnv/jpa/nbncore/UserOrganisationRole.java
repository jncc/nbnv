/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.org.nbn.nbnv.jpa.nbncore;

import java.io.Serializable;
import java.util.Collection;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Paul Gilbertson
 */
@Entity
@Table(name = "UserOrganisationRole")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "UserOrganisationRole.findAll", query = "SELECT u FROM UserOrganisationRole u"),
    @NamedQuery(name = "UserOrganisationRole.findById", query = "SELECT u FROM UserOrganisationRole u WHERE u.id = :id"),
    @NamedQuery(name = "UserOrganisationRole.findByLabel", query = "SELECT u FROM UserOrganisationRole u WHERE u.label = :label")})
public class UserOrganisationRole implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "label")
    private String label;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "userOrganisationRole")
    private Collection<UserOrganisationMembership> userOrganisationMembershipCollection;

    public UserOrganisationRole() {
    }

    public UserOrganisationRole(Integer id) {
        this.id = id;
    }

    public UserOrganisationRole(Integer id, String label) {
        this.id = id;
        this.label = label;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    @XmlTransient
    public Collection<UserOrganisationMembership> getUserOrganisationMembershipCollection() {
        return userOrganisationMembershipCollection;
    }

    public void setUserOrganisationMembershipCollection(Collection<UserOrganisationMembership> userOrganisationMembershipCollection) {
        this.userOrganisationMembershipCollection = userOrganisationMembershipCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof UserOrganisationRole)) {
            return false;
        }
        UserOrganisationRole other = (UserOrganisationRole) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "uk.org.nbn.nbnv.jpa.nbncore.UserOrganisationRole[ id=" + id + " ]";
    }
    
}
