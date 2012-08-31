/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package nbn.webmapping.json.picker.impl;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import nbn.common.database.DataAccessObject;
import nbn.common.feature.Feature;
import nbn.common.taxon.DatasetTaxonObservationListPair;
import nbn.common.taxon.Taxon;
import nbn.common.taxon.TaxonDAO;
import nbn.common.taxon.TaxonPresence;
import nbn.common.taxon.TaxonPresenceDAO;
import nbn.common.user.User;
import nbn.common.util.servlet.Parameter;
import nbn.webmapping.json.picker.AbstractDAOUtilisingTemporalFilteredPickerHandler;
import nbn.webmapping.json.picker.AbstractPickerServlet;
import nbn.webmapping.json.picker.PickerServletHandler;
import org.json.JSONException;
import org.json.JSONObject;


/**
 *
 * @author Administrator
 */
public class DatasetPickerServlet extends AbstractPickerServlet<TaxonPresence> {
    private static final Parameter DATASETS_PARAMETER = new Parameter("datasets");

    @Override
    protected PickerServletHandler<TaxonPresence> getPickerHandler(Map<Parameter, String> requestParameters) {
        return new DatasetPickerHandler();
    }
    
    private class DatasetPickerHandler extends AbstractDAOUtilisingTemporalFilteredPickerHandler<TaxonPresence> {
        private TaxonDAO taxonDAO;
        private TaxonPresenceDAO taxonObservationDAO;

        @Override
        protected void constructRequiredDAOsAndAddThemToList(List<DataAccessObject> listToAddDAOsTo) throws SQLException {
            listToAddDAOsTo.add(taxonDAO = new TaxonDAO());
            listToAddDAOsTo.add(taxonObservationDAO = new TaxonPresenceDAO());
        }

        @Override
        protected List<Taxon> getSpeciesFound(Feature site, User requestingUser, int startYear, int endYear, Map<Parameter, String> requestParameters) throws SQLException {
            return taxonDAO.getTaxonListForDatasetPicker(requestingUser, site, getDataset(requestParameters), startYear, endYear);
        }

        @Override
        protected List<DatasetTaxonObservationListPair<TaxonPresence>> getDatasetRecordsFound(Feature site, User requestingUser, int startYear, int endYear, Map<Parameter, String> requestParameters) throws SQLException {
            List<DatasetTaxonObservationListPair<TaxonPresence>> toReturn = new ArrayList<DatasetTaxonObservationListPair<TaxonPresence>>(); //wrap the result up as a list
            toReturn.add(taxonObservationDAO.getObservationsByDatasetAndFeature(requestingUser, getDataset(requestParameters), site, startYear, endYear));
            return toReturn;
        }

        private String getDataset(Map<Parameter,String> requestParameters) {
            if(!requestParameters.containsKey(DATASETS_PARAMETER))
                throw new IllegalArgumentException("In order to perform a dataset pick, a dataset must be supplied");
            else
                return requestParameters.get(DATASETS_PARAMETER);
        }

        public JSONObject getAdditional(Feature site, List<Taxon> foundSpecies, List<DatasetTaxonObservationListPair<TaxonPresence>> foundRecords) throws JSONException {
            return null;
        }
    }
}
