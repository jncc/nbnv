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
@Table(name = "GatewayAttribute")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "GatewayAttribute.findAll", query = "SELECT g FROM GatewayAttribute g"),
    @NamedQuery(name = "GatewayAttribute.findByGatewayAttributeID", query = "SELECT g FROM GatewayAttribute g WHERE g.gatewayAttributeID = :gatewayAttributeID"),
    @NamedQuery(name = "GatewayAttribute.findByLabel", query = "SELECT g FROM GatewayAttribute g WHERE g.label = :label"),
    @NamedQuery(name = "GatewayAttribute.findByDescription", query = "SELECT g FROM GatewayAttribute g WHERE g.description = :description")})
public class GatewayAttribute implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "gatewayAttributeID")
    private Integer gatewayAttributeID;
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
    @JoinColumn(name = "storageTypeID", referencedColumnName = "attributeStorageTypeID")
    @ManyToOne(optional = false)
    private AttributeStorageType storageTypeID;
    @OneToMany(mappedBy = "gatewayAttributeID")
    private Collection<Attribute> attributeCollection;

    public GatewayAttribute() {
    }

    public GatewayAttribute(Integer gatewayAttributeID) {
        this.gatewayAttributeID = gatewayAttributeID;
    }

    public GatewayAttribute(Integer gatewayAttributeID, String label, String description) {
        this.gatewayAttributeID = gatewayAttributeID;
        this.label = label;
        this.description = description;
    }

    public Integer getGatewayAttributeID() {
        return gatewayAttributeID;
    }

    public void setGatewayAttributeID(Integer gatewayAttributeID) {
        this.gatewayAttributeID = gatewayAttributeID;
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

    public AttributeStorageType getStorageTypeID() {
        return storageTypeID;
    }

    public void setStorageTypeID(AttributeStorageType storageTypeID) {
        this.storageTypeID = storageTypeID;
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
        hash += (gatewayAttributeID != null ? gatewayAttributeID.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof GatewayAttribute)) {
            return false;
        }
        GatewayAttribute other = (GatewayAttribute) object;
        if ((this.gatewayAttributeID == null && other.gatewayAttributeID != null) || (this.gatewayAttributeID != null && !this.gatewayAttributeID.equals(other.gatewayAttributeID))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "uk.org.nbn.nbnv.jpa.nbncore.GatewayAttribute[ gatewayAttributeID=" + gatewayAttributeID + " ]";
    }
    
}
