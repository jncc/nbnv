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
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Paul Gilbertson
 */
@Entity
@Table(name = "Resolution")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Resolution.findAll", query = "SELECT r FROM Resolution r"),
    @NamedQuery(name = "Resolution.findById", query = "SELECT r FROM Resolution r WHERE r.id = :id"),
    @NamedQuery(name = "Resolution.findByLabel", query = "SELECT r FROM Resolution r WHERE r.label = :label"),
    @NamedQuery(name = "Resolution.findByAccuracy", query = "SELECT r FROM Resolution r WHERE r.accuracy = :accuracy"),
    @NamedQuery(name = "Resolution.findByArea", query = "SELECT r FROM Resolution r WHERE r.area = :area")})
public class Resolution implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 20)
    @Column(name = "label")
    private String label;
    @Column(name = "accuracy")
    private Integer accuracy;
    @Column(name = "area")
    private Integer area;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "resolution")
    private Collection<TaxonDataset> taxonDatasetCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "resolution")
    private Collection<GridSquare> gridSquareCollection;

    public Resolution() {
    }

    public Resolution(Integer id) {
        this.id = id;
    }

    public Resolution(Integer id, String label) {
        this.id = id;
        this.label = label;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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
        if (!(object instanceof Resolution)) {
            return false;
        }
        Resolution other = (Resolution) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "uk.org.nbn.nbnv.jpa.nbncore.Resolution[ id=" + id + " ]";
    }
    
}
