package nbn.webmapping.json.treewidget.impl.designation;

import nbn.common.bridging.Bridge;
import nbn.common.taxon.designation.DesignationCategory;
import nbn.webmapping.json.treewidget.DHTMLXTreeNode;

/**
*
* @author	    :- Christopher Johnson
* @date		    :- 26-Nov-2010
* @description	    :-
*/
public class DesignationCategoryToDHTMLXTreeNodeBridge implements Bridge<DesignationCategory,DHTMLXTreeNode> {
    public DHTMLXTreeNode convert(DesignationCategory toConvert) {
	return new DHTMLXTreeNode(toConvert.getName(),Integer.toString(toConvert.getDesignationCategoryKey()),false);
    }
}
