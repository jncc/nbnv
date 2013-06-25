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
@Table(name = "SiteBoundaryDataset")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "SiteBoundaryDataset.findAll", query = "SELECT s FROM SiteBoundaryDataset s"),
    @NamedQuery(name = "SiteBoundaryDataset.findByDatasetKey", query = "SELECT s FROM SiteBoundaryDataset s WHERE s.datasetKey = :datasetKey"),
    @NamedQuery(name = "SiteBoundaryDataset.findByNameField", query = "SELECT s FROM SiteBoundaryDataset s WHERE s.nameField = :nameField")})
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
    @Column(name = "nameField")
    private String nameField;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "siteBoundaryDataset")
    private Collection<SiteBoundary> siteBoundaryCollection;
    @JoinColumn(name = "siteBoundaryType", referencedColumnName = "id")
    @ManyToOne(optional = false, fetch= FetchType.LAZY)
    private SiteBoundaryType siteBoundaryType;
    @JoinColumn(name = "siteBoundaryCategory", referencedColumnName = "id")
    @ManyToOne(optional = false, fetch= FetchType.LAZY)
    private SiteBoundaryCategory siteBoundaryCategory;
    @JoinColumn(name = "datasetKey", referencedColumnName = "key", insertable = false, updatable = false)
    @OneToOne(optional = false)
    private Dataset dataset;

    public SiteBoundaryDataset() {
    }

    public SiteBoundaryDataset(String datasetKey) {
        this.datasetKey = datasetKey;
    }

    public SiteBoundaryDataset(String datasetKey, String nameField) {
        this.datasetKey = datasetKey;
        this.nameField = nameField;
    }

    public String getDatasetKey() {
        return datasetKey;
    }

    public void setDatasetKey(String datasetKey) {
        this.datasetKey = datasetKey;
    }

    public String getNameField() {
        return nameField;
    }

    public void setNameField(String nameField) {
        this.nameField = nameField;
    }

    @XmlTransient
    public Collection<SiteBoundary> getSiteBoundaryCollection() {
        return siteBoundaryCollection;
    }

    public void setSiteBoundaryCollection(Collection<SiteBoundary> siteBoundaryCollection) {
        this.siteBoundaryCollection = siteBoundaryCollection;
    }

    public SiteBoundaryType getSiteBoundaryType() {
        return siteBoundaryType;
    }

    public void setSiteBoundaryType(SiteBoundaryType siteBoundaryType) {
        this.siteBoundaryType = siteBoundaryType;
    }

    public SiteBoundaryCategory getSiteBoundaryCategory() {
        return siteBoundaryCategory;
    }

    public void setSiteBoundaryCategory(SiteBoundaryCategory siteBoundaryCategory) {
        this.siteBoundaryCategory = siteBoundaryCategory;
    }

    public Dataset getDataset() {
        return dataset;
    }

    public void setDataset(Dataset dataset) {
        this.dataset = dataset;
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
