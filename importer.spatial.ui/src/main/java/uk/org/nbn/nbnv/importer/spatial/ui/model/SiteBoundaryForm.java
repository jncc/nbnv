/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.org.nbn.nbnv.importer.spatial.ui.model;

import java.io.Serializable;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import uk.org.nbn.nbnv.importer.spatial.ui.util.DatabaseConnection;
import uk.org.nbn.nbnv.jpa.nbncore.SiteBoundaryCategory;
import uk.org.nbn.nbnv.jpa.nbncore.SiteBoundaryType;

/**
 *
 * @author Paul Gilbertson
 */
public class SiteBoundaryForm implements Serializable {
    private List<SiteBoundaryCategory> categories;
    private List<SiteBoundaryType> types;
    private SiteBoundaryMetadata metadata;
    
    public void fill() {
        EntityManager em = DatabaseConnection.getInstance().createEntityManager();
        Query q = em.createNamedQuery("SiteBoundaryCategory.findAll");
        categories = q.getResultList();
        
        Query qt = em.createNamedQuery("SiteBoundaryType.findAll");
        types = qt.getResultList();
    }

    /**
     * @return the categories
     */
    public List<SiteBoundaryCategory> getCategories() {
        return categories;
    }

    /**
     * @param categories the categories to set
     */
    public void setCategories(List<SiteBoundaryCategory> categories) {
        this.categories = categories;
    }

    /**
     * @return the types
     */
    public List<SiteBoundaryType> getTypes() {
        return types;
    }

    /**
     * @param types the types to set
     */
    public void setTypes(List<SiteBoundaryType> types) {
        this.types = types;
    }

    /**
     * @return the metadata
     */
    public SiteBoundaryMetadata getMetadata() {
        return metadata;
    }

    /**
     * @param metadata the metadata to set
     */
    public void setMetadata(SiteBoundaryMetadata metadata) {
        this.metadata = metadata;
    }
}
