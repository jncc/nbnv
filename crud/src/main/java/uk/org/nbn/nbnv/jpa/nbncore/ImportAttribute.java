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
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
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
 * @author felix mason
 */
@Entity
@Table(name = "ImportAttribute")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ImportAttribute.findAll", query = "SELECT i FROM ImportAttribute i"),
    @NamedQuery(name = "ImportAttribute.findById", query = "SELECT i FROM ImportAttribute i WHERE i.id = :id"),
    @NamedQuery(name = "ImportAttribute.findByLabel", query = "SELECT i FROM ImportAttribute i WHERE i.label = :label"),
    @NamedQuery(name = "ImportAttribute.findByDescription", query = "SELECT i FROM ImportAttribute i WHERE i.description = :description"),
    @NamedQuery(name = "ImportAttribute.findByStorageLevelID", query = "SELECT i FROM ImportAttribute i WHERE i.storageLevelID = :storageLevelID"),
    @NamedQuery(name = "ImportAttribute.findByStorageTypeID", query = "SELECT i FROM ImportAttribute i WHERE i.storageTypeID = :storageTypeID"),
    @NamedQuery(name = "ImportAttribute.findByGatewayAttributeID", query = "SELECT i FROM ImportAttribute i WHERE i.gatewayAttributeID = :gatewayAttributeID")})
public class ImportAttribute implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "label")
    private String label;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 2147483647)
    @Column(name = "description")
    private String description;
    @Basic(optional = false)
    @NotNull
    @Column(name = "storageLevelID")
    private int storageLevelID;
    @Basic(optional = false)
    @NotNull
    @Column(name = "storageTypeID")
    private int storageTypeID;
    @Column(name = "gatewayAttributeID")
    private Integer gatewayAttributeID;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "importAttribute")
    private Collection<ImportTaxonObservationAttribute> importTaxonObservationAttributeCollection;

    public ImportAttribute() {
    }

    public ImportAttribute(Integer id) {
        this.id = id;
    }

    public ImportAttribute(Integer id, String label, String description, int storageLevelID, int storageTypeID) {
        this.id = id;
        this.label = label;
        this.description = description;
        this.storageLevelID = storageLevelID;
        this.storageTypeID = storageTypeID;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getStorageLevelID() {
        return storageLevelID;
    }

    public void setStorageLevelID(int storageLevelID) {
        this.storageLevelID = storageLevelID;
    }

    public int getStorageTypeID() {
        return storageTypeID;
    }

    public void setStorageTypeID(int storageTypeID) {
        this.storageTypeID = storageTypeID;
    }

    public Integer getGatewayAttributeID() {
        return gatewayAttributeID;
    }

    public void setGatewayAttributeID(Integer gatewayAttributeID) {
        this.gatewayAttributeID = gatewayAttributeID;
    }

    @XmlTransient
    public Collection<ImportTaxonObservationAttribute> getImportTaxonObservationAttributeCollection() {
        return importTaxonObservationAttributeCollection;
    }

    public void setImportTaxonObservationAttributeCollection(Collection<ImportTaxonObservationAttribute> importTaxonObservationAttributeCollection) {
        this.importTaxonObservationAttributeCollection = importTaxonObservationAttributeCollection;
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
        if (!(object instanceof ImportAttribute)) {
            return false;
        }
        ImportAttribute other = (ImportAttribute) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "uk.org.nbn.nbnv.jpa.nbncore.ImportAttribute[ id=" + id + " ]";
    }
    
}
