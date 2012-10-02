/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.org.nbn.nbnv.importer.ui.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import org.eclipse.persistence.internal.jpa.JPAQuery;
import uk.org.nbn.nbnv.importer.ui.util.DatabaseConnection;
import uk.org.nbn.nbnv.jpa.nbncore.Organisation;

/**
 *
 * @author Paul Gilbertson
 */
public class MetadataForm implements Serializable {
    private Metadata metadata;
    private boolean storedOrg;
    private List<String> errors;
    private List<Organisation> organisationList;
    
    public MetadataForm() {
        this.metadata = new Metadata();
        this.storedOrg = false;
        this.errors = new ArrayList<String>();
    }

    /**
     * @return the metadata
     */
    public Metadata getMetadata() {
        return metadata;
    }

    /**
     * @param metadata the metadata to set
     */
    public void setMetadata(Metadata metadata) {
        this.metadata = metadata;
    }

    /**
     * @return the errors
     */
    public List<String> getErrors() {
        return errors;
    }

    /**
     * @param errors the errors to set
     */
    public void setErrors(List<String> errors) {
        if (errors != null) {
            this.errors = errors;
        } else {
            this.errors = new ArrayList<String>();
        }
    }

    /**
     * @return the processed
     */
    public boolean hasStoredOrg() {
        return storedOrg;
    }

    /**
     * @param processed the processed to set
     */
    public void setStoredOrg(boolean storedOrg) {
        this.storedOrg = storedOrg;
    }

    /**
     * @return the organisationList
     */
    public List<Organisation> getOrganisationList() {
        return organisationList;
    }

    /**
     * @param organisationList the organisationList to set
     */
    public void setOrganisationList(List<Organisation> organisationList) {
        this.organisationList = organisationList;
    }
    
    public void updateOrganisationList() {
        EntityManager em = DatabaseConnection.getInstance().createEntityManager();
        
        Query q = em.createNamedQuery("Organisation.findAll");
        setOrganisationList(q.getResultList());
    }
}