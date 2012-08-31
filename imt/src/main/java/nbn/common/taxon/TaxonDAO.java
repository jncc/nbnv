/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package nbn.common.taxon;

import nbn.common.feature.Resolution;
import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import nbn.common.database.DataAccessObject;
import nbn.common.dataset.DatasetDAO;
import nbn.common.dataset.TaxonDataset;
import nbn.common.dataset.utils.TaxonDatasetListUtils;
import nbn.common.feature.Feature;
import nbn.common.feature.GridSquare;
import nbn.common.siteboundary.SiteBoundary;
import nbn.common.taxon.designation.Designation;
import nbn.common.taxonreportingcategory.TaxonReportingCategory;
import nbn.common.taxonreportingcategory.TaxonReportingCategoryDAO;
import nbn.common.user.User;
import nbn.common.searching.SearchMatch;

/**
 *
 * @author Administrator
 */
public class TaxonDAO extends DataAccessObject {

    private static final int DEFAULT_MAXIMUM_SEARCH_TAXON_RESULT_SIZE = 10;

    public TaxonDAO() throws SQLException {
        super();
    }

    public List<SearchMatch<Taxon>> searchTaxon(String searchTerm) throws SQLException {
        return searchTaxon(searchTerm, DEFAULT_MAXIMUM_SEARCH_TAXON_RESULT_SIZE);
    }

    public List<SearchMatch<Taxon>> searchTaxon(String searchTerm, int maxResultLength) throws SQLException {
        ArrayList<SearchMatch<Taxon>> toReturn = new ArrayList<SearchMatch<Taxon>>();

        CallableStatement cs = _conn.prepareCall("{call NBNGateway.dbo.usp_wms_searchTaxon(?,?)}");
        cs.setString(1, searchTerm);
        cs.setInt(2, maxResultLength);

        ResultSet rs = cs.executeQuery();
        while (rs.next()) {
            Taxon t = getTaxon(rs.getString("nbnTaxonVersionKey"));

            if (rs.getBoolean("showAuthority") == false)
                t.setAuthority("N/A");

            toReturn.add(new SearchMatch<Taxon>(rs.getString("matchedTerm"), t));
        }
        rs.close();
        cs.close();

        return toReturn;
    }

    public List<Taxon> getTaxonListForDatasetPicker(User user, Feature site, String dataset, int startYear, int endYear) throws SQLException {
        switch(site.getFeatureType()) {
            case GRIDSQUARE :
                GridSquare gridSite = (GridSquare)site;
                return getTaxonListForDatasetPicker(user, gridSite.getGridRef(), dataset, startYear, endYear);
            case SITEBOUNDARY:
                SiteBoundary adminSite = (SiteBoundary)site;
                return getAdminSiteTaxonListForDatasetPicker(user, adminSite.getId(), dataset, startYear, endYear);
            default:
                throw new IllegalArgumentException("Unable to get a taxon list for this type of feature");
        }
    }

    public List<Taxon> getTaxonListForDatasetPicker(User user, String gridRef, String dataset, int startYear, int endYear) throws SQLException {
        List<Taxon> results = new ArrayList<Taxon>();

        CallableStatement cs = this._conn.prepareCall("{call NBNGateway.dbo.usp_wms_getTaxonListForDatasetPicker(?,?,?,?,?)}");
        cs.setInt(1, user.getUserKey());
        cs.setString(2, dataset);
        cs.setInt(3, startYear);
        cs.setInt(4, endYear);
        cs.setString(5, gridRef);

        ResultSet rs = cs.executeQuery();
        try {
            while (rs.next())
                results.add(getTaxon(rs.getInt("taxonVersionKey")));
        } finally {
            rs.close();
            cs.close();
        }
        return results;
    }

