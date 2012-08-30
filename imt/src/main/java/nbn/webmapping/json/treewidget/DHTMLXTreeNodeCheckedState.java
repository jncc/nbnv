package nbn.webmapping.json.treewidget;

/**
 *
 * @author Administrator
 */
public enum DHTMLXTreeNodeCheckedState {
    CHECKED(1),
    UNCHECKED(0),
    PARTIALLY_CHECKED(-1);

    private int type;

    private DHTMLXTreeNodeCheckedState(int type) {
	this.type = type;
    }

    public int getJSONCheckedValue() {
	return type;
    }

    public static DHTMLXTreeNodeCheckedState getDHTMLXTreeNodeType(int type) {
	switch(type) {
            case 1:
                return CHECKED;
            case 0:
                return UNCHECKED;
            case -1:
                return PARTIALLY_CHECKED;
            default:
                throw new IllegalArgumentException("The input type " + type + " does not represent any known DHTMLXTreeNodeCheckedState");
        }
    }
}
