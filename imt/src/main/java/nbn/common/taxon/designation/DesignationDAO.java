/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package nbn.common.taxon.designation;

import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;
import nbn.common.database.DataAccessObject;

/**
 *
 * @author Administrator
 */
public class DesignationDAO extends DataAccessObject {
    public DesignationDAO() throws SQLException {
        super();
    }

    /**
     * This method will iterate through each of the keys to create a list of Designations
     * One designation per input key
     * @param keys A list of keys which will resolve to a
     * @return A list of designations
     * @throws SQLException
     */
    public List<Designation> getDesignations(List<String> keys) throws SQLException {
	TreeSet<Designation> toReturn = new TreeSet<Designation>();
	for(String currKey : keys) {
	    if(!toReturn.add(getDesignation(currKey)))
		throw new IllegalArgumentException("Attempted to add a designation twice");
	}
	return new ArrayList<Designation>(toReturn);
    }

    public List<Designation> getDesignations(DesignationCategory designationCategoryToGetDesignationsOf) throws SQLException {
	List<Designation> toReturn = new ArrayList<Designation>();
	CallableStatement cs = _conn.prepareCall("{call NBNGateway.dbo.usp_getTaxonDesignationsByDesignationCategoryID(?)}");
	cs.setInt(1, designationCategoryToGetDesignationsOf.getDesignationCategoryKey());
        ResultSet rs = cs.executeQuery();
	try {
	    while(rs.next())
		toReturn.add(composeDesignation(rs));
	    return toReturn;
	}
	finally {
	    rs.close();
	    cs.close();
	}
    }

    public List<Designation> getExtantDesignations(DesignationCategory designationCategoryToGetDesignationsOf) throws SQLException {
	List<Designation> toReturn = new ArrayList<Designation>();
	CallableStatement cs = _conn.prepareCall("{call NBNGateway.dbo.usp_getExtantTaxonDesignationsByDesignationCategoryID(?)}");
	cs.setInt(1, designationCategoryToGetDesignationsOf.getDesignationCategoryKey());
        ResultSet rs = cs.executeQuery();
	try {
	    while(rs.next())
		toReturn.add(composeDesignation(rs));
	    return toReturn;
	}
	finally {
	    rs.close();
	    cs.close();
	}
    }

    public List<Designation> getExtantDesignationsWithRecords(DesignationCategory designationCategoryToGetDesignationsOf) throws SQLException {
	List<Designation> toReturn = new ArrayList<Designation>();
	CallableStatement cs = _conn.prepareCall("{call NBNGateway.dbo.usp_wms_getExtantTaxonDesignationsWithRecordsByDesignationCategoryID(?)}");
	cs.setInt(1, designationCategoryToGetDesignationsOf.getDesignationCategoryKey());
        ResultSet rs = cs.executeQuery();
	try {
	    while(rs.next())
		toReturn.add(composeDesignation(rs));
	    return toReturn;
	}
	finally {
	    rs.close();
	    cs.close();
	}
    }

    public List<Designation> getSiteReportDesignations() throws SQLException {
	List<Designation> toReturn = new ArrayList<Designation>();
	CallableStatement cs = _conn.prepareCall("{call NBNGateway.dbo.usp_getSiteReportDesignations()}");
        ResultSet rs = cs.executeQuery();
	try {
	    while(rs.next())
		toReturn.add(composeDesignation(rs));
	    return toReturn;
	}
	finally {
	    rs.close();
	    cs.close();
	}
    }

    public Designation getDesignation(int designationID) throws SQLException {
	CallableStatement cs = this._conn.prepareCall("{call NBNGateway.dbo.usp_getTaxonDesignationByID(?)}");
        cs.setInt(1, designationID);
        ResultSet rs = cs.executeQuery();
	try {
	    if(rs.next())
		return composeDesignation(rs);
	    else
		throw new IllegalArgumentException("Attempted to find Designation by its id which does not exist");
	}
	finally {
	    rs.close();
	    cs.close();
	}
    }

    public Designation getDesignation(String key) throws SQLException {
        CallableStatement cs = this._conn.prepareCall("{call NBNGateway.dbo.usp_getTaxonDesignation(?)}");
        cs.setString(1, key);
        ResultSet rs = cs.executeQuery();
	try {
	    if(rs.next())
		return composeDesignation(rs);
	    else
		throw new IllegalArgumentException("Attempted to find a key Designation which does not exist");
	}
	finally {
	    rs.close();
	    cs.close();
	}
    }

    private Designation composeDesignation(ResultSet rs) throws SQLException {
	return new Designation(new DesignationCategory(rs.getInt("designationCategoryKey")),rs.getInt("TaxonDesignationKey"), rs.getString("TaxonDesignationName"), rs.getString("xmlEnumValue"),rs.getString("abbreviation"),rs.getString("description"));
    }
}
