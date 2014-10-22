/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.org.nbn.nbnv.importer.spatial.ui.model;

import java.io.Serializable;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import uk.org.nbn.nbnv.importer.s1.utils.database.DatabaseConnection;
import uk.org.nbn.nbnv.jpa.nbncore.HabitatCategory;

/**
 *
 * @author Paul Gilbertson
 */
public class HabitatForm implements Serializable {
    private List<HabitatCategory> categories;
    private HabitatMetadata metadata;
    
    public void fill() {
        EntityManager em = DatabaseConnection.getInstance().createEntityManager();
        Query q = em.createNamedQuery("HabitatCategory.findAll");
        categories = q.getResultList();
    }

    /**
     * @return the categories
     */
    public List<HabitatCategory> getCategories() {
        return categories;
    }

    /**
     * @param categories the categories to set
     */
    public void setCategories(List<HabitatCategory> categories) {
        this.categories = categories;
    }

    /**
     * @return the metadata
     */
    public HabitatMetadata getMetadata() {
        return metadata;
    }

    /**
     * @param metadata the metadata to set
     */
    public void setMetadata(HabitatMetadata metadata) {
        this.metadata = metadata;
    }
    
}
