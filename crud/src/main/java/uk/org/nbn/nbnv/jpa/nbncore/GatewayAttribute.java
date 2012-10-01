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
@Table(name = "GatewayAttribute")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "GatewayAttribute.findAll", query = "SELECT g FROM GatewayAttribute g"),
    @NamedQuery(name = "GatewayAttribute.findById", query = "SELECT g FROM GatewayAttribute g WHERE g.id = :id"),
    @NamedQuery(name = "GatewayAttribute.findByLabel", query = "SELECT g FROM GatewayAttribute g WHERE g.label = :label"),
    @NamedQuery(name = "GatewayAttribute.findByDescription", query = "SELECT g FROM GatewayAttribute g WHERE g.description = :description")})
public class GatewayAttribute implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @GeneratedValue(strategy=GenerationType.IDENTITY)
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
    @JoinColumn(name = "storageTypeID", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private AttributeStorageType storageTypeID;
    @OneToMany(mappedBy = "gatewayAttributeID")
    private Collection<Attribute> attributeCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "gatewayAttribute")
    private Collection<GatewayAttributeEnumeration> gatewayAttributeEnumerationCollection;

    public GatewayAttribute() {
    }

    public GatewayAttribute(Integer id) {
        this.id = id;
    }

    public GatewayAttribute(Integer id, String label, String description) {
        this.id = id;
        this.label = label;
        this.description = description;
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

    @XmlTransient
    public Collection<GatewayAttributeEnumeration> getGatewayAttributeEnumerationCollection() {
        return gatewayAttributeEnumerationCollection;
    }

    public void setGatewayAttributeEnumerationCollection(Collection<GatewayAttributeEnumeration> gatewayAttributeEnumerationCollection) {
        this.gatewayAttributeEnumerationCollection = gatewayAttributeEnumerationCollection;
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
        if (!(object instanceof GatewayAttribute)) {
            return false;
        }
        GatewayAttribute other = (GatewayAttribute) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "uk.org.nbn.nbnv.jpa.nbncore.GatewayAttribute[ id=" + id + " ]";
    }
    
}
