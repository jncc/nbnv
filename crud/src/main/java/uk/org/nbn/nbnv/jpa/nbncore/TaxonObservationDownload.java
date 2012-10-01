/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.org.nbn.nbnv.jpa.nbncore;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Paul Gilbertson
 */
@Entity
@Table(name = "TaxonObservationDownload")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "TaxonObservationDownload.findAll", query = "SELECT t FROM TaxonObservationDownload t"),
    @NamedQuery(name = "TaxonObservationDownload.findByFilterID", query = "SELECT t FROM TaxonObservationDownload t WHERE t.filterID = :filterID"),
    @NamedQuery(name = "TaxonObservationDownload.findByReason", query = "SELECT t FROM TaxonObservationDownload t WHERE t.reason = :reason"),
    @NamedQuery(name = "TaxonObservationDownload.findByDownloadTime", query = "SELECT t FROM TaxonObservationDownload t WHERE t.downloadTime = :downloadTime"),
    @NamedQuery(name = "TaxonObservationDownload.findByUserForOrganisation", query = "SELECT t FROM TaxonObservationDownload t WHERE t.userForOrganisation = :userForOrganisation")})
public class TaxonObservationDownload implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "filterID")
    private Integer filterID;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 2147483647)
    @Column(name = "reason")
    private String reason;
    @Basic(optional = false)
    @NotNull
    @Column(name = "downloadTime")
    @Temporal(TemporalType.TIMESTAMP)
    private Date downloadTime;
    @Size(max = 2147483647)
    @Column(name = "userForOrganisation")
    private String userForOrganisation;
    @JoinColumn(name = "userID", referencedColumnName = "id")
    @ManyToOne
    private User userID;
    @JoinColumn(name = "filterID", referencedColumnName = "id", insertable = false, updatable = false)
    @OneToOne(optional = false)
    private TaxonObservationFilter taxonObservationFilter;
    @JoinColumn(name = "purposeID", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private TaxonObservationDownloadPurpose purposeID;
    @JoinColumn(name = "organisationID", referencedColumnName = "id")
    @ManyToOne
    private Organisation organisationID;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "taxonObservationDownload")
    private Collection<TaxonObservationDownloadStatistics> taxonObservationDownloadStatisticsCollection;

    public TaxonObservationDownload() {
    }

    public TaxonObservationDownload(Integer filterID) {
        this.filterID = filterID;
    }

    public TaxonObservationDownload(Integer filterID, String reason, Date downloadTime) {
        this.filterID = filterID;
        this.reason = reason;
        this.downloadTime = downloadTime;
    }

    public Integer getFilterID() {
        return filterID;
    }

    public void setFilterID(Integer filterID) {
        this.filterID = filterID;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public Date getDownloadTime() {
        return downloadTime;
    }

    public void setDownloadTime(Date downloadTime) {
        this.downloadTime = downloadTime;
    }

    public String getUserForOrganisation() {
        return userForOrganisation;
    }

    public void setUserForOrganisation(String userForOrganisation) {
        this.userForOrganisation = userForOrganisation;
    }

    public User getUserID() {
        return userID;
    }

    public void setUserID(User userID) {
        this.userID = userID;
    }

    public TaxonObservationFilter getTaxonObservationFilter() {
        return taxonObservationFilter;
    }

    public void setTaxonObservationFilter(TaxonObservationFilter taxonObservationFilter) {
        this.taxonObservationFilter = taxonObservationFilter;
    }

    public TaxonObservationDownloadPurpose getPurposeID() {
        return purposeID;
    }

    public void setPurposeID(TaxonObservationDownloadPurpose purposeID) {
        this.purposeID = purposeID;
    }

    public Organisation getOrganisationID() {
        return organisationID;
    }

    public void setOrganisationID(Organisation organisationID) {
        this.organisationID = organisationID;
    }

    @XmlTransient
    public Collection<TaxonObservationDownloadStatistics> getTaxonObservationDownloadStatisticsCollection() {
        return taxonObservationDownloadStatisticsCollection;
    }

    public void setTaxonObservationDownloadStatisticsCollection(Collection<TaxonObservationDownloadStatistics> taxonObservationDownloadStatisticsCollection) {
        this.taxonObservationDownloadStatisticsCollection = taxonObservationDownloadStatisticsCollection;
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
        if (!(object instanceof TaxonObservationDownload)) {
            return false;
        }
        TaxonObservationDownload other = (TaxonObservationDownload) object;
        if ((this.filterID == null && other.filterID != null) || (this.filterID != null && !this.filterID.equals(other.filterID))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "uk.org.nbn.nbnv.jpa.nbncore.TaxonObservationDownload[ filterID=" + filterID + " ]";
    }
    
}
