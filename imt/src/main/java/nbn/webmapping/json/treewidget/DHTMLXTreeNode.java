
package nbn.webmapping.json.treewidget;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import nbn.common.bridging.Bridge;
import nbn.common.bridging.BridgingException;
import nbn.common.bridging.ListBridge;
import nbn.common.util.Pair;
import nbn.webmapping.json.bridge.JSONObjectListToJSONArrayBridge;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
*
* @author	    :- Christopher Johnson
* @date		    :- 06-Dec-2010
* @description	    :-
*/
public class DHTMLXTreeNode {
    public DHTMLXTreeNodeType type;
    public DHTMLXTreeNodeCheckedState state;
    public String text;
    public String id;
    public List<Pair<String,String>> userData;

    public DHTMLXTreeNode(String text, String id, boolean isChild) {
	this(text,id,isChild, DHTMLXTreeNodeCheckedState.UNCHECKED);
    }

    public DHTMLXTreeNode(String text, String id, boolean isChild, DHTMLXTreeNodeCheckedState state) {
	this(text,id,isChild,null, state);
    }

    public DHTMLXTreeNode(String text, String id, boolean isChild, List<Pair<String,String>> userData) {
        this(text,id,isChild,userData, DHTMLXTreeNodeCheckedState.UNCHECKED);
    }

    public DHTMLXTreeNode(String text, String id, boolean isChild, List<Pair<String,String>> userData, DHTMLXTreeNodeCheckedState state) {
	this.text = text;
	this.id = id;
	this.userData = userData;
        this.state = state;
	this.type = DHTMLXTreeNodeType.getDHTMLXTreeNodeType(isChild);
    }

    public static class JSONBridge implements Bridge<DHTMLXTreeNode, JSONObject> {
	public JSONObject convert(DHTMLXTreeNode toConvert) throws BridgingException {
	    try {
		JSONObject toReturn = new JSONObject();
		toReturn.put("id", toConvert.type.getJSONIDPrefix() + toConvert.id);
		toReturn.put("text",  toConvert.text);
		
		if(toConvert.type == DHTMLXTreeNodeType.PARENT)
		    toReturn.put("child", "1");

		if(toConvert.userData != null) {
		    ListBridge<Pair<String,String>, JSONObject> userdataListBridge = new ListBridge<Pair<String,String>, JSONObject>(new UserDataBridge());
		    List<JSONObject> userdataJSONObjectList = userdataListBridge.convert(toConvert.userData);
		    toReturn.put("userdata",new JSONObjectListToJSONArrayBridge().convert(userdataJSONObjectList));
		}

                if(toConvert.state != DHTMLXTreeNodeCheckedState.UNCHECKED)
                    toReturn.put("checked", toConvert.state.getJSONCheckedValue());

		return toReturn;
	    }
	    catch (JSONException ex) {
		throw new BridgingException("A JSON Exception Occured", ex);
	    }
	}
    }

    private static class UserDataBridge implements Bridge<Pair<String,String>, JSONObject> {
	public JSONObject convert(Pair<String, String> toConvert) throws BridgingException{
	    try {
		JSONObject toReturn = new JSONObject();
		toReturn.put("name", toConvert.getA());
		toReturn.put("content", toConvert.getB());
		return toReturn;
	    }
	    catch (JSONException ex) {
		throw new BridgingException("A JSON Exception Occured", ex);
	    }
	}
    }
}
