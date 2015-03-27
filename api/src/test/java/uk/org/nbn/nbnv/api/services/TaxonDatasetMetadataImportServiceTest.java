package uk.org.nbn.nbnv.api.services;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import uk.org.nbn.nbnv.api.model.TaxonDataset;
import uk.org.nbn.nbnv.api.nxf.metadata.MetadataValidationException;
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
        List<String> errors = new ArrayList<>();
        WordImporter actual = instance.getImporter(data.listIterator(), errors);
        
        //Then
        assertNotNull(actual);
        assertEquals(WordImporter_3_0.class.getName(), actual.getClass().getName());
    }

    @Test
    public void getImporter_no_version_number() throws Exception {
        //Given
        TaxonDatasetMetadataImportService instance = new TaxonDatasetMetadataImportService();
        List<String> data = new ArrayList<>();
        data.add("Anything can go here");
        
        //When
        List<String> errors = new ArrayList<>();
        WordImporter actual = instance.getImporter(data.listIterator(), errors);
        
        //Then
        assertNull(actual);
        assertFalse("Expected the list of erros not to be empty.", errors.isEmpty());
        assertTrue("Expected to find a message that said the version number was not found.", errors.contains("Could not find a version number in this document"));
    }

    @Test
    public void getImporter_badly_formatted_version_number() throws Exception {
        //Given
        TaxonDatasetMetadataImportService instance = new TaxonDatasetMetadataImportService();
        String badlyFormattedVersionNumber = "Version numpty nine";
        List<String> data = new ArrayList<>();
        data.add(badlyFormattedVersionNumber);
        
        //When
        List<String> errors = new ArrayList<>();
        WordImporter actual = instance.getImporter(data.listIterator(), errors);
        
        //Then
        assertNull(actual);
        assertFalse("Expected the list of messages not to be empty.", errors.isEmpty());
        assertTrue("Expected to find a message that said the version number was not found.", errors.contains("Could not find a properly formated document version number (eg Version 3.2), this is what was found: " + badlyFormattedVersionNumber));
    }

    @Test
    public void getImporterFromVersion_3_0() throws Exception {
        //Given
        TaxonDatasetMetadataImportService instance = new TaxonDatasetMetadataImportService();
        
        //When
        WordImporter actual = instance.getImporterFromVersion(3, 0, new ArrayList<String>());
        
        //Then
        assertNotNull(actual);
        assertEquals(WordImporter_3_0.class.getName(), actual.getClass().getName());
    }

    @Test
    public void getImporterFromVersion_3_1() throws Exception {
        //Given
        TaxonDatasetMetadataImportService instance = new TaxonDatasetMetadataImportService();
        
        //When
        WordImporter actual = instance.getImporterFromVersion(3, 1, new ArrayList<String>());
        
        //Then
        assertNotNull(actual);
        assertEquals(WordImporter_3_1.class.getName(), actual.getClass().getName());
    }

    @Test
    public void getImporterFromVersion_3_2() throws Exception {
        //Given
        TaxonDatasetMetadataImportService instance = new TaxonDatasetMetadataImportService();
        
        //When
        WordImporter actual = instance.getImporterFromVersion(3, 2, new ArrayList<String>());
        
        //Then
        assertNotNull(actual);
        assertEquals(WordImporter_3_2.class.getName(), actual.getClass().getName());
    }

    @Test
    public void getImporterFromVersion_3_3() throws Exception {
        //Given
        TaxonDatasetMetadataImportService instance = new TaxonDatasetMetadataImportService();
        
        //When
        WordImporter actual = instance.getImporterFromVersion(3, 3, new ArrayList<String>());
        
        //Then
        assertNotNull(actual);
        assertEquals(WordImporter_3_3.class.getName(), actual.getClass().getName());
    }

    @Test
    public void getImporterFromVersion_unknow_version() throws Exception {
        //Given
        TaxonDatasetMetadataImportService instance = new TaxonDatasetMetadataImportService();
        
        //When
        List<String> errors = new ArrayList<>();
        WordImporter actual = instance.getImporterFromVersion(9, 9, errors);
        
        //Then
        assertNull(actual);
        assertFalse("Expected the list of messages not to be empty.", errors.isEmpty());
        assertEquals("Expected just one entry in the list of messages", 1, errors.size());
        assertEquals("Expected to find a message that said the version number was not known.", "The version number found in this document (9.9) is not supported.", errors.get(0));
    }

    @Test
    public void taxonDatasetFromValid_3_1_document() throws IOException, InstantiationException, IllegalAccessException, MetadataValidationException {
        //Given
        TaxonDatasetMetadataImportService instance = new TaxonDatasetMetadataImportService();
        TaxonDataset actual = null;
        
        //When
        try(InputStream in = getClass().getResourceAsStream("/test-metadata/Metadata-Form-for-Species-Datasets-v3-1.doc")){
            actual = instance.getTaxonDataset(in);
        }
        
        //Then
        assertEquals("TaxonDataset has incorrect title", "TEST 1km-Demonstration dataset for record access on the NBN Gateway", actual.getTitle());
        assertEquals("TaxonDataset has incorrect description", "TEST 1km -Sightings of 10 common birds species within three national nature reserves (NNRs), Monks Wood, Woodwalton Fen and Holme Fen, occuring in or adjacent to TL28 10km grid square.", actual.getDescription());
        assertEquals("TaxonDataset has incorrect capture method", "TEST 1km-All bird sightings were recorded, using their common name, at 100m (six-figure grid reference) resolution, estimated from Ordnance Survey 1:50 000 map.", actual.getCaptureMethod());
        assertEquals("TaxonDataset has incorrect purpose of data capture", "TEST 1km-Purpose of dataset is to demonstrate the access controls of the NBN Gateway, using bird sighting records gathered during walks through each of the three national nature reserves.", actual.getPurpose());
        assertEquals("TaxonDataset has incorrect geographical coverage", "TEST 1km-All bird sightings are within the three national nature reserves, Monks Wood, Woodwalton Fen and Holme Fen. All three occur within the 10km grid square TL28. Both Holme Fen and Monks Wood also occur in adjacent 10km grid squares.", actual.getGeographicalCoverage());
        assertEquals("TaxonDataset has incorrect temporal coverage", "TEST 1km-All bird sightings were made during 2008, with dates given as the day of the sighting.", actual.getTemporalCoverage());
        assertEquals("TaxonDataset has incorrect data quality", "TEST 1km-The majority of bird sightings were recorded at 100m (six-figure grid reference) resolution, estimated from ordnance survey 1:50 000 map. For demonstration purposes the Moorhen records were recorded at 10m (eight-figure grid reference) and Mute Swan records at 1km (four-figure grid reference).", actual.getQuality());
        assertEquals("TaxonDataset has incorrect additional information", "This is just a test metadata document", actual.getAdditionalInformation());
        assertEquals("TaxonDataset has incorrect access constraints", "TEST 1km-In future, to allow demonstration of the access controls on the NBN Gateway, a range of access constraints will be applied to this dataset. For demonstration the Treecreeper records are flagged as sensitive.", actual.getAccessConstraints());
        assertEquals("TaxonDataset has incorrect use constraint", "Use constraints limited to testing", actual.getUseConstraints());
    }

    @Test
    public void taxonDatasetFromValid_3_3_document() throws IOException, InstantiationException, IllegalAccessException, MetadataValidationException {
        
        
        //Given
        TaxonDatasetMetadataImportService instance = new TaxonDatasetMetadataImportService();
        TaxonDataset actual = null;

        //When
        try(InputStream in = getClass().getResourceAsStream("/test-metadata/Metadata-Form-for-Species-Datasets-v3-3.doc")){
            actual = instance.getTaxonDataset(in);
        }
        
        //Then
        assertEquals("TaxonDataset has incorrect title", "TEST 1km-Demonstration dataset for record access on the NBN Gateway", actual.getTitle());
        assertEquals("TaxonDataset has incorrect description", "TEST 1km -Sightings of 10 common birds species within three national nature reserves (NNRs), Monks Wood, Woodwalton Fen and Holme Fen, occuring in or adjacent to TL28 10km grid square.", actual.getDescription());
        assertEquals("TaxonDataset has incorrect capture method", "TEST 1km-All bird sightings were recorded, using their common name, at 100m (six-figure grid reference) resolution, estimated from Ordnance Survey 1:50 000 map.", actual.getCaptureMethod());
        assertEquals("TaxonDataset has incorrect purpose of data capture", "TEST 1km-Purpose of dataset is to demonstrate the access controls of the NBN Gateway, using bird sighting records gathered during walks through each of the three national nature reserves.", actual.getPurpose());
        assertEquals("TaxonDataset has incorrect geographical coverage", "TEST 1km-All bird sightings are within the three national nature reserves, Monks Wood, Woodwalton Fen and Holme Fen. All three occur within the 10km grid square TL28. Both Holme Fen and Monks Wood also occur in adjacent 10km grid squares.", actual.getGeographicalCoverage());
        assertEquals("TaxonDataset has incorrect temporal coverage", "TEST 1km-All bird sightings were made during 2008, with dates given as the day of the sighting.", actual.getTemporalCoverage());
        assertEquals("TaxonDataset has incorrect data quality", "TEST 1km-The majority of bird sightings were recorded at 100m (six-figure grid reference) resolution, estimated from ordnance survey 1:50 000 map. For demonstration purposes the Moorhen records were recorded at 10m (eight-figure grid reference) and Mute Swan records at 1km (four-figure grid reference).", actual.getQuality());
        assertEquals("TaxonDataset has incorrect additional information", "This is just a test metadata document", actual.getAdditionalInformation());
        assertEquals("TaxonDataset has incorrect access constraints", "TEST 1km-In future, to allow demonstration of the access controls on the NBN Gateway, a range of access constraints will be applied to this dataset. For demonstration the Treecreeper records are flagged as sensitive.", actual.getAccessConstraints());
        assertEquals("TaxonDataset has incorrect use constraint", "Use constraints limited to testing", actual.getUseConstraints());
    }
    
    @Test
    public void metadataForm_3_3_MandatoryFields() throws IOException, InstantiationException, IllegalAccessException {
        //Given
        TaxonDatasetMetadataImportService instance = new TaxonDatasetMetadataImportService();
        List<String> errors = new ArrayList<>();
        
        //When
        try{
            instance.getTaxonDataset(getClass().getResourceAsStream("/test-metadata/v3-3-missing-mandatory.doc"));
        }catch(MetadataValidationException ex){
            errors = ex.getErrors();
        }
        
        //Then
        String messageTemplate = "No error for '%s'";
        String expectedTemplate = "Missing required information '%s'";
        assertTrue(String.format(messageTemplate, WordImporter.ORG_NAME), errors.contains(String.format(expectedTemplate, WordImporter.ORG_NAME)));
        assertTrue(String.format(messageTemplate,WordImporter.ORG_DESC), errors.contains(String.format(expectedTemplate, WordImporter.ORG_DESC)));
        assertTrue(String.format(messageTemplate,WordImporter.ORG_CONTACT_NAME), errors.contains(String.format(expectedTemplate, WordImporter.ORG_CONTACT_NAME)));
        assertTrue(String.format(messageTemplate,WordImporter.ORG_EMAIL), errors.contains(String.format(expectedTemplate, WordImporter.ORG_EMAIL)));
        assertTrue(String.format(messageTemplate,WordImporter.ORG_ADDRESS), errors.contains(String.format(expectedTemplate, WordImporter.ORG_ADDRESS)));
        assertTrue(String.format(messageTemplate,WordImporter.ORG_POSTCODE), errors.contains(String.format(expectedTemplate, WordImporter.ORG_POSTCODE)));
        assertTrue(String.format(messageTemplate,WordImporter.ORG_PHONE), errors.contains(String.format(expectedTemplate, WordImporter.ORG_PHONE)));
        assertTrue(String.format(messageTemplate,WordImporter.META_NAME), errors.contains(String.format(expectedTemplate, WordImporter.META_NAME)));
        assertTrue(String.format(messageTemplate,WordImporter.META_CONTACT_PHONE), errors.contains(String.format(expectedTemplate, WordImporter.META_CONTACT_PHONE)));
        assertTrue(String.format(messageTemplate,WordImporter.META_EMAIL), errors.contains(String.format(expectedTemplate, WordImporter.META_EMAIL)));
        assertTrue(String.format(messageTemplate,WordImporter.META_TITLE), errors.contains(String.format(expectedTemplate, WordImporter.META_TITLE)));
        assertTrue(String.format(messageTemplate,WordImporter.META_DESC), errors.contains(String.format(expectedTemplate, WordImporter.META_DESC)));
        assertTrue(String.format(messageTemplate,WordImporter.META_CAPTURE_METHOD), errors.contains(String.format(expectedTemplate, WordImporter.META_CAPTURE_METHOD)));
        assertTrue(String.format(messageTemplate,WordImporter.META_PURPOSE), errors.contains(String.format(expectedTemplate, WordImporter.META_PURPOSE)));
        assertTrue(String.format(messageTemplate,WordImporter.META_DATA_CONFIDENCE), errors.contains(String.format(expectedTemplate, WordImporter.META_DATA_CONFIDENCE)));
        assertTrue(String.format(messageTemplate,WordImporter.META_ACCESS_CONSTRAINT), errors.contains(String.format(expectedTemplate, WordImporter.META_ACCESS_CONSTRAINT)));
        assertTrue("No error for missing name in data provider agreement section ", errors.contains("No name provided in the NBN Gateway Data Provider Agreement section of Metadata form"));
        assertTrue("No error for missing date in data provider agreement section ", errors.contains("No date provided in the NBN Gateway Data Provider Agreement section of Metadata form"));
        assertFalse("Error found when there shouldn't be one for 'future date in data provider agreement section'", errors.contains("Future date provided in the NBN Gateway Data Provider Agreement section of Metadata form"));
        assertFalse("Error found when there shouldn't be one for 'unreadable data provider agreement section'", errors.contains("Unable to read the NBN Gateway Data Provider Agreement section of Metadata form"));
    }
    
    @Test
    public void metadataForm_3_3_futureDate() throws IOException, InstantiationException, IllegalAccessException {
        //Given
        TaxonDatasetMetadataImportService instance = new TaxonDatasetMetadataImportService();
        List<String> errors = new ArrayList<>();
        
        //When
        try{
            instance.getTaxonDataset(getClass().getResourceAsStream("/test-metadata/v3-3-future-date.doc"));
        }catch(MetadataValidationException ex){
            errors = ex.getErrors();
        }
        
        //Then
        assertEquals("Should only find 1 error message", 1, errors.size());
        assertTrue("No error found for future date in the data provider agreement section ", errors.contains("Future date provided in the NBN Gateway Data Provider Agreement section of Metadata form"));
    }
}
