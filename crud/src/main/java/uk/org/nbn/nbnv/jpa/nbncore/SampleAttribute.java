/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.org.nbn.nbnv.jpa.nbncore;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Paul Gilbertson
 */
@Entity
@Table(name = "SampleAttribute")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "SampleAttribute.findAll", query = "SELECT s FROM SampleAttribute s"),
    @NamedQuery(name = "SampleAttribute.findBySampleID", query = "SELECT s FROM SampleAttribute s WHERE s.sampleAttributePK.sampleID = :sampleID"),
    @NamedQuery(name = "SampleAttribute.findByAttributeID", query = "SELECT s FROM SampleAttribute s WHERE s.sampleAttributePK.attributeID = :attributeID"),
    @NamedQuery(name = "SampleAttribute.findByDecimalValue", query = "SELECT s FROM SampleAttribute s WHERE s.decimalValue = :decimalValue"),
    @NamedQuery(name = "SampleAttribute.findByEnumValue", query = "SELECT s FROM SampleAttribute s WHERE s.enumValue = :enumValue"),
    @NamedQuery(name = "SampleAttribute.findByTextValue", query = "SELECT s FROM SampleAttribute s WHERE s.textValue = :textValue")})
public class SampleAttribute implements Serializable {
    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected SampleAttributePK sampleAttributePK;
    @Column(name = "decimalValue")
    private Long decimalValue;
    @Column(name = "enumValue")
    private Integer enumValue;
    @Size(max = 255)
    @Column(name = "textValue")
    private String textValue;
    @JoinColumn(name = "sampleID", referencedColumnName = "id", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Sample sample;
    @JoinColumn(name = "attributeID", referencedColumnName = "id", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Attribute attribute;

    public SampleAttribute() {
    }

    public SampleAttribute(SampleAttributePK sampleAttributePK) {
        this.sampleAttributePK = sampleAttributePK;
    }

    public SampleAttribute(int sampleID, int attributeID) {
        this.sampleAttributePK = new SampleAttributePK(sampleID, attributeID);
    }

    public SampleAttributePK getSampleAttributePK() {
        return sampleAttributePK;
    }

    public void setSampleAttributePK(SampleAttributePK sampleAttributePK) {
        this.sampleAttributePK = sampleAttributePK;
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

    public Sample getSample() {
        return sample;
    }

    public void setSample(Sample sample) {
        this.sample = sample;
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
        hash += (sampleAttributePK != null ? sampleAttributePK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SampleAttribute)) {
            return false;
        }
        SampleAttribute other = (SampleAttribute) object;
        if ((this.sampleAttributePK == null && other.sampleAttributePK != null) || (this.sampleAttributePK != null && !this.sampleAttributePK.equals(other.sampleAttributePK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "uk.org.nbn.nbnv.jpa.nbncore.SampleAttribute[ sampleAttributePK=" + sampleAttributePK + " ]";
    }
    
}
