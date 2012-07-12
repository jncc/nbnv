/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.org.nbn.nbnv.jpa.nbncore;

import java.io.Serializable;
import java.util.Collection;
import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Administrator
 */
@Entity
@Table(name = "DatasetType")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "DatasetType.findAll", query = "SELECT d FROM DatasetType d"),
    @NamedQuery(name = "DatasetType.findByDatasetTypeKey", query = "SELECT d FROM DatasetType d WHERE d.datasetTypeKey = :datasetTypeKey"),
    @NamedQuery(name = "DatasetType.findByLabel", query = "SELECT d FROM DatasetType d WHERE d.label = :label")})
public class DatasetType implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "datasetTypeKey")
    private Character datasetTypeKey;
    @Basic(optional = false)
    @Column(name = "label")
    private String label;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "datasetTypeKey")
    private Collection<Dataset> datasetCollection;

    public DatasetType() {
    }

    public DatasetType(Character datasetTypeKey) {
        this.datasetTypeKey = datasetTypeKey;
    }

    public DatasetType(Character datasetTypeKey, String label) {
        this.datasetTypeKey = datasetTypeKey;
        this.label = label;
    }

    public Character getDatasetTypeKey() {
        return datasetTypeKey;
    }

    public void setDatasetTypeKey(Character datasetTypeKey) {
        this.datasetTypeKey = datasetTypeKey;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    @XmlTransient
    public Collection<Dataset> getDatasetCollection() {
        return datasetCollection;
    }

    public void setDatasetCollection(Collection<Dataset> datasetCollection) {
        this.datasetCollection = datasetCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (datasetTypeKey != null ? datasetTypeKey.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof DatasetType)) {
            return false;
        }
        DatasetType other = (DatasetType) object;
        if ((this.datasetTypeKey == null && other.datasetTypeKey != null) || (this.datasetTypeKey != null && !this.datasetTypeKey.equals(other.datasetTypeKey))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "uk.org.nbn.nbnv.jpa.nbncore.DatasetType[ datasetTypeKey=" + datasetTypeKey + " ]";
    }
    
}
