/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.org.nbn.nbnv.api.model.meta;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Matt Debont
 */
@XmlRootElement
public class AccessRequestModifiedTimeJSON extends BaseFilterJSON {
    private RequestReasonJSON reason;
    private AccessRequestTimeLimitStringJSON time;

    public AccessRequestModifiedTimeJSON(AccessRequestJSON json, String date) {
        this.dataset = json.getDataset();
        this.polygon = json.getPolygon();
        this.reason = json.getReason();
        this.sensitive = json.getSensitive();
        this.spatial = json.getSpatial();
        this.taxon = json.getTaxon();
        this.year = json.getYear();
        
        AccessRequestTimeLimitStringJSON newTime = new AccessRequestTimeLimitStringJSON();
        
        if (!json.getTime().isAll()) {
            newTime.setAll(false);
            newTime.setDate(date);
        }
        
        this.time = newTime;
    }
    
    /**
     * @return the reason
     */
    public RequestReasonJSON getReason() {
        return reason;
    }

    /**
     * @param reason the reason to set
     */
    public void setReason(RequestReasonJSON reason) {
        this.reason = reason;
    }

    /**
     * @return the time
     */
    public AccessRequestTimeLimitStringJSON getTime() {
        return time;
    }

    /**
     * @param time the time to set
     */
    public void setTime(AccessRequestTimeLimitStringJSON time) {
        this.time = time;
    }
    
    @Override
    public BaseFilterJSON toBaseFilter() {
        return this;
    }
}
