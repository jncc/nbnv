/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package nbn.common.siteboundary;

import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import nbn.common.database.DataAccessObject;

/**
 *
 * @author Administrator
 */
public class SiteBoundaryDAO extends DataAccessObject {
    public SiteBoundaryDAO() throws SQLException {
        super();
    }

    public SiteBoundary getSiteBoundary(String providerKey, String siteKey) throws SQLException{
        CallableStatement cs = this._conn.prepareCall("{call NBNGateway.dbo.usp_getAdminSite(?,?,?)}");
	cs.setInt(1, 0); // Not calling by internal ID
	cs.setString(2, siteKey);
        cs.setString(3, providerKey);
	ResultSet rs = cs.executeQuery();
	try {
	    if (rs.next())
		return composeSiteBoundary(rs);
	    else
		throw new IllegalArgumentException("This site was not found {provider = " + providerKey + ", site = " + siteKey + "}.  It is possible that it doesn't yet exist on the NBN Gateway.");
	}
	finally {
	    rs.close();
	    cs.close();
	}
    }

    public SiteBoundary getSiteBoundary(int internalID) throws SQLException {
        CallableStatement cs = this._conn.prepareCall("{call NBNGateway.dbo.usp_getAdminSite(?,?,?)}");
	cs.setInt(1, internalID); // Calling by internal ID
	cs.setString(2, "");
        cs.setString(3, "");
	ResultSet rs = cs.executeQuery();
	try {
	    if (rs.next())
		return composeSiteBoundary(rs);
	    else
		throw new IllegalArgumentException("This site was not found {internalID = " + String.valueOf(internalID) + "}.  It is possible that it doesn't yet exist on the NBN Gateway.");
	}
	finally {
	    rs.close();
	    cs.close();
	}
    }

    public List<SiteBoundary> getSiteBoundariesByType(String type) throws SQLException {
        List<SiteBoundary> list = new LinkedList<SiteBoundary>();
        CallableStatement cs = this._conn.prepareCall("{call NBNGateway.dbo.usp_getAdminSiteListXML2(?)}");
	cs.setString(1, type);
	ResultSet rs = cs.executeQuery();
	try {
	    while(rs.next())
		list.add(composeSiteBoundary(rs)); //populate list

	    if(list.isEmpty())
		throw new IllegalArgumentException("There are no sites of this type {type = " + type + "}.  Please check your type code.");
	    return list;
	}
	finally {
	    rs.close();
	    cs.close();
	}
    }

    private SiteBoundary composeSiteBoundary(ResultSet rs)  throws SQLException {
	String providerKey = rs.getString("datasetKey");
	String siteKey = rs.getString("providerKey");
	String name = rs.getString("adminSiteName");
	String type = rs.getString("siteTypeCode");
        String layer = rs.getString("layerName");
        int id = rs.getInt("adminSiteKey");

	return new SiteBoundary(id, providerKey, siteKey, name, type, layer);
    }
}
