/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package nbn.common.dataset;

import nbn.common.searching.SearchMatch;
import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import nbn.common.database.DataAccessObject;
import nbn.common.dataset.privileges.BlurLevel;
import nbn.common.dataset.privileges.Privileges;
import nbn.common.siteboundary.SiteBoundary;
import nbn.common.feature.Resolution;
import nbn.common.taxon.Taxon;
import nbn.common.taxon.TaxonDAO;
import nbn.common.taxon.designation.Designation;
import nbn.common.taxonreportingcategory.TaxonReportingCategory;
import nbn.common.taxonreportingcategory.TaxonReportingCategoryDAO;
import nbn.common.user.User;
import nbn.common.organisation.OrganisationDAO;

/**
 *
 * @author Administrator
 */
public class DatasetDAO extends DataAccessObject {

    private static final int DEFAULT_MAXIMUM_SEARCH_DATASET_RESULT_SIZE = 10;
    private OrganisationDAO datasetProviderDAO = null;

    public DatasetDAO() throws SQLException {
        super();
        datasetProviderDAO = new OrganisationDAO();
    }

    public List<SearchMatch<TaxonDataset>> searchTaxonDataset(String searchTerm) throws SQLException {
        return searchTaxonDataset(searchTerm, DEFAULT_MAXIMUM_SEARCH_DATASET_RESULT_SIZE);
    }

    public List<SearchMatch<TaxonDataset>> searchTaxonDataset(String searchTerm, int maxResultLength) throws SQLException {
        ArrayList<SearchMatch<TaxonDataset>> toReturn = new ArrayList<SearchMatch<TaxonDataset>>();

        CallableStatement cs = _conn.prepareCall("{call NBNGateway.dbo.usp_wms_searchTaxonDataset(?,?)}");
        cs.setString(1, searchTerm);
        cs.setInt(2, maxResultLength);

        ResultSet rs = cs.executeQuery();
        while (rs.next()) {
            toReturn.add(new SearchMatch<TaxonDataset>(rs.getString("matchedTerm"), getTaxonDataset(rs.getString("datasetKey"))));
        }
        rs.close();
        cs.close();

        return toReturn;
    }

    public List<HabitatDataset> getHabitatDatasets(List<String> requestLayers) throws SQLException {
        List<HabitatDataset> toReturn = new LinkedList<HabitatDataset>();
        for (String currID : requestLayers) {
            toReturn.add(getHabitatDataset(currID));
        }
        return toReturn;
    }

    public List<SiteBoundaryDataset> getSiteBoundaryDatasets(List<String> requestLayers) throws SQLException {
        List<SiteBoundaryDataset> toReturn = new LinkedList<SiteBoundaryDataset>();
        for (String currID : requestLayers) {
            toReturn.add(getSiteBoundaryDataset(currID));
        }
        return toReturn;
    }

    public List<SiteBoundaryDataset> getAllSiteBoundaryDatasets() throws SQLException {
        CallableStatement cs = _conn.prepareCall("{call NBNGateway.dbo.usp_getAllSiteBoundaryDatasets()}");
        ResultSet rs = cs.executeQuery();

        try {
            LinkedList<SiteBoundaryDataset> toReturn = new LinkedList<SiteBoundaryDataset>();
            while (rs.next()) {
                toReturn.add(composeSiteBoundaryDataset(rs.getString("datasetKey"), rs));
            }

            return toReturn;
        } finally {
            rs.close();
            cs.close();
        }
    }

    public SiteBoundaryDataset getSiteBoundaryDataset(String datasetKey) throws SQLException {
        CallableStatement cs = _conn.prepareCall("{call NBNGateway.dbo.usp_getSiteBoundaryDataset(?)}");
        cs.setString(1, datasetKey);
        ResultSet rs = cs.executeQuery();
        try {
            if (rs.next()) {
                return composeSiteBoundaryDataset(datasetKey, rs);
            } else {
                throw new IllegalArgumentException("The dataset " + datasetKey + " was requested but does not exist");
            }
        } finally {
            rs.close();
            cs.close();
        }
    }

