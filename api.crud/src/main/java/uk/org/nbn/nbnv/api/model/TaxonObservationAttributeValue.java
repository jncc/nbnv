package uk.org.nbn.nbnv.api.model;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class TaxonObservationAttributeValue {
    private int observationID;
    private String label;
    private String description;
    private String textValue;
    
    public TaxonObservationAttributeValue(){}
    
    public TaxonObservationAttributeValue(int observationID, String label, String description, String textValue){
        this.observationID = observationID;
        this.label = label;
        this.description = description;
        this.textValue = textValue;
    }

    public int getObservationID() {
        return observationID;
    }

    public void setObservationID(int observationID) {
        this.observationID = observationID;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTextValue() {
        return textValue;
    }

    public void setTextValue(String textValue) {
        this.textValue = textValue;
    }
    
}
