/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.org.nbn.nbnv.jpa.nbncore;

import java.io.Serializable;
import java.util.Collection;
import javax.persistence.*;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;
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
    @Basic(optional = false)
    @NotNull
    @Lob
    @Column(name = "geom")
    private byte[] geom;
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;
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

    public byte[] getGeom() {
        return geom;
    }

    public void setGeom(byte[] geom) {
        this.geom = geom;
    }
    
}
