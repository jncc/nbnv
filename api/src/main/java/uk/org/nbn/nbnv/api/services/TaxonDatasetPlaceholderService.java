package uk.org.nbn.nbnv.api.services;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.UUID;
import javax.annotation.PostConstruct;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uk.org.nbn.nbnv.api.dao.warehouse.OrganisationMapper;
import uk.org.nbn.nbnv.api.model.Organisation;
import uk.org.nbn.nbnv.api.model.TaxonDataset;

/**
 * The following service enables the placeholder creation of new taxon datasets.
 * In order to use the TaxonDatasetImporterService to upload a new nbn exchange 
 * format file, metadata about a taxon dataset must exist. In the case of updates
 * to a taxon dataset, these can be trivially retrieved from the database.
 * 
 * However when a new dataset is to be created, this metadata MUST (according to
 * the nbn gateway rules) be supplied via a word document.
 * 
 * This class will enable these word documents to be stored on disk and retrieved
 * as TaxonDataset objects.
 * @author cjohn
 */
@Service
public class TaxonDatasetPlaceholderService {
    @Autowired Properties properties;
    @Autowired TaxonDatasetMetadataArchiveService metadataFormService;
    @Autowired OrganisationMapper organisationMapper;
    
    private File datasetsPath;
    
    @PostConstruct
    public void init() {
        datasetsPath = new File(properties.getProperty("taxondataset_metadata_forms"));
    }
    
    /**
     * Stores the supplied archive input stream as a file on disk. The given 
     * file name for the archive will be generated based upon the owning 
     * organisation and a randomly assigned dataset key.
     * 
     * The archive will contain the word document (metadata species form) and a
     * json object of other data which can't be read from the metadata from.
     * 
     * This method will attempt to process the archive into a Taxon Dataset.
     * If that fails, an exception will be thrown and the archive will be
     * removed from disk.
     * 
     * @param organisationId who is the assigned owner of the new taxon dataset
     * @param wordDocument inputstream representing a word document in an archive
     * @return the newly generated dataset with placeholder datasetkey set
     * @throws IOException if failed to write the archive to disk
     */
    public TaxonDataset storeMetadataWordDocument(int organisationId, InputStream wordDocument) throws IOException {
        String placeholderKey = UUID.randomUUID().toString(); //generate a new id to store the incoming word document as
        File upload = getTaxonDatasetPlaceholderArchive(organisationId, placeholderKey);
        TaxonDataset dataset = null;
        try {
            //Copy the supplied input stream to disk, once stored we can try and
            //open it
            FileUtils.copyInputStreamToFile(wordDocument, upload);
            //Make sure that we can actually read the supplied document
            dataset = readTaxonDataset(organisationId, placeholderKey); 
            return dataset;
        }
        finally {
            if(dataset == null) {
                Files.delete(upload.toPath());
            }
        }
    }
    
    /**
     * Re-hydrates the specified metadata form which is registered against the
     * supplied organisation id as a TaxonDataset.
     * @param organisationId attributed to owning the dataset placeholder
     * @param datasetKey generated when the original archived word document was stored
     * @return the taxon dataset represented by the data in the word document or
     *  null if no taxon dataset could be found
     * @throws java.io.IOException if failed to read the document archive
     */
    public TaxonDataset readTaxonDataset(int organisationId, String datasetKey) throws IOException {
        File doc = getTaxonDatasetPlaceholderArchive(organisationId, datasetKey);
        if(doc.exists()) {
            Organisation organisation = organisationMapper.selectByID(organisationId);
            TaxonDataset dataset = metadataFormService.readWordDocument(doc, organisation);
            dataset.setKey(datasetKey);
            return dataset;
        }
        else {
            return null;
        }
    }
    
    /**
     * Reads the placeholder taxon dataset which is represented by the supplied
     * dataset key
     * @param datasetKey to locate
     * @return A taxon dataset representation of the word document combined with
     *  the json object from the archive or null if no dataset could be found
     * @throws java.io.IOException if failed to read the document archive
     */
    public TaxonDataset readTaxonDataset(String datasetKey) throws IOException {
        return readTaxonDataset(getOwningOrganisationAdmin(datasetKey), datasetKey);
    }
    
    /**
     * Obtains the organisations id which attributed as the owner of the 
     * supplied datasetkey. If the dataset is not known, this method will return -1
     * @param datasetKey to look for the owning organisation of
     * @return the owning organisation id or -1
     */
    public int getOwningOrganisationAdmin(String datasetKey) {
        File[] files = datasetsPath.listFiles(new DatasetFileFilter(datasetKey));
        if(files.length >= 1) {
            return Integer.parseInt(files[0].getName().split("-", 2)[0]);
        }
        else {
            return -1; //Not found
        }
    }
    
    /**
     * Re-hydrates a list of all the taxon datasets which have been attributed
     * to the given organisation id. 
     * @param organisationId which is attributed to placeholder datasets
     * @return a list of placeholder TaxonDatasets associated with the provided
     *  organisationId
     * @throws java.io.IOException if failed to read the document archive
     */
    public List<TaxonDataset> readTaxonDatasetsFor(int organisationId) throws IOException {
        File[] files = datasetsPath.listFiles(new OrganisationsDatasetMetadataFileFilter(organisationId));
        List<TaxonDataset> toReturn = new ArrayList<>();
        for(File metadataFile: files) {
            int start = metadataFile.getName().indexOf('-') + 1;
            int end = metadataFile.getName().indexOf('.');
            String datasetKey = metadataFile.getName().substring(start, end);
            TaxonDataset dataset = readTaxonDataset(organisationId, datasetKey);
            if(dataset != null) { //Files may have been removed post .listFiles
                toReturn.add(dataset);
            }
        }
        return toReturn;
    }
    
    // Grab the file on disk where a archive should be stored based on the
    // datasetkey and organisation id
    protected File getTaxonDatasetPlaceholderArchive(int organisation, String id) {
        return new File(datasetsPath, organisation + "-" + id + ".zip");
    }
    
    private static class OrganisationsDatasetMetadataFileFilter implements FileFilter {
        private final int organisationId;
        
        public OrganisationsDatasetMetadataFileFilter(int organisationId) {
            this.organisationId = organisationId;
        }
        
        @Override
        public boolean accept(File pathname) {
            return pathname.isFile() 
                    && pathname.getName().startsWith(organisationId + "-")
                    && pathname.getName().endsWith(".zip");
        }
    }
    
    private static class DatasetFileFilter implements FileFilter {
        private final String datasetKey;
        
        public DatasetFileFilter(String datasetKey) {
            this.datasetKey = datasetKey;
        }

        @Override
        public boolean accept(File pathname) {
            int start = pathname.getName().indexOf('-') + 1;
            return start != 0 //Check that there is a - in the name
                    && pathname.isFile()
                    && pathname.getName().substring(start).equalsIgnoreCase(datasetKey + ".zip");
        }
    }
}
