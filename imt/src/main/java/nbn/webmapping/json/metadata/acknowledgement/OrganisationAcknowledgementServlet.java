/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package nbn.webmapping.json.metadata.acknowledgement;

import nbn.common.dataset.TaxonDataset;
import nbn.common.organisation.Organisation;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import nbn.common.bridging.ListBridge;
import nbn.common.dataset.DatasetDAO;
import nbn.common.organisation.OrganisationDAO;
import nbn.common.taxon.TaxonDAO;
import nbn.common.taxon.designation.DesignationDAO;
import nbn.common.user.User;
import nbn.common.user.UserDAO;
import nbn.common.util.Pair;
import nbn.webmapping.json.bridge.JSONObjectListToJSONArrayBridge;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author Administrator
 */
public class OrganisationAcknowledgementServlet extends HttpServlet {
    private static final String REQUESETED_DATASETS = "datasets";
    private static final String REQUESTED_DESIGNATION = "desig";
    private static final String REQUESTED_SPECIES = "species";
    private static final String USER_NAME_PARAMETER = "username";
    private static final String USER_KEY_HASH_PARAMETER = "userkey";
    private static final String RESPONSE_DATASETS_USED = "datasetsUsed";
    private static final String RESPONSE_DATASETS_NOT_USED = "datasetsNotUsed";
    private static final String RESPONSE_DATASETS_UNVIEWABLE = "datasetsUnviewable";

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
            getResult(request).write(out);
        } catch (JSONException je) {
	    throw new ServletException("A JSON exception was thrown", je);
        } catch (SQLException ex) {
	    throw new ServletException("An SQLException exception was thrown", ex);
	}
	finally {
            out.close();
        }
    }

    private static JSONObject getResult(HttpServletRequest request) throws ServletException, SQLException{
        DatasetDAO datasetDao = new DatasetDAO();
        try{
            OrganisationDAO organisationDao = new OrganisationDAO();
            try{
                User currentUser = getUser(request.getParameter(USER_NAME_PARAMETER),request.getParameter(USER_KEY_HASH_PARAMETER));
                List<TaxonDataset> datasetsUsedForFiltering = getFilteringDatasets(request, datasetDao);
                List<TaxonDataset> allAvailableTaxonDatasets = getAllTaxonDatasets(request, datasetDao, currentUser, datasetsUsedForFiltering);

                JSONObject result = new JSONObject();
                    result.put(RESPONSE_DATASETS_USED, convert(getTaxonDatasetsUsed(allAvailableTaxonDatasets, datasetsUsedForFiltering),organisationDao, currentUser));
                    JSONArray taxonDatasetsNotUsed = convert(getTaxonDatasetsNotUsed(allAvailableTaxonDatasets, datasetsUsedForFiltering),organisationDao, currentUser);
                    if((taxonDatasetsNotUsed != null) && (taxonDatasetsNotUsed.length() > 0))
                        result.put(RESPONSE_DATASETS_NOT_USED,taxonDatasetsNotUsed);
                    JSONArray taxonDatasetsUnviewable = convert(getTaxonDatasetsUnavailable(request, currentUser, datasetDao),organisationDao, currentUser);
                    if((taxonDatasetsUnviewable != null) && (taxonDatasetsUnviewable.length() > 0)){
                        result.put(RESPONSE_DATASETS_UNVIEWABLE,taxonDatasetsUnviewable);
                    }
                return result;
            } catch(JSONException je) {
                throw new ServletException("A JSON exception was thrown", je);
            } catch (SQLException sql) {
                throw new ServletException("An sql exception was thrown", sql);
            }finally{
                organisationDao.dispose();
            }
        }finally{
            datasetDao.dispose();
        }
    }

    private static List<TaxonDataset> getTaxonDatasetsUsed(List<TaxonDataset> available, List<TaxonDataset> filtered) {
        List<TaxonDataset> availableClone = new ArrayList<TaxonDataset>(available);
        if(filtered != null)
            availableClone.retainAll(filtered); //filter the results
        return availableClone;
    }

    private static List<TaxonDataset> getTaxonDatasetsNotUsed(List<TaxonDataset> available, List<TaxonDataset> filtered) {
        if(filtered != null) {
            List<TaxonDataset> availableClone = new ArrayList<TaxonDataset>(available);
            availableClone.removeAll(filtered); //filter the results
            return availableClone;
        }
        else
            return new ArrayList<TaxonDataset>();
    }

    private static List<TaxonDataset> getTaxonDatasetsUnavailable(HttpServletRequest request, User user, DatasetDAO datasetDAO) throws SQLException, ServletException {
        if(request.getParameter(REQUESTED_SPECIES) != null) {
            TaxonDAO taxonDAO = new TaxonDAO();
            try {
                return datasetDAO.getAllNonViewableTaxonDatasetsByTaxonWithAggregates(user, taxonDAO.getTaxon(request.getParameter(REQUESTED_SPECIES)));
            }
            finally {
                taxonDAO.dispose();
            }
        }
        else if(request.getParameter(REQUESTED_DESIGNATION) != null) {
            DesignationDAO desigDAO = new DesignationDAO();
            try {
                return datasetDAO.getAllNonViewableTaxonDatasetsByExtantSpeciesDesignation(user, desigDAO.getDesignation(request.getParameter(REQUESTED_DESIGNATION)));
            }
            finally {
                desigDAO.dispose();
            }
        }
        else
            return null;
    }

    private static List<TaxonDataset> getAllTaxonDatasets(HttpServletRequest request, DatasetDAO datasetDao, User user, List<TaxonDataset> requestedDatasets) throws SQLException, ServletException{
        if(request.getParameter(REQUESTED_SPECIES) != null) {
            TaxonDAO taxonDAO = new TaxonDAO();
            try {
                return datasetDao.getAllViewableTaxonDatasetsByTaxonWithAggregates(user, taxonDAO.getTaxon(request.getParameter(REQUESTED_SPECIES)));
            }
            finally {
                taxonDAO.dispose();
            }
        }
        else if(request.getParameter(REQUESTED_DESIGNATION) != null) {
            DesignationDAO desigDAO = new DesignationDAO();
            try {
                return datasetDao.getAllViewableTaxonDatasetsByExtantSpeciesDesignation(user, desigDAO.getDesignation(request.getParameter(REQUESTED_DESIGNATION)));
            } finally {
                desigDAO.dispose();
            }
        }
        else if(requestedDatasets != null) //if null then need to throw an exception as not enough params were given
            return requestedDatasets;
        else
            throw new ServletException("Either a designation key, species list or single datasetKey is required");
    }


    private static List<TaxonDataset> getFilteringDatasets(HttpServletRequest request, DatasetDAO datasetDao) throws SQLException {
        String csvDatasets = request.getParameter(REQUESETED_DATASETS);
        if(csvDatasets!=null) {
            List<String> requestedDatasetsKeys = Arrays.asList(csvDatasets.split(",")); //get the list of dataset keys
            return datasetDao.getTaxonDatasetListByDatasetKeys(requestedDatasetsKeys);
        }
        else
            return null; //if no requested datasets were passed then return null
    }

    private static User getUser(String userName, String userKeyHash) throws SQLException {
        if(userName !=null && userKeyHash != null) {
            UserDAO dao = new UserDAO();
            try {
                User user = dao.loginCookiesUser(userName, userKeyHash);
                return user;
            }
            finally {
                dao.dispose();
            }
        }
        return User.PUBLIC_USER;
    }

    private static JSONArray convert(List<TaxonDataset> datasets, OrganisationDAO organisationDao, User currentUser) throws SQLException {
        if(datasets != null && !datasets.isEmpty()) {
            List<Pair<Organisation,List<TaxonDataset>>> taxonDatasetsGroupedByOrganisation = organisationDao.getProvidingOrganisationsForDatasets(datasets);
            OrganisationTaxonDatasetsListPairToJSONObjectBridge bridge = new OrganisationTaxonDatasetsListPairToJSONObjectBridge(currentUser);
            ListBridge<Pair<Organisation,List<TaxonDataset>>, JSONObject> listBridge = new ListBridge<Pair<Organisation,List<TaxonDataset>>, JSONObject>(bridge);
            return new JSONObjectListToJSONArrayBridge().convert(listBridge.convert(taxonDatasetsGroupedByOrganisation));
        }
        else
            return null;
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
