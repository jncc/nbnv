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
import javax.validation.constraints.Size;

/**
 *
 * @author Paul Gilbertson
 */
@Embeddable
public class TaxonAttributePK implements Serializable {
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 16)
    @Column(name = "taxonVersionKey")
    private String taxonVersionKey;
    @Basic(optional = false)
    @NotNull
    @Column(name = "attributeID")
    private int attributeID;

    public TaxonAttributePK() {
    }

    public TaxonAttributePK(String taxonVersionKey, int attributeID) {
        this.taxonVersionKey = taxonVersionKey;
        this.attributeID = attributeID;
    }

    public String getTaxonVersionKey() {
        return taxonVersionKey;
    }

    public void setTaxonVersionKey(String taxonVersionKey) {
        this.taxonVersionKey = taxonVersionKey;
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
        hash += (taxonVersionKey != null ? taxonVersionKey.hashCode() : 0);
        hash += (int) attributeID;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TaxonAttributePK)) {
            return false;
        }
        TaxonAttributePK other = (TaxonAttributePK) object;
        if ((this.taxonVersionKey == null && other.taxonVersionKey != null) || (this.taxonVersionKey != null && !this.taxonVersionKey.equals(other.taxonVersionKey))) {
            return false;
        }
        if (this.attributeID != other.attributeID) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "uk.org.nbn.nbnv.jpa.nbncore.TaxonAttributePK[ taxonVersionKey=" + taxonVersionKey + ", attributeID=" + attributeID + " ]";
    }
    
}
