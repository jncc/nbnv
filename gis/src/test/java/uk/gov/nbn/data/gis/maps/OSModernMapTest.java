package uk.gov.nbn.data.gis.maps;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Properties;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Christopher Johnson
 */
public class OSModernMapTest {
    private OSModernMap mapService;
    
    @Before
    public void createOSModernMap() throws IOException {
        Properties properties = new Properties();
        properties.load(getClass().getClassLoader().getResourceAsStream("gis.properties"));
        mapService = new OSModernMap(properties);
    }
    
    @Test
    public void checkThatBarsRequestOverSentFromSubDomainIsValid() throws MalformedURLException {
        //Given
        String referrer = "http://something.ukbars.org.uk";
        String hostname = "bars-gis.nbn.org.uk";
        
        //When
        boolean validRequest = mapService.isValidRequest(referrer, hostname);
        
        //Then
        assertTrue("Expected request to be valid", validRequest);
    }
    
    @Test
    public void checkThatBarsRequestFromDefraIsValid() throws MalformedURLException {
        //Given
        String referrer = "http://ukbars.defra.gov.uk";
        String hostname = "bars-gis.nbn.org.uk";
        
        //When
        boolean validRequest = mapService.isValidRequest(referrer, hostname);
        
        //Then
        assertTrue("Expected request to be valid", validRequest);
    }
    
    @Test
    public void checkBarsDefraRequestToNoneBarsEndPoint() throws MalformedURLException {
        //Given
        String referrer = "http://ukbars.defra.gov.uk";
        String hostname = "gis.nbn.org.uk";
        
        //When
        boolean validRequest = mapService.isValidRequest(referrer, hostname);
        
        //Then
        assertFalse("Expected request to be invalid", validRequest);
    }
    
    @Test
    public void checkBarsSubdomainRequestToNoneBarsEndPoint() throws MalformedURLException {
        //Given
        String referrer = "http://something.ukbars.org.uk";
        String hostname = "gis.nbn.org.uk";
        
        //When
        boolean validRequest = mapService.isValidRequest(referrer, hostname);
        
        //Then
        assertFalse("Expected request to be invalid", validRequest);
    }
    
    @Test
    public void checkThatTheMapWorksOnTheLocalhost() throws MalformedURLException {
        //Given
        String hostname = "localhost";
        
        //When
        boolean validRequest = mapService.isValidRequest(null, hostname);
        
        //Then
        assertTrue("Expected request to be valid", validRequest);
    }
    
    @Test
    public void checkThatTheMapWorksFromTheInteractiveMapTool() throws MalformedURLException {
        //Given
        String referrer = "http://data.nbn.org.uk/imt";
        String hostname = "gis.nbn.org.uk";
        
        //When
        boolean validRequest = mapService.isValidRequest(referrer, hostname);
        
        //Then
        assertTrue("Expected request to be valid", validRequest);
    }
    
    @Test
    public void checkThatTheTestMapWorksFromTheInteractiveMapTool() throws MalformedURLException {
        //Given
        String referrer = "http://dev-data.nbn.org.uk/imt";
        String hostname = "dev-gis.nbn.org.uk";
        
        //When
        boolean validRequest = mapService.isValidRequest(referrer, hostname);
        
        //Then
        assertTrue("Expected request to be valid", validRequest);
    }
    
    @Test
    public void checkThatTheMapCantBeHotLinkedFromGoogle() throws MalformedURLException {
        //Given
        String referrer = "http://www.google.com";
        String hostname = "gis.nbn.org.uk";
        
        //When
        boolean validRequest = mapService.isValidRequest(referrer, hostname);
        
        //Then
        assertFalse("Expected request to be invalid", validRequest);
    }
    
    @Test
    public void checkThatTheMapDoesntRespondIfRefererAndHostNameArntProvided() throws MalformedURLException {
        //Given
        //Nothing
        
        //When
        boolean validRequest = mapService.isValidRequest(null, null);
        
        //Then
        assertFalse("Expected request to be invalid", validRequest);
    }
    
    @Test(expected=MalformedURLException.class)
    public void checkThatInvalidRefererIsInvalid() throws MalformedURLException {
        //Given
        String referrer = "Giberjabber";
        String hostname = "gis.nbn.org.uk";
        
        //When
        boolean validRequest = mapService.isValidRequest(referrer, hostname);
        
        //Then
        fail("Expected request to be invalid");
    }
    
    @Test(expected=MalformedURLException.class)
    public void checkThatEmptyReferrerIsInvalid() throws MalformedURLException {
        //Given
        String referrer = "";
        String hostname = "gis.nbn.org.uk";
        
        //When
        boolean validRequest = mapService.isValidRequest(referrer, hostname);
        
        //Then
        fail("Expected request to be invalid");
    }
    
    @Test
    public void checkThatEmptyHostnameIsInvalid() throws MalformedURLException {
        //Given
        String referrer = "http://web.site.com";
        String hostname = null;
        
        //When
        boolean validRequest = mapService.isValidRequest(referrer, hostname);
        
        //Then
        assertFalse("Expected result to be invalid", validRequest);
        //Then
    }
}