    public List<HabitatDataset> getAllHabitatDatasets() throws SQLException {
        CallableStatement cs = _conn.prepareCall("{call NBNGateway.dbo.usp_getAllHabitatDatasets()}");
        ResultSet rs = cs.executeQuery();

        List<HabitatDataset> result = new LinkedList<HabitatDataset>();
        while (rs.next()) {
            result.add(composeHabitatDataset(rs.getString("datasetKey"), rs));
        }

        rs.close();
        cs.close();

        return result;
    }

    public Dataset getDataset(String datasetKey) throws SQLException {
        CallableStatement cs = _conn.prepareCall("{call NBNGateway.dbo.usp_getBasicDataset2(?)}");
        cs.setString(1, datasetKey);
        ResultSet rs = cs.executeQuery();
        try {
            if (rs.next()) {
                return composeDataset(datasetKey, rs);
            } else {
                throw new IllegalArgumentException("The dataset " + datasetKey + " was requested but does not exist");
            }
        } finally {
            rs.close();
            cs.close();
        }
    }

    public List<Dataset> getDatasets(List<String> datasetKeys) throws SQLException {
        List<Dataset> toReturn = new ArrayList<Dataset>();
        for (String datasetKey : datasetKeys) {
            toReturn.add(getDataset(datasetKey));
        }
        return toReturn;
    }

    public HabitatDataset getHabitatDataset(String datasetKey) throws SQLException {
        CallableStatement cs = _conn.prepareCall("{call NBNGateway.dbo.usp_getHabitatDataset2(?)}");
        cs.setString(1, datasetKey);
        ResultSet rs = cs.executeQuery();
        try {
            if (rs.next()) {
                return composeHabitatDataset(datasetKey, rs);
            } else {
                throw new IllegalArgumentException("The dataset " + datasetKey + " was requested but does not exist");
            }
        } finally {
            rs.close();
            cs.close();
        }
    }

    public TaxonDataset getTaxonDataset(String datasetKey) throws SQLException {
        CallableStatement cs = _conn.prepareCall("{call NBNGateway.dbo.usp_getTaxonDataset(?)}");
        cs.setString(1, datasetKey);
        ResultSet rs = cs.executeQuery();
        try {
            if (rs.next()) {
                return composeTaxonDataset(datasetKey, rs);
            } else {
                throw new IllegalArgumentException("The dataset " + datasetKey + " was requested but does not exist");
            }
        } finally {
            rs.close();
            cs.close();
        }
    }

    public List<TaxonDataset> getTaxonDatasetListByProvider(int providerKey) throws SQLException {
        List<TaxonDataset> list = new ArrayList<TaxonDataset>();

        CallableStatement cs = this._conn.prepareCall("{call NBNGateway.dbo.usp_getDatasetList2(?,?,?,?,?)}");
        cs.setString(1, "T");// Dataset type: use T for Taxon
        cs.setInt(2, providerKey);// Dataset provider: use 0 for 'don't filter by organisation'
        cs.setInt(3, 1);// public access: use 1 for 'select all datasets whether public or not' (use 2 for public only datasets)
        cs.setInt(4, 0);// Order by: 0=datasetTitle,organisation,dateUploaded; 1=organisation,datasetTitle,dateUploaded; 2=dateUploaded,datasetTitle,organisation
        cs.setInt(5, 0);//Contributor: use 0 for 'don't filter by contributor'

        ResultSet rs = cs.executeQuery();

        while (rs.next()) {
            list.add(getTaxonDataset(rs.getString("datasetKey")));
        }

        return list;
    }

    public List<TaxonDataset> getAllDownloadableTaxonDatasets(User user, Resolution resolution, TaxonReportingCategory trc) throws SQLException {
        List<TaxonDataset> list = new ArrayList<TaxonDataset>();

        CallableStatement cs = this._conn.prepareCall("{call NBNGateway.dbo.usp_getUserTaxonDatasetAccessList(?,?,?,?,?,?,?)}");
        cs.setInt(1, user.getUserKey());
        cs.setBoolean(2, true);
        cs.setShort(3, (short) 3);//This is the user's role - we want member or administrator = ALL

        if (trc != null) {
            cs.setInt(4, trc.getTrcKey());
        } else {
            cs.setInt(4, 0);
        }

        cs.setString(5, ""); // String taxon group key - not used here
        cs.setInt(6, 0); // organisation filter parameter - not used here

        if (resolution != Resolution.Any) {
            cs.setShort(7, (short) resolution.getResolutionCode());
        } else {
            cs.setShort(7, (short) 1);
        }

        ResultSet rs = cs.executeQuery();

        while (rs.next()) {
            list.add(getTaxonDataset(rs.getString("datasetKey")));
        }

        return list;
    }


