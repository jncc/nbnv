package uk.org.nbn.nbnv.api.services;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import org.apache.poi.util.IOUtils;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.map.ObjectMapper;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import static org.mockito.Matchers.any;
import org.mockito.Mock;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;
import uk.org.nbn.nbnv.api.model.Organisation;
import uk.org.nbn.nbnv.api.model.TaxonDataset;
import uk.org.nbn.nbnv.api.model.TaxonDatasetAdditions;
import uk.org.nbn.nbnv.api.nxf.metadata.MetadataValidationException;

/**
 *
 * @author cjohn
 */
public class TaxonDatasetMetadataArchiveServiceTest {
    @Rule public TemporaryFolder folder = new TemporaryFolder();
    
    @Mock TaxonDatasetMetadataImportService wordReader;
    
    private TaxonDatasetMetadataArchiveService service;
    
    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
        service = new TaxonDatasetMetadataArchiveService();
        service.wordReader = wordReader;
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void checkThatRecorderNamesTrueFailsIfNot100mAndHasPublicAccess() throws IOException {
        //Given
        TaxonDatasetAdditions additions = new TaxonDatasetAdditions();
        additions.setResolution("No Access");
        additions.setRecorderNames(true);
        File archive = createZipArchive(new ByteArrayInputStream(new byte[]{}), additions);
        Organisation organisation = mock(Organisation.class);
                
        //When
        service.readWordDocument(archive, organisation);
        
        //Then
        fail("Expected to throw an Illegal Argument Exception");
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void checkThatAttributesTrueFailsIfNot100mAndHasPublicAccess() throws IOException {
        //Given
        TaxonDatasetAdditions additions = new TaxonDatasetAdditions();
        additions.setResolution("No Access");
        additions.setRecordAttributes(true);
        File archive = createZipArchive(new ByteArrayInputStream(new byte[]{}), additions);
        Organisation organisation = mock(Organisation.class);
        
        //When
        service.readWordDocument(archive, organisation);
        
        //Then
        fail("Expected to throw an Illegal Argument Exception");
    }
    
    @Test
    public void checkThatCanReadDocumentSuccessfully() throws IOException, MetadataValidationException {
        //Given
        TaxonDatasetAdditions additions = new TaxonDatasetAdditions();
        additions.setResolution("100m");
        additions.setRecordAttributes(true);
        additions.setRecorderNames(true);
        additions.setAdminEmail("admin@email.com");
        File archive = createZipArchive(new ByteArrayInputStream(new byte[]{}), additions);
        TaxonDataset dataset = mock(TaxonDataset.class);
        when(wordReader.getTaxonDataset(any(InputStream.class))).thenReturn(dataset);
        Organisation organisation = mock(Organisation.class);
        
        //When
        TaxonDataset loaded = service.readWordDocument(archive, organisation);
        
        //Then
        assertEquals("Expected to get document loaded from word reader", dataset, loaded);
        verify(loaded).setPublicResolution("100m");
        verify(loaded).setPublicAttribute(true);
        verify(loaded).setPublicRecorder(true);
        verify(loaded).setOrganisation(organisation);
        verify(organisation).setContactEmail("admin@email.com");
    }
    
    private File createZipArchive(InputStream wordDoc, TaxonDatasetAdditions additions) throws IOException {       
        File archive = folder.newFile("zipfile");
        try (ZipOutputStream out = new ZipOutputStream(new FileOutputStream(archive))) {
            ObjectMapper mapper = new ObjectMapper();
            mapper.configure(JsonGenerator.Feature.AUTO_CLOSE_TARGET, false);
            out.putNextEntry(new ZipEntry("form.doc"));
            IOUtils.copy(wordDoc, out);
            out.putNextEntry(new ZipEntry("additions.json"));
            mapper.writeValue(out, additions);
            out.closeEntry();
        }
        return archive;
    }
}
