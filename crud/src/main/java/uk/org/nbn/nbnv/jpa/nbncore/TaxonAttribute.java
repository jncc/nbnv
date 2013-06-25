/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.org.nbn.nbnv.jpa.nbncore;

import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Paul Gilbertson
 */
@Entity
@Table(name = "TaxonAttribute")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "TaxonAttribute.findAll", query = "SELECT t FROM TaxonAttribute t"),
    @NamedQuery(name = "TaxonAttribute.findByTaxonVersionKey", query = "SELECT t FROM TaxonAttribute t WHERE t.taxonAttributePK.taxonVersionKey = :taxonVersionKey"),
    @NamedQuery(name = "TaxonAttribute.findByAttributeID", query = "SELECT t FROM TaxonAttribute t WHERE t.taxonAttributePK.attributeID = :attributeID"),
    @NamedQuery(name = "TaxonAttribute.findByDecimalValue", query = "SELECT t FROM TaxonAttribute t WHERE t.decimalValue = :decimalValue"),
    @NamedQuery(name = "TaxonAttribute.findByEnumValue", query = "SELECT t FROM TaxonAttribute t WHERE t.enumValue = :enumValue"),
    @NamedQuery(name = "TaxonAttribute.findByTextValue", query = "SELECT t FROM TaxonAttribute t WHERE t.textValue = :textValue")})
public class TaxonAttribute implements Serializable {
    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected TaxonAttributePK taxonAttributePK;
    @Column(name = "decimalValue")
    private Long decimalValue;
    @Column(name = "enumValue")
    private Integer enumValue;
    @Size(max = 255)
    @Column(name = "textValue")
    private String textValue;
    @JoinColumn(name = "taxonVersionKey", referencedColumnName = "taxonVersionKey", insertable = false, updatable = false)
    @ManyToOne(optional = false, fetch= FetchType.LAZY)
    private Taxon taxon;
    @JoinColumn(name = "attributeID", referencedColumnName = "id", insertable = false, updatable = false)
    @ManyToOne(optional = false, fetch= FetchType.LAZY)
    private Attribute attribute;

    public TaxonAttribute() {
    }

    public TaxonAttribute(TaxonAttributePK taxonAttributePK) {
        this.taxonAttributePK = taxonAttributePK;
    }

    public TaxonAttribute(String taxonVersionKey, int attributeID) {
        this.taxonAttributePK = new TaxonAttributePK(taxonVersionKey, attributeID);
    }

    public TaxonAttributePK getTaxonAttributePK() {
        return taxonAttributePK;
    }

    public void setTaxonAttributePK(TaxonAttributePK taxonAttributePK) {
        this.taxonAttributePK = taxonAttributePK;
    }

    public Long getDecimalValue() {
        return decimalValue;
    }

    public void setDecimalValue(Long decimalValue) {
        this.decimalValue = decimalValue;
    }

    public Integer getEnumValue() {
        return enumValue;
    }

    public void setEnumValue(Integer enumValue) {
        this.enumValue = enumValue;
    }

    public String getTextValue() {
        return textValue;
    }

    public void setTextValue(String textValue) {
        this.textValue = textValue;
    }

    public Taxon getTaxon() {
        return taxon;
    }

    public void setTaxon(Taxon taxon) {
        this.taxon = taxon;
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
        hash += (taxonAttributePK != null ? taxonAttributePK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TaxonAttribute)) {
            return false;
        }
        TaxonAttribute other = (TaxonAttribute) object;
        if ((this.taxonAttributePK == null && other.taxonAttributePK != null) || (this.taxonAttributePK != null && !this.taxonAttributePK.equals(other.taxonAttributePK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "uk.org.nbn.nbnv.jpa.nbncore.TaxonAttribute[ taxonAttributePK=" + taxonAttributePK + " ]";
    }
    
}