    public List<TaxonDataset> getAllNonViewableTaxonDatasetsByTaxonWithAggregates(User user, Taxon taxon) throws SQLException {
        return new ArrayList<TaxonDataset>(getAllNonViewableTaxonDatasetsByTaxonWithAggregatesInContext(user, taxon));
    }

    public List<TaxonDataset> getAllViewableTaxonDatasetsByTaxonWithAggregates(User user, Taxon taxon) throws SQLException {
        return new ArrayList<TaxonDataset>(getAllViewableTaxonDatasetsByTaxonWithAggregatesInContext(user, taxon));
    }

    public List<TaxonDatasetContext<Taxon>> getAllViewableTaxonDatasetsByTaxonWithAggregatesInContext(User user, Taxon taxon) throws SQLException {
        List<TaxonDatasetContext<Taxon>> list = new ArrayList<TaxonDatasetContext<Taxon>>();
        CallableStatement cs = _conn.prepareCall("{call NBNGateway.dbo.usp_wms_getViewableTaxonDatasetListByTaxon(?,?)}");
        cs.setInt(1, taxon.getTaxonKey());
        cs.setInt(2, user.getUserKey());
        ResultSet rs = cs.executeQuery();
        try {
            while (rs.next()) {
                list.add(composeTaxonDatasetContextCount(rs.getString("datasetKey"), rs, taxon));
            }
            return list;
        } finally {
            rs.close();
            cs.close();
        }
    }

    public List<TaxonDatasetContext<Taxon>> getAllNonViewableTaxonDatasetsByTaxonWithAggregatesInContext(User user, Taxon taxon) throws SQLException {
        List<TaxonDatasetContext<Taxon>> list = new ArrayList<TaxonDatasetContext<Taxon>>();
        CallableStatement cs = _conn.prepareCall("{call NBNGateway.dbo.usp_wms_getNonViewableTaxonDatasetListByTaxon(?,?)}");
        cs.setInt(1, taxon.getTaxonKey());
        cs.setInt(2, user.getUserKey());

        ResultSet rs = cs.executeQuery();
        try {
            while (rs.next()) {
                list.add(composeTaxonDatasetContextCount(rs.getString("datasetKey"), rs, taxon));
            }
            return list;
        } finally {
            rs.close();
            cs.close();
        }
    }

    public List<TaxonDataset> getAllViewableTaxonDatasetsByDesignation(User user, Designation desig) throws SQLException {
        List<TaxonDataset> allTaxonDatasetsForDesignation = getTaxonDatasetListByDesignation(desig);
        List<TaxonDataset> toReturn = new ArrayList<TaxonDataset>();
        for (TaxonDataset taxonDataset : allTaxonDatasetsForDesignation) {
            Privileges privileges = taxonDataset.getPrivileges(user);
            if (privileges.getBlurLevel() != BlurLevel.NO_ACCESS) {
                toReturn.add(taxonDataset);
            }
        }
        return toReturn;
    }

    public List<TaxonDataset> getAllViewableTaxonDatasetsByExtantSpeciesDesignation(User user, Designation desig) throws SQLException {
        List<TaxonDataset> allTaxonDatasetsByExtantSpeciesDesignation = getTaxonDatasetListByExtantSpeciesDesignation(desig);
        List<TaxonDataset> toReturn = new ArrayList<TaxonDataset>();
        for (TaxonDataset taxonDataset : allTaxonDatasetsByExtantSpeciesDesignation) {
            Privileges privileges = taxonDataset.getPrivileges(user);
            if (privileges.getBlurLevel() != BlurLevel.NO_ACCESS) {
                toReturn.add(taxonDataset);
            }
        }
        return toReturn;
    }

