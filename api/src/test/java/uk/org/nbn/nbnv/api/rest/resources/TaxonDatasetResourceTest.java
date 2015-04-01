package uk.org.nbn.nbnv.api.rest.resources;

import freemarker.template.TemplateException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Response;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import org.mockito.Mock;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockHttpServletRequest;
import uk.org.nbn.nbnv.api.dao.warehouse.DatasetAdministratorMapper;
import uk.org.nbn.nbnv.api.dao.warehouse.DatasetMapper;
import uk.org.nbn.nbnv.api.dao.warehouse.OrganisationMembershipMapper;
import uk.org.nbn.nbnv.api.model.ImportCleanup;
import static uk.org.nbn.nbnv.api.model.ImportCleanup.Operation.SET_SENSITIVE_FALSE;
import static uk.org.nbn.nbnv.api.model.ImportCleanup.Operation.SET_SENSITIVE_TRUE;
import static uk.org.nbn.nbnv.api.model.ImportCleanup.Operation.STRIP_INVALID_RECORDS;
import uk.org.nbn.nbnv.api.model.ImporterResult;
import static uk.org.nbn.nbnv.api.model.ImporterResult.State.SUCCESSFUL;
import uk.org.nbn.nbnv.api.model.TaxonDataset;
import uk.org.nbn.nbnv.api.model.TaxonDatasetWithImportStatus;
import uk.org.nbn.nbnv.api.model.User;
import uk.org.nbn.nbnv.api.nxf.NXFReader;
import uk.org.nbn.nbnv.api.services.TaxonDatasetImporterService;
import uk.org.nbn.nbnv.api.services.TaxonDatasetPlaceholderService;

/**
 *
 * @author cjohn
 */
public class TaxonDatasetResourceTest {
    @Mock TaxonDatasetImporterService service;
    @Mock DatasetMapper datasetMapper;
    @Mock DatasetAdministratorMapper datasetAdministratorMapper;
    @Mock OrganisationMembershipMapper organisationMembershipMapper;
    @Mock TaxonDatasetPlaceholderService datasetPlaceholderService;
    
