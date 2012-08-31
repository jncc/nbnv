package nbn.webmapping.json.treewidget;
import java.util.List;

/**
 *
 * @author Administrator
 */
public interface DHTMLXTreeWidgetGenerator {
    public List<DHTMLXTreeNode> getRootList() throws DHTMLXTreeWidgetGenerationException;
    public List<DHTMLXTreeNode> getListFromID(String id) throws DHTMLXTreeWidgetGenerationException;
}
