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
@Table(name = "OrganisationAccessRequest")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "OrganisationAccessRequest.findAll", query = "SELECT o FROM OrganisationAccessRequest o"),
    @NamedQuery(name = "OrganisationAccessRequest.findByFilterID", query = "SELECT o FROM OrganisationAccessRequest o WHERE o.filterID = :filterID"),
    @NamedQuery(name = "OrganisationAccessRequest.findByRequestReason", query = "SELECT o FROM OrganisationAccessRequest o WHERE o.requestReason = :requestReason"),
    @NamedQuery(name = "OrganisationAccessRequest.findByRequestDate", query = "SELECT o FROM OrganisationAccessRequest o WHERE o.requestDate = :requestDate"),
    @NamedQuery(name = "OrganisationAccessRequest.findByResponseReason", query = "SELECT o FROM OrganisationAccessRequest o WHERE o.responseReason = :responseReason"),
    @NamedQuery(name = "OrganisationAccessRequest.findByResponseDate", query = "SELECT o FROM OrganisationAccessRequest o WHERE o.responseDate = :responseDate"),
    @NamedQuery(name = "OrganisationAccessRequest.findByAccessExpires", query = "SELECT o FROM OrganisationAccessRequest o WHERE o.accessExpires = :accessExpires")})
public class OrganisationAccessRequest implements Serializable {
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
    @JoinColumn(name = "filterID", referencedColumnName = "id", insertable = false, updatable = false)
    @OneToOne(optional = false)
    private TaxonObservationFilter taxonObservationFilter;
    @JoinColumn(name = "organisationID", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Organisation organisationID;
    @JoinColumn(name = "requestTypeID", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private AccessRequestType requestTypeID;
    @JoinColumn(name = "requestRoleID", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private AccessRequestRole requestRoleID;
    @JoinColumn(name = "responseTypeID", referencedColumnName = "id")
    @ManyToOne
    private AccessRequestResponseType responseTypeID;

    public OrganisationAccessRequest() {
    }

    public OrganisationAccessRequest(Integer filterID) {
        this.filterID = filterID;
    }

    public OrganisationAccessRequest(Integer filterID, String requestReason, Date requestDate) {
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

    public TaxonObservationFilter getTaxonObservationFilter() {
        return taxonObservationFilter;
    }

    public void setTaxonObservationFilter(TaxonObservationFilter taxonObservationFilter) {
        this.taxonObservationFilter = taxonObservationFilter;
    }

    public Organisation getOrganisationID() {
        return organisationID;
    }

    public void setOrganisationID(Organisation organisationID) {
        this.organisationID = organisationID;
    }

    public AccessRequestType getRequestTypeID() {
        return requestTypeID;
    }

    public void setRequestTypeID(AccessRequestType requestTypeID) {
        this.requestTypeID = requestTypeID;
    }

    public AccessRequestRole getRequestRoleID() {
        return requestRoleID;
    }

    public void setRequestRoleID(AccessRequestRole requestRoleID) {
        this.requestRoleID = requestRoleID;
    }

    public AccessRequestResponseType getResponseTypeID() {
        return responseTypeID;
    }

    public void setResponseTypeID(AccessRequestResponseType responseTypeID) {
        this.responseTypeID = responseTypeID;
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
        if (!(object instanceof OrganisationAccessRequest)) {
            return false;
        }
        OrganisationAccessRequest other = (OrganisationAccessRequest) object;
        if ((this.filterID == null && other.filterID != null) || (this.filterID != null && !this.filterID.equals(other.filterID))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "uk.org.nbn.nbnv.jpa.nbncore.OrganisationAccessRequest[ filterID=" + filterID + " ]";
    }
    
}
