/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.org.nbn.nbnv.api.model;

/**
 *
 * @author Administrator
 */
public class Designation {
    private int designationID;
    private String name;
    private String label;
    private String code;
    private int designationCategoryID;
    private String description;

    public Designation() {}
    
    public Designation(int designationID, String name, String label, String code, int designationCategoryID, String description) {
        super();
        this.designationID = designationID;
        this.name = name;
        this.label = label;
        this.code = code;
        this.designationCategoryID = designationCategoryID;
        this.description = description;
    }
    /**
     * @return the designationID
     */
    public int getDesignationID() {
        return designationID;
    }

    /**
     * @param designationID the designationID to set
     */
    public void setDesignationID(int designationID) {
        this.designationID = designationID;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
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
     * @return the code
     */
    public String getCode() {
        return code;
    }

    /**
     * @param code the code to set
     */
    public void setCode(String code) {
        this.code = code;
    }

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
