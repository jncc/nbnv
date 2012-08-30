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
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import nbn.common.dataset.DatasetDAO;
import nbn.common.dataset.TaxonDataset;
import nbn.common.dataset.utils.TaxonDatasetListUtils;
import nbn.common.siteboundary.SiteBoundary;
import nbn.common.taxon.designation.Designation;
import nbn.common.taxon.utils.TaxonListUtils;
import nbn.common.taxonreportingcategory.TaxonReportingCategory;
import nbn.common.user.User;
import nbn.common.feature.Feature;
import nbn.common.feature.GridSquare;
import nbn.common.feature.recorded.GridSquareRecordedSite;
import nbn.common.siteboundary.SiteBoundaryDAO;

/**
 *
 * @author Administrator
 */
public class TaxonPresenceDAO extends TaxonObservationDAO {

    public TaxonPresenceDAO() throws SQLException {
        super();
    }

    public List<TaxonPresence> getObservationsBySpecies(User user, Taxon taxon, int startYear, int endYear, Resolution resolution) throws SQLException {
        DatasetDAO dao = null;

        try {
            dao = new DatasetDAO();
            List<TaxonDataset> datasets = dao.getTaxonDatasetListByTaxon(taxon);
            return getObservationsBySpecies(user, taxon, datasets, startYear, endYear, resolution);
        } finally {
            if (dao != null) {
                dao.dispose();
            }
        }
    }

    public List<TaxonPresence> getObservationsBySpecies(User user, Taxon taxon, List<TaxonDataset> dataset, int startYear, int endYear, Resolution resolution) throws SQLException {
        switch(resolution) {
            case Any:
                List<TaxonPresence> combined = getObservationsBySpecies_ADMINSITE_ONLY(user, taxon, dataset, startYear, endYear);
                combined.addAll(getObservationsBySpecies_GRIDREF_ONLY(user, taxon, dataset, startYear, endYear, resolution));
                return combined;
            case _site:
                return getObservationsBySpecies_ADMINSITE_ONLY(user, taxon, dataset, startYear, endYear);
            default:
                return getObservationsBySpecies_GRIDREF_ONLY(user, taxon, dataset, startYear, endYear, resolution);
        }
    }

    private List<TaxonPresence> getObservationsBySpecies_GRIDREF_ONLY(User user, Taxon taxon, List<TaxonDataset> dataset, int startYear, int endYear, Resolution resolution) throws SQLException {
        List<TaxonPresence> result = new LinkedList<TaxonPresence>();

        CallableStatement cs = this._conn.prepareCall("{call NBNGateway.dbo.usp_getSpeciesDataXML2(?,?,?,?,?,?)}");
        cs.setInt(1, user.getUserKey());
        cs.setString(2, TaxonDatasetListUtils.getCommaDelimitedDatasetList(dataset));
        cs.setInt(3, startYear);
        cs.setInt(4, endYear);
        cs.setInt(5, resolution.getResolutionCode());
        cs.setInt(6, taxon.getTaxonKey());

        ResultSet rs = cs.executeQuery();

        while (rs.next()) {
            TaxonPresence to = new TaxonPresence(rs.getInt("toKey"));
            to.setDataset(getTaxonDataset(rs.getString("datasetKey")));
            to.setDateType(DateType.getTypeByName(rs.getString("dateTypeName")));
            to.setDeterminer(rs.getString("determiner"));
            to.setEndDate(rs.getDate("endDate"));
            to.setProviderID(rs.getString("toPid"));
            to.setRecorder(rs.getString("recorder"));
            to.setSensitiveRecord(rs.getBoolean("isSensitive"));
            to.setSite(getGridSquareSite(rs.getInt("locationId"), rs));
            to.setStartDate(rs.getDate("startDate"));
            to.setSurveyKey(rs.getInt("surveyKey"));
            to.setTaxon(taxon);

            processTaxonObservationAttributes(rs.getString("attributeSummary"), to);

            result.add(to);
        }
        rs.close();
        cs.close();
        return result;
    }

    private List<TaxonPresence> getObservationsBySpecies_ADMINSITE_ONLY(User user, Taxon taxon, List<TaxonDataset> dataset, int startYear, int endYear) throws SQLException {
        SiteBoundaryDAO siteBoundaryDAO = new SiteBoundaryDAO();
        try {
            List<TaxonPresence> result = new LinkedList<TaxonPresence>();

            CallableStatement cs = this._conn.prepareCall("{call NBNGateway.dbo.usp_getSpeciesDataXML2_ADMINSITE_ONLY(?,?,?,?,?)}");
            cs.setInt(1, user.getUserKey());
            cs.setString(2, TaxonDatasetListUtils.getCommaDelimitedDatasetList(dataset));
            cs.setInt(3, startYear);
            cs.setInt(4, endYear);
            cs.setInt(5, taxon.getTaxonKey());

            ResultSet rs = cs.executeQuery();

            while (rs.next()) {
                TaxonPresence to = new TaxonPresence(rs.getInt("toKey"));
                to.setDataset(getTaxonDataset(rs.getString("datasetKey")));
                to.setDateType(DateType.getTypeByName(rs.getString("dateTypeName")));
                to.setDeterminer(rs.getString("determiner"));
                to.setEndDate(rs.getDate("endDate"));
                to.setProviderID(rs.getString("toPid"));
                to.setRecorder(rs.getString("recorder"));
                to.setSensitiveRecord(rs.getBoolean("isSensitive"));
                to.setSite(getAdminSite(rs.getInt("locationId"), rs, siteBoundaryDAO.getSiteBoundary(rs.getInt("adminSiteKey"))));
                to.setStartDate(rs.getDate("startDate"));
                to.setSurveyKey(rs.getInt("surveyKey"));
                to.setTaxon(taxon);

                processTaxonObservationAttributes(rs.getString("attributeSummary"), to);

                result.add(to);
            }
            rs.close();
            cs.close();
            return result;
        }
        finally {
            siteBoundaryDAO.dispose();
        }
    }


