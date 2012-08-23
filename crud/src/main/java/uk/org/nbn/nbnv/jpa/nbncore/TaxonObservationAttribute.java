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
 * @author Administrator
 */
@Entity
@Table(name = "TaxonObservationAttribute")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "TaxonObservationAttribute.findAll", query = "SELECT t FROM TaxonObservationAttribute t"),
    @NamedQuery(name = "TaxonObservationAttribute.findByObservationID", query = "SELECT t FROM TaxonObservationAttribute t WHERE t.taxonObservationAttributePK.observationID = :observationID"),
    @NamedQuery(name = "TaxonObservationAttribute.findByAttributeID", query = "SELECT t FROM TaxonObservationAttribute t WHERE t.taxonObservationAttributePK.attributeID = :attributeID"),
    @NamedQuery(name = "TaxonObservationAttribute.findByDecimalValue", query = "SELECT t FROM TaxonObservationAttribute t WHERE t.decimalValue = :decimalValue"),
    @NamedQuery(name = "TaxonObservationAttribute.findByEnumValue", query = "SELECT t FROM TaxonObservationAttribute t WHERE t.enumValue = :enumValue"),
    @NamedQuery(name = "TaxonObservationAttribute.findByTextValue", query = "SELECT t FROM TaxonObservationAttribute t WHERE t.textValue = :textValue")})
public class TaxonObservationAttribute implements Serializable {
    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected TaxonObservationAttributePK taxonObservationAttributePK;
    @Column(name = "decimalValue")
    private Long decimalValue;
    @Column(name = "enumValue")
    private Integer enumValue;
    @Size(max = 200)
    @Column(name = "textValue")
    private String textValue;
    @JoinColumn(name = "observationID", referencedColumnName = "observationID", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private TaxonObservation taxonObservation;
    @JoinColumn(name = "attributeID", referencedColumnName = "attributeID", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Attribute attribute;

    public TaxonObservationAttribute() {
    }

    public TaxonObservationAttribute(TaxonObservationAttributePK taxonObservationAttributePK) {
        this.taxonObservationAttributePK = taxonObservationAttributePK;
    }

    public TaxonObservationAttribute(int observationID, int attributeID) {
        this.taxonObservationAttributePK = new TaxonObservationAttributePK(observationID, attributeID);
    }

    public TaxonObservationAttributePK getTaxonObservationAttributePK() {
        return taxonObservationAttributePK;
    }

    public void setTaxonObservationAttributePK(TaxonObservationAttributePK taxonObservationAttributePK) {
        this.taxonObservationAttributePK = taxonObservationAttributePK;
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

    public TaxonObservation getTaxonObservation() {
        return taxonObservation;
    }

    public void setTaxonObservation(TaxonObservation taxonObservation) {
        this.taxonObservation = taxonObservation;
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
        hash += (taxonObservationAttributePK != null ? taxonObservationAttributePK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TaxonObservationAttribute)) {
            return false;
        }
        TaxonObservationAttribute other = (TaxonObservationAttribute) object;
        if ((this.taxonObservationAttributePK == null && other.taxonObservationAttributePK != null) || (this.taxonObservationAttributePK != null && !this.taxonObservationAttributePK.equals(other.taxonObservationAttributePK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "uk.org.nbn.nbnv.jpa.nbncore.TaxonObservationAttribute[ taxonObservationAttributePK=" + taxonObservationAttributePK + " ]";
    }
    
}
