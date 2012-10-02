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
public class TaxonDesignationPK implements Serializable {
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 16)
    @Column(name = "taxonVersionKey")
    private String taxonVersionKey;
    @Basic(optional = false)
    @NotNull
    @Column(name = "designationID")
    private int designationID;

    public TaxonDesignationPK() {
    }

    public TaxonDesignationPK(String taxonVersionKey, int designationID) {
        this.taxonVersionKey = taxonVersionKey;
        this.designationID = designationID;
    }

    public String getTaxonVersionKey() {
        return taxonVersionKey;
    }

    public void setTaxonVersionKey(String taxonVersionKey) {
        this.taxonVersionKey = taxonVersionKey;
    }

    public int getDesignationID() {
        return designationID;
    }

    public void setDesignationID(int designationID) {
        this.designationID = designationID;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (taxonVersionKey != null ? taxonVersionKey.hashCode() : 0);
        hash += (int) designationID;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TaxonDesignationPK)) {
            return false;
        }
        TaxonDesignationPK other = (TaxonDesignationPK) object;
        if ((this.taxonVersionKey == null && other.taxonVersionKey != null) || (this.taxonVersionKey != null && !this.taxonVersionKey.equals(other.taxonVersionKey))) {
            return false;
        }
        if (this.designationID != other.designationID) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "uk.org.nbn.nbnv.jpa.nbncore.TaxonDesignationPK[ taxonVersionKey=" + taxonVersionKey + ", designationID=" + designationID + " ]";
    }
    
}