    public List<DatasetTaxonObservationListPair<TaxonPresence>> getDatasetsAndObservationsByDesignationAndFeature(User user, String datasets, Designation desig, Feature feature, int startYear, int endYear) throws SQLException {
        switch(feature.getFeatureType()) {
            case GRIDSQUARE :
                GridSquare gridSite = (GridSquare)feature;
                return getDatasetsAndObservationsByDesignationAndGridRef(user, datasets, desig, gridSite.getGridRef(), startYear, endYear);
            case SITEBOUNDARY:
                SiteBoundary adminSite = (SiteBoundary)feature;
                return getDatasetsAndObservationsByDesignationAndAdminSite(user, datasets, desig, adminSite, startYear, endYear);
            default:
                throw new IllegalArgumentException("Unable to get a dataset list for this type of feature");
        }
    }

    public List<DatasetTaxonObservationListPair<TaxonPresence>> getDatasetsAndObservationsByDesignationAndGridRef(User user, String datasets, Designation desig, String gridRef, int startYear, int endYear) throws SQLException {
        Map<String, DatasetTaxonObservationListPair<TaxonPresence>> result = new HashMap<String, DatasetTaxonObservationListPair<TaxonPresence>>();

        DatasetDAO ddao = null;
        List<TaxonDataset> datasetList = new ArrayList<TaxonDataset>();
        try {
            ddao = new DatasetDAO();
            datasetList = ddao.getTaxonDatasetListForDesignationPicker(user, gridRef, desig, datasets, startYear, endYear);
        } finally {
            if (ddao != null) {
                ddao.dispose();
            }
        }

        for (TaxonDataset td : datasetList) {
            DatasetTaxonObservationListPair<TaxonPresence> p = new DatasetTaxonObservationListPair<TaxonPresence>(td, new ArrayList<TaxonPresence>());
            result.put(td.getDatasetKey(), p);
        }

        CallableStatement cs = this._conn.prepareCall("{call NBNGateway.dbo.usp_wms_getRecordsForDesignationPicker(?,?,?,?,?,?)}");
        cs.setInt(1, user.getUserKey());
        cs.setInt(2, desig.getId());
        cs.setString(3, datasets);
        cs.setString(4, gridRef);
        cs.setInt(5, startYear);
        cs.setInt(6, endYear);

        ResultSet rs = cs.executeQuery();

        while (rs.next()) {
            TaxonPresence to = new TaxonPresence(rs.getInt("toKey"));
            to.setDataset(getTaxonDataset(rs.getString("datasetKey")));
            to.setDateType(DateType.getTypeByName(rs.getString("dateTypeName")));
            to.setDeterminer(rs.getString("determiner"));
            to.setEndDate(rs.getDate("endDate"));
            to.setProviderID(rs.getString("toPid"));
            to.setRecorder(rs.getString("recorder"));
            to.setSensitiveRecord(rs.getBoolean("isSensitive"));
            to.setSite(getGridSquareSite(rs.getInt("locationId"), rs));
            to.setStartDate(rs.getDate("startDate"));
            to.setSurveyKey(rs.getInt("surveyKey"));
            to.setTaxon(TaxonDAO.composeTaxon(rs));

            processTaxonObservationAttributes(rs.getString("attributeSummary"), to);

            if (result.containsKey(to.getDataset().getDatasetKey())) {
                DatasetTaxonObservationListPair<TaxonPresence> p = result.get(to.getDataset().getDatasetKey());
                p.getTaxonObservationList().add(to);
            }
        }
        rs.close();
        cs.close();
        return new ArrayList<DatasetTaxonObservationListPair<TaxonPresence>>(result.values());
    }

