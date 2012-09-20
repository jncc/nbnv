package nbn.webmapping.json.loadtime;

import java.util.Calendar;
import javax.servlet.http.HttpServletRequest;
import nbn.common.util.properties.ServerSpecificProperties;
import nbn.webmapping.json.bridge.CalendarToJSONObjectBridge;
import nbn.webmapping.json.bridge.ContextToJSONObjectBridge;
import nbn.webmapping.json.bridge.PathToProtocolSpecifiedStringBridge;
import org.json.JSONException;
import org.json.JSONObject;
/**
 *
 * @author Christopher Johnson
 * @description This file is the supporting back end for the web side nbn.util.ServerGeneratedLoadTimeConstants.
 * It is thought that all properties that are required at load time by the IMT should be present inside
 * the output of this class.
 */
public class ServerGeneratedLoadTimeConstantsGenerator {
    public static JSONObject getConstants(HttpServletRequest context) throws JSONException {
        JSONObject toReturn = new JSONObject();
        toReturn.put("date",new CalendarToJSONObjectBridge().convert(Calendar.getInstance()));
		toReturn.put("context",new ContextToJSONObjectBridge().convert(context));
        toReturn.put("gisServers", new PathToProtocolSpecifiedStringBridge("http").convert("staging.testnbn.net/gis"));
        return toReturn;
    }

    public static String getConstantsAsJSONString(HttpServletRequest context) throws JSONException {
        return getConstants(context).toString();
    }
}