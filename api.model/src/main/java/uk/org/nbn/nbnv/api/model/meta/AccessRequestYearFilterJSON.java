/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.org.nbn.nbnv.api.model.meta;

/**
 *
 * @author paulbe
 */
public class AccessRequestYearFilterJSON extends AccessRequestFilterJSON {
    private int startYear;
    private int endYear;

    /**
     * @return the startYear
     */
    public int getStartYear() {
        return startYear;
    }

    /**
     * @param startYear the startYear to set
     */
    public void setStartYear(int startYear) {
        this.startYear = startYear;
    }

    /**
     * @return the endYear
     */
    public int getEndYear() {
        return endYear;
    }

    /**
     * @param endYear the endYear to set
     */
    public void setEndYear(int endYear) {
        this.endYear = endYear;
    }
    
}
