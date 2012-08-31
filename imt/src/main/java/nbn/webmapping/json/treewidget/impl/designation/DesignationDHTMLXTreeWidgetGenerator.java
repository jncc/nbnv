package nbn.webmapping.json.treewidget.impl.designation;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import nbn.common.bridging.ListBridge;
import nbn.common.taxon.designation.Designation;
import nbn.common.taxon.designation.DesignationCategory;
import nbn.common.taxon.designation.DesignationCategoryDAO;
import nbn.common.taxon.designation.DesignationDAO;
import nbn.webmapping.json.treewidget.DHTMLXTreeNode;
import nbn.webmapping.json.treewidget.DHTMLXTreeWidgetGenerationException;
import nbn.webmapping.json.treewidget.DHTMLXTreeWidgetGenerator;

/**
 *
 * @author	    :- Christopher Johnson
 * @date		    :- 06-Dec-2010
 * @description	    :-
 */
public class DesignationDHTMLXTreeWidgetGenerator implements DHTMLXTreeWidgetGenerator {

    public List<DHTMLXTreeNode> getRootList() throws DHTMLXTreeWidgetGenerationException {
        DesignationCategoryToDHTMLXTreeNodeBridge bridge = new DesignationCategoryToDHTMLXTreeNodeBridge();
        ListBridge<DesignationCategory, DHTMLXTreeNode> listBridge = new ListBridge<DesignationCategory, DHTMLXTreeNode>(bridge);
        try {
            DesignationCategoryDAO dao = new DesignationCategoryDAO();
            try {
                List<DHTMLXTreeNode> nodes = new ArrayList<DHTMLXTreeNode>();
                nodes.add(new DHTMLXTreeNode("<em>Popular Designations</em>", "-1", false));
                nodes.addAll(listBridge.convert(dao.getAllExtantDesignationCategories(false)));
                return nodes;
            } finally {
                if(dao != null)
                    dao.dispose();
            }
        } catch (SQLException ex) {
            throw new DHTMLXTreeWidgetGenerationException("As SQLException has occured", ex);
        }
    }

    public List<DHTMLXTreeNode> getListFromID(String id) throws DHTMLXTreeWidgetGenerationException {
        if (id.contentEquals("-1")) {
            return getPopularList();
        }

        DesignationToDHTMLXTreeNodeBridge bridge = new DesignationToDHTMLXTreeNodeBridge();
        ListBridge<Designation, DHTMLXTreeNode> listBridge = new ListBridge<Designation, DHTMLXTreeNode>(bridge);
        try {
            DesignationCategoryDAO dao = new DesignationCategoryDAO();
            try {
                DesignationDAO ddao = new DesignationDAO();
                try {
                    DesignationCategory wantedCategory = dao.getDesignationCategory(Integer.parseInt(id), false);
                    return listBridge.convert(ddao.getExtantDesignationsWithRecords(wantedCategory));
                } finally {
                    if(ddao != null)
                        ddao.dispose();
                }
            } finally {
                if(dao != null)
                    dao.dispose();
            }
        } catch (SQLException ex) {
            throw new DHTMLXTreeWidgetGenerationException("As SQLException has occured", ex);
        } catch (NumberFormatException nfe) {
            throw new DHTMLXTreeWidgetGenerationException("The input id is not an integer and is therefore not a valid id", nfe);
        }
    }

    private List<DHTMLXTreeNode> getPopularList() throws DHTMLXTreeWidgetGenerationException {
        DesignationToDHTMLXTreeNodeBridge bridge = new DesignationToDHTMLXTreeNodeBridge();
        ListBridge<Designation, DHTMLXTreeNode> listBridge = new ListBridge<Designation, DHTMLXTreeNode>(bridge);
        try {
            DesignationDAO ddao = new DesignationDAO();
            try {
                return listBridge.convert(ddao.getSiteReportDesignations());
            } finally {
                if(ddao != null)
                    ddao.dispose();
            }
        } catch (SQLException ex) {
            throw new DHTMLXTreeWidgetGenerationException("As SQLException has occured", ex);
        } catch (NumberFormatException nfe) {
            throw new DHTMLXTreeWidgetGenerationException("The input id is not an integer and is therefore not a valid id", nfe);
        }
    }
}
