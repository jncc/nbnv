/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.org.nbn.nbnv.api.model;

/**
 *
 * @author Matt Debont
 */
public class TaxonObservationAttribute {
    private int observationID;
    private int attributeID;
    private String textValue;

    public int getObservationID() {
        return observationID;
    }

    public void setObservationID(int observationID) {
        this.observationID = observationID;
    }

    public int getAttributeID() {
        return attributeID;
    }

    public void setAttributeID(int attributeID) {
        this.attributeID = attributeID;
    }

    public String getTextValue() {
        return textValue;
    }

    public void setTextValue(String textValue) {
        this.textValue = textValue;
    }
}
