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
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
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
@Table(name = "SiteBoundaryDataset")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "SiteBoundaryDataset.findAll", query = "SELECT s FROM SiteBoundaryDataset s"),
    @NamedQuery(name = "SiteBoundaryDataset.findByDatasetKey", query = "SELECT s FROM SiteBoundaryDataset s WHERE s.datasetKey = :datasetKey"),
    @NamedQuery(name = "SiteBoundaryDataset.findByGeoLayerName", query = "SELECT s FROM SiteBoundaryDataset s WHERE s.geoLayerName = :geoLayerName"),
    @NamedQuery(name = "SiteBoundaryDataset.findByNameField", query = "SELECT s FROM SiteBoundaryDataset s WHERE s.nameField = :nameField"),
    @NamedQuery(name = "SiteBoundaryDataset.findByGisLayerID", query = "SELECT s FROM SiteBoundaryDataset s WHERE s.gisLayerID = :gisLayerID")})
public class SiteBoundaryDataset implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 8)
    @Column(name = "datasetKey")
    private String datasetKey;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "geoLayerName")
    private String geoLayerName;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "nameField")
    private String nameField;
    @Column(name = "gisLayerID")
    private Integer gisLayerID;
    @JoinColumn(name = "siteBoundaryCategory", referencedColumnName = "siteBoundaryTypeID")
    @ManyToOne(optional = false)
    private SiteBoundaryType siteBoundaryCategory;
    @JoinColumn(name = "projection", referencedColumnName = "projectionID")
    @ManyToOne(optional = false)
    private Projection projection;
    @JoinColumn(name = "datasetKey", referencedColumnName = "datasetKey", insertable = false, updatable = false)
    @OneToOne(optional = false)
    private Dataset dataset;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "siteBoundaryDataset")
    private Collection<SiteBoundary> siteBoundaryCollection;

    public SiteBoundaryDataset() {
    }

    public SiteBoundaryDataset(String datasetKey) {
        this.datasetKey = datasetKey;
    }

    public SiteBoundaryDataset(String datasetKey, String geoLayerName, String nameField) {
        this.datasetKey = datasetKey;
        this.geoLayerName = geoLayerName;
        this.nameField = nameField;
    }

    public String getDatasetKey() {
        return datasetKey;
    }

    public void setDatasetKey(String datasetKey) {
        this.datasetKey = datasetKey;
    }

    public String getGeoLayerName() {
        return geoLayerName;
    }

    public void setGeoLayerName(String geoLayerName) {
        this.geoLayerName = geoLayerName;
    }

    public String getNameField() {
        return nameField;
    }

    public void setNameField(String nameField) {
        this.nameField = nameField;
    }

    public Integer getGisLayerID() {
        return gisLayerID;
    }

    public void setGisLayerID(Integer gisLayerID) {
        this.gisLayerID = gisLayerID;
    }

    public SiteBoundaryType getSiteBoundaryCategory() {
        return siteBoundaryCategory;
    }

    public void setSiteBoundaryCategory(SiteBoundaryType siteBoundaryCategory) {
        this.siteBoundaryCategory = siteBoundaryCategory;
    }

    public Projection getProjection() {
        return projection;
    }

    public void setProjection(Projection projection) {
        this.projection = projection;
    }

    public Dataset getDataset() {
        return dataset;
    }

    public void setDataset(Dataset dataset) {
        this.dataset = dataset;
    }

    @XmlTransient
    public Collection<SiteBoundary> getSiteBoundaryCollection() {
        return siteBoundaryCollection;
    }

    public void setSiteBoundaryCollection(Collection<SiteBoundary> siteBoundaryCollection) {
        this.siteBoundaryCollection = siteBoundaryCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (datasetKey != null ? datasetKey.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SiteBoundaryDataset)) {
            return false;
        }
        SiteBoundaryDataset other = (SiteBoundaryDataset) object;
        if ((this.datasetKey == null && other.datasetKey != null) || (this.datasetKey != null && !this.datasetKey.equals(other.datasetKey))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "uk.org.nbn.nbnv.jpa.nbncore.SiteBoundaryDataset[ datasetKey=" + datasetKey + " ]";
    }
    
}
