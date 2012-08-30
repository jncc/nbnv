
package nbn.webmapping.json.treewidget.impl.siteBoundary;

import java.sql.SQLException;
import java.util.List;
import nbn.common.bridging.ListBridge;
import nbn.common.dataset.DatasetDAO;
import nbn.common.dataset.SiteBoundaryDataset;
import nbn.webmapping.json.treewidget.DHTMLXTreeNode;
import nbn.webmapping.json.treewidget.DHTMLXTreeWidgetGenerationException;
import nbn.webmapping.json.treewidget.DHTMLXTreeWidgetGenerator;

/**
*
* @author	    :- Christopher Johnson
* @date		    :- 06-Dec-2010
* @description	    :-
*/
public class SiteBoundaryDHTMLXTreeWidgetGenerator implements DHTMLXTreeWidgetGenerator {
    public List<DHTMLXTreeNode> getRootList() throws DHTMLXTreeWidgetGenerationException {
	SiteBoundaryDatasetToDHTMLXTreeNodeBridge bridge = new SiteBoundaryDatasetToDHTMLXTreeNodeBridge();
	ListBridge<SiteBoundaryDataset,DHTMLXTreeNode> listBridge = new ListBridge<SiteBoundaryDataset,DHTMLXTreeNode>(bridge);
	try {
	    DatasetDAO dao = new DatasetDAO();
	    try {
		return listBridge.convert(dao.getAllSiteBoundaryDatasets());
	    }
	    finally {
		dao.dispose();
	    }
	}
	catch (SQLException ex) {
	    throw new DHTMLXTreeWidgetGenerationException("As SQLException has occured", ex);
	}
    }

    public List<DHTMLXTreeNode> getListFromID(String id) {
	throw new UnsupportedOperationException("Not supported yet.");
    }
}
