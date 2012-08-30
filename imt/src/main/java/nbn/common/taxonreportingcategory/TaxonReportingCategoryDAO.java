/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package nbn.common.taxonreportingcategory;

import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import nbn.common.dataset.DatasetDAO;
import nbn.common.dataset.TaxonDataset;
import nbn.common.dataset.utils.TaxonDatasetListUtils;
import nbn.common.siteboundary.SiteBoundary;
import nbn.common.feature.Resolution;
import nbn.common.taxon.designation.Designation;
import nbn.common.user.User;
import nbn.common.database.DataAccessObject;

/**
 *
 * @author Administrator
 */
public class TaxonReportingCategoryDAO extends DataAccessObject {
    static private HashMap<String, TaxonReportingCategory> _cacheTRCByName = new HashMap<String, TaxonReportingCategory>();;

    public TaxonReportingCategoryDAO() throws SQLException {
        super();
        //this._cacheTRCByName = new HashMap<String, TaxonReportingCategory>();
    }

    public TaxonReportingCategory getTaxonReportingCategory(String nbnKey) throws SQLException, IllegalArgumentException {
        CallableStatement cs = this._conn.prepareCall("{call NBNGateway.dbo.usp_getSpeciesGroup(?,?)}");
        cs.setString(1, nbnKey);
        cs.setInt(2, 0); // 0 as we are not requesting by internal ID
        ResultSet rs = cs.executeQuery();

        int key = -1;
        String nbnTGKey = "";
        String name = "";
	try {
	    if (rs.next()) { // if a record is returned
		key = rs.getInt("taxonGroupKey");
		nbnTGKey = rs.getString("nbnTaxonGroupKey");
		name = rs.getString("taxonGroupName");
		return new TaxonReportingCategory(key, nbnTGKey, name);
	    }
	    else
		throw new IllegalArgumentException("This taxon reporting category was not found {Key = " + nbnKey + "}.");
	}
	finally {
	    rs.close();
	    cs.close();
	}
    }

    public TaxonReportingCategory getTaxonReportingCategoryByName(String name) throws SQLException, IllegalArgumentException {
        if (this._cacheTRCByName.containsKey(name))
            return this._cacheTRCByName.get(name);

        CallableStatement cs = this._conn.prepareCall("{call NBNGateway.dbo.usp_getSpeciesGroupByName(?)}");
        cs.setString(1, name);
        ResultSet rs = cs.executeQuery();

        int key = -1;
        String nbnTGKey = "";
        String tgname = "";
	try {
	    if (rs.next()) { // if a record is returned
		key = rs.getInt("taxonGroupKey");
		nbnTGKey = rs.getString("nbnTaxonGroupKey");
		tgname = rs.getString("taxonGroupName");
		TaxonReportingCategory trc = new TaxonReportingCategory(key, nbnTGKey, tgname);
                this._cacheTRCByName.put(name, trc);
                return trc;
	    }
	    else
		throw new IllegalArgumentException("This taxon reporting category was not found {name = " + name + "}.");
	}
	finally {
	    rs.close();
	    cs.close();
	}
    }

    public List<TaxonReportingCategoryDatasetPair> getTaxonReportingCategoryDatasetList(Designation designation) throws SQLException {
        List<TaxonReportingCategoryDatasetPair> list = new ArrayList<TaxonReportingCategoryDatasetPair>();
        HashMap<String, TaxonDataset> datasets = new HashMap<String, TaxonDataset>();

        DatasetDAO dao = null;

        try {
            dao = new DatasetDAO();
            CallableStatement cs = this._conn.prepareCall("{call NBNGateway.dbo.usp_getSpeciesGroupList(?)}");

            if (designation != null) {
                cs.setInt(1, designation.getId());
            } else {
                cs.setInt(1, 0);
            }

            ResultSet rs = cs.executeQuery();

            while (rs.next()) {
                TaxonDataset td;

                if (datasets.containsKey(rs.getString("datasetKey")))
                    td = datasets.get(rs.getString("datasetKey"));
                else
                    td = dao.getTaxonDataset(rs.getString("datasetKey"));

                list.add(new TaxonReportingCategoryDatasetPair(new TaxonReportingCategory(rs.getInt("taxonGroupKey"), rs.getString("nbnTaxonGroupKey"), rs.getString("taxonGroupName")), td));
            }

            return list;
        } finally {
            if (dao != null)
                dao.dispose();
        }
    }

