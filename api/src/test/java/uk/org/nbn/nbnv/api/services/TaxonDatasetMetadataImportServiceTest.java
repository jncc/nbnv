package uk.org.nbn.nbnv.api.services;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import uk.org.nbn.nbnv.api.model.TaxonDataset;
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
    public void getImporter_30() throws Exception {
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
    public void getImporter_no_version_number() throws Exception {
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
    public void getImporter_badly_formatted_version_number() throws Exception {
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
    public void getImporterFromVersion_3_0() throws Exception {
        //Given
        TaxonDatasetMetadataImportService instance = new TaxonDatasetMetadataImportService();
        
        //When
        WordImporter expected = instance.getImporterFromVersion(3, 0);
        
        //Then
        assertNotNull(expected);
        assertEquals(expected.getClass().getName(), WordImporter_3_0.class.getName());
    }

    @Test
    public void getImporterFromVersion_3_1() throws Exception {
        //Given
        TaxonDatasetMetadataImportService instance = new TaxonDatasetMetadataImportService();
        
        //When
        WordImporter expected = instance.getImporterFromVersion(3, 1);
        
        //Then
        assertNotNull(expected);
        assertEquals(expected.getClass().getName(), WordImporter_3_1.class.getName());
    }

    @Test
    public void getImporterFromVersion_3_2() throws Exception {
        //Given
        TaxonDatasetMetadataImportService instance = new TaxonDatasetMetadataImportService();
        
        //When
        WordImporter expected = instance.getImporterFromVersion(3, 2);
        
        //Then
        assertNotNull(expected);
        assertEquals(expected.getClass().getName(), WordImporter_3_2.class.getName());
    }

    @Test
    public void getImporterFromVersion_3_3() throws Exception {
        //Given
        TaxonDatasetMetadataImportService instance = new TaxonDatasetMetadataImportService();
        
        //When
        WordImporter expected = instance.getImporterFromVersion(3, 3);
        
        //Then
        assertNotNull(expected);
        assertEquals(expected.getClass().getName(), WordImporter_3_3.class.getName());
    }

    @Test
    public void getImporterFromVersion_unknow_version() throws Exception {
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
    
    @Test
    public void validResolution() throws IOException, InstantiationException, IllegalAccessException {
        //Given
        TaxonDatasetMetadataImportService instance = new TaxonDatasetMetadataImportService();
        
        //When
        TaxonDataset actual = instance.getTaxonDataset(getClass().getResourceAsStream("/test-metadata/metadata_3_1.doc"));
        
        //Then
        assertEquals("TaxonDataset has incorrect title", "TEST 1km-Demonstration dataset for record access on the NBN Gateway", actual.getTitle());
        assertEquals("TaxonDataset has incorrect description", "TEST 1km -Sightings of 10 common birds species within three national nature reserves (NNRs), Monks Wood, Woodwalton Fen and Holme Fen, occuring in or adjacent to TL28 10km grid square.", actual.getDescription());
        assertEquals("TaxonDataset has incorrect capture method", "TEST 1km-All bird sightings were recorded, using their common name, at 100m (six-figure grid reference) resolution, estimated from Ordnance Survey 1:50 000 map.", actual.getCaptureMethod());
        assertEquals("TaxonDataset has incorrect purpose of data capture", "TEST 1km-Purpose of dataset is to demonstrate the access controls of the NBN Gateway, using bird sighting records gathered during walks through each of the three national nature reserves.", actual.getPurpose());
        assertEquals("TaxonDataset has incorrect geographical coverage", "TEST 1km-All bird sightings are within the three national nature reserves, Monks Wood, Woodwalton Fen and Holme Fen. All three occur within the 10km grid square TL28. Both Holme Fen and Monks Wood also occur in adjacent 10km grid squares.", actual.getGeographicalCoverage());
        assertEquals("TaxonDataset has incorrect temporal coverage", "TEST 1km-All bird sightings were made during 2008, with dates given as the day of the sighting.", actual.getTemporalCoverage());
        assertEquals("TaxonDataset has incorrect data quality", "TEST 1km-The majority of bird sightings were recorded at 100m (six-figure grid reference) resolution, estimated from ordnance survey 1:50 000 map. For demonstration purposes the Moorhen records were recorded at 10m (eight-figure grid reference) and Mute Swan records at 1km (four-figure grid reference).", actual.getQuality());
        assertEquals("TaxonDataset has incorrect additional information", "TEST 1km", actual.getAdditionalInformation());
        assertEquals("TaxonDataset has incorrect access constraints", "TEST 1km-In future, to allow demonstration of the access controls on the NBN Gateway, a range of access constraints will be applied to this dataset. For demonstration the Treecreeper records are flagged as sensitive.", actual.getAccessConstraints());
        assertEquals("TaxonDataset has incorrect use constraint", "TEST 1km", actual.getUseConstraints());
    }
}
