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
 * @author Administrator
 */
@Entity
@Table(name = "StorageLevel")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "StorageLevel.findAll", query = "SELECT s FROM StorageLevel s"),
    @NamedQuery(name = "StorageLevel.findByStorageLevelID", query = "SELECT s FROM StorageLevel s WHERE s.storageLevelID = :storageLevelID"),
    @NamedQuery(name = "StorageLevel.findByLabel", query = "SELECT s FROM StorageLevel s WHERE s.label = :label")})
public class StorageLevel implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "storageLevelID")
    private Integer storageLevelID;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 12)
    @Column(name = "label")
    private String label;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "storageLevelID")
    private Collection<Attribute> attributeCollection;

    public StorageLevel() {
    }

    public StorageLevel(Integer storageLevelID) {
        this.storageLevelID = storageLevelID;
    }

    public StorageLevel(Integer storageLevelID, String label) {
        this.storageLevelID = storageLevelID;
        this.label = label;
    }

    public Integer getStorageLevelID() {
        return storageLevelID;
    }

    public void setStorageLevelID(Integer storageLevelID) {
        this.storageLevelID = storageLevelID;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    @XmlTransient
    public Collection<Attribute> getAttributeCollection() {
        return attributeCollection;
    }

    public void setAttributeCollection(Collection<Attribute> attributeCollection) {
        this.attributeCollection = attributeCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (storageLevelID != null ? storageLevelID.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof StorageLevel)) {
            return false;
        }
        StorageLevel other = (StorageLevel) object;
        if ((this.storageLevelID == null && other.storageLevelID != null) || (this.storageLevelID != null && !this.storageLevelID.equals(other.storageLevelID))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "uk.org.nbn.nbnv.jpa.nbncore.StorageLevel[ storageLevelID=" + storageLevelID + " ]";
    }
    
}
