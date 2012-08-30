package nbn.webmapping.json.treewidget.impl.species;

import nbn.common.bridging.Bridge;
import nbn.webmapping.json.treewidget.DHTMLXTreeNode;
import nbn.common.util.Pair;

public class LettersToDHTMLXTreeNodeBridge implements Bridge<Pair<String,Character>, DHTMLXTreeNode>{

    public DHTMLXTreeNode convert(Pair<String,Character> toConvert) {
        String hasLevel2 = "F";;
        String needsPaging = "F";
        String hasLetter = "T";
        String level2Key = toConvert.getA();
        Character letter = toConvert.getB();
        String keyForNode = hasLevel2 + needsPaging + hasLetter + letter.toString() + level2Key;

        return new DHTMLXTreeNode(letter.toString(),keyForNode,false);
    }
}
