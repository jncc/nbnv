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
@Table(name = "Feature")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Feature.findAll", query = "SELECT f FROM Feature f"),
    @NamedQuery(name = "Feature.findByFeatureID", query = "SELECT f FROM Feature f WHERE f.featureID = :featureID"),
    @NamedQuery(name = "Feature.findByWkt", query = "SELECT f FROM Feature f WHERE f.wkt = :wkt")})
public class Feature implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "featureID")
    private Integer featureID;
    @Basic(optional = false)
    @Column(name = "wkt")
    private String wkt;
    @Basic(optional = false)
    @Lob
    @Column(name = "geom")
    private byte[] geom;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "featureID")
    private Collection<TaxonObservationPublic> taxonObservationPublicCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "featureID")
    private Collection<TaxonObservation> taxonObservationCollection;

    public Feature() {
    }

    public Feature(Integer featureID) {
        this.featureID = featureID;
    }

    public Feature(Integer featureID, String wkt, byte[] geom) {
        this.featureID = featureID;
        this.wkt = wkt;
        this.geom = geom;
    }

    public Integer getFeatureID() {
        return featureID;
    }

    public void setFeatureID(Integer featureID) {
        this.featureID = featureID;
    }

    public String getWkt() {
        return wkt;
    }

    public void setWkt(String wkt) {
        this.wkt = wkt;
    }

    public byte[] getGeom() {
        return geom;
    }

    public void setGeom(byte[] geom) {
        this.geom = geom;
    }

    @XmlTransient
    public Collection<TaxonObservationPublic> getTaxonObservationPublicCollection() {
        return taxonObservationPublicCollection;
    }

    public void setTaxonObservationPublicCollection(Collection<TaxonObservationPublic> taxonObservationPublicCollection) {
        this.taxonObservationPublicCollection = taxonObservationPublicCollection;
    }

    @XmlTransient
    public Collection<TaxonObservation> getTaxonObservationCollection() {
        return taxonObservationCollection;
    }

    public void setTaxonObservationCollection(Collection<TaxonObservation> taxonObservationCollection) {
        this.taxonObservationCollection = taxonObservationCollection;
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
        if (!(object instanceof Feature)) {
            return false;
        }
        Feature other = (Feature) object;
        if ((this.featureID == null && other.featureID != null) || (this.featureID != null && !this.featureID.equals(other.featureID))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "uk.org.nbn.nbnv.jpa.nbncore.Feature[ featureID=" + featureID + " ]";
    }
    
}
