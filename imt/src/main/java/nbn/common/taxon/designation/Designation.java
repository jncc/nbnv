/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package nbn.common.taxon.designation;

/**
 *
 * @author Administrator
 */
public class Designation {
    private int id;
    private DesignationCategory designationCategory;
    private String name;
    private String key;
    private String abbreviation;
    private String description;


    Designation(DesignationCategory designationCategory, int ID, String name, String key, String abbreviation, String description) {
        this.designationCategory = designationCategory;
	this.id = ID;
        this.name = name;
        this.key = key;
	this.abbreviation = abbreviation;
	this.description = description;
    }

    void setDesignationCategory(DesignationCategory designationCategory) {
	this.designationCategory = designationCategory;
    }

    public DesignationCategory getDesignationCategory() {
	return designationCategory;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getKey() {
        return key;
    }

    public String getAbbreviation() {
	return abbreviation;
    }

    public String getDescription() {
	return description;
    }

    @Override
    public boolean equals(Object o) {
	if(o instanceof Designation) {
	    Designation toCompare = (Designation)o;
	    return toCompare.id == id;
	}
	return false;
    }
}
