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
@Table(name = "HabitatDataset")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "HabitatDataset.findAll", query = "SELECT h FROM HabitatDataset h"),
    @NamedQuery(name = "HabitatDataset.findByDatasetKey", query = "SELECT h FROM HabitatDataset h WHERE h.datasetKey = :datasetKey")})
public class HabitatDataset implements Serializable {
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "habitatDataset")
    private Collection<HabitatFeature> habitatFeatureCollection;
    @JoinColumn(name = "habitatCategory", referencedColumnName = "id")
    @ManyToOne(optional = false, fetch= FetchType.LAZY)
    private HabitatCategory habitatCategory;
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 8)
    @Column(name = "datasetKey")
    private String datasetKey;
    @JoinColumn(name = "datasetKey", referencedColumnName = "key", insertable = false, updatable = false)
    @OneToOne(optional = false)
    private Dataset dataset;

    public HabitatDataset() {
    }

    public HabitatDataset(String datasetKey) {
        this.datasetKey = datasetKey;
    }

    public String getDatasetKey() {
        return datasetKey;
    }

    public void setDatasetKey(String datasetKey) {
        this.datasetKey = datasetKey;
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
        if (!(object instanceof HabitatDataset)) {
            return false;
        }
        HabitatDataset other = (HabitatDataset) object;
        if ((this.datasetKey == null && other.datasetKey != null) || (this.datasetKey != null && !this.datasetKey.equals(other.datasetKey))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "uk.org.nbn.nbnv.jpa.nbncore.HabitatDataset[ datasetKey=" + datasetKey + " ]";
    }

    @XmlTransient
    public Collection<HabitatFeature> getHabitatFeatureCollection() {
        return habitatFeatureCollection;
    }

    public void setHabitatFeatureCollection(Collection<HabitatFeature> habitatFeatureCollection) {
        this.habitatFeatureCollection = habitatFeatureCollection;
    }

    public HabitatCategory getHabitatCategory() {
        return habitatCategory;
    }

    public void setHabitatCategory(HabitatCategory habitatCategory) {
        this.habitatCategory = habitatCategory;
    }
    
}
