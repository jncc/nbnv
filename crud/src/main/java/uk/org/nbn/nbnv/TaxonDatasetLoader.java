/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.org.nbn.nbnv;

import java.util.Date;
import javax.persistence.EntityManager;
import uk.org.nbn.nbnv.jpa.nbncore.*;

/**
 * A dataset loading class for TaxonDataset metadata.
 * 
 * @author Paul Gilbertson
 * @version %I%, %G%
 * @since   1.0
 */
public class TaxonDatasetLoader {
    
    /**
     * This function adds a new dataset entry with minimal data.
     * 
     * Currently the following fields are hard coded :-
     *      Organisation : Organisation 1
     *      DatasetType : 'T' - a taxon dataset
     *      UpdateFrequency : '012' - unknown
     *      publicResolution : Unknown
     *      maxResolution : Unknown
     * 
     * @param em            EntityManager with an open transaction
     * @param datasetKey    Eight character dataset identifier
     * @param datasetTitle  Dataset title
     */
    public static void upsertTaxonDataset(EntityManager em, String datasetKey, String datasetTitle) {
        TaxonDataset td = new TaxonDataset(datasetKey, true, 0);
        Dataset d = new Dataset(datasetKey, true, new Date(), new Date(), datasetTitle);
        
        Organisation o = em.find(Organisation.class, 1);
        DatasetType dt = em.find(DatasetType.class, 'T');
        DatasetUpdateFrequency uf = em.find(DatasetUpdateFrequency.class, "012");
        Resolution res = em.find(Resolution.class, (short)0);
        
        d.setDatasetProvider(o);
        d.setDatasetTypeKey(dt);
        d.setUpdateFrequency(uf);
        td.setDataset(d);
        td.setMaxResolution(res);
        td.setPublicResolution(res);

        em.merge(td);
    }
    
    /**
     * This function removes a dataset from the system. Transaction will need 
     * committing when all changes are completed
     * 
     * @param em            EnityManager instance with an open transaction
     * @param datasetKey    Eight character dataset identifier
     */
    public static void deleteTaxonDataset(EntityManager em, String datasetKey) {
        em.remove(em.find(TaxonDataset.class, datasetKey));
        em.remove(em.find(Dataset.class, datasetKey));
    }
}