    public List<TaxonDataset> getAllNonViewableTaxonDatasetsByExtantSpeciesDesignation(User user, Designation desig) throws SQLException {
        List<TaxonDataset> allTaxonDatasetsByExtantSpeciesDesignation = getTaxonDatasetListByExtantSpeciesDesignation(desig);
        List<TaxonDataset> toReturn = new ArrayList<TaxonDataset>();
        for (TaxonDataset taxonDataset : allTaxonDatasetsByExtantSpeciesDesignation) {
            Privileges privileges = taxonDataset.getPrivileges(user);
            if (privileges.getBlurLevel() == BlurLevel.NO_ACCESS) {
                toReturn.add(taxonDataset);
            }
        }
        return toReturn;
    }

    /**
     * To exist in this list a TaxonDataset must have at least one species that
     * currently has the designation applied (ie endDate of the designation for
     * this species is NULL)
     */
    public List<TaxonDataset> getTaxonDatasetListByExtantSpeciesDesignation(Designation desig) throws SQLException {
        LinkedList<TaxonDataset> list = new LinkedList<TaxonDataset>();

        CallableStatement cs = _conn.prepareCall("{call NBNGateway.dbo.usp_wms_getTaxonDatasetListByExtantSpeciesDesignation(?)}");
        cs.setInt(1, desig.getId());

        ResultSet rs = cs.executeQuery();
        while (rs.next()) {
            list.add(composeTaxonDataset(rs.getString("datasetKey"), rs));
        }

        rs.close();
        cs.close();
        return list;
    }

    public List<TaxonDataset> getTaxonDatasetListByDatasetKeys(String datasetKeys) throws SQLException {
        LinkedList<TaxonDataset> list = new LinkedList<TaxonDataset>();

        CallableStatement cs = _conn.prepareCall("{call NBNGateway.dbo.usp_getTaxonDatasetList1(?)}");
        cs.setString(1, datasetKeys);

        ResultSet rs = cs.executeQuery();
        while (rs.next()) {
            list.add(composeTaxonDataset(rs.getString("datasetKey"), rs));
        }

        rs.close();
        cs.close();
        return list;
    }

    public List<TaxonDataset> getTaxonDatasetListByDatasetKeys(List<String> datasetKeyList) throws SQLException {
        List<TaxonDataset> list = new ArrayList<TaxonDataset>();

        for (String dsKey : datasetKeyList) {
            list.add(getTaxonDataset(dsKey));
        }

        return list;
    }

    public List<TaxonDataset> getTaxonDatasetListByTaxonReportingCategory(TaxonReportingCategory trc) throws SQLException {
        LinkedList<TaxonDataset> list = new LinkedList<TaxonDataset>();

        CallableStatement cs = _conn.prepareCall("{call NBNGateway.dbo.usp_getSpeciesGroupDatasetList1(?,?)}");
        cs.setInt(1, trc.getTrcKey());
        cs.setBoolean(2, true); // Yes, we do want metadata


        ResultSet rs = cs.executeQuery();
        while (rs.next()) {
            list.add(composeTaxonDataset(rs.getString("datasetKey"), rs));
        }

        rs.close();
        cs.close();
        return list;
    }

    public List<TaxonDataset> getTaxonDatasetListByTaxonReportingCategory(String trcKey) throws SQLException {
        TaxonReportingCategoryDAO dao = new TaxonReportingCategoryDAO();
        TaxonReportingCategory trc = dao.getTaxonReportingCategory(trcKey);
        dao.dispose();
        return getTaxonDatasetListByTaxonReportingCategory(trc);
    }

    public List<TaxonDataset> getTaxonDatasetListByDesignation(Designation desig) throws SQLException {
        LinkedList<TaxonDataset> list = new LinkedList<TaxonDataset>();

        CallableStatement cs = _conn.prepareCall("{call NBNGateway.dbo.usp_getDesignationDatasetList(?)}");
        cs.setInt(1, desig.getId());


        ResultSet rs = cs.executeQuery();
        while (rs.next()) {
            list.add(composeTaxonDataset(rs.getString("datasetKey"), rs));
        }

        rs.close();
        cs.close();
        return list;
    }

