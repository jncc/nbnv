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
@Table(name = "AttributeStorageType")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "AttributeStorageType.findAll", query = "SELECT a FROM AttributeStorageType a"),
    @NamedQuery(name = "AttributeStorageType.findByAttributeStorageTypeID", query = "SELECT a FROM AttributeStorageType a WHERE a.attributeStorageTypeID = :attributeStorageTypeID"),
    @NamedQuery(name = "AttributeStorageType.findByLabel", query = "SELECT a FROM AttributeStorageType a WHERE a.label = :label")})
public class AttributeStorageType implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "attributeStorageTypeID")
    private Integer attributeStorageTypeID;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 11)
    @Column(name = "label")
    private String label;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "storageTypeID")
    private Collection<GatewayAttribute> gatewayAttributeCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "storageTypeID")
    private Collection<Attribute> attributeCollection;

    public AttributeStorageType() {
    }

    public AttributeStorageType(Integer attributeStorageTypeID) {
        this.attributeStorageTypeID = attributeStorageTypeID;
    }

    public AttributeStorageType(Integer attributeStorageTypeID, String label) {
        this.attributeStorageTypeID = attributeStorageTypeID;
        this.label = label;
    }

    public Integer getAttributeStorageTypeID() {
        return attributeStorageTypeID;
    }

    public void setAttributeStorageTypeID(Integer attributeStorageTypeID) {
        this.attributeStorageTypeID = attributeStorageTypeID;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    @XmlTransient
    public Collection<GatewayAttribute> getGatewayAttributeCollection() {
        return gatewayAttributeCollection;
    }

    public void setGatewayAttributeCollection(Collection<GatewayAttribute> gatewayAttributeCollection) {
        this.gatewayAttributeCollection = gatewayAttributeCollection;
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
        hash += (attributeStorageTypeID != null ? attributeStorageTypeID.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof AttributeStorageType)) {
            return false;
        }
        AttributeStorageType other = (AttributeStorageType) object;
        if ((this.attributeStorageTypeID == null && other.attributeStorageTypeID != null) || (this.attributeStorageTypeID != null && !this.attributeStorageTypeID.equals(other.attributeStorageTypeID))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "uk.org.nbn.nbnv.jpa.nbncore.AttributeStorageType[ attributeStorageTypeID=" + attributeStorageTypeID + " ]";
    }
    
}
