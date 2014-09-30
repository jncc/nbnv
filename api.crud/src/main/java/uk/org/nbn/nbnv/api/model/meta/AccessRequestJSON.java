/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.org.nbn.nbnv.api.model.meta;

import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Paul Gilbertson
 */
@XmlRootElement
public class AccessRequestJSON extends BaseFilterJSON {
    private RequestReasonJSON reason;
    private AccessRequestTimeLimitJSON time;

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
    public AccessRequestTimeLimitJSON getTime() {
        return time;
    }

    /**
     * @param time the time to set
     */
    public void setTime(AccessRequestTimeLimitJSON time) {
        this.time = time;
    }
    
    @Override
    public BaseFilterJSON toBaseFilter() {
        return this;
    }
}