    public List<TaxonDataset> getTaxonDatasetListForDesignationPicker(User user, String gridRef, Designation desig, String datasets, int startYear, int endYear) throws SQLException {
        LinkedList<TaxonDataset> results = new LinkedList<TaxonDataset>();

        CallableStatement cs = _conn.prepareCall("{call NBNGateway.dbo.usp_wms_getDatasetListForDesignationPicker(?,?,?,?,?,?)}");
        cs.setInt(1, user.getUserKey());
        cs.setInt(2, desig.getId());
        cs.setString(3, datasets);
        cs.setInt(4, startYear);
        cs.setInt(5, endYear);
        cs.setString(6, gridRef);

        ResultSet rs = cs.executeQuery();
        try {
            while (rs.next()) {
                results.add(composeTaxonDataset(rs.getString("datasetKey"), rs));
            }
        } finally {
            rs.close();
            cs.close();
        }
        return results;
    }

    public List<TaxonDataset> getTaxonDatasetListForDesignationAdminSitePicker(User user, int adminSiteKey, Designation desig, String datasets, int startYear, int endYear) throws SQLException {
        LinkedList<TaxonDataset> results = new LinkedList<TaxonDataset>();

        CallableStatement cs = _conn.prepareCall("{call NBNGateway.dbo.usp_wms_getDatasetListForDesignationPicker_AdminSite(?,?,?,?,?,?)}");
        cs.setInt(1, user.getUserKey());
        cs.setInt(2, desig.getId());
        cs.setString(3, datasets);
        cs.setInt(4, startYear);
        cs.setInt(5, endYear);
        cs.setInt(6, adminSiteKey);

        ResultSet rs = cs.executeQuery();
        try {
            while (rs.next()) {
                results.add(composeTaxonDataset(rs.getString("datasetKey"), rs));
            }
        } finally {
            rs.close();
            cs.close();
        }
        return results;
    }

    public List<TaxonDataset> getTaxonDatasetListForSingleSpeciesAdminSitePicker(User user, int adminSiteKey, Taxon taxon, String datasets, int startYear, int endYear) throws SQLException {
        LinkedList<TaxonDataset> results = new LinkedList<TaxonDataset>();

        CallableStatement cs = _conn.prepareCall("{call NBNGateway.dbo.usp_wms_getDatasetListForSingleSpeciesPicker_AdminSite(?,?,?,?,?,?)}");
        cs.setInt(1, user.getUserKey());
        cs.setInt(2, taxon.getTaxonKey());
        cs.setString(3, datasets);
        cs.setInt(4, startYear);
        cs.setInt(5, endYear);
        cs.setInt(6, adminSiteKey);

        ResultSet rs = cs.executeQuery();
        try {
            while (rs.next()) {
                results.add(composeTaxonDataset(rs.getString("datasetKey"), rs));
            }
        } finally {
            rs.close();
            cs.close();
        }
        return results;
    }

    public List<TaxonDataset> getTaxonDatasetListForSingleSpeciesPicker(User user, String gridRef, Taxon taxon, String datasets, int startYear, int endYear) throws SQLException {
        LinkedList<TaxonDataset> results = new LinkedList<TaxonDataset>();

        CallableStatement cs = _conn.prepareCall("{call NBNGateway.dbo.usp_wms_getDatasetListForSingleSpeciesPicker(?,?,?,?,?,?)}");
        cs.setInt(1, user.getUserKey());
        cs.setInt(2, taxon.getTaxonKey());
        cs.setString(3, datasets);
        cs.setInt(4, startYear);
        cs.setInt(5, endYear);
        cs.setString(6, gridRef);

        ResultSet rs = cs.executeQuery();
        try {
            while (rs.next()) {
                results.add(composeTaxonDataset(rs.getString("datasetKey"), rs));
            }
        } finally {
            rs.close();
            cs.close();
        }
        return results;
    }

