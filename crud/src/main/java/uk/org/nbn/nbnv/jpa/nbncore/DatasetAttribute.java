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
@Table(name = "DatasetAttribute")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "DatasetAttribute.findAll", query = "SELECT d FROM DatasetAttribute d"),
    @NamedQuery(name = "DatasetAttribute.findByDatasetKey", query = "SELECT d FROM DatasetAttribute d WHERE d.datasetAttributePK.datasetKey = :datasetKey"),
    @NamedQuery(name = "DatasetAttribute.findByAttributeID", query = "SELECT d FROM DatasetAttribute d WHERE d.datasetAttributePK.attributeID = :attributeID"),
    @NamedQuery(name = "DatasetAttribute.findByDecimalValue", query = "SELECT d FROM DatasetAttribute d WHERE d.decimalValue = :decimalValue"),
    @NamedQuery(name = "DatasetAttribute.findByEnumValue", query = "SELECT d FROM DatasetAttribute d WHERE d.enumValue = :enumValue"),
    @NamedQuery(name = "DatasetAttribute.findByTextValue", query = "SELECT d FROM DatasetAttribute d WHERE d.textValue = :textValue")})
public class DatasetAttribute implements Serializable {
    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected DatasetAttributePK datasetAttributePK;
    @Column(name = "decimalValue")
    private Long decimalValue;
    @Column(name = "enumValue")
    private Integer enumValue;
    @Size(max = 255)
    @Column(name = "textValue")
    private String textValue;
    @JoinColumn(name = "datasetKey", referencedColumnName = "key", insertable = false, updatable = false)
    @ManyToOne(optional = false, fetch= FetchType.LAZY)
    private Dataset dataset;
    @JoinColumn(name = "attributeID", referencedColumnName = "id", insertable = false, updatable = false)
    @ManyToOne(optional = false, fetch= FetchType.LAZY)
    private Attribute attribute;

    public DatasetAttribute() {
    }

    public DatasetAttribute(DatasetAttributePK datasetAttributePK) {
        this.datasetAttributePK = datasetAttributePK;
    }

    public DatasetAttribute(String datasetKey, int attributeID) {
        this.datasetAttributePK = new DatasetAttributePK(datasetKey, attributeID);
    }

    public DatasetAttributePK getDatasetAttributePK() {
        return datasetAttributePK;
    }

    public void setDatasetAttributePK(DatasetAttributePK datasetAttributePK) {
        this.datasetAttributePK = datasetAttributePK;
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

    public Dataset getDataset() {
        return dataset;
    }

    public void setDataset(Dataset dataset) {
        this.dataset = dataset;
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
        hash += (datasetAttributePK != null ? datasetAttributePK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof DatasetAttribute)) {
            return false;
        }
        DatasetAttribute other = (DatasetAttribute) object;
        if ((this.datasetAttributePK == null && other.datasetAttributePK != null) || (this.datasetAttributePK != null && !this.datasetAttributePK.equals(other.datasetAttributePK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "uk.org.nbn.nbnv.jpa.nbncore.DatasetAttribute[ datasetAttributePK=" + datasetAttributePK + " ]";
    }
    
}
