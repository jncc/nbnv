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
@Table(name = "SiteBoundary")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "SiteBoundary.findAll", query = "SELECT s FROM SiteBoundary s"),
    @NamedQuery(name = "SiteBoundary.findByFeatureID", query = "SELECT s FROM SiteBoundary s WHERE s.featureID = :featureID"),
    @NamedQuery(name = "SiteBoundary.findBySiteBoundaryName", query = "SELECT s FROM SiteBoundary s WHERE s.siteBoundaryName = :siteBoundaryName"),
    @NamedQuery(name = "SiteBoundary.findByProviderKey", query = "SELECT s FROM SiteBoundary s WHERE s.providerKey = :providerKey"),
    @NamedQuery(name = "SiteBoundary.findByUploadDate", query = "SELECT s FROM SiteBoundary s WHERE s.uploadDate = :uploadDate"),
    @NamedQuery(name = "SiteBoundary.findByDescription", query = "SELECT s FROM SiteBoundary s WHERE s.description = :description")})
public class SiteBoundary implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "featureID")
    private Integer featureID;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 200)
    @Column(name = "siteBoundaryName")
    private String siteBoundaryName;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "providerKey")
    private String providerKey;
    @Basic(optional = false)
    @NotNull
    @Column(name = "uploadDate")
    @Temporal(TemporalType.TIMESTAMP)
    private Date uploadDate;
    @Size(max = 2147483647)
    @Column(name = "description")
    private String description;
    @JoinColumn(name = "siteBoundaryDataset", referencedColumnName = "datasetKey")
    @ManyToOne(optional = false)
    private SiteBoundaryDataset siteBoundaryDataset;
    @JoinColumn(name = "featureID", referencedColumnName = "featureID", insertable = false, updatable = false)
    @OneToOne(optional = false)
    private Feature feature;

    public SiteBoundary() {
    }

    public SiteBoundary(Integer featureID) {
        this.featureID = featureID;
    }

    public SiteBoundary(Integer featureID, String siteBoundaryName, String providerKey, Date uploadDate) {
        this.featureID = featureID;
        this.siteBoundaryName = siteBoundaryName;
        this.providerKey = providerKey;
        this.uploadDate = uploadDate;
    }

    public Integer getFeatureID() {
        return featureID;
    }

    public void setFeatureID(Integer featureID) {
        this.featureID = featureID;
    }

    public String getSiteBoundaryName() {
        return siteBoundaryName;
    }

    public void setSiteBoundaryName(String siteBoundaryName) {
        this.siteBoundaryName = siteBoundaryName;
    }

    public String getProviderKey() {
        return providerKey;
    }

    public void setProviderKey(String providerKey) {
        this.providerKey = providerKey;
    }

    public Date getUploadDate() {
        return uploadDate;
    }

    public void setUploadDate(Date uploadDate) {
        this.uploadDate = uploadDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public SiteBoundaryDataset getSiteBoundaryDataset() {
        return siteBoundaryDataset;
    }

    public void setSiteBoundaryDataset(SiteBoundaryDataset siteBoundaryDataset) {
        this.siteBoundaryDataset = siteBoundaryDataset;
    }

    public Feature getFeature() {
        return feature;
    }

    public void setFeature(Feature feature) {
        this.feature = feature;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (featureID != null ? featureID.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SiteBoundary)) {
            return false;
        }
        SiteBoundary other = (SiteBoundary) object;
        if ((this.featureID == null && other.featureID != null) || (this.featureID != null && !this.featureID.equals(other.featureID))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "uk.org.nbn.nbnv.jpa.nbncore.SiteBoundary[ featureID=" + featureID + " ]";
    }
    
}
