
package nbn.webmapping.json.picker.impl;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import nbn.common.database.DataAccessObject;
import nbn.common.dataset.DatasetDAO;
import nbn.common.dataset.TaxonDataset;
import nbn.common.dataset.utils.TaxonDatasetListUtils;
import nbn.common.feature.Feature;
import nbn.common.feature.FeatureType;
import nbn.common.taxon.Abundance;
import nbn.common.taxon.DatasetTaxonObservationListPair;
import nbn.common.taxon.Taxon;
import nbn.common.taxon.TaxonAbsence;
import nbn.common.taxon.TaxonDAO;
import nbn.common.taxon.TaxonObservation;
import nbn.common.taxon.TaxonObservationDAO;
import nbn.common.taxon.TaxonPresence;
import nbn.common.user.User;
import nbn.common.util.servlet.Parameter;
import nbn.webmapping.json.picker.AbstractDAOUtilisingTemporalFilteredPickerHandler;
import nbn.webmapping.json.picker.AbstractPickerServlet;
import nbn.webmapping.json.picker.PickerServletHandler;
import org.json.JSONException;
import org.json.JSONObject;

public class SingleSpeciesPickerServlet extends AbstractPickerServlet<TaxonObservation> {
    private static final Parameter TAXON_PARAMETER = new Parameter("species");
    private static final Parameter DATASETS_PARAMETER = new Parameter("datasets");
    private static final Parameter ABUNDANCE_PARAMETER = new Parameter("abundance");

    @Override
    protected PickerServletHandler<TaxonObservation> getPickerHandler(Map<Parameter, String> requestParameters) throws SQLException {
        return new SingleSpeciesPickerServletHandler();
    }

    private class SingleSpeciesPickerServletHandler extends AbstractDAOUtilisingTemporalFilteredPickerHandler<TaxonObservation> {
        private TaxonDAO taxonDAO;
        private TaxonObservationDAO taxonObservationDAO;
        private DatasetDAO datasetDAO;

        @Override
        protected void constructRequiredDAOsAndAddThemToList(List<DataAccessObject> listToAddDAOsTo) throws SQLException {
            listToAddDAOsTo.add(taxonDAO = new TaxonDAO());
            listToAddDAOsTo.add(taxonObservationDAO = new TaxonObservationDAO());
            listToAddDAOsTo.add(datasetDAO = new DatasetDAO());
        }

        @Override
        protected List<Taxon> getSpeciesFound(Feature site, User requestingUser, int startYear, int endYear, Map<Parameter, String> requestParameters) throws SQLException {
            List<Taxon> speciesToReturn = new ArrayList<Taxon>();
            speciesToReturn.add(getTaxon(requestParameters));
            return speciesToReturn;
        }

        @Override
        protected List<DatasetTaxonObservationListPair<TaxonObservation>> getDatasetRecordsFound(Feature site, User requestingUser, int startYear, int endYear, Map<Parameter, String> requestParameters) throws SQLException {
            Taxon taxon = getTaxon(requestParameters);
            String datasets = getDatasetList(requestingUser, getTaxon(requestParameters), requestParameters);
            List<DatasetTaxonObservationListPair<TaxonObservation>> datasetsAndObservationsBySpeciesAndFeature = taxonObservationDAO.getDatasetsAndObservationsBySpeciesAndFeature(requestingUser, datasets, taxon, site, startYear, endYear);
            Abundance abundance = Abundance.getAbundanceFromValue(requestParameters.get(ABUNDANCE_PARAMETER));
            switch(abundance) {
                case ALL: return datasetsAndObservationsBySpeciesAndFeature;
                default: return TaxonObservationDAO.filterDatasetTaxonObservationListPairListToAbundance(datasetsAndObservationsBySpeciesAndFeature, abundance);
            }
        }
        
        private Taxon getTaxon(Map<Parameter, String> requestParameters) throws SQLException {
            if(!requestParameters.containsKey(TAXON_PARAMETER))
                throw new IllegalArgumentException("In order to perform a SingleSpecies pick, a species (taxon) must be supplied");
            else
                return taxonDAO.getTaxon(requestParameters.get(TAXON_PARAMETER));
        }
        
        private String getDatasetList(User user, Taxon taxon, Map<Parameter, String> requestParameters) throws SQLException {
            if(requestParameters.containsKey(DATASETS_PARAMETER))
                return requestParameters.get(DATASETS_PARAMETER);
            else {
                List<TaxonDataset> tdl = datasetDAO.getAllViewableTaxonDatasetsByTaxonWithAggregates(user, taxon);
                return TaxonDatasetListUtils.getCommaDelimitedDatasetList(tdl);
            }
        }

        public JSONObject getAdditional(Feature site, List<Taxon> foundSpecies, List<DatasetTaxonObservationListPair<TaxonObservation>> foundRecords) throws JSONException {
            List<TaxonObservation> records = getRecords(foundRecords);
            if(site.getFeatureType()==FeatureType.GRIDSQUARE && records.size() > 0) {
                JSONObject toReturn = new JSONObject();
                toReturn.put("recordComment", createRecordCommentsObject(site, foundSpecies.get(0), records));
                return toReturn;
            }
            else
                return null;
        }

        //Fix for NBNIV-553, could this be done in a better way?
        private JSONObject createRecordCommentsObject(Feature site, Taxon species, List<TaxonObservation> records) throws JSONException {
            JSONObject toReturn = new JSONObject();
            toReturn.put("name", "More info and comments on " + site.getName() + " records");
            StringBuilder linkBuilder = new StringBuilder("/interactive/info.jsp?MAPSERVICE=imt&HIDECOMMENTS=0&LAYERS=0,1,2,3&species=");
            linkBuilder.append(species.getTaxonVersionKey());
            linkBuilder.append("&SELECTED_IDS=(");
            for(TaxonObservation currRecord: records) {
                linkBuilder.append(currRecord.getObservationID());
                linkBuilder.append(',');
            }
            linkBuilder.setCharAt(linkBuilder.length()-1, ')');
            toReturn.put("link", linkBuilder.toString());
            return toReturn;
        }

        private List<TaxonObservation> getRecords(List<DatasetTaxonObservationListPair<TaxonObservation>> foundRecords) {
            List<TaxonObservation> toReturn = new ArrayList<TaxonObservation>();
            for(DatasetTaxonObservationListPair<TaxonObservation> curr: foundRecords)
                toReturn.addAll(curr.getTaxonObservationList());
            return toReturn;
        }
    }
}