    public List<DatasetTaxonObservationListPair<TaxonPresence>> getDatasetsAndObservationsByDesignationAndAdminSite(User user, String datasets, Designation desig, SiteBoundary site, int startYear, int endYear) throws SQLException {
        Map<String, DatasetTaxonObservationListPair<TaxonPresence>> result = new HashMap<String, DatasetTaxonObservationListPair<TaxonPresence>>();

        DatasetDAO ddao = null;
        List<TaxonDataset> datasetList = new ArrayList<TaxonDataset>();
        try {
            ddao = new DatasetDAO();
            datasetList = ddao.getTaxonDatasetListForDesignationAdminSitePicker(user, site.getId(), desig, datasets, startYear, endYear);
        } finally {
            if (ddao != null) {
                ddao.dispose();
            }
        }

        for (TaxonDataset td : datasetList) {
            DatasetTaxonObservationListPair<TaxonPresence> p = new DatasetTaxonObservationListPair<TaxonPresence>(td, new ArrayList<TaxonPresence>());
            result.put(td.getDatasetKey(), p);
        }

        CallableStatement cs = this._conn.prepareCall("{call NBNGateway.dbo.usp_wms_getRecordsForDesignationPicker_AdminSite(?,?,?,?,?,?)}");
        cs.setInt(1, user.getUserKey());
        cs.setInt(2, desig.getId());
        cs.setString(3, datasets);
        cs.setInt(4, site.getId());
        cs.setInt(5, startYear);
        cs.setInt(6, endYear);

        ResultSet rs = cs.executeQuery();

        while (rs.next()) {
            TaxonPresence to = new TaxonPresence(rs.getInt("toKey"));
            to.setDataset(getTaxonDataset(rs.getString("datasetKey")));
            to.setDateType(DateType.getTypeByName(rs.getString("dateTypeName")));
            to.setDeterminer(rs.getString("determiner"));
            to.setEndDate(rs.getDate("endDate"));
            to.setProviderID(rs.getString("toPid"));
            to.setRecorder(rs.getString("recorder"));
            to.setSensitiveRecord(rs.getBoolean("isSensitive"));
            to.setSite(getAdminSite(rs.getInt("locationId"), rs, site));
            to.setStartDate(rs.getDate("startDate"));
            to.setSurveyKey(rs.getInt("surveyKey"));
            to.setTaxon(TaxonDAO.composeTaxon(rs));

            processTaxonObservationAttributes(rs.getString("attributeSummary"), to);

            if (result.containsKey(to.getDataset().getDatasetKey())) {
                DatasetTaxonObservationListPair<TaxonPresence> p = result.get(to.getDataset().getDatasetKey());
                p.getTaxonObservationList().add(to);
            }
        }
        rs.close();
        cs.close();
        return new ArrayList<DatasetTaxonObservationListPair<TaxonPresence>>(result.values());
    }

        public DatasetTaxonObservationListPair<TaxonPresence> getObservationsByDatasetAndFeature(User user, String dataset, Feature feature, int startYear, int endYear) throws SQLException {
        switch(feature.getFeatureType()) {
            case GRIDSQUARE :
                GridSquare gridSite = (GridSquare)feature;
                return getObservationsByDatasetAndGridRef(user, dataset, gridSite.getGridRef(), startYear, endYear);
            case SITEBOUNDARY:
                SiteBoundary adminSite = (SiteBoundary)feature;
                return getObservationsByDatasetAndAdminSite(user, dataset, adminSite, startYear, endYear);
            default:
                throw new IllegalArgumentException("Unable to get a dataset list for this type of feature");
        }
    }

    public DatasetTaxonObservationListPair<TaxonPresence> getObservationsByDatasetAndGridRef(User user, String dataset, String gridRef, int startYear, int endYear) throws SQLException {
        List<TaxonPresence> result = new LinkedList<TaxonPresence>();

        CallableStatement cs = this._conn.prepareCall("{call NBNGateway.dbo.usp_wms_getRecordsForSingleDatasetPicker(?,?,?,?,?)}");
        cs.setInt(1, user.getUserKey());
        cs.setString(2, dataset);
        cs.setString(3, gridRef);
        cs.setInt(4, startYear);
        cs.setInt(5, endYear);

        ResultSet rs = cs.executeQuery();

        while (rs.next()) {
            TaxonPresence to = new TaxonPresence(rs.getInt("toKey"));
            to.setDataset(getTaxonDataset(rs.getString("datasetKey")));
            to.setDateType(DateType.getTypeByName(rs.getString("dateTypeName")));
            to.setDeterminer(rs.getString("determiner"));
            to.setEndDate(rs.getDate("endDate"));
            to.setProviderID(rs.getString("toPid"));
            to.setRecorder(rs.getString("recorder"));
            to.setSensitiveRecord(rs.getBoolean("isSensitive"));
            to.setSite(getGridSquareSite(rs.getInt("locationId"), rs));
            to.setStartDate(rs.getDate("startDate"));
            to.setSurveyKey(rs.getInt("surveyKey"));
            to.setTaxon(TaxonDAO.composeTaxon(rs));

            processTaxonObservationAttributes(rs.getString("attributeSummary"), to);

            result.add(to);
        }
        rs.close();
        cs.close();
        return new DatasetTaxonObservationListPair<TaxonPresence>(getTaxonDataset(dataset),result);
    }

