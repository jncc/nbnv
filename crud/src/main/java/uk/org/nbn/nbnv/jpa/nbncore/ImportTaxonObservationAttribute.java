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
 * @author felix mason
 */
@Entity
@Table(name = "ImportTaxonObservationAttribute")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ImportTaxonObservationAttribute.findAll", query = "SELECT i FROM ImportTaxonObservationAttribute i"),
    @NamedQuery(name = "ImportTaxonObservationAttribute.findByObservationID", query = "SELECT i FROM ImportTaxonObservationAttribute i WHERE i.importTaxonObservationAttributePK.observationID = :observationID"),
    @NamedQuery(name = "ImportTaxonObservationAttribute.findByAttributeID", query = "SELECT i FROM ImportTaxonObservationAttribute i WHERE i.importTaxonObservationAttributePK.attributeID = :attributeID"),
    @NamedQuery(name = "ImportTaxonObservationAttribute.findByDecimalValue", query = "SELECT i FROM ImportTaxonObservationAttribute i WHERE i.decimalValue = :decimalValue"),
    @NamedQuery(name = "ImportTaxonObservationAttribute.findByEnumValue", query = "SELECT i FROM ImportTaxonObservationAttribute i WHERE i.enumValue = :enumValue"),
    @NamedQuery(name = "ImportTaxonObservationAttribute.findByTextValue", query = "SELECT i FROM ImportTaxonObservationAttribute i WHERE i.textValue = :textValue")})
public class ImportTaxonObservationAttribute implements Serializable {
    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected ImportTaxonObservationAttributePK importTaxonObservationAttributePK;
    @Column(name = "decimalValue")
    private Long decimalValue;
    @Column(name = "enumValue")
    private Integer enumValue;
    @Size(max = 255)
    @Column(name = "textValue")
    private String textValue;
    @JoinColumn(name = "observationID", referencedColumnName = "id", insertable = false, updatable = false)
    @ManyToOne(optional = false, fetch= FetchType.LAZY)
    private ImportTaxonObservation importTaxonObservation;
    @JoinColumn(name = "attributeID", referencedColumnName = "id", insertable = false, updatable = false)
    @ManyToOne(optional = false, fetch= FetchType.LAZY)
    private ImportAttribute importAttribute;

    public ImportTaxonObservationAttribute() {
    }

    public ImportTaxonObservationAttribute(ImportTaxonObservationAttributePK importTaxonObservationAttributePK) {
        this.importTaxonObservationAttributePK = importTaxonObservationAttributePK;
    }

    public ImportTaxonObservationAttribute(int observationID, int attributeID) {
        this.importTaxonObservationAttributePK = new ImportTaxonObservationAttributePK(observationID, attributeID);
    }

    public ImportTaxonObservationAttributePK getImportTaxonObservationAttributePK() {
        return importTaxonObservationAttributePK;
    }

    public void setImportTaxonObservationAttributePK(ImportTaxonObservationAttributePK importTaxonObservationAttributePK) {
        this.importTaxonObservationAttributePK = importTaxonObservationAttributePK;
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

    public ImportTaxonObservation getImportTaxonObservation() {
        return importTaxonObservation;
    }

    public void setImportTaxonObservation(ImportTaxonObservation importTaxonObservation) {
        this.importTaxonObservation = importTaxonObservation;
    }

    public ImportAttribute getImportAttribute() {
        return importAttribute;
    }

    public void setImportAttribute(ImportAttribute importAttribute) {
        this.importAttribute = importAttribute;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (importTaxonObservationAttributePK != null ? importTaxonObservationAttributePK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ImportTaxonObservationAttribute)) {
            return false;
        }
        ImportTaxonObservationAttribute other = (ImportTaxonObservationAttribute) object;
        if ((this.importTaxonObservationAttributePK == null && other.importTaxonObservationAttributePK != null) || (this.importTaxonObservationAttributePK != null && !this.importTaxonObservationAttributePK.equals(other.importTaxonObservationAttributePK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "uk.org.nbn.nbnv.jpa.nbncore.ImportTaxonObservationAttribute[ importTaxonObservationAttributePK=" + importTaxonObservationAttributePK + " ]";
    }
    
}
