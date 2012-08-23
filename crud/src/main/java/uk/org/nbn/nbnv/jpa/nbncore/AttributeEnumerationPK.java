/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.org.nbn.nbnv.jpa.nbncore;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;

/**
 *
 * @author Administrator
 */
@Embeddable
public class AttributeEnumerationPK implements Serializable {
    @Basic(optional = false)
    @NotNull
    @Column(name = "attributeID")
    private int attributeID;
    @Basic(optional = false)
    @NotNull
    @Column(name = "enumValue")
    private int enumValue;

    public AttributeEnumerationPK() {
    }

    public AttributeEnumerationPK(int attributeID, int enumValue) {
        this.attributeID = attributeID;
        this.enumValue = enumValue;
    }

    public int getAttributeID() {
        return attributeID;
    }

    public void setAttributeID(int attributeID) {
        this.attributeID = attributeID;
    }

    public int getEnumValue() {
        return enumValue;
    }

    public void setEnumValue(int enumValue) {
        this.enumValue = enumValue;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) attributeID;
        hash += (int) enumValue;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof AttributeEnumerationPK)) {
            return false;
        }
        AttributeEnumerationPK other = (AttributeEnumerationPK) object;
        if (this.attributeID != other.attributeID) {
            return false;
        }
        if (this.enumValue != other.enumValue) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "uk.org.nbn.nbnv.jpa.nbncore.AttributeEnumerationPK[ attributeID=" + attributeID + ", enumValue=" + enumValue + " ]";
    }
    
}
