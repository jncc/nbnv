/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.org.nbn.nbnv.api.model;

import java.util.List;

/**
 *
 * @author Administrator
 */
public class DesignationCategory {
    private int designationCategoryID;
    private String label;
    private String description;

    public DesignationCategory(int designationCategoryID, String label, String description) {
        super();
        this.designationCategoryID = designationCategoryID;
        this.label = label;
        this.description = description;
    }
    
    public DesignationCategory() { }
    /**
     * @return the designationCategoryID
     */
    public int getDesignationCategoryID() {
        return designationCategoryID;
    }

    /**
     * @param designationCategoryID the designationCategoryID to set
     */
    public void setDesignationCategoryID(int designationCategoryID) {
        this.designationCategoryID = designationCategoryID;
    }

    /**
     * @return the label
     */
    public String getLabel() {
        return label;
    }

    /**
     * @param label the label to set
     */
    public void setLabel(String label) {
        this.label = label;
    }

    /**
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * @param description the description to set
     */
    public void setDescription(String description) {
        this.description = description;
    }
}
