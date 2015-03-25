package uk.org.nbn.nbnv.api.services;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Properties;
import org.apache.commons.io.IOUtils;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import org.mockito.Mock;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;
import uk.org.nbn.nbnv.api.model.TaxonDataset;

/**
 *
 * @author cjohn
 */
public class TaxonDatasetPlaceholderServiceTest {
    @Mock MetadataWordDocumentService metadataFormService;
    @Rule public TemporaryFolder folder = new TemporaryFolder();
    
    private TaxonDatasetPlaceholderService service;
    
    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
        service = new TaxonDatasetPlaceholderService();
        service.properties = new Properties();
        service.properties.setProperty("taxondataset_metadata_forms", folder.getRoot().getAbsolutePath());
        service.metadataFormService = metadataFormService;
        service.init();
    }
    
    @Test
    public void getOrganisationIdForOwnerOfPlaceholderDatasetKey() throws IOException {
        //Given
        folder.newFile("12-My-Metadata-Placeholder-Key.doc");
        
        //When
        int owningOrganisation = service.getOwningOrganisationAdmin("My-Metadata-Placeholder-Key");
        
        //Then
        assertEquals("Expected owning organisation to be 12", 12, owningOrganisation);
    }
    
    @Test
    public void checkThatdoesntGetOrganisationForMissingPlaceholderDataset() {
        //Given
        String datasetKey = "dataset-key-which-doesn't-exist";
        
        //When
        int owningOrganisation = service.getOwningOrganisationAdmin(datasetKey);
        
        //Then
        assertEquals("Expected to get not get an organisation", -1, owningOrganisation);
    }
    
    @Test
    public void checkThatCanWriteMetadataDocumentToDisk() throws IOException {
        //Given
        InputStream input = IOUtils.toInputStream("Not really a word document");
        int organisation = 7331;
        TaxonDataset dataset = mock(TaxonDataset.class);
        when(metadataFormService.readWordDocument(anyInt(), any(InputStream.class))).thenReturn(dataset);
        
        //When
        String datasetKey = service.storeMetadataWordDocument(organisation, input);
        
        //Then
        File wordDocument = service.getWordDocument(organisation, datasetKey);
        assertTrue("Expected the word document to exist", wordDocument.exists());
    }
    
    @Test
    public void checkThatFailureToReadMetadataFormYeildsNoWordDocumentOnDisk() {
         //Given
        InputStream input = IOUtils.toInputStream("Not really a word document");
        int organisation = 7331;
        when(metadataFormService.readWordDocument(anyInt(), any(InputStream.class)))
                .thenThrow(new IllegalArgumentException("Failed for some reason"));
        
        //When
        try {
            String datasetKey = service.storeMetadataWordDocument(organisation, input);
        }
        catch(Exception e) {}
        
        //Then
        String[] files = folder.getRoot().list();
        assertEquals("Expected no word documents to be stored", files.length, 0);
    }
    
    @Test
    public void checkThatCanReadGivenOrganisationsDatasetFile() throws IOException {
        //Given
        File wordDoc = folder.newFile("5000-SomeDataset.doc");
        TaxonDataset mockedDataset = mock(TaxonDataset.class);
        when(metadataFormService.readWordDocument(anyInt(), any(InputStream.class)))
                .thenReturn(mockedDataset);
        
        //When
        TaxonDataset dataset = service.readTaxonDataset(5000, "SomeDataset");
        
        //Then
        assertEquals("Expected mocked dataset", mockedDataset, dataset);
    }
    
    @Test(expected=FileNotFoundException.class)
    public void failsWhenAttemptingToReadMetadataDocumentWhichDoesntExist() throws FileNotFoundException {
        //Given
        String datasetKey = "something that doesn't exist";
        int organisationId = 4000;
        
        //When
        TaxonDataset dataset = service.readTaxonDataset(organisationId, datasetKey);
        
        //Then
        fail("Expected to not find the specified file");
    }
    
    @Test
    public void checkThatCanObtainAListOfDatasetsAttributedToAGivenOrganisationId() throws IOException {
        //Given
        folder.newFile("5000-SomeDataset1.doc");
        folder.newFile("5000-SomeDataset2.doc");
        folder.newFile("3333-Someoneelses.doc");
        
        //When
        List<TaxonDataset> datasets = service.readTaxonDatasetsFor(5000);
        
        //Then
        assertEquals("Expected only two datasets to be loaded", datasets.size(), 2);
    }
}
