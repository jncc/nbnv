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
public class SampleAttributePK implements Serializable {
    @Basic(optional = false)
    @NotNull
    @Column(name = "sampleID")
    private int sampleID;
    @Basic(optional = false)
    @NotNull
    @Column(name = "attributeID")
    private int attributeID;

    public SampleAttributePK() {
    }

    public SampleAttributePK(int sampleID, int attributeID) {
        this.sampleID = sampleID;
        this.attributeID = attributeID;
    }

    public int getSampleID() {
        return sampleID;
    }

    public void setSampleID(int sampleID) {
        this.sampleID = sampleID;
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
        hash += (int) sampleID;
        hash += (int) attributeID;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SampleAttributePK)) {
            return false;
        }
        SampleAttributePK other = (SampleAttributePK) object;
        if (this.sampleID != other.sampleID) {
            return false;
        }
        if (this.attributeID != other.attributeID) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "uk.org.nbn.nbnv.jpa.nbncore.SampleAttributePK[ sampleID=" + sampleID + ", attributeID=" + attributeID + " ]";
    }
    
}
