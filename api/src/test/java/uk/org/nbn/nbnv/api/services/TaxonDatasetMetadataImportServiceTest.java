package uk.org.nbn.nbnv.api.services;

import java.util.ArrayList;
import java.util.List;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import uk.org.nbn.nbnv.api.nxf.metadata.WordImporter;
import uk.org.nbn.nbnv.api.nxf.metadata.WordImporter_3_0;
import uk.org.nbn.nbnv.api.nxf.metadata.WordImporter_3_1;
import uk.org.nbn.nbnv.api.nxf.metadata.WordImporter_3_2;
import uk.org.nbn.nbnv.api.nxf.metadata.WordImporter_3_3;

/**
 *
 * @author jcoop
 */
public class TaxonDatasetMetadataImportServiceTest {
    
    public TaxonDatasetMetadataImportServiceTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    @Test
    public void testGetImporter_30() throws Exception {
        //Given
        TaxonDatasetMetadataImportService instance = new TaxonDatasetMetadataImportService();
        List<String> data = new ArrayList<>();
        data.add("Version 3.0");
        
        //When
        WordImporter expected = instance.getImporter(data.listIterator());
        
        //Then
        assertNotNull(expected);
        assertEquals(expected.getClass().getName(), WordImporter_3_0.class.getName());
    }

    @Test
    public void testGetImporter_no_version_number() throws Exception {
        //Given
        TaxonDatasetMetadataImportService instance = new TaxonDatasetMetadataImportService();
        List<String> data = new ArrayList<>();
        data.add("Anything can go here");
        
        //When
        WordImporter expected = instance.getImporter(data.listIterator());
        
        //Then
        assertNull(expected);
        assertFalse("Expected the list of messages not to be empty.", instance.getMessages().isEmpty());
        assertTrue("Expected to find a message that said the version number was not found.", instance.getMessages().contains("Could not find a version number in this document"));
    }

    @Test
    public void testGetImporter_badly_formatted_version_number() throws Exception {
        //Given
        TaxonDatasetMetadataImportService instance = new TaxonDatasetMetadataImportService();
        String badlyFormattedVersionNumber = "Version numpty nine";
        List<String> data = new ArrayList<>();
        data.add(badlyFormattedVersionNumber);
        
        //When
        WordImporter expected = instance.getImporter(data.listIterator());
        
        //Then
        assertNull(expected);
        assertFalse("Expected the list of messages not to be empty.", instance.getMessages().isEmpty());
        assertTrue("Expected to find a message that said the version number was not found.", instance.getMessages().contains("Could not find a properly formated document version number (eg Version 3.2), this is what was found: " + badlyFormattedVersionNumber));
    }

    @Test
    public void testGetImporterFromVersion_3_0() throws Exception {
        //Given
        TaxonDatasetMetadataImportService instance = new TaxonDatasetMetadataImportService();
        
        //When
        WordImporter expected = instance.getImporterFromVersion(3, 0);
        
        //Then
        assertNotNull(expected);
        assertEquals(expected.getClass().getName(), WordImporter_3_0.class.getName());
    }

    @Test
    public void testGetImporterFromVersion_3_1() throws Exception {
        //Given
        TaxonDatasetMetadataImportService instance = new TaxonDatasetMetadataImportService();
        
        //When
        WordImporter expected = instance.getImporterFromVersion(3, 1);
        
        //Then
        assertNotNull(expected);
        assertEquals(expected.getClass().getName(), WordImporter_3_1.class.getName());
    }

    @Test
    public void testGetImporterFromVersion_3_2() throws Exception {
        //Given
        TaxonDatasetMetadataImportService instance = new TaxonDatasetMetadataImportService();
        
        //When
        WordImporter expected = instance.getImporterFromVersion(3, 2);
        
        //Then
        assertNotNull(expected);
        assertEquals(expected.getClass().getName(), WordImporter_3_2.class.getName());
    }

    @Test
    public void testGetImporterFromVersion_3_3() throws Exception {
        //Given
        TaxonDatasetMetadataImportService instance = new TaxonDatasetMetadataImportService();
        
        //When
        WordImporter expected = instance.getImporterFromVersion(3, 3);
        
        //Then
        assertNotNull(expected);
        assertEquals(expected.getClass().getName(), WordImporter_3_3.class.getName());
    }

    @Test
    public void testGetImporterFromVersion_unknow_version() throws Exception {
        //Given
        TaxonDatasetMetadataImportService instance = new TaxonDatasetMetadataImportService();
        
        //When
        WordImporter expected = instance.getImporterFromVersion(9, 9);
        
        //Then
        assertNull(expected);
        assertFalse("Expected the list of messages not to be empty.", instance.getMessages().isEmpty());
        assertEquals("Expected just one entry in the list of messages", instance.getMessages().size(), 1);
        assertEquals("Expected to find a message that said the version number was not known.", instance.getMessages().get(0), "The version number found in this document (9.9) is not supported.");
    }
}
