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
@Table(name = "Projection")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Projection.findAll", query = "SELECT p FROM Projection p"),
    @NamedQuery(name = "Projection.findByProjectionID", query = "SELECT p FROM Projection p WHERE p.projectionID = :projectionID"),
    @NamedQuery(name = "Projection.findBySrcSRID", query = "SELECT p FROM Projection p WHERE p.srcSRID = :srcSRID"),
    @NamedQuery(name = "Projection.findByLabel", query = "SELECT p FROM Projection p WHERE p.label = :label")})
public class Projection implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "projectionID")
    private Integer projectionID;
    @Basic(optional = false)
    @NotNull
    @Column(name = "srcSRID")
    private int srcSRID;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "label")
    private String label;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "projection")
    private Collection<SiteBoundaryDataset> siteBoundaryDatasetCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "projectionID")
    private Collection<GridSquare> gridSquareCollection;

    public Projection() {
    }

    public Projection(Integer projectionID) {
        this.projectionID = projectionID;
    }

    public Projection(Integer projectionID, int srcSRID, String label) {
        this.projectionID = projectionID;
        this.srcSRID = srcSRID;
        this.label = label;
    }

    public Integer getProjectionID() {
        return projectionID;
    }

    public void setProjectionID(Integer projectionID) {
        this.projectionID = projectionID;
    }

    public int getSrcSRID() {
        return srcSRID;
    }

    public void setSrcSRID(int srcSRID) {
        this.srcSRID = srcSRID;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    @XmlTransient
    public Collection<SiteBoundaryDataset> getSiteBoundaryDatasetCollection() {
        return siteBoundaryDatasetCollection;
    }

    public void setSiteBoundaryDatasetCollection(Collection<SiteBoundaryDataset> siteBoundaryDatasetCollection) {
        this.siteBoundaryDatasetCollection = siteBoundaryDatasetCollection;
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
        hash += (projectionID != null ? projectionID.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Projection)) {
            return false;
        }
        Projection other = (Projection) object;
        if ((this.projectionID == null && other.projectionID != null) || (this.projectionID != null && !this.projectionID.equals(other.projectionID))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "uk.org.nbn.nbnv.jpa.nbncore.Projection[ projectionID=" + projectionID + " ]";
    }
    
}
