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
    private String tvk = "";
    private String designation = "";
    private String output = "";

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

    /**
     * @return the designation
     */
    public String getDesignation() {
        return designation;
    }

    /**
     * @param designation the designation to set
     */
    public void setDesignation(String designation) {
        this.designation = designation;
    }

    /**
     * @return the output
     */
    public String getOutput() {
        return output;
    }

    /**
     * @param output the output to set
     */
    public void setOutput(String output) {
        this.output = output;
    }
    
}