    public List<TaxonReportingCategoryDatasetPair> getGridSquareFilteredTaxonReportingCategoryDatasetList(User user, List<TaxonDataset> querylist, int startYear, int endYear, Resolution res, String gridref, Designation designation) throws SQLException {
        List<TaxonReportingCategoryDatasetPair> list = new ArrayList<TaxonReportingCategoryDatasetPair>();
        HashMap<String, TaxonDataset> datasets = new HashMap<String, TaxonDataset>();

        DatasetDAO dao = null;

        try {
            dao = new DatasetDAO();


            CallableStatement cs = this._conn.prepareCall("{call NBNGateway.dbo.usp_getGridSquareSpeciesGroupXML(?,?,?,?,?,?)}");
            cs.setString(1, gridref);
            cs.setInt(2, user.getUserKey());

            if (designation != null)
                cs.setInt(3, designation.getId());
            else
                cs.setInt(3, 0);

            cs.setInt(4, endYear);
            cs.setInt(5, startYear);
            cs.setString(6, TaxonDatasetListUtils.getCommaDelimitedDatasetList(querylist));

            ResultSet rs = cs.executeQuery();

            while (rs.next()) {
                TaxonDataset td;

                if (datasets.containsKey(rs.getString("datasetKey"))) {
                    td = datasets.get(rs.getString("datasetKey"));
                } else {
                    td = dao.getTaxonDataset(rs.getString("datasetKey"));
                }

                list.add(new TaxonReportingCategoryDatasetPair(new TaxonReportingCategory(rs.getInt("taxonGroupKey"), rs.getString("nbnTaxonGroupKey"), rs.getString("taxonGroupName")), td));
            }

            return list;
        } finally {
            if (dao != null) {
                dao.dispose();
            }
        }
    }

    public List<TaxonReportingCategoryDatasetPair> getSiteBoundaryFilteredTaxonReportingCategoryDatasetList(User user, List<TaxonDataset> querylist, int startYear, int endYear, Resolution res, SiteBoundary site, Designation designation, boolean overlap) throws SQLException {
        List<TaxonReportingCategoryDatasetPair> list = new ArrayList<TaxonReportingCategoryDatasetPair>();
        HashMap<String, TaxonDataset> datasets = new HashMap<String, TaxonDataset>();

        DatasetDAO dao = null;

        try {
            dao = new DatasetDAO();

            CallableStatement cs = this._conn.prepareCall("{call NBNGateway.dbo.usp_getSiteSpeciesGroupsWithFeatureXML(?,?,?,?,?,?,?,?)}");
            cs.setInt(1, user.getUserKey());

            if (designation != null)
                cs.setInt(2, designation.getId());
            else
                cs.setInt(2, 0);

            cs.setInt(3, endYear);
            cs.setInt(4, startYear);
            cs.setInt(5, site.getId());

            if (res != null && res != Resolution.Any)
                cs.setShort(6, (short) res.getResolutionCode());
            else
                cs.setShort(6, (short) 1);

            cs.setBoolean(7, overlap);
            cs.setString(8, TaxonDatasetListUtils.getCommaDelimitedDatasetList(querylist));

            ResultSet rs = cs.executeQuery();

            while (rs.next()) {
                TaxonDataset td;

                if (datasets.containsKey(rs.getString("datasetKey"))) {
                    td = datasets.get(rs.getString("datasetKey"));
                } else {
                    td = dao.getTaxonDataset(rs.getString("datasetKey"));
                }

                list.add(new TaxonReportingCategoryDatasetPair(new TaxonReportingCategory(rs.getInt("taxonGroupKey"), rs.getString("nbnTaxonGroupKey"), rs.getString("taxonGroupName")), td));
            }

            return list;
        } finally {
            if (dao != null) {
                dao.dispose();
            }
        }
    }

    public List<TaxonReportingCategoryDatasetPair> getPolygonFilteredTaxonReportingCategoryDatasetList(User user, List<TaxonDataset> querylist, int startYear, int endYear, Resolution res, String squares, Designation designation, boolean overlap) throws SQLException {
        List<TaxonReportingCategoryDatasetPair> list = new ArrayList<TaxonReportingCategoryDatasetPair>();
        HashMap<String, TaxonDataset> datasets = new HashMap<String, TaxonDataset>();

        DatasetDAO dao = null;

        try {
            dao = new DatasetDAO();

            CallableStatement cs = this._conn.prepareCall("{call NBNGateway.dbo.usp_getUserPolySpeciesGroupsXML(?,?,?,?,?,?,?,?)}");
            cs.setInt(1, user.getUserKey());
            cs.setString(2, squares);
            cs.setString(3, TaxonDatasetListUtils.getCommaDelimitedDatasetList(querylist));
            cs.setInt(4, startYear);
            cs.setInt(5, endYear);

            if (designation != null)
                cs.setInt(6, designation.getId());
            else
                cs.setInt(6, 0);

            if (res != null && res != Resolution.Any)
                cs.setShort(7, (short) res.getResolutionCode());
            else
                cs.setShort(7, (short) 1);

            cs.setBoolean(8, overlap);

            ResultSet rs = cs.executeQuery();

            while (rs.next()) {
                TaxonDataset td;

                if (datasets.containsKey(rs.getString("datasetKey"))) {
                    td = datasets.get(rs.getString("datasetKey"));
                } else {
                    td = dao.getTaxonDataset(rs.getString("datasetKey"));
                }

                list.add(new TaxonReportingCategoryDatasetPair(new TaxonReportingCategory(rs.getInt("taxonGroupKey"), rs.getString("nbnTaxonGroupKey"), rs.getString("taxonGroupName")), td));
            }

            return list;
        } finally {
            if (dao != null) {
                dao.dispose();
            }
        }
    }
}
