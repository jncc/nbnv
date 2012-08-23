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
public class TaxonObservationAttributePK implements Serializable {
    @Basic(optional = false)
    @NotNull
    @Column(name = "observationID")
    private int observationID;
    @Basic(optional = false)
    @NotNull
    @Column(name = "attributeID")
    private int attributeID;

    public TaxonObservationAttributePK() {
    }

    public TaxonObservationAttributePK(int observationID, int attributeID) {
        this.observationID = observationID;
        this.attributeID = attributeID;
    }

    public int getObservationID() {
        return observationID;
    }

    public void setObservationID(int observationID) {
        this.observationID = observationID;
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
        hash += (int) observationID;
        hash += (int) attributeID;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TaxonObservationAttributePK)) {
            return false;
        }
        TaxonObservationAttributePK other = (TaxonObservationAttributePK) object;
        if (this.observationID != other.observationID) {
            return false;
        }
        if (this.attributeID != other.attributeID) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "uk.org.nbn.nbnv.jpa.nbncore.TaxonObservationAttributePK[ observationID=" + observationID + ", attributeID=" + attributeID + " ]";
    }
    
}
