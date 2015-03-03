package uk.org.nbn.nbnv.api.rest.resources;

import freemarker.template.TemplateException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Response;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import org.mockito.Mock;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockHttpServletRequest;
import uk.org.nbn.nbnv.api.dao.warehouse.DatasetAdministratorMapper;
import uk.org.nbn.nbnv.api.dao.warehouse.DatasetMapper;
import uk.org.nbn.nbnv.api.model.Dataset;
import uk.org.nbn.nbnv.api.model.DatasetImportStatus;
import uk.org.nbn.nbnv.api.model.ImporterResult;
import uk.org.nbn.nbnv.api.model.TaxonDataset;
import uk.org.nbn.nbnv.api.model.User;
import uk.org.nbn.nbnv.api.services.TaxonDatasetImporterService;

/**
 *
 * @author cjohn
 */
public class TaxonDatasetResourceTest {
    @Mock TaxonDatasetImporterService service;
    @Mock DatasetMapper datasetMapper;
    @Mock DatasetAdministratorMapper datasetAdministratorMapper;
    
    private TaxonDatasetResource resource;
    
    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
        resource = new TaxonDatasetResource();
        resource.datasetMapper = datasetMapper;
        resource.importerService = service;
        resource.datasetAdministratorMapper = datasetAdministratorMapper;
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
        HttpServletRequest request = new MockHttpServletRequest();
        TaxonDataset taxonDataset = mock(TaxonDataset.class);
        when(datasetMapper.selectTaxonDatasetByID("TestDataset")).thenReturn(taxonDataset);
        when(service.isQueued(datasetKey)).thenReturn(false);
        doThrow(new IOException("Invalid nxf"))
                .when(service)
                .importDataset(any(InputStream.class), eq(taxonDataset), eq(true));
        
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
        HttpServletRequest request = new MockHttpServletRequest();
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
        HttpServletRequest request = new MockHttpServletRequest();
        TaxonDataset taxonDataset = mock(TaxonDataset.class);
        when(datasetMapper.selectTaxonDatasetByID("TestDataset")).thenReturn(taxonDataset);
        when(service.isQueued(datasetKey)).thenReturn(false);
        doThrow(new IOException("Invalid nxf"))
                .when(service)
                .importDataset(any(InputStream.class), eq(taxonDataset), eq(false));
        
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
        HttpServletRequest request = new MockHttpServletRequest();
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
        Dataset dataset1 = mock(Dataset.class);
        Dataset dataset2 = mock(Dataset.class);
        when(user.getId()).thenReturn(4);
        when(datasetAdministratorMapper.selectTaxonDatasetsByUser(4))
                .thenReturn(Arrays.asList(dataset1,dataset2));
        
        when(dataset1.getKey()).thenReturn("GA000001");
        when(dataset2.getKey()).thenReturn("GA000002");
        when(service.isQueued("GA000001")).thenReturn(true);
        
        //When
        Map<String, DatasetImportStatus> statuses = resource.getImportStatusForAdminableDatasets(user);
        
        //Then
        assertEquals("Expected only one dataset", statuses.size(), 1);
        assertTrue("Expected GA000001 import status to be present", statuses.containsKey("GA000001"));
    }
  
    @Test
    public void checkThatProcessingStatusInformationCanBeGenerated() throws IOException {
        //Given
        User user = mock(User.class);
        Dataset dataset1 = mock(Dataset.class);
        Dataset dataset2 = mock(Dataset.class);
        when(user.getId()).thenReturn(4);
        when(datasetAdministratorMapper.selectTaxonDatasetsByUser(4))
                .thenReturn(Arrays.asList(dataset1,dataset2));
        
        when(dataset1.getKey()).thenReturn("GA000001");
        when(dataset2.getKey()).thenReturn("GA000002");
        when(service.getCurrentlyProcessedDataset()).thenReturn("GA000001");
        
        //When
        Map<String, DatasetImportStatus> statuses = resource.getImportStatusForAdminableDatasets(user);
        
        //Then
        assertEquals("Expected only one dataset", statuses.size(), 1);
        assertTrue("Expected GA000001 import status to be present", statuses.containsKey("GA000001"));
    }
    
    @Test
    public void checkThatHistoryStatusInformationCanBeGenerated() throws IOException {
        //Given
        User user = mock(User.class);
        Dataset dataset1 = mock(Dataset.class);
        Dataset dataset2 = mock(Dataset.class);
        when(user.getId()).thenReturn(4);
        when(datasetAdministratorMapper.selectTaxonDatasetsByUser(4))
                .thenReturn(Arrays.asList(dataset1,dataset2));
        
        when(dataset1.getKey()).thenReturn("GA000001");
        when(dataset2.getKey()).thenReturn("GA000002");
        
        Map<String, ImporterResult> importerResults = new HashMap<>();
        importerResults.put("TimeStamp", mock(ImporterResult.class));
        when(service.getImportHistory("GA000001")).thenReturn(importerResults);
        
        //When
        Map<String, DatasetImportStatus> statuses = resource.getImportStatusForAdminableDatasets(user);
        
        //Then
        assertEquals("Expected only one dataset", statuses.size(), 1);
        assertTrue("Expected GA000001 import status to be present", statuses.containsKey("GA000001"));
    }
}
