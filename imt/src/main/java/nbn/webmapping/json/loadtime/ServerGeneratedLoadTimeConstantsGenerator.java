package nbn.webmapping.json.loadtime;

import java.io.IOException;
import java.util.Calendar;
import javax.servlet.http.HttpServletRequest;
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
        toReturn.put("date", convert(Calendar.getInstance()));
        toReturn.put("context",convert(context));
        return toReturn;
    }
    
    public static JSONObject convert(Calendar toConvert) throws JSONException {
        JSONObject toReturn = new JSONObject();
        toReturn.put("year", toConvert.get(Calendar.YEAR));
        toReturn.put("month", toConvert.get(Calendar.MONTH));
        toReturn.put("dayOfMonth", toConvert.get(Calendar.DAY_OF_MONTH));
        return toReturn;
    }

    public static JSONObject convert(HttpServletRequest toConvert) throws JSONException {
        JSONObject toReturn = new JSONObject();
        toReturn.put("scheme", toConvert.getScheme());
        toReturn.put("server", toConvert.getServerName());
        toReturn.put("port", toConvert.getServerPort());
        toReturn.put("context", toConvert.getContextPath());

        StringBuilder appPathBuilder = new StringBuilder();
        appPathBuilder.append(toConvert.getScheme());
        appPathBuilder.append("://");
        appPathBuilder.append(toConvert.getServerName());
        if(toConvert.getServerPort()!=80) {
            appPathBuilder.append(":");
            appPathBuilder.append(toConvert.getServerPort());
        }
        appPathBuilder.append(toConvert.getContextPath());

        toReturn.put("appPath", appPathBuilder.toString());
        return toReturn;
    }
    public static String getConstantsAsJSONString(HttpServletRequest context) throws JSONException, IOException {
        return getConstants(context).toString();
    }
}