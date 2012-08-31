/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package nbn.common.taxoninputcategory;

import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.TreeSet;
import nbn.common.database.DataAccessObject;

public class TaxonInputCategoryDAO extends DataAccessObject {
    static private HashMap<String, TaxonInputCategory> _cacheTICByName = new HashMap<String, TaxonInputCategory>();;

    public TaxonInputCategoryDAO() throws SQLException {
        super();
        //this._cacheTICByName = new HashMap<String, TaxonReportingCategory>();
    }

    /**
     * @param hasData: true = categories must have at least one species observation,
     *                 false = all categories returned irrespective of underlying species data
     * @param useCommonNames: indicates whether the species count for the category should be based on common or scientific names
     * @return List<TaxonInputCategory>
     * @throws SQLException
     * @throws IllegalArgumentException
     */
    public List<TaxonInputCategory> getTaxonInputCategoryLevel1(boolean hasData, boolean useCommonNames) throws SQLException, IllegalArgumentException {
        List<TaxonInputCategory> result = new ArrayList<TaxonInputCategory>();
        CallableStatement cs = this._conn.prepareCall("{call NBNGateway.dbo.usp_getSpeciesGroupLevel1(?,?)}");
        cs.setBoolean(1, hasData);
        cs.setBoolean(2, useCommonNames);
        ResultSet rs = cs.executeQuery();
        String key = "";
        String name = "";
        int speciesCount = 0;
        boolean hasLevel2 = false;
	try {
	    while (rs.next()) { // if a record is returned
		key = rs.getString("nbnTaxonGroupKey");
		name = rs.getString("taxonGroupName");
		speciesCount = rs.getInt("speciesCount");
                hasLevel2 = rs.getBoolean("hasLevel2");
		result.add(new TaxonInputCategory(key,name,speciesCount,!useCommonNames,true,hasLevel2,false));
	    }
            return result;
	}
	finally {
	    rs.close();
	    cs.close();
	}
    }

    /**
     * @param level1Key: the level 1 TaxonInputCategory key that we want level 2 TaxonInputCategories for
     * @param hasData: true = categories must have at least one species observation,
     *                 false = all categories returned irrespective of underlying species data
     * @param useCommonNames: indicates whether the species count for the category should be based on common or scientific names
     * @return List<TaxonInputCategory>
     * @throws SQLException
     * @throws IllegalArgumentException
     */
    public List<TaxonInputCategory> getTaxonInputCategoryLevel2(String level1Key, boolean hasData, boolean useCommonNames) throws SQLException, IllegalArgumentException {
        List<TaxonInputCategory> result = new ArrayList<TaxonInputCategory>();
        CallableStatement cs = this._conn.prepareCall("{call NBNGateway.dbo.usp_getSpeciesGroupLevel2(?,?,?)}");
        cs.setBoolean(1, hasData);
        cs.setString(2, level1Key);
        cs.setBoolean(3, useCommonNames);
        ResultSet rs = cs.executeQuery();
        String key = "";
        String name = "";
        int speciesCount = 0;
	try {
	    while (rs.next()) { // if a record is returned
		key = rs.getString("nbnTaxonGroupKey");
		name = rs.getString("taxonGroupName");
		speciesCount = rs.getInt("speciesCount");
		result.add(new TaxonInputCategory(key,name,speciesCount,!useCommonNames,false,false,true));
	    }
            return result;
	}
	finally {
	    rs.close();
	    cs.close();
	}
    }
    /**
     * Gets the distinct first letters of the species list for the species category in alphabetical order
     * @param categoryKey the level 2 category key we want a species list for
     * @param hasData only return species that have occurrence records
     */
    public TreeSet<Character> getTaxonInputCategorySpeciesLetters(String categoryKey, boolean hasData) throws SQLException {
        TreeSet<Character> result = new TreeSet<Character>();
        CallableStatement cs = this._conn.prepareCall("{call NBNGateway.dbo.usp_wms_getSpeciesListForCategory(?,?,?)}");
//        cs.setString("level2Key", categoryKey);
//        cs.setBoolean("hasData", hasData);
//        cs.setNull("letter", java.sql.Types.CHAR);
        cs.setString(1, categoryKey);
        cs.setBoolean(2, hasData);
        cs.setNull(3, java.sql.Types.CHAR);//the 'letter' argument is set to null to force all letters to come back
        ResultSet rs = cs.executeQuery();
        try {
            while (rs.next()) { // if a record is returned
                result.add(new Character(rs.getString("taxonName").charAt(0)));
            }
            return result;
        } finally {
            rs.close();
            cs.close();
        }
    }
}
