/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.org.nbn.nbnv.api.model.meta;

/**
 *
 * @author paulbe
 */
public class AccessRequestTaxonFilterJSON extends AccessRequestFilterJSON {
    private String tvk;

    /**
     * @return the tvk
     */
    public String getTvk() {
        return tvk;
    }

    /**
     * @param tvk the tvk to set
     */
    public void setTvk(String tvk) {
        this.tvk = tvk;
    }
    
}
