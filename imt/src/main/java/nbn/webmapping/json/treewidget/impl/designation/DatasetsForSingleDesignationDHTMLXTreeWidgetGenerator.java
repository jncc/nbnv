package nbn.webmapping.json.treewidget.impl.designation;

import nbn.webmapping.json.treewidget.impl.dataset.TaxonDatasetToDHTMLXTreeNodeBridge;
import java.sql.SQLException;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import nbn.common.bridging.ListBridge;
import nbn.common.dataset.DatasetDAO;
import nbn.common.dataset.TaxonDataset;
import nbn.common.taxon.designation.Designation;
import nbn.common.taxon.designation.DesignationDAO;
import nbn.common.user.User;
import nbn.common.user.UserDAO;
import nbn.webmapping.json.treewidget.ArgumentalDHTMLXtreeWidgetGenerator;
import nbn.webmapping.json.treewidget.DHTMLXTreeNode;
import nbn.webmapping.json.treewidget.DHTMLXTreeNodeCheckedState;
import nbn.webmapping.json.treewidget.DHTMLXTreeWidgetGenerationException;

public class DatasetsForSingleDesignationDHTMLXTreeWidgetGenerator implements ArgumentalDHTMLXtreeWidgetGenerator{

    private final String DESIGNATION_KEY_ARG_NAME = "desig";
    private final String USER_ARG_NAME = "user";
    private String _designationKey;
    private int _userKey;

    public List<DHTMLXTreeNode> getRootList() throws DHTMLXTreeWidgetGenerationException {

        TaxonDatasetToDHTMLXTreeNodeBridge bridge = new TaxonDatasetToDHTMLXTreeNodeBridge(DHTMLXTreeNodeCheckedState.CHECKED);
        ListBridge<TaxonDataset,DHTMLXTreeNode> listBridge = new ListBridge<TaxonDataset,DHTMLXTreeNode>(bridge);
	try {
            DatasetDAO datasetDao = new DatasetDAO();
            try{
                UserDAO userDao = new UserDAO();
                try{
                    DesignationDAO designationDAO = new DesignationDAO();
                    try {
                        User user = userDao.getUser(_userKey);
                        Designation desig = designationDAO.getDesignation(_designationKey);
                        return listBridge.convert(datasetDao.getAllViewableTaxonDatasetsByExtantSpeciesDesignation(user, desig));
                    } finally {
                        if(designationDAO != null)
                            designationDAO.dispose();
                    }
                }finally{
                    if(userDao != null)
                        userDao.dispose();
                }
            }finally{
                if(datasetDao != null)
                    datasetDao.dispose();
            }
        }
	catch (SQLException ex) {
	    throw new DHTMLXTreeWidgetGenerationException("As SQLException has occured", ex);
	}
    }

    public List<DHTMLXTreeNode> getListFromID(String id) throws DHTMLXTreeWidgetGenerationException {
        return null;
    }

    public void setArgs(HttpServletRequest request) throws NumberFormatException {
        if(request.getParameter(DESIGNATION_KEY_ARG_NAME)!=null){
            _designationKey = request.getParameter(DESIGNATION_KEY_ARG_NAME);
        }else{
            throw new IllegalArgumentException("Mandatory designation key not found in the request.");
        }
        if(request.getParameter(USER_ARG_NAME)!=null){
            _userKey = new Integer(request.getParameter(USER_ARG_NAME));
        }else{
            throw new IllegalArgumentException("Mandatory user key not found in the request");
        }

    }

}
