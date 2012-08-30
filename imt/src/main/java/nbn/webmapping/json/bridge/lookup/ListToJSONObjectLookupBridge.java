
package nbn.webmapping.json.bridge.lookup;

import java.util.List;
import nbn.common.bridging.*;
import org.json.JSONException;
import org.json.JSONObject;

public class ListToJSONObjectLookupBridge<T,R> implements Bridge<List<? extends T>,JSONObject> {
    private Bridge<T, String> lookUpBridge;
    private Bridge<T, R> objectBridge;

    public ListToJSONObjectLookupBridge(NameLookupableBridge<T,R> lookupableBridge) {
        this(lookupableBridge.getNamedLookupBridge(),lookupableBridge);
    }
    
    public ListToJSONObjectLookupBridge(Bridge<T, String> lookUpBridge, Bridge<T, R> objectBridge) {
        this.lookUpBridge = lookUpBridge;
        this.objectBridge = objectBridge;
    }

    public JSONObject convert(List<? extends T> toConvert) throws BridgingException {
        try {
            JSONObject toReturn = new JSONObject();
            for(T currObj : toConvert)
                toReturn.put(lookUpBridge.convert(currObj), objectBridge.convert(currObj));
            return toReturn;
        }
        catch(JSONException jsonEx) {
            throw new BridgingException("A JSON Exception has occurred", jsonEx);
        }
    }
}

