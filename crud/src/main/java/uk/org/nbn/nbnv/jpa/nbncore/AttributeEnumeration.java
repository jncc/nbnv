/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.org.nbn.nbnv.jpa.nbncore;

import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Administrator
 */
@Entity
@Table(name = "AttributeEnumeration")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "AttributeEnumeration.findAll", query = "SELECT a FROM AttributeEnumeration a"),
    @NamedQuery(name = "AttributeEnumeration.findByAttributeID", query = "SELECT a FROM AttributeEnumeration a WHERE a.attributeEnumerationPK.attributeID = :attributeID"),
    @NamedQuery(name = "AttributeEnumeration.findByEnumValue", query = "SELECT a FROM AttributeEnumeration a WHERE a.attributeEnumerationPK.enumValue = :enumValue"),
    @NamedQuery(name = "AttributeEnumeration.findByLabel", query = "SELECT a FROM AttributeEnumeration a WHERE a.label = :label"),
    @NamedQuery(name = "AttributeEnumeration.findByDescription", query = "SELECT a FROM AttributeEnumeration a WHERE a.description = :description")})
public class AttributeEnumeration implements Serializable {
    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected AttributeEnumerationPK attributeEnumerationPK;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "label")
    private String label;
    @Size(max = 2147483647)
    @Column(name = "description")
    private String description;
    @JoinColumn(name = "attributeID", referencedColumnName = "attributeID", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Attribute attribute;

    public AttributeEnumeration() {
    }

    public AttributeEnumeration(AttributeEnumerationPK attributeEnumerationPK) {
        this.attributeEnumerationPK = attributeEnumerationPK;
    }

    public AttributeEnumeration(AttributeEnumerationPK attributeEnumerationPK, String label) {
        this.attributeEnumerationPK = attributeEnumerationPK;
        this.label = label;
    }

    public AttributeEnumeration(int attributeID, int enumValue) {
        this.attributeEnumerationPK = new AttributeEnumerationPK(attributeID, enumValue);
    }

    public AttributeEnumerationPK getAttributeEnumerationPK() {
        return attributeEnumerationPK;
    }

    public void setAttributeEnumerationPK(AttributeEnumerationPK attributeEnumerationPK) {
        this.attributeEnumerationPK = attributeEnumerationPK;
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

    public Attribute getAttribute() {
        return attribute;
    }

    public void setAttribute(Attribute attribute) {
        this.attribute = attribute;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (attributeEnumerationPK != null ? attributeEnumerationPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof AttributeEnumeration)) {
            return false;
        }
        AttributeEnumeration other = (AttributeEnumeration) object;
        if ((this.attributeEnumerationPK == null && other.attributeEnumerationPK != null) || (this.attributeEnumerationPK != null && !this.attributeEnumerationPK.equals(other.attributeEnumerationPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "uk.org.nbn.nbnv.jpa.nbncore.AttributeEnumeration[ attributeEnumerationPK=" + attributeEnumerationPK + " ]";
    }
    
}
