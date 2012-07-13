package uk.org.nbn.nbnv;

import java.util.Date;
import javax.persistence.EntityManager;
import uk.org.nbn.nbnv.jpa.nbncore.*;

public class TaxonDatasetLoader {

    /**
     * This function adds a new dataset entry with minimal data.
     * @param em            EntityManager with an open transaction
     * @param datasetKey    Eight character dataset identifier
     * @param datasetTitle  Dataset title
     */
    public static void upsertTaxonDataset(EntityManager em, String datasetKey, String datasetTitle) {
        // insert the top level object (it's a table-per-type model so need two tables)
        // allowRecordValidation = true, recordCount = 0 -- what do these mean?
        TaxonDataset td = new TaxonDataset(datasetKey, true, 0);
        Dataset d = new Dataset(datasetKey, true, new Date(), new Date(), datasetTitle);
        
        Organisation o = em.find(Organisation.class, 1);
        DatasetType taxonDatasetType = em.find(DatasetType.class, 'T');
        DatasetUpdateFrequency uf = em.find(DatasetUpdateFrequency.class, "012");
        Resolution res = em.find(Resolution.class, (short)0);
        
        d.setDatasetProvider(o);
        d.setDatasetTypeKey(taxonDatasetType);
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

    public static void upsertTaxonObservation(
            int observationID,
            int sampleID,
            String observationKey,
            Date dateStart,
            Date dateEnd,
            String dateType,
            int siteID,
            int featureID,
            String taxonVersionKey,
            boolean absenceRecord,
            boolean sensitiveRecord,
            int recordersID,
            int determinerID) {

        // todo...
//        TaxonObservation o = new TaxonObservation(
//                observationID,
//                observationKey ...);
    }
}
