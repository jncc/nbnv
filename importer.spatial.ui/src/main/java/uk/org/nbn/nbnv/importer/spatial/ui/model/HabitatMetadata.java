/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.org.nbn.nbnv.importer.spatial.ui.model;

import java.io.Serializable;

/**
 *
 * @author Paul Gilbertson
 */
public class HabitatMetadata implements Serializable {
    private int category;

    /**
     * @return the category
     */
    public int getCategory() {
        return category;
    }

    /**
     * @param category the category to set
     */
    public void setCategory(int category) {
        this.category = category;
    }
    
}
