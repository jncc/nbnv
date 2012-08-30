package nbn.webmapping.json.treewidget.impl.dataset;

import nbn.common.bridging.Bridge;
import nbn.common.organisation.Organisation;
import nbn.webmapping.json.treewidget.DHTMLXTreeNode;

public class OrganisationToDHTMLXTreeNodeBridge  implements Bridge<Organisation,DHTMLXTreeNode>{
    public DHTMLXTreeNode convert(Organisation toConvert) {
        return new DHTMLXTreeNode(toConvert.getOrganisationName(),Integer.toString(toConvert.getOrganisationKey()),false);
    }
}
