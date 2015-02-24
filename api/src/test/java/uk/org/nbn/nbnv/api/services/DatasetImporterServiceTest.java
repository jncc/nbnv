package uk.org.nbn.nbnv.api.services;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import org.apache.commons.io.FileUtils;
import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import uk.org.nbn.nbnv.api.model.ImporterResult;
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
        URL url = getClass().getResource("/test-data/invalid-import/ConsoleOutput.txt");
        File validationErrorsLog = new File(url.getFile());
        
        //When
        List<ValidationError> validationErrors = service.getValidationErrors(validationErrorsLog);
        
        //Then
        assertEquals("Expected 4 errors", 4, validationErrors.size());
        ValidationError error = validationErrors.get(0);
        
        assertEquals("Expected to read record key", error.getRecordKey(), "3");
        assertEquals("Expected to read rule", error.getRule(), "NBNV-73");
        assertEquals("Expected to read message", error.getMessage(), "The same date must be used for both the StartDate and EndDate for a date with DateType 'Some(D)'");
    }
    
    @Test
    public void checkCanReadHistoryOfOldImports() throws IOException {
        //Given
        folder.newFolder("completed/myDatasetKey-201502191415031682");
        URL output = getClass().getResource("/test-data/invalid-import/ConsoleOutput.txt");
        FileUtils.copyURLToFile(output, folder.newFile("completed/myDatasetKey-201502191415031682/ConsoleOutput.txt"));
        URL errors = getClass().getResource("/test-data/invalid-import/ConsoleErrors.txt");
        FileUtils.copyURLToFile(errors, folder.newFile("completed/myDatasetKey-201502191415031682/ConsoleErrors.txt"));
        
        //When
        Map<String, ImporterResult> history = service.getImportHistory("myDatasetKey");
        
        //Then
        assertEquals("Has ony entry in map", 1, history.size());
        assertTrue("Has timestamp as key", history.containsKey("201502191415031682"));
    }
    
    @Test
    public void checkCanStripErrorsFromArchivedImport() throws IOException {
        //Given
        File archived = folder.newFolder("completed/invalid-201502191415031682");
        URL testArchive = getClass().getResource("/test-data/invalid-import");
        FileUtils.copyDirectory(new File(testArchive.getFile()), archived);
        
        ZipFile originalArchive = new ZipFile(new File(archived, "invalid.zip"));
        
        //When
        service.stripInvalidRecords("invalid", "201502191415031682");
        
        //Then
        File queuedImport = new File(folder.getRoot(), "queue/invalid.zip");
        assertTrue("The queued dataset exists", queuedImport.exists());
        ZipFile zipFile = new ZipFile(queuedImport);
        ZipEntry dataTab = zipFile.getEntry("data.tab");
        assertNotNull("Expected to file data.tab", dataTab);
        assertNotNull("Expected to file eml.xmk", zipFile.getEntry("eml.xml"));
        assertNotNull("Expected to file meta.xml", zipFile.getEntry("meta.xml"));
        assertThat("Expected datatab not to be empty", dataTab.getSize(), not(equalTo(0L)));
        assertTrue("Expected datatab to be smaller", dataTab.getSize() < originalArchive.getEntry("data.tab").getSize());
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void checkFailsToReprocessSuccessfulImport() throws IOException {
        //Given
        File archived = folder.newFolder("completed/valid-201502191415031682");
        URL testArchive = getClass().getResource("/test-data/valid-import");
        FileUtils.copyDirectory(new File(testArchive.getFile()), archived);
        
        //When
        service.stripInvalidRecords("valid", "201502191415031682");
        
        //Then
        fail("Expected to fail with an illegal argument exception");
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void checkFailsToReprocessMissingImport() throws IOException {
        //Given
        //Nothing
        
        //When
        service.stripInvalidRecords("missing", "201502191415031682");
        
        //Then
        fail("Expected to fail with an illegal argument exception");
    }
    
    @Test(expected=FileAlreadyExistsException.class)
    public void checkThatFailsToProcessArchiveWhenDatasetIsAlreadyOnTheQueue() throws IOException {
        //Given
        File archived = folder.newFolder("completed/invalid-201502191415031682");
        URL testArchive = getClass().getResource("/test-data/invalid-import");
        URL oldImport = getClass().getResource("/test-data/invalid-import/invalid.zip");
        FileUtils.copyDirectory(new File(testArchive.getFile()), archived);
        FileUtils.copyFile(new File(oldImport.getFile()), new File(folder.getRoot(), "queue/invalid.zip"));
        
        //When
        service.stripInvalidRecords("invalid", "201502191415031682");
        
        //Then
        fail("Expected to fail with exception");
    }
    
    @Test
    public void canDetectImportInErrors() throws IOException {
        //Given
        URL url = getClass().getResource("/test-data/invalid-import/ConsoleErrors.txt");
        File errors = new File(url.getFile());
        
        //When
        boolean success = service.isSuccessfulImport(errors);
        
        //Then
        assertFalse("Should not have been success", success);
    }
    
        
    @Test
    public void canDetectSuccessfulImport() throws IOException {
        //Given
        URL url = getClass().getResource("/test-data/valid-import/ConsoleErrors.txt");
        File errors = new File(url.getFile());
        
        //When
        boolean success = service.isSuccessfulImport(errors);
        
        //Then
        assertTrue("Should have been success", success);
    }
}
