package uk.org.nbn.nbnv.api.rest.resources;

import freemarker.template.TemplateException;
import java.io.IOException;
import java.io.InputStream;
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
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockHttpServletRequest;
import uk.org.nbn.nbnv.api.dao.warehouse.DatasetMapper;
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
    
    private TaxonDatasetResource resource;
    
    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
        resource = new TaxonDatasetResource();
        resource.datasetMapper = datasetMapper;
        resource.importerService = service;
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
}
