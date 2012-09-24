package nbn.webmapping.json.loadtime;

import java.io.IOException;
import java.util.Calendar;
import javax.servlet.http.HttpServletRequest;
import nbn.webmapping.json.bridge.CalendarToJSONObjectBridge;
import nbn.webmapping.json.bridge.ContextToJSONObjectBridge;
import org.json.JSONException;
import org.json.JSONObject;
import uk.gov.nbn.data.properties.PropertiesReader;
/**
 *
 * @author Christopher Johnson
 * @description This file is the supporting back end for the web side nbn.util.ServerGeneratedLoadTimeConstants.
 * It is thought that all properties that are required at load time by the IMT should be present inside
 * the output of this class.
 */
public class ServerGeneratedLoadTimeConstantsGenerator {
    private static final String IMT_PROPERTIES_FILE = "imt.properties";
    public static JSONObject getConstants(HttpServletRequest context) throws JSONException, IOException {
        JSONObject toReturn = new JSONObject(PropertiesReader.getEffectiveProperties(IMT_PROPERTIES_FILE));
        toReturn.put("date",new CalendarToJSONObjectBridge().convert(Calendar.getInstance()));
        toReturn.put("context",new ContextToJSONObjectBridge().convert(context));
        return toReturn;
    }

    public static String getConstantsAsJSONString(HttpServletRequest context) throws JSONException, IOException {
        return getConstants(context).toString();
    }
}