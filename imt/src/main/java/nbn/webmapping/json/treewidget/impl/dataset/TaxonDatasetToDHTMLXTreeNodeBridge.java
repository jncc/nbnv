package nbn.webmapping.json.treewidget.impl.dataset;

import java.util.ArrayList;
import java.util.List;
import nbn.common.bridging.Bridge;
import nbn.common.dataset.TaxonDataset;
import nbn.common.util.Pair;
import nbn.webmapping.json.treewidget.DHTMLXTreeNode;
import nbn.webmapping.json.treewidget.DHTMLXTreeNodeCheckedState;

public class TaxonDatasetToDHTMLXTreeNodeBridge implements Bridge<TaxonDataset, DHTMLXTreeNode>{
    private DHTMLXTreeNodeCheckedState initialState;

    public TaxonDatasetToDHTMLXTreeNodeBridge(DHTMLXTreeNodeCheckedState initialState) {
        this.initialState = initialState;
    }

    public DHTMLXTreeNode convert(TaxonDataset toConvert) {
        List<Pair<String,String>> userData = new ArrayList<Pair<String,String>>();
        userData.add(new Pair<String,String>("name",toConvert.getDatasetTitle()));
        StringBuilder sb = new StringBuilder();
        sb.append("<strong>").append(toConvert.getDatasetProvider().getOrganisationName()).append("</strong>")
                .append(": ")
                .append(toConvert.getDatasetTitle())
                .append(" ");
        return new DHTMLXTreeNode("<a href=\"http://data.nbn.org.uk/datasetInfo/taxonDataset.jsp?dsKey=" + toConvert.getDatasetKey() + "\" target=\"_blank\">Info</a> - " + sb.toString(), toConvert.getDatasetKey(), true, userData, initialState);
    }
}
