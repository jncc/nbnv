/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.org.nbn.nbnv.jpa.nbncore;

import java.io.Serializable;
import java.util.Collection;
import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Administrator
 */
@Entity
@Table(name = "Resolution")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Resolution.findAll", query = "SELECT r FROM Resolution r"),
    @NamedQuery(name = "Resolution.findByResolutionID", query = "SELECT r FROM Resolution r WHERE r.resolutionID = :resolutionID"),
    @NamedQuery(name = "Resolution.findByLabel", query = "SELECT r FROM Resolution r WHERE r.label = :label"),
    @NamedQuery(name = "Resolution.findByAccuracy", query = "SELECT r FROM Resolution r WHERE r.accuracy = :accuracy"),
    @NamedQuery(name = "Resolution.findByArea", query = "SELECT r FROM Resolution r WHERE r.area = :area")})
public class Resolution implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "resolutionID")
    private Short resolutionID;
    @Basic(optional = false)
    @Column(name = "label")
    private String label;
    @Column(name = "accuracy")
    private Integer accuracy;
    @Column(name = "area")
    private Integer area;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "publicResolution")
    private Collection<TaxonDataset> taxonDatasetCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "maxResolution")
    private Collection<TaxonDataset> taxonDatasetCollection1;

    public Resolution() {
    }

    public Resolution(Short resolutionID) {
        this.resolutionID = resolutionID;
    }

    public Resolution(Short resolutionID, String label) {
        this.resolutionID = resolutionID;
        this.label = label;
    }

    public Short getResolutionID() {
        return resolutionID;
    }

    public void setResolutionID(Short resolutionID) {
        this.resolutionID = resolutionID;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public Integer getAccuracy() {
        return accuracy;
    }

    public void setAccuracy(Integer accuracy) {
        this.accuracy = accuracy;
    }

    public Integer getArea() {
        return area;
    }

    public void setArea(Integer area) {
        this.area = area;
    }

    @XmlTransient
    public Collection<TaxonDataset> getTaxonDatasetCollection() {
        return taxonDatasetCollection;
    }

    public void setTaxonDatasetCollection(Collection<TaxonDataset> taxonDatasetCollection) {
        this.taxonDatasetCollection = taxonDatasetCollection;
    }

    @XmlTransient
    public Collection<TaxonDataset> getTaxonDatasetCollection1() {
        return taxonDatasetCollection1;
    }

    public void setTaxonDatasetCollection1(Collection<TaxonDataset> taxonDatasetCollection1) {
        this.taxonDatasetCollection1 = taxonDatasetCollection1;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (resolutionID != null ? resolutionID.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Resolution)) {
            return false;
        }
        Resolution other = (Resolution) object;
        if ((this.resolutionID == null && other.resolutionID != null) || (this.resolutionID != null && !this.resolutionID.equals(other.resolutionID))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "uk.org.nbn.nbnv.jpa.nbncore.Resolution[ resolutionID=" + resolutionID + " ]";
    }
    
}
