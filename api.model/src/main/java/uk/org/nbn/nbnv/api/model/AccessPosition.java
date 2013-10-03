/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.org.nbn.nbnv.api.model;

/**
 *
 * @author paulbe
 */
public class AccessPosition {
    private String filterText;
    private String owner;

    /**
     * @return the owner
     */
    public String getOwner() {
        return owner;
    }

    /**
     * @param owner the owner to set
     */
    public void setOwner(String owner) {
        this.owner = owner;
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
