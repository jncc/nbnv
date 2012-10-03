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
import javax.persistence.Lob;
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
@Table(name = "SiteBoundary")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "SiteBoundary.findAll", query = "SELECT s FROM SiteBoundary s"),
    @NamedQuery(name = "SiteBoundary.findByFeatureID", query = "SELECT s FROM SiteBoundary s WHERE s.featureID = :featureID"),
    @NamedQuery(name = "SiteBoundary.findByName", query = "SELECT s FROM SiteBoundary s WHERE s.name = :name"),
    @NamedQuery(name = "SiteBoundary.findByDescription", query = "SELECT s FROM SiteBoundary s WHERE s.description = :description"),
    @NamedQuery(name = "SiteBoundary.findByProviderKey", query = "SELECT s FROM SiteBoundary s WHERE s.providerKey = :providerKey"),
    @NamedQuery(name = "SiteBoundary.findByUploadDate", query = "SELECT s FROM SiteBoundary s WHERE s.uploadDate = :uploadDate")})
public class SiteBoundary implements Serializable {
    @Basic(optional = false)
    @NotNull
    @Lob
    @Column(name = "originalGeom")
    private byte[] originalGeom;
    @JoinColumn(name = "originalProjectionID", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Projection originalProjectionID;
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "featureID")
    private Integer featureID;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 200)
    @Column(name = "name")
    private String name;
    @Size(max = 2147483647)
    @Column(name = "description")
    private String description;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "providerKey")
    private String providerKey;
    @Basic(optional = false)
    @NotNull
    @Lob
    @Column(name = "geom")
    private byte[] geom;
    @Basic(optional = false)
    @NotNull
    @Column(name = "uploadDate")
    @Temporal(TemporalType.TIMESTAMP)
    private Date uploadDate;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "siteBoundary")
    private Collection<SiteBoundaryAttribute> siteBoundaryAttributeCollection;
    @JoinColumn(name = "siteBoundaryDataset", referencedColumnName = "datasetKey")
    @ManyToOne(optional = false)
    private SiteBoundaryDataset siteBoundaryDataset;
    @JoinColumn(name = "projectionID", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Projection projectionID;
    @JoinColumn(name = "featureID", referencedColumnName = "id", insertable = false, updatable = false)
    @OneToOne(optional = false)
    private Feature feature;
    @OneToMany(mappedBy = "filterSiteBoundary")
    private Collection<TaxonObservationFilterElement> taxonObservationFilterElementCollection;

    public SiteBoundary() {
    }

    public SiteBoundary(Integer featureID) {
        this.featureID = featureID;
    }

    public SiteBoundary(Integer featureID, String name, String providerKey, byte[] geom, Date uploadDate) {
        this.featureID = featureID;
        this.name = name;
        this.providerKey = providerKey;
        this.geom = geom;
        this.uploadDate = uploadDate;
    }

    public Integer getFeatureID() {
        return featureID;
    }

    public void setFeatureID(Integer featureID) {
        this.featureID = featureID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getProviderKey() {
        return providerKey;
    }

    public void setProviderKey(String providerKey) {
        this.providerKey = providerKey;
    }

    public byte[] getGeom() {
        return geom;
    }

    public void setGeom(byte[] geom) {
        this.geom = geom;
    }

    public Date getUploadDate() {
        return uploadDate;
    }

    public void setUploadDate(Date uploadDate) {
        this.uploadDate = uploadDate;
    }

    @XmlTransient
    public Collection<SiteBoundaryAttribute> getSiteBoundaryAttributeCollection() {
        return siteBoundaryAttributeCollection;
    }

    public void setSiteBoundaryAttributeCollection(Collection<SiteBoundaryAttribute> siteBoundaryAttributeCollection) {
        this.siteBoundaryAttributeCollection = siteBoundaryAttributeCollection;
    }

    public SiteBoundaryDataset getSiteBoundaryDataset() {
        return siteBoundaryDataset;
    }

    public void setSiteBoundaryDataset(SiteBoundaryDataset siteBoundaryDataset) {
        this.siteBoundaryDataset = siteBoundaryDataset;
    }

    public Projection getProjectionID() {
        return projectionID;
    }

    public void setProjectionID(Projection projectionID) {
        this.projectionID = projectionID;
    }

    public Feature getFeature() {
        return feature;
    }

    public void setFeature(Feature feature) {
        this.feature = feature;
    }

    @XmlTransient
    public Collection<TaxonObservationFilterElement> getTaxonObservationFilterElementCollection() {
        return taxonObservationFilterElementCollection;
    }

    public void setTaxonObservationFilterElementCollection(Collection<TaxonObservationFilterElement> taxonObservationFilterElementCollection) {
        this.taxonObservationFilterElementCollection = taxonObservationFilterElementCollection;
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

    public byte[] getOriginalGeom() {
        return originalGeom;
    }

    public void setOriginalGeom(byte[] originalGeom) {
        this.originalGeom = originalGeom;
    }

    public Projection getOriginalProjectionID() {
        return originalProjectionID;
    }

    public void setOriginalProjectionID(Projection originalProjectionID) {
        this.originalProjectionID = originalProjectionID;
    }
    
}