    public DatasetTaxonObservationListPair<TaxonPresence> getObservationsByDatasetAndAdminSite(User user, String dataset, SiteBoundary site, int startYear, int endYear) throws SQLException {
        List<TaxonPresence> result = new LinkedList<TaxonPresence>();

        CallableStatement cs = this._conn.prepareCall("{call NBNGateway.dbo.usp_wms_getRecordsForSingleDatasetPicker_AdminSite(?,?,?,?,?)}");
        cs.setInt(1, user.getUserKey());
        cs.setString(2, dataset);
        cs.setInt(3, site.getId());
        cs.setInt(4, startYear);
        cs.setInt(5, endYear);

        ResultSet rs = cs.executeQuery();

        while (rs.next()) {
            TaxonPresence to = new TaxonPresence(rs.getInt("toKey"));
            to.setDataset(getTaxonDataset(rs.getString("datasetKey")));
            to.setDateType(DateType.getTypeByName(rs.getString("dateTypeName")));
            to.setDeterminer(rs.getString("determiner"));
            to.setEndDate(rs.getDate("endDate"));
            to.setProviderID(rs.getString("toPid"));
            to.setRecorder(rs.getString("recorder"));
            to.setSensitiveRecord(rs.getBoolean("isSensitive"));
            to.setSite(getAdminSite(rs.getInt("locationId"), rs, site));
            to.setStartDate(rs.getDate("startDate"));
            to.setSurveyKey(rs.getInt("surveyKey"));
            to.setTaxon(TaxonDAO.composeTaxon(rs));

            processTaxonObservationAttributes(rs.getString("attributeSummary"), to);

            result.add(to);
        }
        rs.close();
        cs.close();
        return new DatasetTaxonObservationListPair<TaxonPresence>(getTaxonDataset(dataset),result);
    }

    public List<TaxonObservationGridSquareSiteAggregateData> getOneSpeciesObservationSiteAggregateData(User user, Taxon taxon, int startYear, int endYear, Resolution minResolution, Resolution maxResolution) throws SQLException {
        DatasetDAO dao = null;

        try {
            dao = new DatasetDAO();
            List<TaxonDataset> datasets = dao.getTaxonDatasetListByTaxon(taxon);
            return getOneSpeciesObservationSiteAggregateData(user, taxon, datasets, startYear, endYear, minResolution, maxResolution);
        } finally {
            if (dao != null) {
                dao.dispose();
            }
        }
    }

    public List<TaxonObservationGridSquareSiteAggregateData> getOneSpeciesObservationSiteAggregateData(User user, Taxon taxon, List<TaxonDataset> dataset, int startYear, int endYear, Resolution minResolution, Resolution maxResolution) throws SQLException {
        HashMap<String, TaxonObservationGridSquareSiteAggregateData> list = new HashMap<String, TaxonObservationGridSquareSiteAggregateData>();

        CallableStatement cs = this._conn.prepareCall("{call NBNGateway.dbo.usp_getOneSpeciesLocationDataXML(?,?,?,?,?,?,?)}");
        cs.setInt(1, user.getUserKey());
        cs.setString(2, TaxonDatasetListUtils.getCommaDelimitedDatasetList(dataset));
        cs.setInt(3, startYear);
        cs.setInt(4, endYear);
        cs.setInt(5, minResolution.getResolutionCode());
        cs.setInt(6, maxResolution.getResolutionCode());
        cs.setInt(7, taxon.getTaxonKey());

        ResultSet rs = cs.executeQuery();

        while (rs.next()) {
            String gridref = rs.getString("gridref");

            if (!list.containsKey(gridref))
                list.put(gridref, getTaxonObservationGridSquareSiteAggregateData(rs.getInt("locationId"), gridref, rs));


            TaxonObservationGridSquareSiteAggregateData data = list.get(gridref);
            data.addRecordToSite(taxon, getTaxonDataset(rs.getString("datasetKey")), rs.getBoolean("isSensitive"), rs.getInt("startDate"), rs.getInt("endDate"));
        }

        return new ArrayList<TaxonObservationGridSquareSiteAggregateData>(list.values());
    }

    public List<TaxonObservationGridSquareSiteAggregateData> getObservationSiteAggregateData(User user, List<Taxon> taxa, List<TaxonDataset> dataset, int startYear, int endYear, Resolution minResolution, Resolution maxResolution, Designation designation, TaxonReportingCategory trc) throws SQLException {
        return getObservationSiteAggregateData(user, TaxonListUtils.getHashMappedTaxonList(taxa), dataset, startYear, endYear, minResolution, maxResolution, designation, trc);
    }

