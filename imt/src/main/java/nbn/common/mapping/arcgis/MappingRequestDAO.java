/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package nbn.common.mapping.arcgis;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;
import nbn.common.database.DataAccessObject;
import nbn.common.database.DatabaseConnectionFactory;
import nbn.common.dataset.DatasetDAO;
import nbn.common.dataset.TaxonDataset;
import nbn.common.logging.GatewayLogger;
import nbn.common.logging.RequestMetadata;
import nbn.common.logging.RequestParameter;
import nbn.common.logging.RequestParameters;
import nbn.common.mapping.arcgis.requestcache.DatasetSpeciesDensityRequestCache;
import nbn.common.mapping.arcgis.requestcache.DesignationSpeciesDensityRequestCache;
import nbn.common.mapping.arcgis.requestcache.SingleSpeciesRequestCache;
import nbn.common.mapping.arcgis.requestcache.TaxonGroupSpeciesDensityRequestCache;
import nbn.common.taxon.Taxon;
import nbn.common.taxon.TaxonDAO;
import nbn.common.taxon.designation.Designation;
import nbn.common.taxon.designation.DesignationDAO;
import nbn.common.taxonreportingcategory.TaxonReportingCategory;
import nbn.common.taxonreportingcategory.TaxonReportingCategoryDAO;
import nbn.common.user.User;

/**
 *
 * @author Paul Gilbertson
 * @date 20110215
 *
 * Encapsulates a ArcGIS mapping service request
 *
 * @version 1.0
 */
public class MappingRequestDAO extends DataAccessObject {
    static {
        purgeCache();
    }

    private final static Object _desdInterlock = new Object();
    private final static Object _dsdInterlock = new Object();
    private final static Object _ssInterlock = new Object();
    private final static Object _tgsdInterlock = new Object();
    private static DatasetSpeciesDensityRequestCache _dsdCache = new DatasetSpeciesDensityRequestCache();
    private static SingleSpeciesRequestCache _ssCache = new SingleSpeciesRequestCache();
    private static DesignationSpeciesDensityRequestCache _desdCache = new DesignationSpeciesDensityRequestCache();
    private static TaxonGroupSpeciesDensityRequestCache _tgsdCache = new TaxonGroupSpeciesDensityRequestCache();

    public MappingRequestDAO() throws SQLException {
        super();
    }


    public synchronized void createDatasetSpeciesDensityRequest(DatasetSpeciesDensityRequest request) throws SQLException {
        synchronized (_dsdInterlock) {
            String rkey = _dsdCache.getRequestKey(request);

            if (rkey != null) {
                request._requestKey = rkey;
            } else {
                makeDatasetSpeciesDensityRequest(request);
		logRequest(request);
            }
        }
    }

    private void makeDatasetSpeciesDensityRequest(DatasetSpeciesDensityRequest request) throws SQLException {
        CallableStatement cs = _conn.prepareCall("{call NBNGateway.dbo.usp_wms_makeDatasetSpeciesDensityMappingRequest(?,?,?,?,?)}");
	cs.setString(1, request.getDatasetKey());
        cs.setInt(2, request.getUser().getUserKey());
        cs.setInt(3, request.getStartYear());
        cs.setInt(4, request.getEndYear());
        cs.registerOutParameter(5, java.sql.Types.VARCHAR);

        cs.execute();

        request._requestKey = cs.getString(5);

        _dsdCache.addRequest(request);

        cs = _conn.prepareCall("{call NBNGateway.dbo.usp_wms_makeCachedDatasetSpeciesDensityMappingRequest(?)}");
	cs.setString(1, request.getRequestKey());
        cs.execute();

        _dsdCache.completeRequest(request);
    }

    public synchronized void createDesignationSpeciesDensityRequest(DesignationSpeciesDensityRequest request) throws SQLException {
        fillDesignationSpeciesDensityRequest(request);
        synchronized (_desdInterlock) {
            String rkey = _desdCache.getRequestKey(request);

            if (rkey != null) {
                request._requestKey = rkey;
            } else {
                makeDesignationSpeciesDensityRequest(request);
		logRequest(request);
            }
        }
    }

    public void fillDesignationSpeciesDensityRequest(DesignationSpeciesDensityRequest request) throws SQLException {
    
        DesignationDAO ddao = null;
        Designation designation;

        try {
            ddao = new DesignationDAO();
            designation = ddao.getDesignation(request.getDesignationKey());
        } finally {
            if (ddao != null)
                ddao.dispose();
        }

        if (request.getDatasets().isEmpty()) {
            DatasetDAO datadao = null;

            try {
                datadao = new DatasetDAO();
                List<TaxonDataset> tdl = datadao.getTaxonDatasetListByDesignation(designation);

                for (TaxonDataset dataset : tdl) {
                    request.addDataset(dataset.getDatasetKey());
                }
            } finally {
                if (datadao != null) {
                    datadao.dispose();
                }
            }
        }
    }

