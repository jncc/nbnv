
package nbn.common.taxon;

import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import nbn.common.dataset.DatasetDAO;
import nbn.common.dataset.TaxonDataset;
import nbn.common.siteboundary.SiteBoundary;
import nbn.common.user.User;
import nbn.common.database.DataAccessObject;
import nbn.common.feature.Feature;
import nbn.common.feature.GridSquare;
import nbn.common.feature.recorded.SiteBoundaryRecordedSite;
import nbn.common.feature.recorded.GridSquareRecordedSite;

/*This DAO will return information about TaxonObservations of all types,
 * sub classes of this will return more specific data.
 * @see TaxonPresenceDAO
 */
public class TaxonObservationDAO extends DataAccessObject {

    private HashMap<String, TaxonDataset> _cacheOfTaxonDatasets;
    private HashMap<Integer, TaxonObservationSite<GridSquareRecordedSite>> _cacheOfGridSites;
    private HashMap<Integer, TaxonObservationSite<SiteBoundaryRecordedSite>> _cacheOfAdminSites;
    private HashMap<String, Taxon> _cacheOfTaxa;

    public TaxonObservationDAO() throws SQLException {
        super();

        this._cacheOfTaxonDatasets = new HashMap<String, TaxonDataset>();
        this._cacheOfGridSites = new HashMap<Integer, TaxonObservationSite<GridSquareRecordedSite>>();
        this._cacheOfAdminSites = new HashMap<Integer, TaxonObservationSite<SiteBoundaryRecordedSite>>();
        this._cacheOfTaxa = new HashMap<String, Taxon>();
    }

    public List<DatasetTaxonObservationListPair<TaxonObservation>> getDatasetsAndObservationsBySpeciesAndFeature(User user, String datasets, Taxon taxon, Feature feature, int startYear, int endYear) throws SQLException {
        switch(feature.getFeatureType()) {
            case GRIDSQUARE :
                GridSquare gridSite = (GridSquare)feature;
                return getDatasetsAndObservationsBySpeciesAndGridRef(user, datasets, taxon, gridSite.getGridRef(), startYear, endYear);
            case SITEBOUNDARY:
                SiteBoundary adminSite = (SiteBoundary)feature;
                return getDatasetsAndObservationsBySpeciesAndAdminSite(user, datasets, taxon, adminSite, startYear, endYear);

            default:
                throw new IllegalArgumentException("Unable to get a dataset list for this type of feature");
        }
    }

