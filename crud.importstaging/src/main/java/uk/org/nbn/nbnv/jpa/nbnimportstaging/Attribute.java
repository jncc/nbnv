/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.org.nbn.nbnv.jpa.nbnimportstaging;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author felix mason
 */
@Entity
@Table(name = "Attribute")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Attribute.findAll", query = "SELECT a FROM Attribute a"),
    @NamedQuery(name = "Attribute.findById", query = "SELECT a FROM Attribute a WHERE a.id = :id"),
    @NamedQuery(name = "Attribute.findByLabel", query = "SELECT a FROM Attribute a WHERE a.label = :label"),
    @NamedQuery(name = "Attribute.findByDescription", query = "SELECT a FROM Attribute a WHERE a.description = :description"),
    @NamedQuery(name = "Attribute.findByStorageLevelID", query = "SELECT a FROM Attribute a WHERE a.storageLevelID = :storageLevelID"),
    @NamedQuery(name = "Attribute.findByStorageTypeID", query = "SELECT a FROM Attribute a WHERE a.storageTypeID = :storageTypeID"),
    @NamedQuery(name = "Attribute.findByGatewayAttributeID", query = "SELECT a FROM Attribute a WHERE a.gatewayAttributeID = :gatewayAttributeID")})
public class Attribute implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "id", nullable = false)
    private Integer id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "label", nullable = false, length = 50)
    private String label;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 2147483647)
    @Column(name = "description", nullable = false, length = 2147483647)
    private String description;
    @Basic(optional = false)
    @NotNull
    @Column(name = "storageLevelID", nullable = false)
    private int storageLevelID;
    @Basic(optional = false)
    @NotNull
    @Column(name = "storageTypeID", nullable = false)
    private int storageTypeID;
    @Column(name = "gatewayAttributeID")
    private Integer gatewayAttributeID;

    public Attribute() {
    }

    public Attribute(Integer id) {
        this.id = id;
    }

    public Attribute(Integer id, String label, String description, int storageLevelID, int storageTypeID) {
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

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Attribute)) {
            return false;
        }
        Attribute other = (Attribute) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "uk.org.nbn.nbnv.jpa.nbnimportstaging.Attribute[ id=" + id + " ]";
    }
    
}
