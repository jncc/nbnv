
package nbn.common.taxon.designation;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
*
* @author	    :- Christopher Johnson
* @date		    :- 01-Oct-2010
* @description	    :-
*/
public class DesignationCategory {
    private int designationCategoryKey;
    private boolean isRealised;
    private String name,description;
    private List<Designation> designationsWithinCategory;

    DesignationCategory(int designationCategoryKey) {
	this(designationCategoryKey,null,false);
    }

    DesignationCategory(int designationCategoryKey, String name) {
	this(designationCategoryKey,name,true);
    }
    
    private DesignationCategory(int designationCategoryKey, String name, boolean isRealised) {
	this.name = name;
	this.isRealised = isRealised;
	this.designationCategoryKey = designationCategoryKey;
	designationsWithinCategory = new ArrayList<Designation>();
    }

    boolean addDesignation(Designation toAdd) {
	if(!equals(toAdd.getDesignationCategory()))
	    throw new IllegalArgumentException("Attempted to add a designation to a category it does not exist in");
	toAdd.setDesignationCategory(this);
	return designationsWithinCategory.add(toAdd);
    }

    void addAllDesignations(Collection<Designation> designations) {
	for(Designation currDesignation : designations)
	    addDesignation(currDesignation);
    }

    void setDescription(String description) {
	this.description = description;
    }

    /**
     * This method will check to see if this particular designation has been realised.
     *
     * A Designation category has been realised if it has been constructed via the DesignationCategoryDAO
     *
     * If it has not been realised then it has been constructed temporarily for a particular designation
     * @return true if this designation category has been realised
     */
    public boolean isRealised() {
	return isRealised;
    }

    public List<Designation> getDesignationsWithinCategory() {
	return designationsWithinCategory;
    }

    public String getName() {
	return name;
    }

    public String getDescription() {
	return description;
    }

    public int getDesignationCategoryKey() {
	return designationCategoryKey;
    }

    @Override
    public boolean equals(Object o) {
	if(o instanceof DesignationCategory) {
	    DesignationCategory toCompare = (DesignationCategory)o;
	    return toCompare.designationCategoryKey == designationCategoryKey;
	}
	return false;
    }
}
