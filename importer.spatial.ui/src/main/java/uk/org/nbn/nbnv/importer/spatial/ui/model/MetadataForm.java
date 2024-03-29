/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.org.nbn.nbnv.importer.spatial.ui.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import uk.org.nbn.nbnv.importer.s1.utils.database.DatabaseConnection;
import uk.org.nbn.nbnv.jpa.nbncore.Dataset;
import uk.org.nbn.nbnv.jpa.nbncore.DatasetType;
import uk.org.nbn.nbnv.jpa.nbncore.Organisation;

/**
 *
 * @author Paul Gilbertson
 */
public class MetadataForm implements Serializable {
    private Metadata metadata = new Metadata();;
    private boolean storedOrg = false;
    private List<String> errors = new ArrayList<String>();
    private List<Organisation> organisationList;
    private List<Dataset> datasets;
    private boolean orgError = false;
    private boolean datasetError = false;
    private boolean datasetUpdate = false;
    private boolean spatialError = false;
    
    public MetadataForm() {
        
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

    public boolean isSpatialError() {
        return spatialError;
    }

    public void setSpatialError(boolean spatialError) {
        this.spatialError = spatialError;
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
    public boolean getStoredOrg() {
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
        
        Query q = em.createNamedQuery("Organisation.findAllNameSort");
        setOrganisationList(q.getResultList());
    }
    
    public boolean getOrgError() {
        return orgError;
    }

    public void setOrgError(boolean orgError) {
        this.orgError = orgError;
    }
    
    public List<Dataset> getDatasets() {
        return datasets;
    }
    
    public void setDatasets(List<Dataset> datasets) {
        this.datasets = datasets;
    }
    
    public void updateDatasests() {
        EntityManager em = DatabaseConnection.getInstance().createEntityManager();
        Query q = em.createNamedQuery("DatasetType.findByKey");
        q.setParameter("key", 'H');
        List<Dataset> lis = new ArrayList<Dataset>(((DatasetType)q.getSingleResult()).getDatasetCollection());
        q.setParameter("key", 'A');
        lis.addAll(((DatasetType)q.getSingleResult()).getDatasetCollection());
        lis.add(0, new Dataset(""));
        setDatasets(lis);
    }

    public boolean getDatasetError() {
        return datasetError;
    }

    public void setDatasetError(boolean datasetError) {
        this.datasetError = datasetError;
    }
    
    public void setDatasetUpdate(boolean datasetUpdate) {
        this.datasetUpdate = datasetUpdate;
    }
    
    public boolean getDatasetUpdate() {
        return this.datasetUpdate;
    }
    
    public void resetForm() {
        this.storedOrg = false;
        this.errors = new ArrayList<String>();
        this.orgError = false;
        this.datasetError = false;
    }
}