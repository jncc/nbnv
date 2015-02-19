package uk.org.nbn.nbnv.api.services;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Properties;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.mockito.MockitoAnnotations;
import uk.org.nbn.nbnv.api.model.ValidationError;

/**
 *
 * @author cjohn
 */
public class DatasetImporterServiceTest {
    @Rule public TemporaryFolder folder = new TemporaryFolder();
    
    private DatasetImporterService service;
    
    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
        folder.newFolder("uploads");
        folder.newFolder("queue");
        folder.newFolder("processing");
        folder.newFolder("completed");
        
        service = new DatasetImporterService();
        service.properties = new Properties();
        service.properties.setProperty("importer_location", folder.getRoot().getAbsolutePath());
    }
    
    @Test
    public void checkCanGetCurrentlyProcessedDataset() throws IOException {
        //Given
        folder.newFile("processing/log-mydataset.zip.2015.02.19.14.06.07.log");
        
        //When
        String dataset = service.getCurrentlyProcessedDataset();
        
        //Then
        assertEquals("Expected my dataset to be processed",  "mydataset", dataset);
    }
    
    @Test
    public void checkNullWhenNoDatasetIsBeingProcessed() {
        //Given
        //nothing
        
        //When
        String dataset = service.getCurrentlyProcessedDataset();
        
        //Then
        assertNull("Expected no dataset to be being processed", dataset);
    }
    
    @Test
    public void checkIfTheQueuedDatasetCanBeFound() throws IOException {
        //Given
        String datasetKey = "queuedup";
        folder.newFile("queue/" + datasetKey + ".zip");
        
        //When
        boolean queued = service.isQueued(datasetKey);
        
        //Then
        assertTrue("The dataset key should be queued", queued);
    }
    
    @Test
    public void checkUnqueuedDatasetIsntReportedAsQueued() throws IOException {
        //Given
        String datasetKey = "notQueued";
        
        //When
        boolean queued = service.isQueued(datasetKey);
        
        //Then
        assertFalse("The dataset is not queued", queued);
    }
    
    @Test
    public void checkCanReadValidationErrors() throws IOException {
        //Given
        URL classLoader = getClass().getResource("/test-data/importerValidationError.log");
        File validationErrorsLog = new File(classLoader.getFile());
        
        //When
        List<ValidationError> validationErrors = service.getValidationErrors(validationErrorsLog);
        
        //Then
        assertEquals("Expected 4 errors", 4, validationErrors.size());
        ValidationError error = validationErrors.get(0);
        
        assertEquals("Expected to read line number", error.getLineNumber(), 3);
        assertEquals("Expected to read rule", error.getRule(), "NBNV-73");
        assertEquals("Expected to read message", error.getMessage(), "The same date must be used for both the StartDate and EndDate for a date with DateType 'Some(D)'");
    }
}