    public List<DatasetTaxonObservationListPair<TaxonObservation>> getDatasetsAndObservationsBySpeciesAndGridRef(User user, String datasets, Taxon taxon, String gridRef, int startYear, int endYear) throws SQLException {
        Map<String, DatasetTaxonObservationListPair<TaxonObservation>> result = new HashMap<String, DatasetTaxonObservationListPair<TaxonObservation>>();

        DatasetDAO ddao = null;
        List<TaxonDataset> datasetList = new ArrayList<TaxonDataset>();
        try {
            ddao = new DatasetDAO();
            datasetList = ddao.getTaxonDatasetListForSingleSpeciesPicker(user, gridRef, taxon, datasets, startYear, endYear);
        } finally {
            if (ddao != null) {
                ddao.dispose();
            }
        }

        for (TaxonDataset td : datasetList) {
            DatasetTaxonObservationListPair<TaxonObservation> p = new DatasetTaxonObservationListPair<TaxonObservation>(td, new ArrayList<TaxonObservation>());
            result.put(td.getDatasetKey(), p);
        }

        CallableStatement cs = this._conn.prepareCall("{call NBNGateway.dbo.usp_wms_getRecordsForSingleSpeciesPicker(?,?,?,?,?,?)}");
        cs.setInt(1, user.getUserKey());
        cs.setInt(2, taxon.getTaxonKey());
        cs.setString(3, datasets);
        cs.setString(4, gridRef);
        cs.setInt(5, startYear);
        cs.setInt(6, endYear);

        ResultSet rs = cs.executeQuery();

        while (rs.next()) {
            TaxonObservation to = (rs.getBoolean("zeroAbundance")) ? new TaxonAbsence(rs.getInt("toKey")) : new TaxonPresence(rs.getInt("toKey"));
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
                DatasetTaxonObservationListPair<TaxonObservation> p = result.get(to.getDataset().getDatasetKey());
                p.getTaxonObservationList().add(to);
            }
        }
        rs.close();
        cs.close();
        return new ArrayList<DatasetTaxonObservationListPair<TaxonObservation>>(result.values());
    }

    public List<DatasetTaxonObservationListPair<TaxonObservation>> getDatasetsAndObservationsBySpeciesAndAdminSite(User user, String datasets, Taxon taxon, SiteBoundary site, int startYear, int endYear) throws SQLException {
        Map<String, DatasetTaxonObservationListPair<TaxonObservation>> result = new HashMap<String, DatasetTaxonObservationListPair<TaxonObservation>>();

        DatasetDAO ddao = null;
        List<TaxonDataset> datasetList = new ArrayList<TaxonDataset>();
        try {
            ddao = new DatasetDAO();
            datasetList = ddao.getTaxonDatasetListForSingleSpeciesAdminSitePicker(user, site.getId(), taxon, datasets, startYear, endYear);
        } finally {
            if (ddao != null) {
                ddao.dispose();
            }
        }

        for (TaxonDataset td : datasetList) {
            DatasetTaxonObservationListPair<TaxonObservation> p = new DatasetTaxonObservationListPair<TaxonObservation>(td, new ArrayList<TaxonObservation>());
            result.put(td.getDatasetKey(), p);
        }

        CallableStatement cs = this._conn.prepareCall("{call NBNGateway.dbo.usp_wms_getRecordsForSingleSpeciesPicker_AdminSite(?,?,?,?,?,?)}");
        cs.setInt(1, user.getUserKey());
        cs.setInt(2, taxon.getTaxonKey());
        cs.setString(3, datasets);
        cs.setInt(4, site.getId());
        cs.setInt(5, startYear);
        cs.setInt(6, endYear);

        ResultSet rs = cs.executeQuery();

        while (rs.next()) {
            TaxonObservation to = (rs.getBoolean("zeroAbundance")) ? new TaxonAbsence(rs.getInt("toKey")) : new TaxonPresence(rs.getInt("toKey"));
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
                DatasetTaxonObservationListPair<TaxonObservation> p = result.get(to.getDataset().getDatasetKey());
                p.getTaxonObservationList().add(to);
            }
        }
        rs.close();
        cs.close();
        return new ArrayList<DatasetTaxonObservationListPair<TaxonObservation>>(result.values());
    }

    protected TaxonDataset getTaxonDataset(String key) throws SQLException {
        if (this._cacheOfTaxonDatasets.containsKey(key)) {
            return this._cacheOfTaxonDatasets.get(key);
        }

        DatasetDAO dao = null;

        try {
            dao = new DatasetDAO();
            TaxonDataset result = dao.getTaxonDataset(key);
            this._cacheOfTaxonDatasets.put(key, result);
            return result;
        } finally {
            if (dao != null) {
                dao.dispose();
            }
        }
    }

    protected void processTaxonObservationAttributes(String attribString, TaxonObservation observation) {
        if (attribString == null || attribString.isEmpty()) {
            return;
        }

        String[] attribs = attribString.split("!%");

        for (String attrib : attribs) {
            String[] elements = attrib.split("!#");

            if (elements.length != 2) {
                continue; // Bad attribute data
            }
            observation.addAttribute(new TaxonObservationAttribute(elements[0], elements[1]));
        }
    }

    protected Taxon getTaxon(String key) throws SQLException {
        if (this._cacheOfTaxa.containsKey(key)) {
            return this._cacheOfTaxa.get(key);
        }

        TaxonDAO dao = null;

        try {
            dao = new TaxonDAO();
            Taxon t = dao.getTaxon(key);
            this._cacheOfTaxa.put(key, t);
            return t;
        } finally {
            if (dao != null) {
                dao.dispose();
            }
        }
    }

    //////////////////////////////////////////////////////////////////////////////////////
    protected TaxonObservationSite<GridSquareRecordedSite> getGridSquareSite(int key, ResultSet rs) throws SQLException {
        if (this._cacheOfGridSites.containsKey(key)) {
            return this._cacheOfGridSites.get(key);
        }

        GridSquare square = new GridSquare(rs.getString("gridRef"),rs.getInt("east"),rs.getInt("north"));
        GridSquareRecordedSite recordedSite = new GridSquareRecordedSite(square, rs.getBoolean("blurred"));
        TaxonObservationSite<GridSquareRecordedSite> site = new TaxonObservationSite<GridSquareRecordedSite>(key);
        site.setRecordedSite(recordedSite);
        site.setProviderLocID(rs.getString("locationPid"));
        site.setSiteName(rs.getString("siteName"));

        this._cacheOfGridSites.put(key, site);

        return site;
    }

    protected TaxonObservationSite<SiteBoundaryRecordedSite> getAdminSite(int key, ResultSet rs, SiteBoundary site) throws SQLException {
        if (this._cacheOfAdminSites.containsKey(key)) {
            return this._cacheOfAdminSites.get(key);
        }

        SiteBoundaryRecordedSite recordedSite = new SiteBoundaryRecordedSite(site);
        TaxonObservationSite<SiteBoundaryRecordedSite> observationSite = new TaxonObservationSite<SiteBoundaryRecordedSite>(key);
        observationSite.setRecordedSite(recordedSite);
        observationSite.setProviderLocID(rs.getString("locationPid"));
        observationSite.setSiteName(rs.getString("siteName"));

        this._cacheOfAdminSites.put(key, observationSite);

        return observationSite;
    }

    public static List<DatasetTaxonObservationListPair<TaxonObservation>> filterDatasetTaxonObservationListPairListToAbundance(List<DatasetTaxonObservationListPair<TaxonObservation>> in, Abundance abundance) {
        List<DatasetTaxonObservationListPair<TaxonObservation>> toReturn = new ArrayList<DatasetTaxonObservationListPair<TaxonObservation>>();
        for(DatasetTaxonObservationListPair<TaxonObservation> currDatasetTaxonObservationListPair : in) {
            List<TaxonObservation> filteredTaxonObservationListToTaxonPresenceList = filterTaxonObservationListToAbundance(currDatasetTaxonObservationListPair.getTaxonObservationList(), abundance);
            if(!filteredTaxonObservationListToTaxonPresenceList.isEmpty())
                toReturn.add(new DatasetTaxonObservationListPair<TaxonObservation>(currDatasetTaxonObservationListPair.getDataset(), filteredTaxonObservationListToTaxonPresenceList));
        }
        return toReturn;
    }

    public static List<TaxonObservation> filterTaxonObservationListToAbundance(List<TaxonObservation> in, Abundance abundance) {
        List<TaxonObservation> toReturn = new ArrayList<TaxonObservation>();
        for(TaxonObservation currTaxonOccurrence : in) {
            if(currTaxonOccurrence.getAbundance() == abundance)
                toReturn.add(currTaxonOccurrence);
        }
        return toReturn;
    }
}
