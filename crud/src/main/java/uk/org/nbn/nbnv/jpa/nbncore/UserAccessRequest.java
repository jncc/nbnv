/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.org.nbn.nbnv.jpa.nbncore;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Paul Gilbertson
 */
@Entity
@Table(name = "UserAccessRequest")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "UserAccessRequest.findAll", query = "SELECT u FROM UserAccessRequest u"),
    @NamedQuery(name = "UserAccessRequest.findByFilterID", query = "SELECT u FROM UserAccessRequest u WHERE u.filterID = :filterID"),
    @NamedQuery(name = "UserAccessRequest.findByRequestReason", query = "SELECT u FROM UserAccessRequest u WHERE u.requestReason = :requestReason"),
    @NamedQuery(name = "UserAccessRequest.findByRequestDate", query = "SELECT u FROM UserAccessRequest u WHERE u.requestDate = :requestDate"),
    @NamedQuery(name = "UserAccessRequest.findByResponseReason", query = "SELECT u FROM UserAccessRequest u WHERE u.responseReason = :responseReason"),
    @NamedQuery(name = "UserAccessRequest.findByResponseDate", query = "SELECT u FROM UserAccessRequest u WHERE u.responseDate = :responseDate"),
    @NamedQuery(name = "UserAccessRequest.findByAccessExpires", query = "SELECT u FROM UserAccessRequest u WHERE u.accessExpires = :accessExpires")})
public class UserAccessRequest implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "filterID")
    private Integer filterID;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 2147483647)
    @Column(name = "requestReason")
    private String requestReason;
    @Basic(optional = false)
    @NotNull
    @Column(name = "requestDate")
    @Temporal(TemporalType.TIMESTAMP)
    private Date requestDate;
    @Size(max = 2147483647)
    @Column(name = "responseReason")
    private String responseReason;
    @Column(name = "responseDate")
    @Temporal(TemporalType.TIMESTAMP)
    private Date responseDate;
    @Column(name = "accessExpires")
    @Temporal(TemporalType.DATE)
    private Date accessExpires;
    @JoinColumn(name = "userID", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private User user;
    @JoinColumn(name = "filterID", referencedColumnName = "id", insertable = false, updatable = false)
    @OneToOne(optional = false)
    private TaxonObservationFilter taxonObservationFilter;
    @JoinColumn(name = "requestTypeID", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private AccessRequestType accessRequestType;
    @JoinColumn(name = "requestRoleID", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private AccessRequestRole accessRequestRole;
    @JoinColumn(name = "responseTypeID", referencedColumnName = "id")
    @ManyToOne
    private AccessRequestResponseType accessRequestResponseType;

    public UserAccessRequest() {
    }

    public UserAccessRequest(Integer filterID) {
        this.filterID = filterID;
    }

    public UserAccessRequest(Integer filterID, String requestReason, Date requestDate) {
        this.filterID = filterID;
        this.requestReason = requestReason;
        this.requestDate = requestDate;
    }

    public Integer getFilterID() {
        return filterID;
    }

    public void setFilterID(Integer filterID) {
        this.filterID = filterID;
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

    public Date getAccessExpires() {
        return accessExpires;
    }

    public void setAccessExpires(Date accessExpires) {
        this.accessExpires = accessExpires;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public TaxonObservationFilter getTaxonObservationFilter() {
        return taxonObservationFilter;
    }

    public void setTaxonObservationFilter(TaxonObservationFilter taxonObservationFilter) {
        this.taxonObservationFilter = taxonObservationFilter;
    }

    public AccessRequestType getAccessRequestType() {
        return accessRequestType;
    }

    public void setAccessRequestType(AccessRequestType accessRequestType) {
        this.accessRequestType = accessRequestType;
    }

    public AccessRequestRole getAccessRequestRole() {
        return accessRequestRole;
    }

    public void setAccessRequestRole(AccessRequestRole accessRequestRole) {
        this.accessRequestRole = accessRequestRole;
    }

    public AccessRequestResponseType getAccessRequestResponseType() {
        return accessRequestResponseType;
    }

    public void setAccessRequestResponseType(AccessRequestResponseType accessRequestResponseType) {
        this.accessRequestResponseType = accessRequestResponseType;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (filterID != null ? filterID.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof UserAccessRequest)) {
            return false;
        }
        UserAccessRequest other = (UserAccessRequest) object;
        if ((this.filterID == null && other.filterID != null) || (this.filterID != null && !this.filterID.equals(other.filterID))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "uk.org.nbn.nbnv.jpa.nbncore.UserAccessRequest[ filterID=" + filterID + " ]";
    }
    
}
