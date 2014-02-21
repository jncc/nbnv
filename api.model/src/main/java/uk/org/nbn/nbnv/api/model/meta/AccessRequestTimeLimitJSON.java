/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.org.nbn.nbnv.api.model.meta;

import java.util.Date;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author paulbe
 */
@XmlRootElement
public class AccessRequestTimeLimitJSON extends AccessRequestFilterJSON {
    private Date date;

    /**
     * @return the date
     */
    public Date getDate() {
        return date;
    }

    /**
     * @param date the date to set
     */
    public void setDate(Date date) {
        this.date = date;
    }
    
}
