package nbn.webmapping.json.treewidget.impl.species;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;
import nbn.common.bridging.ListBridge;
import nbn.common.taxon.Taxon;
import nbn.common.taxon.TaxonDAO;
import nbn.common.taxoninputcategory.TaxonInputCategory;
import nbn.common.taxoninputcategory.TaxonInputCategoryDAO;
import nbn.common.util.Pair;
import nbn.webmapping.json.treewidget.DHTMLXTreeNode;
import nbn.webmapping.json.treewidget.DHTMLXTreeWidgetGenerationException;
import nbn.webmapping.json.treewidget.DHTMLXTreeWidgetGenerator;

public class SpeciesDHTMLXTreeWidgetGenerator implements DHTMLXTreeWidgetGenerator {

    public List<DHTMLXTreeNode> getRootList() throws DHTMLXTreeWidgetGenerationException {
        TaxonInputCategoryToDHTMLXTreeNodeBridge bridge = new TaxonInputCategoryToDHTMLXTreeNodeBridge();
        ListBridge<TaxonInputCategory, DHTMLXTreeNode> listBridge = new ListBridge<TaxonInputCategory, DHTMLXTreeNode>(bridge);
        try {
            TaxonInputCategoryDAO dao = new TaxonInputCategoryDAO();
            try {
                return listBridge.convert(dao.getTaxonInputCategoryLevel1(true, false));
            } finally {
                dao.dispose();
            }
        } catch (SQLException ex) {
            throw new DHTMLXTreeWidgetGenerationException("As SQLException has occured", ex);
        }
    }

    public List<DHTMLXTreeNode> getListFromID(String id) throws DHTMLXTreeWidgetGenerationException {
        boolean hasLevel2 = (id.charAt(0) == 'T') ? true : false;
        boolean needsPaging = (id.charAt(1) == 'T') ? true : false;

        if (hasLevel2) {
            return getSecondLevelList(id.substring(3));
        } else if (needsPaging) {
            return getLetters(id.substring(3));
        } else {
            return getSpeciesLevelList(id);
        }
    }

    private List<DHTMLXTreeNode> getSecondLevelList(String id) throws DHTMLXTreeWidgetGenerationException {
        TaxonInputCategoryToDHTMLXTreeNodeBridge bridge = new TaxonInputCategoryToDHTMLXTreeNodeBridge();
        ListBridge<TaxonInputCategory, DHTMLXTreeNode> listBridge = new ListBridge<TaxonInputCategory, DHTMLXTreeNode>(bridge);
        try {
            TaxonInputCategoryDAO dao = new TaxonInputCategoryDAO();
            try {
                return listBridge.convert(dao.getTaxonInputCategoryLevel2(id, true, false));
            } finally {
                dao.dispose();
            }
        } catch (SQLException ex) {
            throw new DHTMLXTreeWidgetGenerationException("As SQLException has occured", ex);
        }
    }

    private List<DHTMLXTreeNode> getLetters(String taxonInputCategoryLevel2Key) throws DHTMLXTreeWidgetGenerationException {
        LettersToDHTMLXTreeNodeBridge bridge = new LettersToDHTMLXTreeNodeBridge();
        ListBridge<Pair<String, Character>, DHTMLXTreeNode> listBridge = new ListBridge<Pair<String, Character>, DHTMLXTreeNode>(bridge);
        try {
            TaxonInputCategoryDAO dao = new TaxonInputCategoryDAO();
            try {
                TreeSet<Character> speciesLetters = dao.getTaxonInputCategorySpeciesLetters(taxonInputCategoryLevel2Key, true);
                List<Pair<String, Character>> lettersWithCategoryKey = new ArrayList<Pair<String, Character>>();
                for (Character letter : speciesLetters) {
                    lettersWithCategoryKey.add(new Pair<String, Character>(taxonInputCategoryLevel2Key, letter));
                }
                return listBridge.convert(lettersWithCategoryKey);
            } finally {
                dao.dispose();
            }
        } catch (SQLException ex) {
            throw new DHTMLXTreeWidgetGenerationException("An SQLException has occured", ex);
        }
    }

    private List<DHTMLXTreeNode> getSpeciesLevelList(String id) throws DHTMLXTreeWidgetGenerationException {
        TaxonToDHTMLXTreeNodeBridge bridge = new TaxonToDHTMLXTreeNodeBridge();
        ListBridge<Taxon, DHTMLXTreeNode> listBridge = new ListBridge<Taxon, DHTMLXTreeNode>(bridge);

        boolean hasLetter = (id.charAt(2) == 'T') ? true : false;
        Character letter = getLetterFromId(id);
        String taxonInputCategoryLevel2Key;
        if (hasLetter) {
            taxonInputCategoryLevel2Key = id.substring(4);
        } else {
            taxonInputCategoryLevel2Key = id.substring(3);
        }

        try {
            TaxonDAO dao = new TaxonDAO();
            try {
                return listBridge.convert(dao.getTaxonNavigationList(taxonInputCategoryLevel2Key, true, letter));
            } finally {
                dao.dispose();
            }
        } catch (SQLException ex) {
            throw new DHTMLXTreeWidgetGenerationException("As SQLException has occured", ex);
        }
    }

    private Character getLetterFromId(String id) throws DHTMLXTreeWidgetGenerationException {
        boolean hasLetter = (id.charAt(2) == 'T') ? true : false;
        if (hasLetter) {
            return new Character(id.charAt(3));
        } else {
            return null;//null is what we want, the stored procedure expects null if no letter available
        }
    }
}
