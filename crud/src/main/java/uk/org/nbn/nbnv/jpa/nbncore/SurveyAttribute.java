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
@Table(name = "SurveyAttribute")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "SurveyAttribute.findAll", query = "SELECT s FROM SurveyAttribute s"),
    @NamedQuery(name = "SurveyAttribute.findBySurveyID", query = "SELECT s FROM SurveyAttribute s WHERE s.surveyAttributePK.surveyID = :surveyID"),
    @NamedQuery(name = "SurveyAttribute.findByAttributeID", query = "SELECT s FROM SurveyAttribute s WHERE s.surveyAttributePK.attributeID = :attributeID"),
    @NamedQuery(name = "SurveyAttribute.findByDecimalValue", query = "SELECT s FROM SurveyAttribute s WHERE s.decimalValue = :decimalValue"),
    @NamedQuery(name = "SurveyAttribute.findByEnumValue", query = "SELECT s FROM SurveyAttribute s WHERE s.enumValue = :enumValue"),
    @NamedQuery(name = "SurveyAttribute.findByTextValue", query = "SELECT s FROM SurveyAttribute s WHERE s.textValue = :textValue")})
public class SurveyAttribute implements Serializable {
    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected SurveyAttributePK surveyAttributePK;
    @Column(name = "decimalValue")
    private Long decimalValue;
    @Column(name = "enumValue")
    private Integer enumValue;
    @Size(max = 255)
    @Column(name = "textValue")
    private String textValue;
    @JoinColumn(name = "surveyID", referencedColumnName = "id", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Survey survey;
    @JoinColumn(name = "attributeID", referencedColumnName = "id", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Attribute attribute;

    public SurveyAttribute() {
    }

    public SurveyAttribute(SurveyAttributePK surveyAttributePK) {
        this.surveyAttributePK = surveyAttributePK;
    }

    public SurveyAttribute(int surveyID, int attributeID) {
        this.surveyAttributePK = new SurveyAttributePK(surveyID, attributeID);
    }

    public SurveyAttributePK getSurveyAttributePK() {
        return surveyAttributePK;
    }

    public void setSurveyAttributePK(SurveyAttributePK surveyAttributePK) {
        this.surveyAttributePK = surveyAttributePK;
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

    public Survey getSurvey() {
        return survey;
    }

    public void setSurvey(Survey survey) {
        this.survey = survey;
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
        hash += (surveyAttributePK != null ? surveyAttributePK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SurveyAttribute)) {
            return false;
        }
        SurveyAttribute other = (SurveyAttribute) object;
        if ((this.surveyAttributePK == null && other.surveyAttributePK != null) || (this.surveyAttributePK != null && !this.surveyAttributePK.equals(other.surveyAttributePK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "uk.org.nbn.nbnv.jpa.nbncore.SurveyAttribute[ surveyAttributePK=" + surveyAttributePK + " ]";
    }
    
}
