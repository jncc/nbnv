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
public class DownloadFilterJSON extends BaseFilterJSON {
    private DownloadReasonJSON reason;

    /**
     * @return the reason
     */
    public DownloadReasonJSON getReason() {
        return reason;
    }

    /**
     * @param reason the reason to set
     */
    public void setReason(DownloadReasonJSON reason) {
        this.reason = reason;
    }
    
    @Override
    public BaseFilterJSON toBaseFilter() {
        return this;
    }
}