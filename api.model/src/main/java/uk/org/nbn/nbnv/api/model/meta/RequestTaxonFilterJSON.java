/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.org.nbn.nbnv.api.model.meta;

import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author paulbe
 */
@XmlRootElement
public class RequestTaxonFilterJSON extends AccessRequestFilterJSON {
    private String tvk = "";
    private String designation = "";
    private String output = "";
    private int orgSuppliedList = -1;

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

    public int getOrgSuppliedList() {
        return orgSuppliedList;
    }

    public void setOrgSuppliedList(int orgSuppliedList) {
        this.orgSuppliedList = orgSuppliedList;
    }
}
