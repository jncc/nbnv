
package nbn.webmapping.json.picker.impl;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import nbn.common.database.DataAccessObject;
import nbn.common.dataset.DatasetDAO;
import nbn.common.dataset.TaxonDataset;
import nbn.common.dataset.utils.TaxonDatasetListUtils;
import nbn.common.feature.Feature;
import nbn.common.taxon.DatasetTaxonObservationListPair;
import nbn.common.taxon.Taxon;
import nbn.common.taxon.TaxonDAO;
import nbn.common.taxon.TaxonPresence;
import nbn.common.taxon.TaxonPresenceDAO;
import nbn.common.taxon.designation.Designation;
import nbn.common.taxon.designation.DesignationDAO;
import nbn.common.user.User;
import nbn.common.util.servlet.Parameter;
import nbn.webmapping.json.picker.AbstractDAOUtilisingTemporalFilteredPickerHandler;
import nbn.webmapping.json.picker.AbstractPickerServlet;
import nbn.webmapping.json.picker.PickerServletHandler;
import org.json.JSONException;
import org.json.JSONObject;

public class DesignationPickerServlet extends AbstractPickerServlet<TaxonPresence> {
    private static final Parameter DESIGNATION_PARAMETER = new Parameter("desig");
    private static final Parameter DATASETS_PARAMETER = new Parameter("datasets");

    @Override
    protected PickerServletHandler<TaxonPresence> getPickerHandler(Map<Parameter, String> requestParameters) throws SQLException {
        return new DesignationPickerHandler();
    }
    
    private class DesignationPickerHandler extends AbstractDAOUtilisingTemporalFilteredPickerHandler<TaxonPresence> {
        private DesignationDAO designationDAO;
        private DatasetDAO datasetDAO;
        private TaxonDAO taxonDAO;
        private TaxonPresenceDAO taxonObservationDAO;

        @Override
        protected void constructRequiredDAOsAndAddThemToList(List<DataAccessObject> listToAddDAOsTo) throws SQLException {
            listToAddDAOsTo.add(designationDAO = new DesignationDAO());
            listToAddDAOsTo.add(datasetDAO = new DatasetDAO());
            listToAddDAOsTo.add(taxonDAO = new TaxonDAO());
            listToAddDAOsTo.add(taxonObservationDAO = new TaxonPresenceDAO());
        }
        
        @Override
        protected List<Taxon> getSpeciesFound(Feature site, User requestingUser, int startYear, int endYear, Map<Parameter, String> requestParameters) throws SQLException {
            Designation desig = getDesignation(requestParameters);
            String datasets = getDatasetList(desig, requestParameters);
            return taxonDAO.getTaxonListForDesignationPicker(requestingUser, site, desig, datasets, startYear, endYear);
        }

        @Override
        protected List<DatasetTaxonObservationListPair<TaxonPresence>> getDatasetRecordsFound(Feature site, User requestingUser, int startYear, int endYear, Map<Parameter, String> requestParameters) throws SQLException {
            Designation desig = getDesignation(requestParameters);
            String datasets = getDatasetList(desig, requestParameters);
            return taxonObservationDAO.getDatasetsAndObservationsByDesignationAndFeature(requestingUser, datasets, desig, site, startYear, endYear);
        }

        private String getDatasetList(Designation designation, Map<Parameter, String> requestParameters) throws SQLException {
            if(requestParameters.containsKey(DATASETS_PARAMETER))
                return requestParameters.get(DATASETS_PARAMETER);
            else {
                List<TaxonDataset> tdl = datasetDAO.getTaxonDatasetListByDesignation(designation);
                return TaxonDatasetListUtils.getCommaDelimitedDatasetList(tdl);
            }
        }
        
        private Designation getDesignation(Map<Parameter, String> requestParameters) throws SQLException {
            if(!requestParameters.containsKey(DESIGNATION_PARAMETER))
                throw new IllegalArgumentException("In order to perform a designation pick, a designation must be supplied");
            else
                return designationDAO.getDesignation(requestParameters.get(DESIGNATION_PARAMETER));
        }

        public JSONObject getAdditional(Feature site, List<Taxon> foundSpecies, List<DatasetTaxonObservationListPair<TaxonPresence>> foundRecords) throws JSONException {
            return null;
        }
    }
}
