package nbn.webmapping.json.treewidget.impl.dataset;

import java.sql.SQLException;
import java.util.List;
import nbn.common.bridging.ListBridge;
import nbn.common.organisation.Organisation;
import nbn.common.organisation.*;
import nbn.common.dataset.*;
import nbn.webmapping.json.treewidget.DHTMLXTreeNode;
import nbn.webmapping.json.treewidget.DHTMLXTreeWidgetGenerationException;
import nbn.webmapping.json.treewidget.DHTMLXTreeWidgetGenerator;

public class DatasetDHTMLXTreeWidgetGenerator implements DHTMLXTreeWidgetGenerator {

    public List<DHTMLXTreeNode> getRootList() throws DHTMLXTreeWidgetGenerationException {
        OrganisationToDHTMLXTreeNodeBridge bridge = new OrganisationToDHTMLXTreeNodeBridge();
        ListBridge<Organisation, DHTMLXTreeNode> listBridge = new ListBridge<Organisation, DHTMLXTreeNode>(bridge);
        try {
            OrganisationDAO dao = new OrganisationDAO();
            try {
                return listBridge.convert(dao.getOrganisationsWithSpeciesDatasets());
            } finally {
                dao.dispose();
            }
        } catch (SQLException ex) {
            throw new DHTMLXTreeWidgetGenerationException("As SQLException has occured", ex);
        }

    }

    public List<DHTMLXTreeNode> getListFromID(String id) throws DHTMLXTreeWidgetGenerationException {
        DatasetToDHTMLXTreeNodeBridge bridge = new DatasetToDHTMLXTreeNodeBridge();
        ListBridge<TaxonDataset, DHTMLXTreeNode> listBridge = new ListBridge<TaxonDataset, DHTMLXTreeNode>(bridge);
        try {
            DatasetDAO dao = new DatasetDAO();
            try {
                return listBridge.convert(dao.getTaxonDatasetListByProvider(Integer.parseInt(id)));
            } finally {
                dao.dispose();
            }
        } catch (SQLException ex) {
            throw new DHTMLXTreeWidgetGenerationException("As SQLException has occured", ex);
        }
    }
}
