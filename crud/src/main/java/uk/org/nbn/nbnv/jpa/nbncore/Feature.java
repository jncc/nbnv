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
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Paul Gilbertson
 */
@Entity
@Table(name = "Feature")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Feature.findAll", query = "SELECT f FROM Feature f"),
    @NamedQuery(name = "Feature.findById", query = "SELECT f FROM Feature f WHERE f.id = :id")})
public class Feature implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @NotNull
    @Lob
    @Column(name = "geom")
    private byte[] geom;
    @JoinTable(name = "FeatureOverlaps", joinColumns = {
        @JoinColumn(name = "featureID", referencedColumnName = "id")}, inverseJoinColumns = {
        @JoinColumn(name = "overlappedFeatureID", referencedColumnName = "id")})
    @ManyToMany
    private Collection<Feature> featureCollection;
    @ManyToMany(mappedBy = "featureCollection")
    private Collection<Feature> featureCollection1;
    @JoinTable(name = "GridTree", joinColumns = {
        @JoinColumn(name = "featureID", referencedColumnName = "id")}, inverseJoinColumns = {
        @JoinColumn(name = "parentFeatureID", referencedColumnName = "id")})
    @ManyToMany
    private Collection<Feature> featureCollection2;
    @ManyToMany(mappedBy = "featureCollection2")
    private Collection<Feature> featureCollection3;
    @JoinTable(name = "FeatureContains", joinColumns = {
        @JoinColumn(name = "featureID", referencedColumnName = "id")}, inverseJoinColumns = {
        @JoinColumn(name = "containedFeatureID", referencedColumnName = "id")})
    @ManyToMany
    private Collection<Feature> featureCollection4;
    @ManyToMany(mappedBy = "featureCollection4")
    private Collection<Feature> featureCollection5;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "feature")
    private Collection<TaxonObservationPublic> taxonObservationPublicCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "feature")
    private Collection<TaxonObservation> taxonObservationCollection;
    @OneToOne(cascade = CascadeType.ALL, mappedBy = "feature")
    private SiteBoundary siteBoundary;
    @OneToMany(mappedBy = "feature")
    private Collection<Designation> designationCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "feature")
    private Collection<GridSquare> gridSquareCollection;

    public Feature() {
    }

    public Feature(Integer id) {
        this.id = id;
    }

    public Feature(Integer id, byte[] geom) {
        this.id = id;
        this.geom = geom;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public byte[] getGeom() {
        return geom;
    }

    public void setGeom(byte[] geom) {
        this.geom = geom;
    }

    @XmlTransient
    public Collection<Feature> getFeatureCollection() {
        return featureCollection;
    }

    public void setFeatureCollection(Collection<Feature> featureCollection) {
        this.featureCollection = featureCollection;
    }

    @XmlTransient
    public Collection<Feature> getFeatureCollection1() {
        return featureCollection1;
    }

    public void setFeatureCollection1(Collection<Feature> featureCollection1) {
        this.featureCollection1 = featureCollection1;
    }

    @XmlTransient
    public Collection<Feature> getFeatureCollection2() {
        return featureCollection2;
    }

    public void setFeatureCollection2(Collection<Feature> featureCollection2) {
        this.featureCollection2 = featureCollection2;
    }

    @XmlTransient
    public Collection<Feature> getFeatureCollection3() {
        return featureCollection3;
    }

    public void setFeatureCollection3(Collection<Feature> featureCollection3) {
        this.featureCollection3 = featureCollection3;
    }

    @XmlTransient
    public Collection<Feature> getFeatureCollection4() {
        return featureCollection4;
    }

    public void setFeatureCollection4(Collection<Feature> featureCollection4) {
        this.featureCollection4 = featureCollection4;
    }

    @XmlTransient
    public Collection<Feature> getFeatureCollection5() {
        return featureCollection5;
    }

    public void setFeatureCollection5(Collection<Feature> featureCollection5) {
        this.featureCollection5 = featureCollection5;
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

    public SiteBoundary getSiteBoundary() {
        return siteBoundary;
    }

    public void setSiteBoundary(SiteBoundary siteBoundary) {
        this.siteBoundary = siteBoundary;
    }

    @XmlTransient
    public Collection<Designation> getDesignationCollection() {
        return designationCollection;
    }

    public void setDesignationCollection(Collection<Designation> designationCollection) {
        this.designationCollection = designationCollection;
    }

    @XmlTransient
    public Collection<GridSquare> getGridSquareCollection() {
        return gridSquareCollection;
    }

    public void setGridSquareCollection(Collection<GridSquare> gridSquareCollection) {
        this.gridSquareCollection = gridSquareCollection;
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
        if (!(object instanceof Feature)) {
            return false;
        }
        Feature other = (Feature) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "uk.org.nbn.nbnv.jpa.nbncore.Feature[ id=" + id + " ]";
    }
    
}
