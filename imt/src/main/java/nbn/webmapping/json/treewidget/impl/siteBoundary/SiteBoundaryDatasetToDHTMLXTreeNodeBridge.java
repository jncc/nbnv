package nbn.webmapping.json.treewidget.impl.siteBoundary;

import java.util.ArrayList;
import java.util.List;
import nbn.common.bridging.Bridge;
import nbn.common.dataset.SiteBoundaryDataset;
import nbn.common.util.Pair;
import nbn.webmapping.json.treewidget.DHTMLXTreeNode;

/**
*
* @author	    :- Christopher Johnson
* @date		    :- 26-Nov-2010
* @description	    :-
*/
public class SiteBoundaryDatasetToDHTMLXTreeNodeBridge implements Bridge<SiteBoundaryDataset,DHTMLXTreeNode> {
    public DHTMLXTreeNode convert(SiteBoundaryDataset toConvert) {
	Pair<String,String> key = new Pair<String,String>("datasetKey",toConvert.getDatasetKey());
	List<Pair<String,String>> userData = new ArrayList<Pair<String,String>>();
	userData.add(key);
	return new DHTMLXTreeNode(toConvert.getDatasetTitle(),toConvert.getGisLayerID(),true,userData);
    }
}
