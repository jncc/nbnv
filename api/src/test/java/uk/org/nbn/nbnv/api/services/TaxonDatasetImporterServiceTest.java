package uk.org.nbn.nbnv.api.services;

import freemarker.template.TemplateException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.net.URL;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.NoSuchFileException;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Properties;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.codehaus.jackson.map.ObjectMapper;
import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import static org.mockito.Mockito.mock;
import uk.org.nbn.nbnv.api.model.ImporterResult;
import uk.org.nbn.nbnv.api.model.ImporterResult.State;
import static uk.org.nbn.nbnv.api.model.ImporterResult.State.BAD_FILE;
import static uk.org.nbn.nbnv.api.model.ImporterResult.State.MISSING_SENSITIVE_COLUMN;
import static uk.org.nbn.nbnv.api.model.ImporterResult.State.SUCCESSFUL;
import static uk.org.nbn.nbnv.api.model.ImporterResult.State.VALIDATION_ERRORS;
import uk.org.nbn.nbnv.api.model.TaxonDataset;
import uk.org.nbn.nbnv.api.model.ValidationError;
import uk.org.nbn.nbnv.api.nxf.NXFReader;

/**
 *
 * @author cjohn
 */
public class TaxonDatasetImporterServiceTest {
    @Rule public TemporaryFolder folder = new TemporaryFolder();
    
    private TaxonDatasetImporterService service;
    
