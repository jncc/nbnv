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
 * @author Paul Gilbertson
 */
@Embeddable
public class SiteBoundaryAttributePK implements Serializable {
    @Basic(optional = false)
    @NotNull
    @Column(name = "featureID")
    private int featureID;
    @Basic(optional = false)
    @NotNull
    @Column(name = "attributeID")
    private int attributeID;

    public SiteBoundaryAttributePK() {
    }

    public SiteBoundaryAttributePK(int featureID, int attributeID) {
        this.featureID = featureID;
        this.attributeID = attributeID;
    }

    public int getFeatureID() {
        return featureID;
    }

    public void setFeatureID(int featureID) {
        this.featureID = featureID;
    }

    public int getAttributeID() {
        return attributeID;
    }

    public void setAttributeID(int attributeID) {
        this.attributeID = attributeID;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) featureID;
        hash += (int) attributeID;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SiteBoundaryAttributePK)) {
            return false;
        }
        SiteBoundaryAttributePK other = (SiteBoundaryAttributePK) object;
        if (this.featureID != other.featureID) {
            return false;
        }
        if (this.attributeID != other.attributeID) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "uk.org.nbn.nbnv.jpa.nbncore.SiteBoundaryAttributePK[ featureID=" + featureID + ", attributeID=" + attributeID + " ]";
    }
    
}
