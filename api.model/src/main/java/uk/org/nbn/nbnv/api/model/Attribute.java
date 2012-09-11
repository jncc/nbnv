package uk.org.nbn.nbnv.api.model;

public class Attribute {
    
    private int attributeID;
    private String label, description;
    
    public Attribute(){}
    
    public Attribute(int attributeID, String label, String description){
        this.attributeID = attributeID;
        this.label = label;
        this.description = description;
    }

    public int getAttributeID() {
        return attributeID;
    }

    public void setAttributeID(int attributeID) {
        this.attributeID = attributeID;
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
    
}
