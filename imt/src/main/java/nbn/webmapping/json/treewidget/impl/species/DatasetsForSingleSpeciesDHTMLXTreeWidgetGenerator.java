package nbn.webmapping.json.treewidget.impl.species;

import nbn.webmapping.json.treewidget.impl.dataset.TaxonDatasetToDHTMLXTreeNodeBridge;
import java.sql.SQLException;
import java.lang.IllegalArgumentException;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import nbn.common.bridging.ListBridge;
import nbn.common.dataset.DatasetDAO;
import nbn.common.dataset.TaxonDataset;
import nbn.common.taxon.Taxon;
import nbn.common.taxon.TaxonDAO;
import nbn.common.user.User;
import nbn.common.user.UserDAO;
import nbn.webmapping.json.treewidget.ArgumentalDHTMLXtreeWidgetGenerator;
import nbn.webmapping.json.treewidget.DHTMLXTreeNode;
import nbn.webmapping.json.treewidget.DHTMLXTreeNodeCheckedState;
import nbn.webmapping.json.treewidget.DHTMLXTreeWidgetGenerationException;

public class DatasetsForSingleSpeciesDHTMLXTreeWidgetGenerator implements ArgumentalDHTMLXtreeWidgetGenerator{

    private final String TAXON_VERSION_KEY_ARG_NAME = "tvk";
    private final String USER_ARG_NAME = "user";
    private String _tvk;
    private int _userKey;

    public List<DHTMLXTreeNode> getRootList() throws DHTMLXTreeWidgetGenerationException {

        TaxonDatasetToDHTMLXTreeNodeBridge bridge = new TaxonDatasetToDHTMLXTreeNodeBridge(DHTMLXTreeNodeCheckedState.CHECKED);
        ListBridge<TaxonDataset,DHTMLXTreeNode> listBridge = new ListBridge<TaxonDataset,DHTMLXTreeNode>(bridge);
	try {
	    DatasetDAO datasetDao = new DatasetDAO();
            try{
                UserDAO userDao = new UserDAO();
                try {
                    User user = userDao.getUser(_userKey);
                    TaxonDAO taxonDao = new TaxonDAO();
                    try {
                        Taxon taxon = taxonDao.getTaxon(_tvk);
                        return listBridge.convert(datasetDao.getAllViewableTaxonDatasetsByTaxonWithAggregates(user, taxon));
                    }
                    finally {
                        taxonDao.dispose();
                    }

                } finally {
                    userDao.dispose();
                }
            } finally{
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
        if(request.getParameter(TAXON_VERSION_KEY_ARG_NAME)!=null){
            _tvk = request.getParameter(TAXON_VERSION_KEY_ARG_NAME);
        }else{
            throw new IllegalArgumentException("Mandatory species key not found in the request.");
        }
        if(request.getParameter(USER_ARG_NAME)!=null){
            _userKey = new Integer(request.getParameter(USER_ARG_NAME));
        }else{
            throw new IllegalArgumentException("Mandatory user key not found in the request");
        }

    }

}
