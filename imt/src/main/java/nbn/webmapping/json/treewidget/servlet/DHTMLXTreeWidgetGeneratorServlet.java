/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package nbn.webmapping.json.treewidget.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import nbn.common.bridging.ListBridge;
import nbn.webmapping.json.bridge.JSONObjectListToJSONArrayBridge;
import nbn.webmapping.json.treewidget.ArgumentalDHTMLXtreeWidgetGenerator;
import nbn.webmapping.json.treewidget.DHTMLXTreeNode;
import nbn.webmapping.json.treewidget.DHTMLXTreeNodeType;
import nbn.webmapping.json.treewidget.DHTMLXTreeWidgetGenerationException;
import nbn.webmapping.json.treewidget.DHTMLXTreeWidgetGenerator;
import nbn.webmapping.json.treewidget.impl.species.DatasetsForSingleSpeciesDHTMLXTreeWidgetGenerator;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author Administrator
 */
public class DHTMLXTreeWidgetGeneratorServlet extends HttpServlet {
    public static final String TREE_ROOT_ID = "0";
    private static final String TREE_GENERATION_TYPE = "type";

    private void generateXMLWidgetResponse(List<DHTMLXTreeNode> toConvert, PrintWriter out) throws JSONException {
	generateXMLWidgetResponse(toConvert,out,TREE_ROOT_ID);
    }
    
    private void generateXMLWidgetResponse(List<DHTMLXTreeNode> toConvert, PrintWriter out, String id) throws JSONException {
	ListBridge<DHTMLXTreeNode,JSONObject> dhtmlToJSONListBridget = new ListBridge<DHTMLXTreeNode,JSONObject>(new DHTMLXTreeNode.JSONBridge());

	JSONObject treeContainer = new JSONObject();
	JSONObjectListToJSONArrayBridge jsonArrBridge = new JSONObjectListToJSONArrayBridge();

	treeContainer.put("id",id);
	treeContainer.put("item",jsonArrBridge.convert(dhtmlToJSONListBridget.convert(toConvert)));
	treeContainer.write(out);
    }
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
	    DHTMLXTreeWidgetServletType toSwitchOn = DHTMLXTreeWidgetServletType.getDHTMLXTreeWidgetServletTypeRepresentedBy(request.getParameter(TREE_GENERATION_TYPE)); //get the correct DHTMLXWidget servlet type (This should be a factory)
	    DHTMLXTreeWidgetGenerator servletGenerator = toSwitchOn.getServletGenerator(); //get the servlet generator for this type
	    String id = request.getParameter("id"); //get the requested id

            if(servletGenerator instanceof ArgumentalDHTMLXtreeWidgetGenerator){
                ((ArgumentalDHTMLXtreeWidgetGenerator)servletGenerator).setArgs(request);
            }

	    if(id==null || id.equals(TREE_ROOT_ID))
		generateXMLWidgetResponse(servletGenerator.getRootList(),out);
	    else {
		DHTMLXTreeNodeType parentType = DHTMLXTreeNodeType.PARENT; //this request is not of type root, it must be parent
		List<DHTMLXTreeNode> listFromID = servletGenerator.getListFromID(parentType.removeJSONIDPrefix(id)); //remove the parent prefix and get the list from the original id
		generateXMLWidgetResponse(listFromID,out,id);  //generate a response prefixed with the requested id
	    }
	}
	catch (JSONException ex) {
	    throw new ServletException("A JSON exception has occured",ex);
	}
	catch (DHTMLXTreeWidgetGenerationException ex) {
	    throw new ServletException("A DHTMLXTreeWidgetGenerationException exception has occured",ex);
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
