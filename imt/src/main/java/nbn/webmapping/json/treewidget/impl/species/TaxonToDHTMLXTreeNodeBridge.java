/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package nbn.webmapping.json.treewidget.impl.species;

import nbn.common.bridging.Bridge;
import nbn.common.taxon.Taxon;
import nbn.webmapping.json.treewidget.DHTMLXTreeNode;

/**
 *
 * @author Administrator
 */
public class TaxonToDHTMLXTreeNodeBridge implements Bridge<Taxon,DHTMLXTreeNode>{
    public DHTMLXTreeNode convert(Taxon toConvert) {
        return new DHTMLXTreeNode(toConvert.getName(),toConvert.getTaxonVersionKey(),true);
    }
}
