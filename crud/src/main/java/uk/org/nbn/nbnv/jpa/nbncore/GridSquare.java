/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.org.nbn.nbnv.jpa.nbncore;

import java.io.Serializable;
import java.util.Collection;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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
@Table(name = "GridSquare")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "GridSquare.findAll", query = "SELECT g FROM GridSquare g"),
    @NamedQuery(name = "GridSquare.findByGridRef", query = "SELECT g FROM GridSquare g WHERE g.gridRef = :gridRef")})
public class GridSquare implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 12)
    @Column(name = "gridRef")
    private String gridRef;
    @JoinColumn(name = "resolutionID", referencedColumnName = "resolutionID")
    @ManyToOne(optional = false)
    private Resolution resolutionID;
    @JoinColumn(name = "projectionID", referencedColumnName = "projectionID")
    @ManyToOne(optional = false)
    private Projection projectionID;
    @OneToMany(mappedBy = "parentSquare")
    private Collection<GridSquare> gridSquareCollection;
    @JoinColumn(name = "parentSquare", referencedColumnName = "gridRef")
    @ManyToOne
    private GridSquare parentSquare;
    @JoinColumn(name = "featureID", referencedColumnName = "featureID")
    @ManyToOne(optional = false)
    private Feature featureID;

    public GridSquare() {
    }

    public GridSquare(String gridRef) {
        this.gridRef = gridRef;
    }

    public String getGridRef() {
        return gridRef;
    }

    public void setGridRef(String gridRef) {
        this.gridRef = gridRef;
    }

    public Resolution getResolutionID() {
        return resolutionID;
    }

    public void setResolutionID(Resolution resolutionID) {
        this.resolutionID = resolutionID;
    }

    public Projection getProjectionID() {
        return projectionID;
    }

    public void setProjectionID(Projection projectionID) {
        this.projectionID = projectionID;
    }

    @XmlTransient
    public Collection<GridSquare> getGridSquareCollection() {
        return gridSquareCollection;
    }

    public void setGridSquareCollection(Collection<GridSquare> gridSquareCollection) {
        this.gridSquareCollection = gridSquareCollection;
    }

    public GridSquare getParentSquare() {
        return parentSquare;
    }

    public void setParentSquare(GridSquare parentSquare) {
        this.parentSquare = parentSquare;
    }

    public Feature getFeatureID() {
        return featureID;
    }

    public void setFeatureID(Feature featureID) {
        this.featureID = featureID;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (gridRef != null ? gridRef.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof GridSquare)) {
            return false;
        }
        GridSquare other = (GridSquare) object;
        if ((this.gridRef == null && other.gridRef != null) || (this.gridRef != null && !this.gridRef.equals(other.gridRef))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "uk.org.nbn.nbnv.jpa.nbncore.GridSquare[ gridRef=" + gridRef + " ]";
    }
    
}
