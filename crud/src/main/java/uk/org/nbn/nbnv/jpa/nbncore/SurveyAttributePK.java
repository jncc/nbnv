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
public class SurveyAttributePK implements Serializable {
    @Basic(optional = false)
    @NotNull
    @Column(name = "surveyID")
    private int surveyID;
    @Basic(optional = false)
    @NotNull
    @Column(name = "attributeID")
    private int attributeID;

    public SurveyAttributePK() {
    }

    public SurveyAttributePK(int surveyID, int attributeID) {
        this.surveyID = surveyID;
        this.attributeID = attributeID;
    }

    public int getSurveyID() {
        return surveyID;
    }

    public void setSurveyID(int surveyID) {
        this.surveyID = surveyID;
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
        hash += (int) surveyID;
        hash += (int) attributeID;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SurveyAttributePK)) {
            return false;
        }
        SurveyAttributePK other = (SurveyAttributePK) object;
        if (this.surveyID != other.surveyID) {
            return false;
        }
        if (this.attributeID != other.attributeID) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "uk.org.nbn.nbnv.jpa.nbncore.SurveyAttributePK[ surveyID=" + surveyID + ", attributeID=" + attributeID + " ]";
    }
    
}