    public List<TaxonObservationGridSquareSiteAggregateData> getObservationSiteAggregateData(User user, Map<String, Taxon> taxa, List<TaxonDataset> dataset, int startYear, int endYear, Resolution minResolution, Resolution maxResolution, Designation designation, TaxonReportingCategory trc) throws SQLException {
        HashMap<String, TaxonObservationGridSquareSiteAggregateData> list = new HashMap<String, TaxonObservationGridSquareSiteAggregateData>();

        CallableStatement cs = this._conn.prepareCall("{call NBNGateway.dbo.usp_getSpeciesDensityDataXML(?,?,?,?,?,?,?,?,?)}");
        cs.setInt(1, user.getUserKey());
        cs.setString(2, TaxonDatasetListUtils.getCommaDelimitedDatasetList(dataset));
        cs.setInt(3, startYear);
        cs.setInt(4, endYear);
        cs.setInt(5, minResolution.getResolutionCode());
        cs.setInt(6, maxResolution.getResolutionCode());

        if (taxa != null) {
            cs.setString(7, TaxonListUtils.getCommaDelimitedTaxonKeyList(taxa.values()));
        } else {
            cs.setString(7, "");
        }

        if (trc != null) {
            cs.setInt(8, trc.getTrcKey());
        } else {
            cs.setInt(8, 0);
        }

        if (designation != null) {
            cs.setInt(9, designation.getId());
        } else {
            cs.setInt(9, 0);
        }

        ResultSet rs = cs.executeQuery();

        while (rs.next()) {
            String gridref = rs.getString("gridref");

            if (!list.containsKey(gridref))
                list.put(gridref, getTaxonObservationGridSquareSiteAggregateData(rs.getInt("locationId"), gridref, rs));


            TaxonObservationGridSquareSiteAggregateData data = list.get(gridref);

            if (taxa != null) {
                data.addRecordToSite(taxa.get(rs.getString("taxonVersionKey")), getTaxonDataset(rs.getString("datasetKey")), rs.getBoolean("isSensitive"), rs.getInt("startDate"), rs.getInt("endDate"));
            } else {
                data.addRecordToSite(getTaxon(rs.getString("taxonVersionKey")), getTaxonDataset(rs.getString("datasetKey")), rs.getBoolean("isSensitive"), rs.getInt("startDate"), rs.getInt("endDate"));
            }
        }

        return new ArrayList<TaxonObservationGridSquareSiteAggregateData>(list.values());
    }

    public List<TaxonObservationGridSquareSiteAggregateData> getGridSquareObservationSiteAggregateData(User user, String gridref, List<Taxon> taxa, List<TaxonDataset> dataset, int startYear, int endYear, Resolution minResolution, Resolution maxResolution, Designation designation, TaxonReportingCategory trc) throws SQLException {
        return getGridSquareObservationSiteAggregateData(user, gridref, TaxonListUtils.getHashMappedTaxonList(taxa), dataset, startYear, endYear, minResolution, maxResolution, designation, trc);
    }

    public List<TaxonObservationGridSquareSiteAggregateData> getGridSquareObservationSiteAggregateData(User user, String square, Map<String, Taxon> taxa, List<TaxonDataset> dataset, int startYear, int endYear, Resolution minResolution, Resolution maxResolution, Designation designation, TaxonReportingCategory trc) throws SQLException {
        HashMap<String, TaxonObservationGridSquareSiteAggregateData> list = new HashMap<String, TaxonObservationGridSquareSiteAggregateData>();

        CallableStatement cs = this._conn.prepareCall("{call NBNGateway.dbo.usp_getGridSquareSpeciesDensityDataXML(?,?,?,?,?,?,?,?,?,?)}");
        cs.setInt(1, user.getUserKey());
        cs.setString(2, TaxonDatasetListUtils.getCommaDelimitedDatasetList(dataset));
        cs.setInt(3, startYear);
        cs.setInt(4, endYear);
        cs.setInt(5, minResolution.getResolutionCode());
        cs.setInt(6, maxResolution.getResolutionCode());


        if (taxa != null) {
            cs.setString(7, TaxonListUtils.getCommaDelimitedTaxonKeyList(taxa.values()));
        } else {
            cs.setString(7, "");
        }

        cs.setString(8, square);

        if (trc != null) {
            cs.setInt(9, trc.getTrcKey());
        } else {
            cs.setInt(9, 0);
        }

        if (designation != null) {
            cs.setInt(10, designation.getId());
        } else {
            cs.setInt(10, 0);
        }

        ResultSet rs = cs.executeQuery();

        while (rs.next()) {
            String gridref = rs.getString("gridref");

            if (!list.containsKey(gridref))
                list.put(gridref, getTaxonObservationGridSquareSiteAggregateData(rs.getInt("locationId"), gridref, rs));


            TaxonObservationGridSquareSiteAggregateData data = list.get(gridref);

            if (taxa != null) {
                data.addRecordToSite(taxa.get(rs.getString("taxonVersionKey")), getTaxonDataset(rs.getString("datasetKey")), rs.getBoolean("isSensitive"), rs.getInt("startDate"), rs.getInt("endDate"));
            } else {
                data.addRecordToSite(getTaxon(rs.getString("taxonVersionKey")), getTaxonDataset(rs.getString("datasetKey")), rs.getBoolean("isSensitive"), rs.getInt("startDate"), rs.getInt("endDate"));
            }
        }

        return new ArrayList<TaxonObservationGridSquareSiteAggregateData>(list.values());
    }

    public List<TaxonObservationGridSquareSiteAggregateData> getSiteBoundaryObservationSiteAggregateData(User user, SiteBoundary site, List<Taxon> species, List<TaxonDataset> datasets, int startYear, int endYear, Resolution minResolution, Resolution maxResolution, Designation designation, TaxonReportingCategory trc, boolean intersects) throws SQLException {
        return getSiteBoundaryObservationSiteAggregateData(user, site, TaxonListUtils.getHashMappedTaxonList(species), datasets, startYear, endYear, minResolution, maxResolution, designation, trc, intersects);
    }

