/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.org.nbn.nbnv.api.model;

/**
 *
 * @author Paul Gilbertson
 */
public class TaxonObservationFilter {
    private int id;
    private String filterJSON;
    private String filterText;

    /**
     * @return the id
     */
    public int getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * @return the filterJSON
     */
    public String getFilterJSON() {
        return filterJSON;
    }

    /**
     * @param filterJSON the filterJSON to set
     */
    public void setFilterJSON(String filterJSON) {
        this.filterJSON = filterJSON;
    }

    /**
     * @return the filterText
     */
    public String getFilterText() {
        return filterText;
    }

    /**
     * @param filterText the filterText to set
     */
    public void setFilterText(String filterText) {
        this.filterText = filterText;
    }
}