    public List<TaxonDataset> getTaxonDatasetListByTaxon(Taxon taxon) throws SQLException {
        LinkedList<TaxonDataset> list = new LinkedList<TaxonDataset>();

        CallableStatement cs = _conn.prepareCall("{call NBNGateway.dbo.usp_getSpeciesDatasetList1(?,?)}");
        cs.setInt(1, taxon.getTaxonKey());
        cs.setBoolean(2, true); // Yes, we do want metadata

        ResultSet rs = cs.executeQuery();
        while (rs.next()) {
            list.add(composeTaxonDataset(rs.getString("datasetKey"), rs));
        }

        rs.close();
        cs.close();

        return list;
    }

    public List<TaxonDataset> getTaxonDatasetListByTaxonList(List<Taxon> taxa) throws SQLException {
        Map<String, TaxonDataset> list = new HashMap<String, TaxonDataset>();

        for (Taxon taxon : taxa) {
            for (TaxonDataset td : getTaxonDatasetListByTaxon(taxon)) {
                list.put(td.getDatasetKey(), td);
            }
        }

        return new ArrayList<TaxonDataset>(list.values());
    }

    public List<TaxonDataset> getTaxonDatasetListByTaxon(String tvk) throws SQLException {
        TaxonDAO dao = new TaxonDAO();
        Taxon t = dao.getTaxon(tvk);
        dao.dispose();
        return getTaxonDatasetListByTaxon(t);
    }

    public List<TaxonDataset> getTaxonDatasetListByGridSquare(String gridref, Designation designation, TaxonReportingCategory trc) throws SQLException {
        List<TaxonDataset> list = new ArrayList<TaxonDataset>();

        CallableStatement cs = this._conn.prepareCall("{call NBNGateway.dbo.usp_getGridSquareDatasetList2(?,?,?)}");
        cs.setString(1, gridref);

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

        ResultSet rs = cs.executeQuery();

        while (rs.next()) {
            list.add(getTaxonDataset(rs.getString("datasetKey")));
        }

        return list;
    }

    public List<TaxonDataset> getTaxonDatasetListBySiteBoundary(User user, SiteBoundary site, Resolution resolution, Designation designation, TaxonReportingCategory trc, boolean overlaps) throws SQLException {
        List<TaxonDataset> list = new ArrayList<TaxonDataset>();

        CallableStatement cs = this._conn.prepareCall("{call NBNGateway.dbo.usp_getAdminSiteDatasetListWithFeature(?,?,?,?,?,?,?)}");
        cs.setInt(1, site.getId());
        cs.setBoolean(2, overlaps);

        if (resolution != Resolution.Any) {
            cs.setShort(3, (short) resolution.getResolutionCode());
        } else {
            cs.setShort(3, (short) 1);
        }

        if (designation != null) {
            cs.setInt(4, designation.getId());
        } else {
            cs.setInt(4, 0);
        }

        if (trc != null) {
            cs.setInt(5, trc.getTrcKey());
        } else {
            cs.setInt(5, 0);
        }

        cs.setInt(6, user.getUserKey());
        cs.setBoolean(7, false);

        ResultSet rs = cs.executeQuery();

        while (rs.next()) {
            list.add(getTaxonDataset(rs.getString("datasetKey")));
        }

        return list;
    }
    
    // <editor-fold defaultstate="collapsed" desc="Code which constructs the various entities generated by this DAO">
    private Dataset composeDataset(String datasetKey, ResultSet rs) throws SQLException {
        return new Dataset(datasetKey, rs.getString("datasetTitle"), datasetProviderDAO.getOrganisationByOrganisationKey(rs.getInt("datasetProviderKey")));
    }

    private SiteBoundaryDataset composeSiteBoundaryDataset(String datasetKey, ResultSet rs) throws SQLException {
        SiteBoundaryDataset toReturn = new SiteBoundaryDataset(datasetKey, rs.getString("datasetTitle"), datasetProviderDAO.getOrganisationByOrganisationKey(rs.getInt("datasetProviderKey")));
        toReturn.setDateLoaded(rs.getString("dateUploaded"));
        toReturn.setDescription(rs.getString("description"));
        toReturn.setLayerName(rs.getString("layerName"));
        toReturn.setGisLayerID(rs.getString("gisLayerID"));
        return toReturn;
    }

