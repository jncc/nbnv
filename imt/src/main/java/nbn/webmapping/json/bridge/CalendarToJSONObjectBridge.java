package nbn.webmapping.json.bridge;

import java.util.Calendar;
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
public class CalendarToJSONObjectBridge implements Bridge<Calendar,JSONObject>{
    public JSONObject convert(Calendar toConvert) throws BridgingException {
        try {
            JSONObject toReturn = new JSONObject();
            toReturn.put("year", toConvert.get(Calendar.YEAR));
            toReturn.put("month", toConvert.get(Calendar.MONTH));
            toReturn.put("dayOfMonth", toConvert.get(Calendar.DAY_OF_MONTH));
            return toReturn;
        }
        catch (JSONException ex) {
            throw new BridgingException("A JSON exception has occured whilst converting a Calendar to a JSONObject",ex);
        }
    }

}