    public List<Taxon> getAdminSiteTaxonListForDatasetPicker(User user, int adminSiteKey, String dataset, int startYear, int endYear) throws SQLException {
        List<Taxon> results = new ArrayList<Taxon>();

        CallableStatement cs = this._conn.prepareCall("{call NBNGateway.dbo.usp_wms_getTaxonListForDatasetPicker_AdminSite(?,?,?,?,?)}");
        cs.setInt(1, user.getUserKey());
        cs.setString(2, dataset);
        cs.setInt(3, startYear);
        cs.setInt(4, endYear);
        cs.setInt(5, adminSiteKey);

        ResultSet rs = cs.executeQuery();
        try {
            while (rs.next())
                results.add(getTaxon(rs.getInt("taxonVersionKey")));
        } finally {
            rs.close();
            cs.close();
        }
        return results;
    }

    public List<Taxon> getTaxonListForDesignationPicker(User user, Feature site, Designation desig, String datasets, int startYear, int endYear) throws SQLException {
        switch(site.getFeatureType()) {
            case GRIDSQUARE :
                GridSquare gridSite = (GridSquare)site;
                return getTaxonListForDesignationPicker(user, gridSite.getGridRef(), desig, datasets, startYear, endYear);
            case SITEBOUNDARY:
                SiteBoundary adminSite = (SiteBoundary)site;
                return getAdminSiteTaxonListForDesignationPicker(user, adminSite.getId(), desig, datasets, startYear, endYear);
            default:
                throw new IllegalArgumentException("Unable to get a taxon list for this type of feature");
        }
    }

    public List<Taxon> getTaxonListForDesignationPicker(User user, String gridRef, Designation desig, String datasets, int startYear, int endYear) throws SQLException {
        List<Taxon> results = new ArrayList<Taxon>();

        CallableStatement cs = this._conn.prepareCall("{call NBNGateway.dbo.usp_wms_getTaxonListForDesignationPicker(?,?,?,?,?,?)}");
        cs.setInt(1, user.getUserKey());
        cs.setInt(2, desig.getId());
        cs.setString(3, datasets);
        cs.setInt(4, startYear);
        cs.setInt(5, endYear);
        cs.setString(6, gridRef);

        ResultSet rs = cs.executeQuery();
        try {
            while (rs.next())
                results.add(getTaxon(rs.getInt("taxonVersionKey")));
        } finally {
            rs.close();
            cs.close();
        }
        return results;

    }

    public List<Taxon> getAdminSiteTaxonListForDesignationPicker(User user, int adminSiteKey, Designation desig, String datasets, int startYear, int endYear) throws SQLException {
        List<Taxon> results = new ArrayList<Taxon>();

        CallableStatement cs = this._conn.prepareCall("{call NBNGateway.dbo.usp_wms_getTaxonListForDesignationPicker_AdminSite(?,?,?,?,?,?)}");
        cs.setInt(1, user.getUserKey());
        cs.setInt(2, desig.getId());
        cs.setString(3, datasets);
        cs.setInt(4, startYear);
        cs.setInt(5, endYear);
        cs.setInt(6, adminSiteKey);

        ResultSet rs = cs.executeQuery();
        try {
            while (rs.next())
                results.add(getTaxon(rs.getInt("taxonVersionKey")));
        } finally {
            rs.close();
            cs.close();
        }
        return results;

    }

    public Taxon getTaxon(int taxonVersionKey) throws SQLException, IllegalArgumentException {
        return getTaxon(null,taxonVersionKey); //use the private generic method for getting a taxon
    }

    public Taxon getTaxon(String taxonVersionKey) throws SQLException, IllegalArgumentException {
        return getTaxon(taxonVersionKey, 0); //use the private generic method for getting a taxon
    }

