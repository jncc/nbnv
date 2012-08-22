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
@Table(name = "Attribute")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Attribute.findAll", query = "SELECT a FROM Attribute a"),
    @NamedQuery(name = "Attribute.findByAttributeID", query = "SELECT a FROM Attribute a WHERE a.attributeID = :attributeID"),
    @NamedQuery(name = "Attribute.findByLabel", query = "SELECT a FROM Attribute a WHERE a.label = :label"),
    @NamedQuery(name = "Attribute.findByDescription", query = "SELECT a FROM Attribute a WHERE a.description = :description")})
public class Attribute implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "attributeID")
    private Integer attributeID;
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
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "attribute")
    private Collection<TaxonObservationAttribute> taxonObservationAttributeCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "attribute")
    private Collection<AttributeEnumeration> attributeEnumerationCollection;
    @JoinColumn(name = "storageLevelID", referencedColumnName = "storageLevelID")
    @ManyToOne(optional = false)
    private StorageLevel storageLevelID;
    @JoinColumn(name = "gatewayAttributeID", referencedColumnName = "gatewayAttributeID")
    @ManyToOne
    private GatewayAttribute gatewayAttributeID;
    @JoinColumn(name = "storageTypeID", referencedColumnName = "attributeStorageTypeID")
    @ManyToOne(optional = false)
    private AttributeStorageType storageTypeID;

    public Attribute() {
    }

    public Attribute(Integer attributeID) {
        this.attributeID = attributeID;
    }

    public Attribute(Integer attributeID, String label, String description) {
        this.attributeID = attributeID;
        this.label = label;
        this.description = description;
    }

    public Integer getAttributeID() {
        return attributeID;
    }

    public void setAttributeID(Integer attributeID) {
        this.attributeID = attributeID;
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

    @XmlTransient
    public Collection<TaxonObservationAttribute> getTaxonObservationAttributeCollection() {
        return taxonObservationAttributeCollection;
    }

    public void setTaxonObservationAttributeCollection(Collection<TaxonObservationAttribute> taxonObservationAttributeCollection) {
        this.taxonObservationAttributeCollection = taxonObservationAttributeCollection;
    }

    @XmlTransient
    public Collection<AttributeEnumeration> getAttributeEnumerationCollection() {
        return attributeEnumerationCollection;
    }

    public void setAttributeEnumerationCollection(Collection<AttributeEnumeration> attributeEnumerationCollection) {
        this.attributeEnumerationCollection = attributeEnumerationCollection;
    }

    public StorageLevel getStorageLevelID() {
        return storageLevelID;
    }

    public void setStorageLevelID(StorageLevel storageLevelID) {
        this.storageLevelID = storageLevelID;
    }

    public GatewayAttribute getGatewayAttributeID() {
        return gatewayAttributeID;
    }

    public void setGatewayAttributeID(GatewayAttribute gatewayAttributeID) {
        this.gatewayAttributeID = gatewayAttributeID;
    }

    public AttributeStorageType getStorageTypeID() {
        return storageTypeID;
    }

    public void setStorageTypeID(AttributeStorageType storageTypeID) {
        this.storageTypeID = storageTypeID;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (attributeID != null ? attributeID.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Attribute)) {
            return false;
        }
        Attribute other = (Attribute) object;
        if ((this.attributeID == null && other.attributeID != null) || (this.attributeID != null && !this.attributeID.equals(other.attributeID))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "uk.org.nbn.nbnv.jpa.nbncore.Attribute[ attributeID=" + attributeID + " ]";
    }
    
}