    public List<TaxonObservationGridSquareSiteAggregateData> getSiteBoundaryObservationSiteAggregateData(User user, SiteBoundary site, Map<String, Taxon> taxa, List<TaxonDataset> datasets, int startYear, int endYear, Resolution minResolution, Resolution maxResolution, Designation designation, TaxonReportingCategory trc, boolean intersects) throws SQLException {
        HashMap<String, TaxonObservationGridSquareSiteAggregateData> list = new HashMap<String, TaxonObservationGridSquareSiteAggregateData>();

        CallableStatement cs = this._conn.prepareCall("{call NBNGateway.dbo.usp_getSiteSpeciesDensityDataXML(?,?,?,?,?,?,?,?,?,?,?)}");
        cs.setInt(1, user.getUserKey());
        cs.setInt(2, endYear);
        cs.setInt(3, startYear);
        cs.setInt(4, site.getId());
        cs.setInt(5, minResolution.getResolutionCode());
        cs.setInt(6, maxResolution.getResolutionCode());
        cs.setBoolean(7, intersects);

        if (taxa != null) {
            cs.setString(8, TaxonListUtils.getCommaDelimitedTaxonKeyList(taxa.values()));
        } else {
            cs.setString(8, "");
        }

        if (trc != null) {
            cs.setInt(9, trc.getTrcKey());
        } else {
            cs.setInt(9, 0);
        }

        if (designation != null) {
            cs.setInt(10, designation.getId());
        } else {
            cs.setInt(10, 0);
        }

        cs.setString(11, TaxonDatasetListUtils.getCommaDelimitedDatasetList(datasets));

        ResultSet rs = cs.executeQuery();

        while (rs.next()) {
            String gridref = rs.getString("gridref");

            if (!list.containsKey(gridref))
                list.put(gridref, getTaxonObservationGridSquareSiteAggregateData(rs.getInt("locationId"), gridref, rs));


            TaxonObservationGridSquareSiteAggregateData data = list.get(gridref);

            if (taxa != null) {
                data.addRecordToSite(taxa.get(rs.getString("taxonVersionKey")), getTaxonDataset(rs.getString("datasetKey")), rs.getBoolean("isSensitive"), rs.getInt("startDate"), rs.getInt("endDate"));
            } else {
                data.addRecordToSite(getTaxon(rs.getString("taxonVersionKey")), getTaxonDataset(rs.getString("datasetKey")), rs.getBoolean("isSensitive"), rs.getInt("startDate"), rs.getInt("endDate"));
            }
        }

        return new ArrayList<TaxonObservationGridSquareSiteAggregateData>(list.values());
    }

    public List<TaxonObservationGridSquareSiteAggregateData> getUserPolygonObservationSiteAggregateData(User user, String squares, List<Taxon> species, List<TaxonDataset> datasets, int startYear, int endYear, Resolution minResolution, Resolution maxResolution, Designation designation, TaxonReportingCategory trc, boolean overlaps) throws SQLException {
        return getUserPolygonObservationSiteAggregateData(user, squares, TaxonListUtils.getHashMappedTaxonList(species), datasets, startYear, endYear, minResolution, maxResolution, designation, trc, overlaps);
    }

    public List<TaxonObservationGridSquareSiteAggregateData> getUserPolygonObservationSiteAggregateData(User user, String squares, Map<String, Taxon> taxa, List<TaxonDataset> datasets, int startYear, int endYear, Resolution minResolution, Resolution maxResolution, Designation designation, TaxonReportingCategory trc, boolean overlaps) throws SQLException {
        HashMap<String, TaxonObservationGridSquareSiteAggregateData> list = new HashMap<String, TaxonObservationGridSquareSiteAggregateData>();

        CallableStatement cs = this._conn.prepareCall("{call NBNGateway.dbo.usp_getUserPolySpeciesDensityDataXML(?,?,?,?,?,?,?,?,?,?,?)}");
        cs.setInt(1, user.getUserKey());
        cs.setString(2, squares);
        cs.setString(3, TaxonDatasetListUtils.getCommaDelimitedDatasetList(datasets));
        cs.setInt(4, startYear);
        cs.setInt(5, endYear);

        if (taxa != null) {
            cs.setString(6, TaxonListUtils.getCommaDelimitedTaxonKeyList(taxa.values()));
        } else {
            cs.setString(6, "");
        }

        if (trc != null) {
            cs.setInt(7, trc.getTrcKey());
        } else {
            cs.setInt(7, 0);
        }

        if (designation != null) {
            cs.setInt(8, designation.getId());
        } else {
            cs.setInt(8, 0);
        }

        cs.setInt(9, minResolution.getResolutionCode());
        cs.setInt(10, maxResolution.getResolutionCode());
        cs.setBoolean(11, overlaps);

        ResultSet rs = cs.executeQuery();

        while (rs.next()) {
            String gridref = rs.getString("gridref");

            if (!list.containsKey(gridref))
                list.put(gridref, getTaxonObservationGridSquareSiteAggregateData(rs.getInt("locationId"), gridref, rs));

            TaxonObservationGridSquareSiteAggregateData data = list.get(gridref);

            if (taxa != null) {
                data.addRecordToSite(taxa.get(rs.getString("taxonVersionKey")), getTaxonDataset(rs.getString("datasetKey")), rs.getBoolean("isSensitive"), rs.getInt("startDate"), rs.getInt("endDate"));
            } else {
                data.addRecordToSite(getTaxon(rs.getString("taxonVersionKey")), getTaxonDataset(rs.getString("datasetKey")), rs.getBoolean("isSensitive"), rs.getInt("startDate"), rs.getInt("endDate"));
            }
        }

        return new ArrayList<TaxonObservationGridSquareSiteAggregateData>(list.values());

    }

