package nbn.webmapping.json.search.dataset;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import nbn.common.bridging.ListBridge;
import nbn.common.dataset.DatasetDAO;
import nbn.common.dataset.TaxonDataset;
import nbn.common.searching.SearchMatch;
import nbn.webmapping.json.bridge.JSONObjectListToJSONArrayBridge;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * This class Sevlet will provide recommendations to partly complete search terms.
 * The response of which will be in JSON.
 * @author Chris Johnson
 * @date    19-Nov-2010
 */
public class AutoCompleteDatasetSearchJSON extends HttpServlet {
    private static final String SEARCH_PARAMETER = "term";

    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
	PrintWriter out = response.getWriter();
        try {
	    DatasetDAO dao = new DatasetDAO();
	    try {
		List<SearchMatch<TaxonDataset>> searchResults = dao.searchTaxonDataset(request.getParameter(SEARCH_PARAMETER));
		SearchedDatasetMatchToJSONObjectBridge<TaxonDataset> bridge = new SearchedDatasetMatchToJSONObjectBridge<TaxonDataset>();
		ListBridge<SearchMatch<TaxonDataset>, JSONObject> listBridge = new ListBridge<SearchMatch<TaxonDataset>, JSONObject>(bridge);
		JSONObjectListToJSONArrayBridge jsonArrBridge = new JSONObjectListToJSONArrayBridge();
		jsonArrBridge.convert(listBridge.convert(searchResults)).write(out);
	    }
	    finally {
		dao.dispose();
	    }
	}
	catch (JSONException json) {
	    throw new ServletException("A json exception was thrown", json);
	}
	catch (SQLException sql) {
	    throw new ServletException("An sql exception was thrown", sql);
	}
	finally {
            out.close();
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
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
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
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        processRequest(request, response);
    }

    /** 
     * Returns a short description of the servlet.
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "This servlet will provide suggestions to partly complete dataset search terms. These will be provided as a JSON object.";
    }// </editor-fold>

}
