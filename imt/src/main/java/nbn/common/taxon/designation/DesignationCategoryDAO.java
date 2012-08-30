
package nbn.common.taxon.designation;

import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;
import nbn.common.database.DataAccessObject;

/**
*
* @author	    :- Christopher Johnson
* @date		    :- 01-Oct-2010
* @description	    :-
*/
public class DesignationCategoryDAO extends DataAccessObject {
    private static final boolean RETURN_DESIGNATIONS_BY_DEFAULT = true;
    private DesignationDAO designationDao;
    public DesignationCategoryDAO() throws SQLException {
	super();
	designationDao = new DesignationDAO();
    }

    public List<DesignationCategory> getAllDesignationCategories() throws SQLException {
	return getAllDesignationCategories(RETURN_DESIGNATIONS_BY_DEFAULT);
    }

    public List<DesignationCategory> getAllDesignationCategories(boolean addDesignations) throws SQLException {
	List<DesignationCategory> toReturn = new ArrayList<DesignationCategory>();
	CallableStatement cs = _conn.prepareCall("{call NBNGateway.dbo.usp_getAllDesignationCategories()}");
        ResultSet rs = cs.executeQuery();
	try {
	    while(rs.next())
		toReturn.add(composeDesignationCategory(rs,addDesignations));
	    return toReturn;
	}
	finally {
	    rs.close();
	    cs.close();
	}
    }

    public List<DesignationCategory> getAllExtantDesignationCategories() throws SQLException {
	return getAllExtantDesignationCategories(RETURN_DESIGNATIONS_BY_DEFAULT);
    }

    public List<DesignationCategory> getAllExtantDesignationCategories(boolean addDesignations) throws SQLException {
	List<DesignationCategory> toReturn = new ArrayList<DesignationCategory>();
	CallableStatement cs = _conn.prepareCall("{call NBNGateway.dbo.usp_getAllExtantDesignationCategories()}");
        ResultSet rs = cs.executeQuery();
	try {
	    while(rs.next())
		toReturn.add(composeDesignationCategory(rs,addDesignations));
	    return toReturn;
	}
	finally {
	    rs.close();
	    cs.close();
	}
    }

    public DesignationCategory getDesignationCategory(int designationCategoryKey) throws SQLException {
	return getDesignationCategory(designationCategoryKey,RETURN_DESIGNATIONS_BY_DEFAULT);
    }

    public DesignationCategory getDesignationCategory(int designationCategoryKey, boolean addDesignations) throws SQLException {
	CallableStatement cs = _conn.prepareCall("{call NBNGateway.dbo.usp_getDesignationCategoryByID(?)}");
	cs.setInt(1, designationCategoryKey);
        ResultSet rs = cs.executeQuery();
	try {
	    if(rs.next())
		return composeDesignationCategory(rs,addDesignations);
	    else
		throw new IllegalArgumentException("No Designation category exists with this particular key");
	}
	finally {
	    rs.close();
	    cs.close();
	}
    }

    public DesignationCategory getDesignationCategory(String categoryName) throws SQLException {
	return getDesignationCategory(categoryName,RETURN_DESIGNATIONS_BY_DEFAULT);
    }

    public DesignationCategory getDesignationCategory(String categoryName, boolean addDesignations) throws SQLException {
	CallableStatement cs = _conn.prepareCall("{call NBNGateway.dbo.usp_getDesignationCategoryByName(?)}");
	cs.setString(1,categoryName);
        ResultSet rs = cs.executeQuery();
	try {
	    if(rs.next())
		return composeDesignationCategory(rs,addDesignations);
	    else
		throw new IllegalArgumentException("There is no such designation with this category name");
	}
	finally {
	    rs.close();
	    cs.close();
	}
    }


    /**
     * This method will return a set of designation categories which will only contain the designations
     * which were passed in to this method
     * @param designationsToFind The designations to resolve to DesignationCategories
     * @return A List of Designations Categories which only have the designations of designationsToFind.
     */
    public List<DesignationCategory> getDesignationCategories(List<Designation> designationsToFind) throws SQLException {
	TreeMap<Integer,DesignationCategory> toReturn = new TreeMap<Integer,DesignationCategory>();
	for(Designation designationToAdd : designationsToFind) {
	    DesignationCategory toAddTo;
	    int currDesignationsCategoryKey = designationToAdd.getDesignationCategory().getDesignationCategoryKey(); //get the current designation category key
	    if(!toReturn.containsKey(currDesignationsCategoryKey)) { //doesn't exist yet
		toAddTo = getDesignationCategory(currDesignationsCategoryKey,false); //create the key
		toReturn.put(currDesignationsCategoryKey,toAddTo);
	    }
	    else //already created. lets get it out
		toAddTo = toReturn.get(currDesignationsCategoryKey);
	    toAddTo.addDesignation(designationToAdd);
	}
	return new ArrayList<DesignationCategory>(toReturn.values()); //return the set as a list
    }

    private DesignationCategory composeDesignationCategory(ResultSet rs, boolean addAllDesignations) throws SQLException {
	DesignationCategory toReturn = new DesignationCategory(rs.getInt("designationCategoryKey"),rs.getString("name"));
	toReturn.setDescription(rs.getString("description"));
	if(addAllDesignations)
	    toReturn.addAllDesignations(designationDao.getDesignations(toReturn));
	return toReturn;
    }

    @Override
    public void dispose() {
	super.dispose();
	designationDao.dispose();
    }
}
