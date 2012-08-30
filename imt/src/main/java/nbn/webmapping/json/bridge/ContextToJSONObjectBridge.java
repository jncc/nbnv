package nbn.webmapping.json.bridge;

import javax.servlet.http.HttpServletRequest;
import nbn.common.bridging.Bridge;
import nbn.common.bridging.BridgingException;
import org.json.JSONException;
import org.json.JSONObject;


/**
 *
 * @author	    :- Christopher Johnson
 * @date            :- 16th-May-2011
 * @description	    :-
 */
public class ContextToJSONObjectBridge implements Bridge<HttpServletRequest,JSONObject>{
    public JSONObject convert(HttpServletRequest toConvert) throws BridgingException {
        try {
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
        catch (JSONException ex) {
            throw new BridgingException("A JSON exception has occured whilst converting a Calendar to a JSONObject",ex);
        }
    }

}