    private TaxonDatasetResource resource;
    
    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
        resource = new TaxonDatasetResource();
        resource.datasetMapper = datasetMapper;
        resource.importerService = service;
        resource.datasetAdministratorMapper = datasetAdministratorMapper;
        resource.organisationMembershipMapper = organisationMembershipMapper;
        resource.datasetPlaceholderService = datasetPlaceholderService;
    }
    
    @Test
    public void checkThatReturns404IfNoDatasetExistsOnReplace() throws TemplateException {
        //Given
        User user = mock(User.class);
        String datasetKey = "TestDataset";
        HttpServletRequest request = new MockHttpServletRequest();
        when(datasetMapper.selectTaxonDatasetByID("TestDataset")).thenReturn(null);
        
        //When
        Response response = resource.queueReplacementDataset(user, datasetKey, request);
        
        //Then
        assertEquals("Expected 404 status", response.getStatus(), 404);
    }
    
    @Test
    public void checkThatReturns409IfDatasetIsQueuedOnReplace() throws TemplateException {
        //Given
        User user = mock(User.class);
        String datasetKey = "TestDataset";
        HttpServletRequest request = new MockHttpServletRequest();
        TaxonDataset taxonDataset = mock(TaxonDataset.class);
        when(datasetMapper.selectTaxonDatasetByID("TestDataset")).thenReturn(taxonDataset);
        when(service.isQueued(datasetKey)).thenReturn(true);
        
        //When
        Response response = resource.queueReplacementDataset(user, datasetKey, request);
        
        //Then
        assertEquals("Expected 409 status", response.getStatus(), 409);
    }
    
    @Test
    public void checkThatReturns400IfDatasetCannotBeReadOnReplace() throws TemplateException, IOException {
        //Given
        User user = mock(User.class);
        String datasetKey = "TestDataset";
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setContent("dummyContent".getBytes());
        TaxonDataset taxonDataset = mock(TaxonDataset.class);
        when(datasetMapper.selectTaxonDatasetByID("TestDataset")).thenReturn(taxonDataset);
        when(service.isQueued(datasetKey)).thenReturn(false);
        doThrow(new IOException("Invalid nxf"))
                .when(service)
                .importDataset(any(NXFReader.class), eq(taxonDataset), eq(true));
        
        //When
        Response response = resource.queueReplacementDataset(user, datasetKey, request);
        
        //Then
        assertEquals("Expected 400 status", response.getStatus(), 400);
    }
    
    @Test
    public void checkThatReturns200IfDatasetImportsOnReplace() throws TemplateException, IOException {
        //Given
        User user = mock(User.class);
        String datasetKey = "TestDataset";
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setContent("dummyContent".getBytes());
        TaxonDataset taxonDataset = mock(TaxonDataset.class);
        when(datasetMapper.selectTaxonDatasetByID("TestDataset")).thenReturn(taxonDataset);
        when(service.isQueued(datasetKey)).thenReturn(false);
        
        //When
        Response response = resource.queueReplacementDataset(user, datasetKey, request);
        
        //Then
        assertEquals("Expected 200 status", response.getStatus(), 200);
    }
        
    @Test
    public void checkThatReturns404IfNoDatasetExistsOnAppend() throws TemplateException {
        //Given
        User user = mock(User.class);
        String datasetKey = "TestDataset";
        HttpServletRequest request = new MockHttpServletRequest();
        when(datasetMapper.selectTaxonDatasetByID("TestDataset")).thenReturn(null);
        
        //When
        Response response = resource.queueAppendDataset(user, datasetKey, request);
        
        //Then
        assertEquals("Expected 404 status", response.getStatus(), 404);
    }
    
    @Test
    public void checkThatReturns409IfDatasetIsQueuedOnAppend() throws TemplateException {
        //Given
        User user = mock(User.class);
        String datasetKey = "TestDataset";
        HttpServletRequest request = new MockHttpServletRequest();
        TaxonDataset taxonDataset = mock(TaxonDataset.class);
        when(datasetMapper.selectTaxonDatasetByID("TestDataset")).thenReturn(taxonDataset);
        when(service.isQueued(datasetKey)).thenReturn(true);
        
        //When
        Response response = resource.queueAppendDataset(user, datasetKey, request);
        
        //Then
        assertEquals("Expected 409 status", response.getStatus(), 409);
    }
    
    @Test
    public void checkThatReturns400IfDatasetCannotBeReadOnAppend() throws TemplateException, IOException {
        //Given
        User user = mock(User.class);
        String datasetKey = "TestDataset";
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setContent("dummyContent".getBytes());
        TaxonDataset taxonDataset = mock(TaxonDataset.class);
        when(datasetMapper.selectTaxonDatasetByID("TestDataset")).thenReturn(taxonDataset);
        when(service.isQueued(datasetKey)).thenReturn(false);
        doThrow(new IOException("Invalid nxf"))
                .when(service)
                .importDataset(any(NXFReader.class), eq(taxonDataset), eq(false));
        
        //When
        Response response = resource.queueAppendDataset(user, datasetKey, request);
        
        //Then
        assertEquals("Expected 400 status", response.getStatus(), 400);
    }
    
    @Test
    public void checkThatReturns200IfDatasetImportsOnAppend() throws TemplateException, IOException {
        //Given
        User user = mock(User.class);
        String datasetKey = "TestDataset";
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setContent("dummyContent".getBytes());
        TaxonDataset taxonDataset = mock(TaxonDataset.class);
        when(datasetMapper.selectTaxonDatasetByID("TestDataset")).thenReturn(taxonDataset);
        when(service.isQueued(datasetKey)).thenReturn(false);
        
        //When
        Response response = resource.queueAppendDataset(user, datasetKey, request);
        
        //Then
        assertEquals("Expected 200 status", response.getStatus(), 200);
    }
    
    @Test
    public void checkThatQueuedStatusInformationCanBeGenerated() throws IOException {
        //Given
        User user = mock(User.class);
        TaxonDataset dataset1 = mock(TaxonDataset.class);
        TaxonDataset dataset2 = mock(TaxonDataset.class);
        when(user.getId()).thenReturn(4);
        when(datasetAdministratorMapper.selectTaxonDatasetsByUser(4))
                .thenReturn(Arrays.asList(dataset1,dataset2));
        
        when(dataset1.getKey()).thenReturn("GA000001");
        when(dataset2.getKey()).thenReturn("GA000002");
        when(service.isQueued("GA000001")).thenReturn(true);
        
        //When
        List<TaxonDatasetWithImportStatus> statuses = resource.getImportStatusForAdminableDatasets(user);
        
        //Then
        assertEquals("Expected only one dataset", statuses.size(), 1);
        assertEquals("Expected GA000001 import status to be present", statuses.get(0).getDataset().getKey(), "GA000001");
    }
  
    @Test
    public void checkThatProcessingStatusInformationCanBeGenerated() throws IOException {
        //Given
        User user = mock(User.class);
        TaxonDataset dataset1 = mock(TaxonDataset.class);
        TaxonDataset dataset2 = mock(TaxonDataset.class);
        when(user.getId()).thenReturn(4);
        when(datasetAdministratorMapper.selectTaxonDatasetsByUser(4))
                .thenReturn(Arrays.asList(dataset1,dataset2));
        
        when(dataset1.getKey()).thenReturn("GA000001");
        when(dataset2.getKey()).thenReturn("GA000002");
        when(service.getCurrentlyProcessedDataset()).thenReturn("GA000001");
        
        //When
        List<TaxonDatasetWithImportStatus> statuses = resource.getImportStatusForAdminableDatasets(user);
        
        //Then
        assertEquals("Expected only one dataset", statuses.size(), 1);
        assertEquals("Expected GA000001 import status to be present", statuses.get(0).getDataset().getKey(), "GA000001");
    }
    
    @Test
    public void checkThatHistoryStatusInformationCanBeGenerated() throws IOException {
        //Given
        User user = mock(User.class);
        TaxonDataset dataset1 = mock(TaxonDataset.class);
        TaxonDataset dataset2 = mock(TaxonDataset.class);
        when(user.getId()).thenReturn(4);
        when(datasetAdministratorMapper.selectTaxonDatasetsByUser(4))
                .thenReturn(Arrays.asList(dataset1,dataset2));
        
        when(dataset1.getKey()).thenReturn("GA000001");
        when(dataset2.getKey()).thenReturn("GA000002");
        
        List<ImporterResult> importerResults = new ArrayList<>();
        importerResults.add(mock(ImporterResult.class));
        when(service.getImportHistory("GA000001")).thenReturn(importerResults);
        
        //When
        List<TaxonDatasetWithImportStatus> statuses = resource.getImportStatusForAdminableDatasets(user);
        
        //Then
        assertEquals("Expected only one dataset", statuses.size(), 1);
        assertEquals("Expected GA000001 import status to be present", statuses.get(0).getDataset().getKey(), "GA000001");
    }
    
    @Test
    public void checkThatRequestingImportShouldHaveInvalidRecordsStripped() throws TemplateException, IOException {
        //Given
        User user = mock(User.class);
        String datasetKey = "dataset";
        String timestamp = "what's the time mr wolf";
        ImportCleanup cleanup = new ImportCleanup(STRIP_INVALID_RECORDS);
        
        //When
        resource.reprocessHistoricalImport(user, datasetKey, timestamp, cleanup);
        
        //Then
        verify(service).stripInvalidRecords(datasetKey, timestamp);
    }
            
    @Test
    public void appendingSensitiveColumnWithValuesSetToTrue() throws TemplateException, IOException {
        //Given
        User user = mock(User.class);
        String datasetKey = "dataset";
        String timestamp = "what's the time mr wolf";
        ImportCleanup cleanup = new ImportCleanup(SET_SENSITIVE_TRUE);
        
        //When
        resource.reprocessHistoricalImport(user, datasetKey, timestamp, cleanup);
        
        //Then
        verify(service).queueDatasetWithSensitiveColumnSet(datasetKey, timestamp, true);
    }
    
    @Test
    public void appendingSensitiveColumnWithValuesSetToFalse() throws TemplateException, IOException {
        //Given
        User user = mock(User.class);
        String datasetKey = "dataset";
        String timestamp = "what's the time mr wolf";
        ImportCleanup cleanup = new ImportCleanup(SET_SENSITIVE_FALSE);
        
        //When
        resource.reprocessHistoricalImport(user, datasetKey, timestamp, cleanup);
        
        //Then
        verify(service).queueDatasetWithSensitiveColumnSet(datasetKey, timestamp, false);
    }
    
    @Test
    public void removingAnImporterResultWhichExists() throws IOException {
        //Given
        User user = mock(User.class);
        String datasetKey = "dataset";
        String timestamp = "sponsored by accurist";
        
        when(service.getImportHistory(datasetKey)).thenReturn(Arrays.asList(
            new ImporterResult(SUCCESSFUL, timestamp))
        );
        //When
        Response response = resource.removeImporterResult(user, datasetKey, timestamp);
        
        //Then
        verify(service).removeImporterResult(datasetKey, timestamp, SUCCESSFUL);
        assertEquals("Expected 200 response", 200, response.getStatus());
    }
    
    @Test
    public void removingAnImporterResultWhichDoesntExist() throws IOException {
        //Given
        User user = mock(User.class);
        String datasetKey = "dataset";
        String timestamp = "hammer time";
        
        when(service.getImportHistory(datasetKey)).thenReturn(Collections.EMPTY_LIST);
        
        //When
        Response response = resource.removeImporterResult(user, datasetKey, timestamp);
        
        //Then
        assertEquals("Expected 404 response", 404, response.getStatus());
    }
    
    @Test
    public void removingAnImportResultWhichFails() throws IOException {
        //Given
        User user = mock(User.class);
        String datasetKey = "dataset";
        String timestamp = "October 21st 2015";
        
        when(service.getImportHistory(datasetKey)).thenThrow(new IOException("Epic fail"));
        
        //When
        Response response = resource.removeImporterResult(user, datasetKey, timestamp);
        
        //Then
        assertEquals("Expected 400 response", 400, response.getStatus());
    }
    
    @Test
    public void checkCanLocateTaxonDatasetFromPlaceholderServiceIfNotInDatabase() throws IOException {
        //Given
        String datasetKey = "something not in database";
        TaxonDataset mockedDataset = mock(TaxonDataset.class);
        when(datasetMapper.selectTaxonDatasetByID(datasetKey)).thenReturn(null);
        when(datasetPlaceholderService.readTaxonDataset(datasetKey)).thenReturn(mockedDataset);
        
        //When
        TaxonDataset dataset = resource.getAdminableOrPlaceholderDataset(datasetKey);
        
        //Then
        assertEquals("Expected the mocked dataset", mockedDataset, dataset);
    }
        
    @Test
    public void checkCanLocateTaxonDatasetFromPlaceholderServiceWhenInDatabase() throws IOException {
        //Given
        String datasetKey = "something not in database";
        TaxonDataset mockedDataset = mock(TaxonDataset.class);
        when(datasetMapper.selectTaxonDatasetByID(datasetKey)).thenReturn(mockedDataset);
        
        //When
        TaxonDataset dataset = resource.getAdminableOrPlaceholderDataset(datasetKey);
        
        //Then
        assertEquals("Expected the mocked dataset", mockedDataset, dataset);
    }
}