    public void makeDesignationSpeciesDensityRequest(DesignationSpeciesDensityRequest request) throws SQLException {
        DesignationDAO ddao = null;
        Designation designation;

        _desdCache.addRequest(request);

        try {
            ddao = new DesignationDAO();
            designation = ddao.getDesignation(request.getDesignationKey());
        } finally {
            if (ddao != null)
                ddao.dispose();
        }

        if (request.getDatasets().isEmpty()) {
            DatasetDAO datadao = null;

            try {
                datadao = new DatasetDAO();
                List<TaxonDataset> tdl = datadao.getTaxonDatasetListByDesignation(designation);

                for (TaxonDataset dataset : tdl) {
                    request.addDataset(dataset.getDatasetKey());
                }
            } finally {
                if (datadao != null) {
                    datadao.dispose();
                }
            }
        }

        CallableStatement cs = _conn.prepareCall("{call NBNGateway.dbo.usp_wms_makeDesignationSpeciesDensityMappingRequest(?,?,?,?,?)}");
	cs.setInt(1, designation.getId());
        cs.setInt(2, request.getUser().getUserKey());
        cs.setInt(3, request.getStartYear());
        cs.setInt(4, request.getEndYear());
        cs.registerOutParameter(5, java.sql.Types.VARCHAR);

        cs.execute();

        request._requestKey = cs.getString(5);

        cs.close();

        for (String datasetKey : request.getDatasets()) {
            cs = _conn.prepareCall("{call NBNGateway.dbo.usp_wms_addDatasetToRequest(?,?)}");
            cs.setString(1, request.getRequestKey());
            cs.setString(2, datasetKey);
            cs.execute();
            cs.close();
        }

        

        cs = _conn.prepareCall("{call NBNGateway.dbo.usp_wms_makeCachedDesignationSpeciesDensityMappingRequest(?)}");
	cs.setString(1, request.getRequestKey());
        cs.execute();

        _desdCache.completeRequest(request);
    }

    public synchronized void createSingleSpeciesRequest(SingleSpeciesRequest request) throws SQLException {
        fillSingleSpeciesRequest(request);
        synchronized (_ssInterlock) {
            String rkey = _ssCache.getRequestKey(request);

            if (rkey != null) {
                request._requestKey = rkey;
            } else {
                makeSingleSpeciesRequest(request);
		logRequest(request);
            }
        }
    }

    private void fillSingleSpeciesRequest(SingleSpeciesRequest request) throws SQLException {
        TaxonDAO tdao = null;
        Taxon taxon;

        try {
            tdao = new TaxonDAO();
            taxon = tdao.getTaxon(request.getTaxonKey());
        } finally {
            if (tdao != null)
                tdao.dispose();
        }

        if (request.getDatasets().isEmpty()) {
            DatasetDAO ddao = null;

            try {
                ddao = new DatasetDAO();
                List<TaxonDataset> tdl = ddao.getTaxonDatasetListByTaxon(taxon);

                for (TaxonDataset dataset : tdl) {
                    request.addDataset(dataset.getDatasetKey());
                }
            } finally {
                if (ddao != null) {
                    ddao.dispose();
                }
            }
        }
    }

    private void makeSingleSpeciesRequest(SingleSpeciesRequest request) throws SQLException {
        TaxonDAO tdao = null;
        Taxon taxon;

        _ssCache.addRequest(request);

        try {
            tdao = new TaxonDAO();
            taxon = tdao.getTaxon(request.getTaxonKey());
        } finally {
            if (tdao != null)
                tdao.dispose();
        }

        if (request.getDatasets().isEmpty()) {
            DatasetDAO ddao = null;

            try {
                ddao = new DatasetDAO();
                List<TaxonDataset> tdl = ddao.getTaxonDatasetListByTaxon(taxon);

                for (TaxonDataset dataset : tdl) {
                    request.addDataset(dataset.getDatasetKey());
                }
            } finally {
                if (ddao != null) {
                    ddao.dispose();
                }
            }
        }

        CallableStatement cs = _conn.prepareCall("{call NBNGateway.dbo.usp_wms_makeSingleSpeciesMappingRequest(?,?,?,?,?)}");
	cs.setInt(1, taxon.getTaxonKey());
        cs.setInt(2, request.getUser().getUserKey());
        cs.setInt(3, request.getStartYear());
        cs.setInt(4, request.getEndYear());
        cs.registerOutParameter(5, java.sql.Types.VARCHAR);

        cs.execute();

        request._requestKey = cs.getString(5);

        cs.close();

        for (String datasetKey : request.getDatasets()) {
            cs = _conn.prepareCall("{call NBNGateway.dbo.usp_wms_addDatasetToRequest(?,?)}");
            cs.setString(1, request.getRequestKey());
            cs.setString(2, datasetKey);
            cs.execute();
            cs.close();
        }


        cs = _conn.prepareCall("{call NBNGateway.dbo.usp_wms_makeCachedSingleSpeciesMappingRequest(?)}");
	cs.setString(1, request.getRequestKey());
        cs.execute();

        _ssCache.completeRequest(request);


    }