    private HabitatDataset composeHabitatDataset(String datasetKey, ResultSet rs) throws SQLException {
        HabitatDataset toReturn = new HabitatDataset(datasetKey, rs.getString("datasetTitle"), datasetProviderDAO.getOrganisationByOrganisationKey(rs.getInt("datasetProviderKey")));
        toReturn.setLayerName(rs.getString("geoLayerName"));
        toReturn.setAbstract(rs.getString("description"));
        toReturn.setDataCaptureMethod(rs.getString("dataCaptureMethod"));
        toReturn.setPurpose(rs.getString("purpose"));
        toReturn.setGeographicalCoverage(rs.getString("geographicalCoverage"));
        toReturn.setTemporalCoverage(rs.getString("temporalCoverage"));
        toReturn.setDataQuality(rs.getString("dataQuality"));
        toReturn.setAdditionalInformation(rs.getString("additionalInformation"));
        toReturn.setUseConstraint(rs.getString("useConstraints"));
        toReturn.setAccessConstraint(rs.getString("accessConstraints"));
        toReturn.setUpdateFrequency(rs.getString("updateFrequency"));
        toReturn.setDateLoaded(rs.getString("dateUploaded"));
        toReturn.setMetadataLastUpdated(rs.getString("metadataLastEdited"));
        toReturn.setGisLayerID(rs.getString("gisLayerID"));
        return toReturn;
    }

    private <T> TaxonDatasetContext<T> composeTaxonDatasetContextCount(String datasetKey, ResultSet rs, T context) throws SQLException {
        TaxonDatasetContext<T> toReturn = new TaxonDatasetContext<T>(datasetKey, rs.getString("datasetTitle"), datasetProviderDAO.getOrganisationByOrganisationKey(rs.getInt("datasetProviderKey")), context, rs.getInt("taxonOccurenceInDataset"));
        constructTaxonDataset(toReturn, rs);
        return toReturn;
    }

    private TaxonDataset composeTaxonDataset(String datasetKey, ResultSet rs) throws SQLException {
        TaxonDataset toReturn = new TaxonDataset(datasetKey, rs.getString("datasetTitle"), datasetProviderDAO.getOrganisationByOrganisationKey(rs.getInt("datasetProviderKey")));
        constructTaxonDataset(toReturn, rs);
        return toReturn;
    }

    private void constructTaxonDataset(TaxonDataset toConstruct, ResultSet rs) throws SQLException {
        toConstruct.setDescription(rs.getString("description"));
        toConstruct.setDataCaptureMethod(rs.getString("dataCaptureMethod"));
        toConstruct.setPurpose(rs.getString("purpose"));
        toConstruct.setGeographicalCoverage(rs.getString("geographicalCoverage"));
        toConstruct.setTemporalCoverage(rs.getString("temporalCoverage"));
        toConstruct.setDataQuality(rs.getString("dataQuality"));
        toConstruct.setAdditionalInformation(rs.getString("additionalInformation"));
        toConstruct.setUseConstraint(rs.getString("useConstraints"));
        toConstruct.setAccessConstraint(rs.getString("accessConstraints"));
        toConstruct.setUpdateFrequency(rs.getString("updateFrequency"));
        toConstruct.setMaxResolution(Resolution.getResolutionByName(rs.getString("maxResolution")));
        toConstruct.setLastEdited(rs.getString("metadataLastEdited"));
        toConstruct.setDateUploaded(rs.getString("dateUploaded"));

        //dateCaptureStart, dateCaptureEnd, td.recordCount, sampleCount, siteCount, speciesCount#
        toConstruct.setCaptureStartYear(rs.getShort("dateCaptureStart"));
        toConstruct.setCaptureEndYear(rs.getShort("dateCaptureEnd"));
        toConstruct.setAmountOfRecords(rs.getInt("recordCount"));
        toConstruct.setAmountOfSamples(rs.getInt("sampleCount"));
        toConstruct.setAmountOfSites(rs.getInt("siteCount"));
        toConstruct.setAmountOfSpecies(rs.getInt("speciesCount"));
    }
    // </editor-fold>
    
    @Override
    public void dispose() {
        if (datasetProviderDAO != null) {
            datasetProviderDAO.dispose();
        }

        super.dispose();
    }
}