    private Taxon getTaxon(String nbnTaxonVersionKey, int taxonVersionKey) throws SQLException, IllegalArgumentException {
        CallableStatement cs = this._conn.prepareCall("{call NBNGateway.dbo.usp_getSpecies2(?,?)}");
        cs.setString(1, nbnTaxonVersionKey);
        cs.setInt(2, taxonVersionKey);

        ResultSet rs = cs.executeQuery();
        try {
            if (rs.next()) {
                return composeTaxonWithRank(rs);
            }
            else {
                throw new IllegalArgumentException("There are no taxa with this taxon version key {taxonVersionKey = " + taxonVersionKey + "}.  Please check your tvk.");
            }
        } finally {
            rs.close();
            cs.close();
        }
    }

    public List<Taxon> getTaxonAggregateList(Taxon taxon) throws SQLException {
        CallableStatement cs = this._conn.prepareCall("{call NBNGateway.dbo.usp_getSpeciesAggregateList(?)}");
        cs.setInt(1, taxon.getTaxonKey());

        ResultSet rs = cs.executeQuery();
        try {
            List<Taxon> list = new ArrayList<Taxon>();
            while (rs.next())
                list.add(getTaxon(rs.getString("nbnTaxonVersionkey")));
            return list;
        } finally {
            rs.close();
            cs.close();
        }
    }

    public List<TaxonDatasetPair> getTaxonList(User user, List<TaxonDataset> datasetFilter, Designation designation, int startYear, int endYear, TaxonReportingCategory trc, String taxonFilter, boolean scientificOrder) throws SQLException {
        List<TaxonDatasetPair> list = new ArrayList<TaxonDatasetPair>();
        HashMap<String, TaxonDataset> datasets = new HashMap<String, TaxonDataset>();

        CallableStatement cs = this._conn.prepareCall("{call NBNGateway.dbo.usp_getSpeciesList4(?,?,?,?,?,?,?,?)}");
        cs.setInt(1, user.getUserKey());

        if (designation != null) {
            cs.setInt(2, designation.getId());
        } else {
            cs.setInt(2, 0);
        }

        if (trc != null) {
            cs.setInt(3, trc.getTrcKey());
        } else {
            cs.setInt(3, 0);
        }

        cs.setString(4, TaxonDatasetListUtils.getCommaDelimitedDatasetList(datasetFilter));
        cs.setBoolean(5, !scientificOrder);

        if (taxonFilter != null) {
            cs.setString(6, taxonFilter);
        } else {
            cs.setString(6, "");
        }

        cs.setInt(7, startYear);
        cs.setInt(8, endYear);

        DatasetDAO dao = null;
        try {
            dao = new DatasetDAO();

            ResultSet rs = cs.executeQuery();

            while (rs.next()) {
                TaxonDataset td;

                if (datasets.containsKey(rs.getString("datasetKey"))) {
                    td = datasets.get(rs.getString("datasetKey"));
                } else {
                    td = dao.getTaxonDataset(rs.getString("datasetKey"));
                }

                list.add(new TaxonDatasetPair(composeTaxon(rs), td));
            }

        } finally {
            if (dao != null) {
                dao.dispose();
            }
        }

        return list;
    }

    public List<Taxon> getTaxonNavigationList(String taxonInputGroupLevel2Key, boolean hasData, Character letter) throws SQLException {
        List<Taxon> results = new ArrayList<Taxon>();

        CallableStatement cs = this._conn.prepareCall("{call NBNGateway.dbo.usp_wms_getSpeciesListForCategory(?,?,?)}");
        cs.setString(1, taxonInputGroupLevel2Key);
        cs.setBoolean(2, hasData);
        if(letter != null){
            cs.setString(3,letter.toString());
        }else{
            cs.setNull(3,java.sql.Types.CHAR);
        }

        ResultSet rs = cs.executeQuery();
        try {
            while (rs.next()) {
                results.add(getTaxon(rs.getInt("taxonVersionKey")));
            }
        } finally {
            rs.close();
            cs.close();
        }
        return results;
    }

