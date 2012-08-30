package nbn.webmapping.json.treewidget.impl.species;

import nbn.common.bridging.Bridge;
import nbn.common.taxoninputcategory.TaxonInputCategory;
import nbn.webmapping.json.treewidget.DHTMLXTreeNode;

public class TaxonInputCategoryToDHTMLXTreeNodeBridge implements Bridge<TaxonInputCategory, DHTMLXTreeNode> {

    private final int MAX_NUM_SPECIES_BEFORE_ADDING_ALPHABETIC_LEVEL = 200;

    public DHTMLXTreeNode convert(TaxonInputCategory toConvert) {
        String hasLevel2 = toConvert.hasLevel2() ? "T" : "F";
        String needsPaging = (hasLevel2.equalsIgnoreCase("F") && (toConvert.getSpeciesCount() > MAX_NUM_SPECIES_BEFORE_ADDING_ALPHABETIC_LEVEL)) ? "T" : "F";
        String hasLetter = "F";
        return new DHTMLXTreeNode(toConvert.getName(), hasLevel2 + needsPaging + hasLetter + toConvert.getKey(), false);
    }
}