    @Before
    public void init() {
        folder.newFolder("uploads");
        folder.newFolder("queue");
        folder.newFolder("processing");
        folder.newFolder("completed");
        folder.newFolder("issues");
        folder.newFolder("archived");
        
        service = new TaxonDatasetImporterService();
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
        List<ImporterResult> history = service.getImportHistory("myDatasetKey");
        
        //Then
        assertEquals("Has ony entry in map", 1, history.size());
        assertEquals("Has timestamp as key", history.get(0).getTimestamp(),"201502191415031682");
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void checkThatEmptyReaderResultsInError() throws IOException, TemplateException {
        //Given
        NXFReader reader = mock(NXFReader.class);
        TaxonDataset dataset = mock(TaxonDataset.class);
        
        //When
        service.importDataset(reader, dataset, true);
        
        //Then
        fail("Expected empty reader to throw illegal argument exception");
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
    
    @Test(expected=NoSuchFileException.class)
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
    
    @Test(expected=NoSuchFileException.class)
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
    public void canDetectImportWithBadDataStructure() throws IOException {
        //Given
        URL url = getClass().getResource("/test-data/ConsoleErrors-invalid-structure.txt");        
        File errors = new File(url.getFile());
        
        //When
        State state = service.getImporterResultState(errors);
        
        //Then
        assertEquals("Should have detected that the import log stated this was a bad file", BAD_FILE, state);
    }
    
    @Test
    public void canDetectImportInValidationErrors() throws IOException {
        //Given
        URL url = getClass().getResource("/test-data/invalid-import/ConsoleErrors.txt");
        File errors = new File(url.getFile());
        
        //When
        State state = service.getImporterResultState(errors);
        
        //Then
        assertEquals("Should have detected valdiation errors for import", VALIDATION_ERRORS, state);
    }
    
    @Test
    public void canDetectSuccessfulImport() throws IOException {
        //Given
        URL url = getClass().getResource("/test-data/valid-import/ConsoleErrors.txt");
        File errors = new File(url.getFile());
        
        //When
        State state = service.getImporterResultState(errors);
        
        //Then
        assertEquals("Should have detected a successful import", SUCCESSFUL, state);
    }
    
    @Test
    public void canQueueUpDatasetUpload() throws IOException, TemplateException {
        //Given 
        List<String> expectedDataTab = IOUtils.readLines(getClass().getResourceAsStream("/test-data/GA000466-normalised.nxf"));
        List<String> expectedEml = IOUtils.readLines(getClass().getResourceAsStream("/test-data/GA000466-eml.xml"));
        List<String> expectedMappings = IOUtils.readLines(getClass().getResourceAsStream("/test-data/GA000466-meta.xml"));
        
        InputStream datasetStream = getClass().getResourceAsStream("/test-data/GA000466.json");
        TaxonDataset dataset = new ObjectMapper().readValue(datasetStream, TaxonDataset.class);
        InputStream nxfFile = getClass().getResourceAsStream("/test-data/GA000466.nxf");
        NXFReader nxf = new NXFReader(new LineNumberReader(new InputStreamReader(nxfFile)));
        
        Calendar date = Calendar.getInstance();
        date.set(2014, 11, 05); //Months are zero indexes
        dataset.setDateUploaded(date.getTime());
        
        //When
        service.importDataset(nxf, dataset, true);
        
        //Then
        ZipFile archive = new ZipFile(new File(folder.getRoot(), "queue/GA000466.zip"));
        List<String> dataTab = IOUtils.readLines(archive.getInputStream(archive.getEntry("data.tab")));
        List<String> eml  = IOUtils.readLines(archive.getInputStream(archive.getEntry("eml.xml")));
        List<String> mappings  = IOUtils.readLines(archive.getInputStream(archive.getEntry("meta.xml")));
        
        assertEquals("Expected to read dataTab from archive", expectedDataTab, dataTab);
        assertEquals("Expected to read mappings from archive", expectedMappings, mappings);
        assertEquals("Expected to read eml from archive", expectedEml, eml);
    }
    
    @Test
    public void checkCanRemoveADatasetFromTheQueue() throws IOException {
        //Given
        folder.newFile("queue/queuedKey.zip");
        
        //When
        boolean success = service.removeFromQueue("queuedKey");
        
        //Then
        assertTrue("Expected to remove the dataset", success);
    }
    
    @Test
    public void checkThatFailsWhenRemovingDatasetWhichIsNotInQueue() throws IOException {
        //Given
        //Nothing
        //When
        boolean success = service.removeFromQueue("somethingNotThere");
        
        //Then
        assertFalse("Can't remove file which is not there", success);
    }
    
    @Test
    public void checkThatMissingSensitiveColumnGetsPutIntoTheIssuesDirectory() throws IOException, TemplateException {
        //Given
        InputStream datasetStream = getClass().getResourceAsStream("/test-data/GA000466.json");
        TaxonDataset dataset = new ObjectMapper().readValue(datasetStream, TaxonDataset.class);
        InputStream nxfFile = getClass().getResourceAsStream("/test-data/GA000466-no-sensitive.nxf");
        NXFReader nxf = new NXFReader(new LineNumberReader(new InputStreamReader(nxfFile)));
        
        //When
        service.importDataset(nxf, dataset, true);
        
        //Then
        List<ImporterResult> history = service.getImportHistory("GA000466");
        assertEquals("Expected only one result", history.size(), 1);
        assertEquals("Expected result to have failed with missing column", history.get(0).getState(), MISSING_SENSITIVE_COLUMN);
        
        File[] filesInIssues = new File(folder.getRoot(), "issues").listFiles();
        assertEquals("Expected one file in issue", 1, filesInIssues.length);
    }
    
    @Test
    public void checkThatSensitiveColumnCanBeAppendedToImportWhichPreviouslyFailed() throws IOException, TemplateException {
        //Given
        File archive = folder.newFile("issues/GA000466-timestamp-MISSING_SENSITIVE_COLUMN.zip");
        try (ZipOutputStream out = new ZipOutputStream(new FileOutputStream(archive))) {
            out.putNextEntry(new ZipEntry("data.tab"));
            IOUtils.write("HEADING\ndata", out);
            out.putNextEntry(new ZipEntry("eml.xml"));
            IOUtils.write("leave as is", out);
        }
        
        //When
        service.queueDatasetWithSensitiveColumnSet("GA000466", "timestamp", true);
        
        //Then
        ZipFile fixedArchive = new ZipFile(new File(folder.getRoot(), "queue/GA000466.zip"));
        List<String> dataTab = IOUtils.readLines(fixedArchive.getInputStream(fixedArchive.getEntry("data.tab")));
        List<String> eml  = IOUtils.readLines(fixedArchive.getInputStream(fixedArchive.getEntry("eml.xml")));
        String mappings  = IOUtils.toString(fixedArchive.getInputStream(fixedArchive.getEntry("meta.xml")));
        
        assertEquals("Expected sensitive column to be added", dataTab, Arrays.asList("HEADING\tSENSITIVE", "data\ttrue"));
        assertEquals("Expected eml to be left as is", eml, Arrays.asList("leave as is"));
        assertTrue("Expected the sensitive column to be mapped", mappings.contains("sensitiveOccurrence"));
    }
    
    @Test(expected=FileNotFoundException.class)
    public void checkThatFailsToSetSensitiveColumnForFileWhichDoesNotExist() throws IOException, TemplateException {
        //Given
        String datasetKey = "never uploaded";
        String timestamp = "never uploaded";
        
        //When
        service.queueDatasetWithSensitiveColumnSet(datasetKey, timestamp, true);
        
        //Then
        fail("Expected to fail as file could not have been found");
    }
    
    @Test
    public void checkThatCanArchiveSuccessfulResults() throws IOException {
        //Given
        folder.newFolder("completed/DATASET-201503201542082419");
        folder.newFile("completed/DATASET-201503201542082419/ConsoleErrors.txt");
        folder.newFile("completed/DATASET-201503201542082419/ConsoleOutput.txt");
        
        //When
        service.removeImporterResult("DATASET", "201503201542082419", SUCCESSFUL);
        
        //Then
        File archived = new File(folder.getRoot(), "archived/DATASET-201503201542082419");
        assertTrue("Expected the result to have been archived", archived.exists());
        assertTrue("Expected the result to be a directory still", archived.isDirectory());
    }
    
    @Test
    public void checkThatResultFromTheIssuesDirectoryCanBeRemoved() throws IOException {
        //Given
        File issue = folder.newFile("issues/DATASET-201503191619484227-MISSING_SENSITIVE_COLUMN.zip");
        
        //When
        service.removeImporterResult("DATASET", "201503191619484227", MISSING_SENSITIVE_COLUMN);
        
        //Then
        assertFalse("Expected the file to have been deleted", issue.exists());
    }
    
    @Test
    public void checkThatValidationErrorsGetsRemoved() throws IOException {
        //Given
        File validationErrors = folder.newFolder("completed/DATASET-201503201542082419");
        File errorsLog = folder.newFile("completed/DATASET-201503201542082419/ConsoleErrors.txt");
        folder.newFile("completed/DATASET-201503201542082419/ConsoleOutput.txt");
        FileUtils.write(errorsLog, "validation errors");
        
        //When
        service.removeImporterResult("DATASET", "201503201542082419", VALIDATION_ERRORS);
        
        //Then
        assertFalse("Expected the result to have been archived", validationErrors.exists());
    }
    
    @Test(expected=NoSuchFileException.class)
    public void checkThatFailsToRemoveResultWhichDoesNotExist() throws IOException {
        //Given
        //Nothing
        
        //When
        service.removeImporterResult("DATASET", "201503201542082419", SUCCESSFUL);
        
        //Then
        fail("Expected to fail as no result is present");
    }
}