    private static TaxonReportingCategory getTRC(String group) throws SQLException {
        TaxonReportingCategoryDAO dao = null;
        try {
            dao = new TaxonReportingCategoryDAO();
            return dao.getTaxonReportingCategoryByName(group);
        } finally {
            if (dao != null) {
                dao.dispose();
            }
        }
    }

    public static Taxon composeTaxon(ResultSet rs) throws SQLException {
        Taxon toReturn = new Taxon(rs.getInt("taxonVersionKey"), rs.getString("nbnTaxonVersionkey"));
        toReturn.setName(rs.getString("taxonName"));
        toReturn.setAuthority(rs.getString("taxonAuthority"));
        toReturn.setTaxonReportingCategory(getTRC(rs.getString("taxonGroupName")));
        toReturn.setCommonName(rs.getString("commonName"));
        return toReturn;
    }

    public static Taxon composeTaxonWithRank(ResultSet rs) throws SQLException {
        Taxon toReturn = new Taxon(rs.getInt("taxonVersionKey"), rs.getString("nbnTaxonVersionkey"));
        toReturn.setName(rs.getString("taxonName"));
        toReturn.setAuthority(rs.getString("taxonAuthority"));
        toReturn.setTaxonReportingCategory(getTRC(rs.getString("taxonGroupName")));
        toReturn.setCommonName(rs.getString("commonName"));
        toReturn.setTaxonRank(rs.getString("taxonRank"));
        return toReturn;
    }

    public List<TaxonDatasetPair> getGridSquareFilteredTaxonDatasetPairList(User user, List<TaxonDataset> datasetFilter, int startYear, int endYear, Resolution res, String gridref, Designation designation, TaxonReportingCategory trc, String taxonFilter, boolean scientificOrder) throws SQLException {
        List<TaxonDatasetPair> list = new ArrayList<TaxonDatasetPair>();
        HashMap<String, TaxonDataset> datasets = new HashMap<String, TaxonDataset>();

        CallableStatement cs = this._conn.prepareCall("{call NBNGateway.dbo.usp_getGridSquareSpeciesListXML2(?,?,?,?,?,?,?,?,?)}");
        cs.setString(1, gridref);
        cs.setInt(2, user.getUserKey());

        if (designation != null) {
            cs.setInt(3, designation.getId());
        } else {
            cs.setInt(3, 0);
        }

        cs.setInt(4, endYear);
        cs.setInt(5, startYear);
        cs.setBoolean(6, !scientificOrder);

        if (trc != null) {
            cs.setInt(7, trc.getTrcKey());
        } else {
            cs.setInt(7, 0);
        }

        cs.setString(8, TaxonDatasetListUtils.getCommaDelimitedDatasetList(datasetFilter));

        if (taxonFilter != null) {
            cs.setString(9, taxonFilter);
        } else {
            cs.setString(9, "");
        }

        DatasetDAO dao = null;
        try {
            dao = new DatasetDAO();

            ResultSet rs = cs.executeQuery();

            while (rs.next()) {
                TaxonDataset td;

                if (datasets.containsKey(rs.getString("datasetKey"))) {
                    td = datasets.get(rs.getString("datasetKey"));
                } else {
                    td = dao.getTaxonDataset(rs.getString("datasetKey"));
                }

                list.add(new TaxonDatasetPair(composeTaxon(rs), td));
            }

        } finally {
            if (dao != null) {
                dao.dispose();
            }
        }

        return list;
    }