    public synchronized void createTaxonGroupSpeciesDensityRequest(TaxonGroupSpeciesDensityRequest request) throws SQLException {
        fillTaxonGroupSpeciesDensityRequest(request);

        synchronized (_tgsdInterlock) {
            String rkey = _tgsdCache.getRequestKey(request);

            if (rkey != null) {
                request._requestKey = rkey;
            } else {
                makeTaxonGroupSpeciesDensityRequest(request);
            }
        }
    }

    public void fillTaxonGroupSpeciesDensityRequest(TaxonGroupSpeciesDensityRequest request) throws SQLException {
        TaxonReportingCategoryDAO trcdao = null;
        TaxonReportingCategory trc;

        try {
            trcdao = new TaxonReportingCategoryDAO();
            trc = trcdao.getTaxonReportingCategory(request.getTaxonGroupKey());
        } finally {
            if (trcdao != null)
                trcdao.dispose();
        }

        if (request.getDatasets().isEmpty()) {
            DatasetDAO datadao = null;

            try {
                datadao = new DatasetDAO();
                List<TaxonDataset> tdl = datadao.getTaxonDatasetListByTaxonReportingCategory(trc);

                for (TaxonDataset dataset : tdl) {
                    request.addDataset(dataset.getDatasetKey());
                }
            } finally {
                if (datadao != null) {
                    datadao.dispose();
                }
            }
        }
    }


    public void makeTaxonGroupSpeciesDensityRequest(TaxonGroupSpeciesDensityRequest request) throws SQLException {
        TaxonReportingCategoryDAO trcdao = null;
        TaxonReportingCategory trc;

        _tgsdCache.addRequest(request);

        try {
            trcdao = new TaxonReportingCategoryDAO();
            trc = trcdao.getTaxonReportingCategory(request.getTaxonGroupKey());
        } finally {
            if (trcdao != null)
                trcdao.dispose();
        }

        if (request.getDatasets().isEmpty()) {
            DatasetDAO datadao = null;

            try {
                datadao = new DatasetDAO();
                List<TaxonDataset> tdl = datadao.getTaxonDatasetListByTaxonReportingCategory(trc);

                for (TaxonDataset dataset : tdl) {
                    request.addDataset(dataset.getDatasetKey());
                }
            } finally {
                if (datadao != null) {
                    datadao.dispose();
                }
            }
        }

        CallableStatement cs = _conn.prepareCall("{call NBNGateway.dbo.usp_wms_makeTaxonGroupSpeciesDensityMappingRequest(?,?,?,?,?)}");
	cs.setInt(1, trc.getTrcKey());
        cs.setInt(2, request.getUser().getUserKey());
        cs.setInt(3, request.getStartYear());
        cs.setInt(4, request.getEndYear());
        cs.registerOutParameter(5, java.sql.Types.VARCHAR);

        cs.execute();

        request._requestKey = cs.getString(5);

        cs.close();

        for (String datasetKey : request.getDatasets()) {
            cs = _conn.prepareCall("{call NBNGateway.dbo.usp_wms_addDatasetToRequest(?,?)}");
            cs.setString(1, request.getRequestKey());
            cs.setString(2, datasetKey);
            cs.execute();
            cs.close();
        }

        cs = _conn.prepareCall("{call NBNGateway.dbo.usp_wms_makeCachedTaxonGroupSpeciesDensityMappingRequest(?)}");
	cs.setString(1, request.getRequestKey());
        cs.execute();

        _tgsdCache.completeRequest(request);
    }

    public void disposeRequest(SpeciesDataRequest request) throws SQLException {
        CallableStatement cs = _conn.prepareCall("{call NBNGateway.dbo.usp_wms_disposeMappingRequest(?)}");
	cs.setString(1, request.getRequestKey());
        cs.executeUpdate();
        cs.close();
    }

    private static void purgeCache() {
        Connection conn = null;
        try {
            if (Boolean.valueOf(ResourceBundle.getBundle("nbnServerSpecificProperties").getString("nbn.nim.cache.purge"))) {
                conn = DatabaseConnectionFactory.getNBNGatewayConnection();
                CallableStatement cs = conn.prepareCall("{call NBNGateway.dbo.usp_wms_purgeMappingCache()}");
                cs.execute();
                cs.close();
            }
        } catch (SQLException ex) {
        } finally {
            if (conn != null)
                DatabaseConnectionFactory.disposeConnection(conn);
        }
    }

    private void logRequest(SpeciesDataRequest request) throws SQLException{
	GatewayLogger.log(request.getRequestParameters());
    }

}
