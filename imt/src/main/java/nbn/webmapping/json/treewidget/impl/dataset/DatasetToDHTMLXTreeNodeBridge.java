package nbn.webmapping.json.treewidget.impl.dataset;

import java.util.ArrayList;
import java.util.List;
import nbn.common.bridging.Bridge;
import nbn.common.dataset.TaxonDataset;
import nbn.common.util.Pair;
import nbn.webmapping.json.treewidget.DHTMLXTreeNode;

public class DatasetToDHTMLXTreeNodeBridge implements Bridge<TaxonDataset,DHTMLXTreeNode>{
    public DHTMLXTreeNode convert(TaxonDataset toConvert) {
        List<Pair<String,String>> userData = new ArrayList<Pair<String,String>>();
        userData.add(new Pair<String,String>("name",toConvert.getDatasetTitle()));
        return new DHTMLXTreeNode("<a href=\"http://data.nbn.org.uk/datasetInfo/taxonDataset.jsp?dsKey=" + toConvert.getDatasetKey() + "\" target=\"_blank\">Info</a> - " + toConvert.getDatasetTitle(),toConvert.getDatasetKey(),true,userData);
    }
}
