/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package nbn.webmapping.json.picker;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.*;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import nbn.common.bridging.Bridge;
import nbn.common.bridging.ListBridge;
import nbn.common.dataset.TaxonDataset;
import nbn.common.feature.Feature;
import nbn.common.taxon.DatasetTaxonObservationListPair;
import nbn.common.taxon.Taxon;
import nbn.common.taxon.TaxonObservation;
import nbn.common.user.User;
import nbn.common.user.UserDAO;
import nbn.common.util.servlet.Parameter;
import nbn.common.util.servlet.ParameterHelper;
import nbn.webmapping.json.bridge.DatasetTaxonObservationListPairLookupToJSONArrayBridge;
import nbn.webmapping.json.bridge.FeatureToJSONObjectBridge;
import nbn.webmapping.json.bridge.TaxonDatasetToJSONObjectBridge;
import nbn.webmapping.json.bridge.TaxonToJSONObjectBridge;
import nbn.webmapping.json.bridge.lookup.ListToJSONObjectLookupBridge;
import nbn.webmapping.json.bridge.TaxonObservationToJSONObjectBridge;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author Administrator
 */
public abstract class AbstractPickerServlet<T extends TaxonObservation> extends HttpServlet {
    private static final Parameter SITES_PARAMETER = new Parameter("sites");
    private static final Parameter USER_NAME_PARAMETER = new Parameter("username");
    private static final Parameter USER_KEY_HASH_PARAMETER = new Parameter("userkey");

    private static final Bridge<Feature, JSONObject> FEATURE_BRIDGE = new FeatureToJSONObjectBridge();
    private static final TaxonToJSONObjectBridge TAXON_BRIDGE = new TaxonToJSONObjectBridge();
    private static final ListToJSONObjectLookupBridge<DatasetTaxonObservationListPair<?>, JSONArray> DATASET_TAXON_OBSERVATION_LOOKUP_BRIDGE = new ListToJSONObjectLookupBridge<DatasetTaxonObservationListPair<?>, JSONArray>(new DatasetTaxonObservationListPairLookupToJSONArrayBridge());
    private static final ListToJSONObjectLookupBridge<Taxon, JSONObject> TAXON_LIST_LOOKUP_BRIDGE = new ListToJSONObjectLookupBridge<Taxon,JSONObject>(new TaxonToJSONObjectBridge());
    private static final ListToJSONObjectLookupBridge<TaxonObservation, JSONObject> TAXON_OBSERVATION_LOOKUP_BRIDGE = new ListToJSONObjectLookupBridge<TaxonObservation,JSONObject>(new TaxonObservationToJSONObjectBridge());

    protected abstract PickerServletHandler<T> getPickerHandler(Map<Parameter, String> requestParameters) throws SQLException;

    /** 
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {   
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        try {
            Map<Parameter, String> parametersAsParamValueMap = ParameterHelper.getParametersAsParamValueMap(request.getParameterMap());
            String userName = parametersAsParamValueMap.get(USER_NAME_PARAMETER);
            String userKeyHash = parametersAsParamValueMap.get(USER_KEY_HASH_PARAMETER);
            
            User user = getUser(userName, userKeyHash); //get the user
            ListToJSONObjectLookupBridge<TaxonDataset, JSONObject> taxonDatasetListLookupableBridge = new ListToJSONObjectLookupBridge<TaxonDataset,JSONObject>(new TaxonDatasetToJSONObjectBridge(user)); //create a taxon dataset bridge specific for this user

            Set<Taxon> speciesUsed = new HashSet<Taxon>();
            Set<TaxonDataset> datasetsUsed = new HashSet<TaxonDataset>();
            Set<T> taxonObservationsUsed = new HashSet<T>();

            JSONObject toReturn = new JSONObject();
            /*Populate the sites*/   
            JSONObject sites = new JSONObject();
            PickerServletHandler<T> requestHandler = getPickerHandler(parametersAsParamValueMap);
            requestHandler.construct();
            try {
                for(Feature currRequestedSite: getTaxonObservationSites(parametersAsParamValueMap.get(SITES_PARAMETER))) {
                    List<Taxon> speciesFound = requestHandler.getSpeciesFound(currRequestedSite, user, parametersAsParamValueMap);
                    List<DatasetTaxonObservationListPair<T>> datasetRecordsFound = requestHandler.getDatasetRecordsFound(currRequestedSite, user, parametersAsParamValueMap);

                    JSONObject site = FEATURE_BRIDGE.convert(currRequestedSite);

                    speciesUsed.addAll(speciesFound);
                    for(DatasetTaxonObservationListPair<T> currDatasetOberservation : datasetRecordsFound) {
                        datasetsUsed.add(currDatasetOberservation.getDataset());
                        taxonObservationsUsed.addAll(currDatasetOberservation.getTaxonObservationList());
                    }
                    site.put("DATASETS",DATASET_TAXON_OBSERVATION_LOOKUP_BRIDGE.convert(datasetRecordsFound));
                    site.put("SPECIES",new JSONArray(new ListBridge<Taxon,String>(TAXON_BRIDGE.getNamedLookupBridge()).convert(speciesFound)));
                    site.put("additional", requestHandler.getAdditional(currRequestedSite, speciesFound, datasetRecordsFound));
                    sites.put(currRequestedSite.getUniqueFeatureID(), site);
                }
            } finally {
                requestHandler.dispose();
            }

            toReturn.put("TAXON", TAXON_LIST_LOOKUP_BRIDGE.convert(new ArrayList<Taxon>(speciesUsed)));
            toReturn.put("DATASETS",taxonDatasetListLookupableBridge.convert(new ArrayList<TaxonDataset>(datasetsUsed)));
            toReturn.put("RECORDS",TAXON_OBSERVATION_LOOKUP_BRIDGE.convert(new ArrayList<TaxonObservation>(taxonObservationsUsed)));

            toReturn.put("SITES", sites);
            toReturn.write(out);
        } catch(SQLException sqlEx) {
            throw new ServletException("An SQL Exception has occurred", sqlEx);
        } catch(JSONException jsonEx) {
            throw new ServletException("An JSON Exception has occurred", jsonEx);
        } catch(RuntimeException rEx) {
            throw new ServletException(rEx);
        } finally {
            out.close();
        }
    }
    
    private static List<Feature> getTaxonObservationSites(String sitesToGet) {
        if(sitesToGet == null)
            throw new IllegalArgumentException("In order to be able to return information about a site(s), then the site(s) must be specified");
        else 
            return Feature.getFeatures(Arrays.asList(sitesToGet.split((","))));
    }

    private User getUser(String userName, String userKeyHash) throws SQLException {
        UserDAO dao = new UserDAO();
        try {
            return dao.loginCookiesUser(userName, userKeyHash);
        } catch (IllegalArgumentException ex) { //failed to login
            return User.PUBLIC_USER;
        } finally {
            dao.dispose();
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /** 
     * Handles the HTTP <code>GET</code> method.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        processRequest(request, response);
    } 

    /** 
     * Handles the HTTP <code>POST</code> method.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        processRequest(request, response);
    }

    /** 
     * Returns a short description of the servlet.
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>
}
