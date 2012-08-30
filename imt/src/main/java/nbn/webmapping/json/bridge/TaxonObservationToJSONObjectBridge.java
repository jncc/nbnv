/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package nbn.webmapping.json.bridge;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import nbn.common.bridging.AbstractNameLookupableBridge;
import nbn.common.bridging.BridgingException;
import nbn.common.taxon.DateType;
import nbn.common.taxon.Taxon;
import nbn.common.taxon.TaxonObservation;
import nbn.common.taxon.TaxonPresence;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author Administrator
 */
public class TaxonObservationToJSONObjectBridge extends AbstractNameLookupableBridge<TaxonObservation, JSONObject> {

    public JSONObject convert(TaxonObservation toConvert) throws BridgingException{
        try {
            JSONObject toReturn = new JSONObject();
            toReturn.put("species", getTaxonName(toConvert.getTaxon()));
            toReturn.put("recorder", toConvert.getRecorder());
            toReturn.put("determiner", toConvert.getDeterminer());
            toReturn.put("date", dateToString(toConvert));
            toReturn.put("tocID", toConvert.getObservationID());
            toReturn.put("sensitive", toConvert.isSensitiveRecord());
            toReturn.put("site", toConvert.getSite().getSiteName());
            toReturn.put("presence", toConvert instanceof TaxonPresence);
            return toReturn;
        } catch (JSONException jsone) {
            throw new BridgingException("A JSON Exception has occured", jsone);
        }
    }

    private String getTaxonName(Taxon toConvert) {
        if((toConvert.getCommonName() != null) && (!toConvert.getCommonName().equals(""))){
            return toConvert.getName() + " (" + toConvert.getCommonName() + ")";
        } else {
            return toConvert.getName();
        }
    }

    private String dateToString(TaxonObservation obs) {
        StringBuilder sb = new StringBuilder();

        DateFormat dayf = new SimpleDateFormat("yyyy-MM-dd");
        DateFormat monthf = new SimpleDateFormat("yyyy-MM");
        DateFormat yearf = new SimpleDateFormat("yyyy");

        sb.append("Date: ");

        if (obs.getDateType() == DateType.Day) {
            sb.append(dayf.format(obs.getStartDate()));
            sb.append(" (Day)");
        } else if (obs.getDateType() == DateType.DayRange) {
            sb.append(dayf.format(obs.getStartDate()));
            sb.append("/");
            sb.append(dayf.format(obs.getEndDate()));
            sb.append(" (Day Range)");
        } else if (obs.getDateType() == DateType.Month) {
            sb.append(monthf.format(obs.getStartDate()));
            sb.append(" (Month)");
        } else if (obs.getDateType() == DateType.MonthRange) {
            sb.append(monthf.format(obs.getStartDate()));
            sb.append("/");
            sb.append(monthf.format(obs.getEndDate()));
            sb.append(" (Month Range)");
        } else if (obs.getDateType() == DateType.Year) {
            sb.append(yearf.format(obs.getStartDate()));
            sb.append(" (Year)");
        } else if (obs.getDateType() == DateType.YearRange) {
            sb.append(yearf.format(obs.getStartDate()));
            sb.append("/");
            sb.append(yearf.format(obs.getEndDate()));
            sb.append(" (Year Range)");
        } else if (obs.getDateType() == DateType.BeforeYear) {
            sb.append("<= ");
            sb.append(yearf.format(obs.getEndDate()));
            sb.append(" (To Year)");
        } else if (obs.getDateType() == DateType.AfterYear) {
            sb.append(">= ");
            sb.append(yearf.format(obs.getStartDate()));
            sb.append(" (From Year)");
        } else {
            sb.append("No Date");
        }

        return sb.toString();
    }

    @Override
    public String getLookupableName(TaxonObservation toName) {
        return Integer.toString(toName.getObservationID());
    }
}
