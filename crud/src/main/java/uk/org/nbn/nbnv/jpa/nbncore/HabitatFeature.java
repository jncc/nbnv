/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.org.nbn.nbnv.jpa.nbncore;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Paul Gilbertson
 */
@Entity
@Table(name = "HabitatFeature")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "HabitatFeature.findAll", query = "SELECT h FROM HabitatFeature h"),
    @NamedQuery(name = "HabitatFeature.findByFeatureID", query = "SELECT h FROM HabitatFeature h WHERE h.featureID = :featureID"),
    @NamedQuery(name = "HabitatFeature.findByProviderKey", query = "SELECT h FROM HabitatFeature h WHERE h.providerKey = :providerKey"),
    @NamedQuery(name = "HabitatFeature.findByUploadDate", query = "SELECT h FROM HabitatFeature h WHERE h.uploadDate = :uploadDate")})
public class HabitatFeature implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "featureID")
    private Integer featureID;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "providerKey")
    private String providerKey;
    @Basic(optional = false)
    @NotNull
    @Lob
    @Column(name = "originalGeom")
    private byte[] originalGeom;
    @Basic(optional = false)
    @NotNull
    @Column(name = "uploadDate")
    @Temporal(TemporalType.TIMESTAMP)
    private Date uploadDate;
    @JoinColumn(name = "habitatDataset", referencedColumnName = "datasetKey")
    @ManyToOne(optional = false, fetch= FetchType.LAZY)
    private HabitatDataset habitatDataset;

    public HabitatFeature() {
    }

    public HabitatFeature(Integer featureID) {
        this.featureID = featureID;
    }

    public HabitatFeature(Integer featureID, String providerKey, byte[] originalGeom, Date uploadDate) {
        this.featureID = featureID;
        this.providerKey = providerKey;
        this.originalGeom = originalGeom;
        this.uploadDate = uploadDate;
    }

    public Integer getFeatureID() {
        return featureID;
    }

    public void setFeatureID(Integer featureID) {
        this.featureID = featureID;
    }

    public String getProviderKey() {
        return providerKey;
    }

    public void setProviderKey(String providerKey) {
        this.providerKey = providerKey;
    }

    public byte[] getOriginalGeom() {
        return originalGeom;
    }

    public void setOriginalGeom(byte[] originalGeom) {
        this.originalGeom = originalGeom;
    }

    public Date getUploadDate() {
        return uploadDate;
    }

    public void setUploadDate(Date uploadDate) {
        this.uploadDate = uploadDate;
    }

    public HabitatDataset getHabitatDataset() {
        return habitatDataset;
    }

    public void setHabitatDataset(HabitatDataset habitatDataset) {
        this.habitatDataset = habitatDataset;
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
        if (!(object instanceof HabitatFeature)) {
            return false;
        }
        HabitatFeature other = (HabitatFeature) object;
        if ((this.featureID == null && other.featureID != null) || (this.featureID != null && !this.featureID.equals(other.featureID))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "uk.org.nbn.nbnv.jpa.nbncore.HabitatFeature[ featureID=" + featureID + " ]";
    }
    
}
