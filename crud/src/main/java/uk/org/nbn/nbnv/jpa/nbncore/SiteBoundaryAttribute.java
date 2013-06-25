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
@Table(name = "SiteBoundaryAttribute")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "SiteBoundaryAttribute.findAll", query = "SELECT s FROM SiteBoundaryAttribute s"),
    @NamedQuery(name = "SiteBoundaryAttribute.findByFeatureID", query = "SELECT s FROM SiteBoundaryAttribute s WHERE s.siteBoundaryAttributePK.featureID = :featureID"),
    @NamedQuery(name = "SiteBoundaryAttribute.findByAttributeID", query = "SELECT s FROM SiteBoundaryAttribute s WHERE s.siteBoundaryAttributePK.attributeID = :attributeID"),
    @NamedQuery(name = "SiteBoundaryAttribute.findByDecimalValue", query = "SELECT s FROM SiteBoundaryAttribute s WHERE s.decimalValue = :decimalValue"),
    @NamedQuery(name = "SiteBoundaryAttribute.findByEnumValue", query = "SELECT s FROM SiteBoundaryAttribute s WHERE s.enumValue = :enumValue"),
    @NamedQuery(name = "SiteBoundaryAttribute.findByTextValue", query = "SELECT s FROM SiteBoundaryAttribute s WHERE s.textValue = :textValue")})
public class SiteBoundaryAttribute implements Serializable {
    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected SiteBoundaryAttributePK siteBoundaryAttributePK;
    @Column(name = "decimalValue")
    private Long decimalValue;
    @Column(name = "enumValue")
    private Integer enumValue;
    @Size(max = 255)
    @Column(name = "textValue")
    private String textValue;
    @JoinColumn(name = "featureID", referencedColumnName = "featureID", insertable = false, updatable = false)
    @ManyToOne(optional = false, fetch= FetchType.LAZY)
    private SiteBoundary siteBoundary;
    @JoinColumn(name = "attributeID", referencedColumnName = "id", insertable = false, updatable = false)
    @ManyToOne(optional = false, fetch= FetchType.LAZY)
    private Attribute attribute;

    public SiteBoundaryAttribute() {
    }

    public SiteBoundaryAttribute(SiteBoundaryAttributePK siteBoundaryAttributePK) {
        this.siteBoundaryAttributePK = siteBoundaryAttributePK;
    }

    public SiteBoundaryAttribute(int featureID, int attributeID) {
        this.siteBoundaryAttributePK = new SiteBoundaryAttributePK(featureID, attributeID);
    }

    public SiteBoundaryAttributePK getSiteBoundaryAttributePK() {
        return siteBoundaryAttributePK;
    }

    public void setSiteBoundaryAttributePK(SiteBoundaryAttributePK siteBoundaryAttributePK) {
        this.siteBoundaryAttributePK = siteBoundaryAttributePK;
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

    public SiteBoundary getSiteBoundary() {
        return siteBoundary;
    }

    public void setSiteBoundary(SiteBoundary siteBoundary) {
        this.siteBoundary = siteBoundary;
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
        hash += (siteBoundaryAttributePK != null ? siteBoundaryAttributePK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SiteBoundaryAttribute)) {
            return false;
        }
        SiteBoundaryAttribute other = (SiteBoundaryAttribute) object;
        if ((this.siteBoundaryAttributePK == null && other.siteBoundaryAttributePK != null) || (this.siteBoundaryAttributePK != null && !this.siteBoundaryAttributePK.equals(other.siteBoundaryAttributePK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "uk.org.nbn.nbnv.jpa.nbncore.SiteBoundaryAttribute[ siteBoundaryAttributePK=" + siteBoundaryAttributePK + " ]";
    }
    
}
