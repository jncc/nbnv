/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.org.nbn.nbnv.jpa.nbncore;

import java.io.Serializable;
import java.util.Collection;
import javax.persistence.*;
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
    @Basic(optional = false)
    @NotNull
    @Lob
    @Column(name = "originalGeom")
    private byte[] originalGeom;
    @JoinColumn(name = "resolutionID", referencedColumnName = "id")
    @ManyToOne(optional = false, fetch= FetchType.LAZY)
    private Resolution resolution;
    @JoinColumn(name = "originalProjectionID", referencedColumnName = "id")
    @ManyToOne(optional = false, fetch= FetchType.LAZY)
    private Projection projection;
    @OneToMany(mappedBy = "gridSquare")
    private Collection<GridSquare> gridSquareCollection;
    @JoinColumn(name = "parentSquareGridRef", referencedColumnName = "gridRef")
    @ManyToOne
    private GridSquare gridSquare;
    @JoinColumn(name = "featureID", referencedColumnName = "id")
    @ManyToOne(optional = false, fetch= FetchType.LAZY)
    private Feature feature;

    public GridSquare() {
    }

    public GridSquare(String gridRef) {
        this.gridRef = gridRef;
    }

    public GridSquare(String gridRef, byte[] originalGeom) {
        this.gridRef = gridRef;
        this.originalGeom = originalGeom;
    }

    public String getGridRef() {
        return gridRef;
    }

    public void setGridRef(String gridRef) {
        this.gridRef = gridRef;
    }

    public byte[] getOriginalGeom() {
        return originalGeom;
    }

    public void setOriginalGeom(byte[] originalGeom) {
        this.originalGeom = originalGeom;
    }

    public Resolution getResolution() {
        return resolution;
    }

    public void setResolution(Resolution resolution) {
        this.resolution = resolution;
    }

    public Projection getProjection() {
        return projection;
    }

    public void setProjection(Projection projection) {
        this.projection = projection;
    }

    @XmlTransient
    public Collection<GridSquare> getGridSquareCollection() {
        return gridSquareCollection;
    }

    public void setGridSquareCollection(Collection<GridSquare> gridSquareCollection) {
        this.gridSquareCollection = gridSquareCollection;
    }

    // manually renamed to *parent* gridsquare for clarity

    public GridSquare getParentGridSquare() {
        return gridSquare;
    }

    public void setParentGridSquare(GridSquare gridSquare) {
        this.gridSquare = gridSquare;
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
