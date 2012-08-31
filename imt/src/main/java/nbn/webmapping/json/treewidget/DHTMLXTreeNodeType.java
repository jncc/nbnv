package nbn.webmapping.json.treewidget;

/**
 *
 * @author Administrator
 */
public enum DHTMLXTreeNodeType {
    PARENT(false),
    CHILD(true);

    private static final char PARENT_NODE_PREFIX = 'P';
    private static final char CHILD_NODE_PREFIX = 'C';

    private boolean isChild;

    private DHTMLXTreeNodeType(boolean isChild) {
	this.isChild = isChild;
    }

    public String removeJSONIDPrefix(String prefixedID) {
	if(prefixedID.charAt(0)==getJSONIDPrefix())
	    return prefixedID.substring(1);
	else
	    throw new IllegalArgumentException("The input id " + prefixedID + " is not prefixed by this DHTMLXTreeNodeType");
    }
    public char getJSONIDPrefix() {
	return (isChild) ? CHILD_NODE_PREFIX : PARENT_NODE_PREFIX;
    }

    public static DHTMLXTreeNodeType getDHTMLXTreeNodeType(boolean isChild) {
	return (isChild) ? CHILD : PARENT;
    }
}
