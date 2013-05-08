/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.org.nbn.nbnv.jpa.nbnimportstaging;

import java.io.Serializable;
import java.util.Collection;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
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
 * @author felix mason
 */
@Entity
@Table(name = "GridSquare")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "GridSquare.findAll", query = "SELECT g FROM GridSquare g"),
    @NamedQuery(name = "GridSquare.findByGridRef", query = "SELECT g FROM GridSquare g WHERE g.gridRef = :gridRef"),
    @NamedQuery(name = "GridSquare.findByResolutionID", query = "SELECT g FROM GridSquare g WHERE g.resolutionID = :resolutionID"),
    @NamedQuery(name = "GridSquare.findByOriginalProjectionID", query = "SELECT g FROM GridSquare g WHERE g.originalProjectionID = :originalProjectionID")})
public class GridSquare implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 12)
    @Column(name = "gridRef", nullable = false, length = 12)
    private String gridRef;
    @Basic(optional = false)
    @NotNull
    @Column(name = "resolutionID", nullable = false)
    private int resolutionID;
    @Basic(optional = false)
    @NotNull
    @Column(name = "originalProjectionID", nullable = false)
    private int originalProjectionID;
    @Basic(optional = false)
    @NotNull
    @Lob
    @Column(name = "originalGeom", nullable = false)
    private byte[] originalGeom;
    @OneToMany(mappedBy = "parentSquareGridRef")
    private Collection<GridSquare> gridSquareCollection;
    @JoinColumn(name = "parentSquareGridRef", referencedColumnName = "gridRef")
    @ManyToOne
    private GridSquare parentSquareGridRef;
    @JoinColumn(name = "featureID", referencedColumnName = "id", nullable = false)
    @ManyToOne(optional = false)
    private Feature featureID;

    public GridSquare() {
    }

    public GridSquare(String gridRef) {
        this.gridRef = gridRef;
    }

    public GridSquare(String gridRef, int resolutionID, int originalProjectionID, byte[] originalGeom) {
        this.gridRef = gridRef;
        this.resolutionID = resolutionID;
        this.originalProjectionID = originalProjectionID;
        this.originalGeom = originalGeom;
    }

    public String getGridRef() {
        return gridRef;
    }

    public void setGridRef(String gridRef) {
        this.gridRef = gridRef;
    }

    public int getResolutionID() {
        return resolutionID;
    }

    public void setResolutionID(int resolutionID) {
        this.resolutionID = resolutionID;
    }

    public int getOriginalProjectionID() {
        return originalProjectionID;
    }

    public void setOriginalProjectionID(int originalProjectionID) {
        this.originalProjectionID = originalProjectionID;
    }

    public byte[] getOriginalGeom() {
        return originalGeom;
    }

    public void setOriginalGeom(byte[] originalGeom) {
        this.originalGeom = originalGeom;
    }

    @XmlTransient
    public Collection<GridSquare> getGridSquareCollection() {
        return gridSquareCollection;
    }

    public void setGridSquareCollection(Collection<GridSquare> gridSquareCollection) {
        this.gridSquareCollection = gridSquareCollection;
    }

    public GridSquare getParentSquareGridRef() {
        return parentSquareGridRef;
    }

    public void setParentSquareGridRef(GridSquare parentSquareGridRef) {
        this.parentSquareGridRef = parentSquareGridRef;
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
        return "uk.org.nbn.nbnv.jpa.nbnimportstaging.GridSquare[ gridRef=" + gridRef + " ]";
    }
    
}