    public List<TaxonDatasetPair> getSiteBoundaryFilteredTaxonDatasetPairList(User user, List<TaxonDataset> datasetFilter, int startYear, int endYear, Resolution res, SiteBoundary site, Designation designation, TaxonReportingCategory trc, String taxonFilter, boolean scientificOrder, boolean overlaps) throws SQLException {
        List<TaxonDatasetPair> list = new ArrayList<TaxonDatasetPair>();
        HashMap<String, TaxonDataset> datasets = new HashMap<String, TaxonDataset>();

        CallableStatement cs = this._conn.prepareCall("NBNGateway.dbo.usp_getSiteSpeciesListInFeatureXML2(?,?,?,?,?,?,?,?,?,?,?)");
        cs.setInt(1, user.getUserKey());

        if (designation != null) {
            cs.setInt(2, designation.getId());
        } else {
            cs.setInt(2, 0);
        }

        cs.setInt(3, endYear);
        cs.setInt(4, startYear);
        cs.setInt(5, site.getId());

        if (trc != null) {
            cs.setInt(6, trc.getTrcKey());
        } else {
            cs.setInt(6, 0);
        }

        cs.setBoolean(7, overlaps);

        if (res != Resolution.Any) {
            cs.setShort(8, (short) res.getResolutionCode());
        } else {
            cs.setShort(8, (short) 1);
        }

        cs.setString(9, TaxonDatasetListUtils.getCommaDelimitedDatasetList(datasetFilter));
        cs.setBoolean(10, !scientificOrder);

        if (taxonFilter != null) {
            cs.setString(11, taxonFilter);
        } else {
            cs.setString(11, "");
        }


        DatasetDAO dao = null;
        try {
            dao = new DatasetDAO();

            ResultSet rs = cs.executeQuery();

            while (rs.next()) {
                TaxonDataset td;

                if (datasets.containsKey(rs.getString("datasetKey"))) {
                    td = datasets.get(rs.getString("datasetKey"));
                } else {
                    td = dao.getTaxonDataset(rs.getString("datasetKey"));
                }

                list.add(new TaxonDatasetPair(composeTaxon(rs), td));
            }

        } finally {
            if (dao != null) {
                dao.dispose();
            }
        }

        return list;
    }

    public List<TaxonDatasetPair> getPolygonFilteredTaxonDatasetPairList(User user, List<TaxonDataset> datasetFilter, int startYear, int endYear, Resolution res, String squares, Designation designation, TaxonReportingCategory trc, String taxonFilter, boolean scientificOrder, boolean overlaps) throws SQLException {
        List<TaxonDatasetPair> list = new ArrayList<TaxonDatasetPair>();
        HashMap<String, TaxonDataset> datasets = new HashMap<String, TaxonDataset>();


        CallableStatement cs = this._conn.prepareCall("{call NBNGateway.dbo.usp_getUserPolySpeciesListXML3(?,?,?,?,?,?,?,?,?,?)}");
        cs.setInt(1, user.getUserKey());
        cs.setString(2, squares);
        cs.setString(3, TaxonDatasetListUtils.getCommaDelimitedDatasetList(datasetFilter));
        cs.setInt(4, startYear);
        cs.setInt(5, endYear);

        if (trc != null) {
            cs.setInt(6, trc.getTrcKey());
        } else {
            cs.setInt(6, 0);
        }

        if (designation != null) {
            cs.setInt(7, designation.getId());
        } else {
            cs.setInt(7, 0);
        }

        if (res != Resolution.Any) {
            cs.setShort(8, (short) res.getResolutionCode());
        } else {
            cs.setShort(8, (short) 1);
        }

        cs.setBoolean(9, overlaps);

        if (taxonFilter != null) {
            cs.setString(10, taxonFilter);
        } else {
            cs.setString(10, "");
        }


        DatasetDAO dao = null;
        try {
            dao = new DatasetDAO();

            ResultSet rs = cs.executeQuery();

            while (rs.next()) {
                TaxonDataset td;

                if (datasets.containsKey(rs.getString("datasetKey"))) {
                    td = datasets.get(rs.getString("datasetKey"));
                } else {
                    td = dao.getTaxonDataset(rs.getString("datasetKey"));
                }

                list.add(new TaxonDatasetPair(composeTaxon(rs), td));
            }

        } finally {
            if (dao != null) {
                dao.dispose();
            }
        }

        return list;
    }

}
