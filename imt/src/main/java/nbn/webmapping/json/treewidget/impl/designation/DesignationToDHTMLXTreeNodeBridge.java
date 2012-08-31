package nbn.webmapping.json.treewidget.impl.designation;

import nbn.common.bridging.Bridge;
import nbn.common.taxon.designation.Designation;
import nbn.webmapping.json.treewidget.DHTMLXTreeNode;

/**
*
* @author	    :- Christopher Johnson
* @date		    :- 26-Nov-2010
* @description	    :-
*/
public class DesignationToDHTMLXTreeNodeBridge implements Bridge<Designation,DHTMLXTreeNode> {
    public DHTMLXTreeNode convert(Designation toConvert) {
	StringBuilder textBuilder = new StringBuilder();
	textBuilder.append(toConvert.getName());
	textBuilder.append(": ");
	textBuilder.append(toConvert.getKey());

	return new DHTMLXTreeNode(textBuilder.toString(),toConvert.getKey(),true);
    }
}