    public List<TaxonPresence> getGridSquareFilteredObservations(User user, List<Taxon> taxa, List<TaxonDataset> dataset, int startYear, int endYear, Resolution resolution, String gridRef, TaxonReportingCategory trc) throws SQLException {
        return getGridSquareFilteredObservations(user, taxa, dataset, startYear, endYear, resolution, gridRef, trc, null);
    }

    public List<TaxonPresence> getGridSquareFilteredObservations(User user, List<Taxon> taxa, List<TaxonDataset> dataset, int startYear, int endYear, Resolution resolution, String gridRef, Designation desig) throws SQLException {
        return getGridSquareFilteredObservations(user, taxa, dataset, startYear, endYear, resolution, gridRef, null, desig);
    }

    public List<TaxonPresence> getGridSquareFilteredObservations(User user, List<Taxon> taxa, List<TaxonDataset> dataset, int startYear, int endYear, Resolution resolution, String gridRef) throws SQLException {
        return getGridSquareFilteredObservations(user, taxa, dataset, startYear, endYear, resolution, gridRef, null, null);
    }

    public List<TaxonPresence> getGridSquareFilteredObservations(User user, List<Taxon> taxa, List<TaxonDataset> dataset, int startYear, int endYear, Resolution resolution, String gridRef, TaxonReportingCategory trc, Designation desig) throws SQLException {
        List<TaxonPresence> list = new ArrayList<TaxonPresence>();

        CallableStatement cs = this._conn.prepareCall("{call NBNGateway.dbo.usp_getGridSquareSpeciesDataXML4(?,?,?,?,?,?,?,?,?)}");
        cs.setInt(1, user.getUserKey());
        cs.setString(2, gridRef);
        cs.setString(3, TaxonDatasetListUtils.getCommaDelimitedDatasetList(dataset));
        cs.setInt(4, startYear);
        cs.setInt(5, endYear);

        if (taxa != null) {
            cs.setString(6, TaxonListUtils.getCommaDelimitedTaxonKeyList(taxa));
        } else {
            cs.setString(6, ""); // No Taxon Filter
        }
        if (trc != null) {
            cs.setInt(7, trc.getTrcKey());
        } else {
            cs.setInt(7, 0); // No TRC filter
        }
        if (desig != null) {
            cs.setInt(8, desig.getId());
        } else {
            cs.setInt(8, 0); // No designations filter
        }
        if (resolution != Resolution.Any) {
            cs.setShort(9, (short) resolution.getResolutionCode());
        } else {
            cs.setShort(9, (short) 1);
        }

        ResultSet rs = cs.executeQuery();

        while (rs.next()) {
            TaxonPresence to = new TaxonPresence(rs.getInt("toKey"));
            to.setDataset(getTaxonDataset(rs.getString("datasetKey")));
            to.setDateType(DateType.getTypeByName(rs.getString("dateTypeName")));
            to.setDeterminer(rs.getString("determiner"));
            to.setEndDate(rs.getDate("endDate"));
            to.setProviderID(rs.getString("toPid"));
            to.setRecorder(rs.getString("recorder"));
            to.setSensitiveRecord(rs.getBoolean("isSensitive"));
            to.setSite(getGridSquareSite(rs.getInt("locationId"), rs));
            to.setStartDate(rs.getDate("startDate"));
            to.setSurveyKey(rs.getInt("surveyKey"));
            to.setTaxon(TaxonDAO.composeTaxon(rs));

            processTaxonObservationAttributes(rs.getString("attributeSummary"), to);

            list.add(to);
        }

        return list;
    }

