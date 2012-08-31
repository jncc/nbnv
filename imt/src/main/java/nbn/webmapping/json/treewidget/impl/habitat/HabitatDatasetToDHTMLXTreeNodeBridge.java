package nbn.webmapping.json.treewidget.impl.habitat;

import java.util.ArrayList;
import java.util.List;
import nbn.common.bridging.Bridge;
import nbn.common.dataset.HabitatDataset;
import nbn.common.util.Pair;
import nbn.webmapping.json.treewidget.DHTMLXTreeNode;
/**
*
* @author	    :- Christopher Johnson
* @date		    :- 26-Nov-2010
* @description	    :-
*/
public class HabitatDatasetToDHTMLXTreeNodeBridge implements Bridge<HabitatDataset,DHTMLXTreeNode> {
    public DHTMLXTreeNode convert(HabitatDataset toConvert) {
	Pair<String,String> key = new Pair<String,String>("datasetKey",toConvert.getDatasetKey());
	List<Pair<String,String>> userData = new ArrayList<Pair<String,String>>();
	userData.add(key);

	return new DHTMLXTreeNode(toConvert.getDatasetTitle(),toConvert.getGisLayerID(),true,userData);
    }
}