    public List<TaxonPresence> getSiteBoundaryFilteredObservations(User user, List<Taxon> taxa, List<TaxonDataset> dataset, int startYear, int endYear, Resolution resolution, SiteBoundary site, TaxonReportingCategory trc, Designation designation, boolean useOverlap) throws SQLException {
        List<TaxonPresence> list = new ArrayList<TaxonPresence>();

        CallableStatement cs = this._conn.prepareCall("{call NBNGateway.dbo.usp_getSiteSpeciesDataInFeatureXML4(?,?,?,?,?,?,?,?,?,?)}");
        cs.setInt(1, user.getUserKey());
        cs.setInt(2, endYear);
        cs.setInt(3, startYear);
        cs.setInt(4, site.getId());

        if (resolution != Resolution.Any) {
            cs.setShort(5, (short) resolution.getResolutionCode());
        } else {
            cs.setShort(5, (short) 1);
        }

        cs.setBoolean(6, useOverlap);

        if (taxa != null) {
            cs.setString(7, TaxonListUtils.getCommaDelimitedTaxonKeyList(taxa));
        } else {
            cs.setString(7, "");
        }

        if (trc != null) {
            cs.setInt(8, trc.getTrcKey());
        } else {
            cs.setInt(8, 0); // No TRC filter
        }
        if (designation != null) {
            cs.setInt(9, designation.getId());
        } else {
            cs.setInt(9, 0); // No designations filter
        }
        cs.setString(10, TaxonDatasetListUtils.getCommaDelimitedDatasetList(dataset));

        ResultSet rs = cs.executeQuery();

        while (rs.next()) {
            TaxonPresence to = new TaxonPresence(rs.getInt("toKey"));
            to.setDataset(getTaxonDataset(rs.getString("datasetKey")));
            to.setDateType(DateType.getTypeByName(rs.getString("dateTypeName")));
            to.setDeterminer(rs.getString("determiner"));
            to.setEndDate(rs.getDate("endDate"));
            to.setProviderID(rs.getString("toPid"));
            to.setRecorder(rs.getString("recorder"));
            to.setSensitiveRecord(rs.getBoolean("isSensitive"));
            to.setSite((rs.getBoolean("isPolygonRecord")) ? getAdminSite(rs.getInt("locationId"), rs, site) : getGridSquareSite(rs.getInt("locationId"), rs));
            to.setStartDate(rs.getDate("startDate"));
            to.setSurveyKey(rs.getInt("surveyKey"));
            to.setTaxon(TaxonDAO.composeTaxon(rs));

            processTaxonObservationAttributes(rs.getString("attributeSummary"), to);

            list.add(to);
        }

        return list;

    }

    public List<TaxonPresence> getPolygonFilteredObservations(User user, List<Taxon> taxa, List<TaxonDataset> dataset, int startYear, int endYear, Resolution resolution, String squareIDs, TaxonReportingCategory trc, Designation designation, boolean useOverlap) throws SQLException {
        List<TaxonPresence> list = new ArrayList<TaxonPresence>();

        CallableStatement cs = this._conn.prepareCall("{call NBNGateway.dbo.usp_getUserPolySpeciesDataXML4(?,?,?,?,?,?,?,?,?,?)}");
        cs.setInt(1, user.getUserKey());
        cs.setString(2, squareIDs);
        cs.setString(3, TaxonDatasetListUtils.getCommaDelimitedDatasetList(dataset));
        cs.setInt(4, startYear);
        cs.setInt(5, endYear);

        if (taxa != null) {
            cs.setString(6, TaxonListUtils.getCommaDelimitedTaxonKeyList(taxa));
        } else {
            cs.setString(6, "");
        }

        if (trc != null) {
            cs.setInt(7, trc.getTrcKey());
        } else {
            cs.setInt(7, 0); // No TRC filter
        }

        if (designation != null) {
            cs.setInt(8, designation.getId());
        } else {
            cs.setInt(8, 0); // No designations filter
        }

        if (resolution != Resolution.Any) {
            cs.setShort(9, (short) resolution.getResolutionCode());
        } else {
            cs.setShort(9, (short) 1);
        }

        cs.setBoolean(10, !useOverlap); // field is bit reversed (ie within, not overlaps)

        ResultSet rs = cs.executeQuery();

        while (rs.next()) {
            TaxonPresence to = new TaxonPresence(rs.getInt("toKey"));
            to.setDataset(getTaxonDataset(rs.getString("datasetKey")));
            to.setDateType(DateType.getTypeByName(rs.getString("dateTypeName")));
            to.setDeterminer(rs.getString("determiner"));
            to.setEndDate(rs.getDate("endDate"));
            to.setProviderID(rs.getString("toPid"));
            to.setRecorder(rs.getString("recorder"));
            to.setSensitiveRecord(rs.getBoolean("isSensitive"));
            to.setSite(getGridSquareSite(rs.getInt("locationId"), rs));
            to.setStartDate(rs.getDate("startDate"));
            to.setSurveyKey(rs.getInt("surveyKey"));
            to.setTaxon(TaxonDAO.composeTaxon(rs));

            processTaxonObservationAttributes(rs.getString("attributeSummary"), to);

            list.add(to);
        }

        return list;
    }



    private TaxonObservationGridSquareSiteAggregateData getTaxonObservationGridSquareSiteAggregateData(int key, String gridRef, ResultSet rs) throws SQLException {
        TaxonObservationGridSquareSiteAggregateData aggData = new TaxonObservationGridSquareSiteAggregateData(rs.getInt("locationId"));
        GridSquare square = new GridSquare(gridRef,rs.getInt("east"),rs.getInt("north"));
        GridSquareRecordedSite recordedSite = new GridSquareRecordedSite(square, false);
        aggData.setRecordedSite(recordedSite);
        return aggData;
    }
}