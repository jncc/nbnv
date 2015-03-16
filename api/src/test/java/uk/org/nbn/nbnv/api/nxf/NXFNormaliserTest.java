package uk.org.nbn.nbnv.api.nxf;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import org.junit.Test;

/**
 *
 * @author cjohn
 */
public class NXFNormaliserTest {
    @Test(expected=IllegalArgumentException.class)
    public void checkThatANullHeaderIsAnIllegalArgument() {
        //Given
        NXFLine header = null;
        
        //When
        NXFNormaliser normaliser = new NXFNormaliser(header);
        
        //Then
        fail("Expected an illegal argument exception when constructed with null header");
    }
    
    @Test
    public void checkThatHeaderGetsNormalized() {
        //Given
        NXFLine origHeader = new NXFLine("Record key\t Sensitive");
        NXFNormaliser normaliser = new NXFNormaliser(origHeader);
        
        //When
        NXFLine header = normaliser.header();
        
        //Then
        assertEquals("Expected only two values", header.getValues().size(), 2);
        assertTrue("Expected record key normalised", header.getValues().contains("RECORDKEY"));
        assertTrue("Expected sensitive normalised", header.getValues().contains("SENSITIVE"));
    }
    
    @Test
    public void checkThatAttributesCreateDynamicProperties() {
        //Given
        NXFLine origHeader = new NXFLine("attribute1");
        NXFLine origData = new NXFLine("my data");
        NXFNormaliser normaliser = new NXFNormaliser(origHeader);
        
        //When
        NXFLine header = normaliser.header();
        NXFLine data = normaliser.normalise(origData);
        
        //Then
        assertEquals("Expected header to be DynamicProperties", header.getLine(), "DYNAMICPROPERTIES");
        assertEquals("Expected json in attribute column", data.getLine(), "{\"ATTRIBUTE1\":\"my data\"}");
    }
    
    @Test
    public void checkThatFillsCreatesSRSColumnIfMissing() {
        //Given
        NXFLine origHeader = new NXFLine("GRIDREFERENCE");
        NXFNormaliser normaliser = new NXFNormaliser(origHeader);
        
        //When
        NXFLine header = normaliser.header();
        NXFLine wgs84 = normaliser.normalise(new NXFLine("WGS84"));
        NXFLine other = normaliser.normalise(new NXFLine("some unkown type"));
        
        //Then
        assertEquals("Expected srs column created", header.getLine(), "GRIDREFERENCE\tSRS");
        assertEquals("Expected srs to be filled in", wgs84.getLine(), "WGS84\t4326");
        assertEquals("Expected srs to be left blank", other.getLine(), "some unkown type\t");
    }
    
    @Test
    public void checkThatReplacesValuesInSRSColumnIfPresent() {
        //Given
        NXFLine origHeader = new NXFLine("Srs\tGrid Reference");
        NXFNormaliser normaliser = new NXFNormaliser(origHeader);
        
        //When
        NXFLine header = normaliser.header();
        NXFLine wgs84 = normaliser.normalise(new NXFLine("Wrong epsg code \t WGS84"));
        NXFLine other = normaliser.normalise(new NXFLine("leave me \t hmm"));
        
        //Then
        assertEquals("Expected columns in standard order", header.getLine(), "GRIDREFERENCE\tSRS");
        assertEquals("Expected srs to be filled in", wgs84.getLine(), "WGS84\t4326");
        assertEquals("Expected srs to be left as is", other.getLine(), "hmm\tleave me");
    }
    
    @Test
    public void checkThatDateTypeValuesOfPGetChangedToOO() {
        //Given
        NXFLine origHeader = new NXFLine("Date type");
        NXFNormaliser normaliser = new NXFNormaliser(origHeader);
        
        //When
        NXFLine pValue = normaliser.normalise(new NXFLine("P"));
        NXFLine ddValue = normaliser.normalise(new NXFLine("DD"));
        
        //Then
        assertEquals("Expected P to be turned to OO", pValue.getLine(), "OO");
        assertEquals("Expected DD to have been left alone", ddValue.getLine(), "DD");
    }
    
    @Test
    public void checkThatRubbishInGridRefGetsRemoved() {
        //Given
        NXFLine origHeader = new NXFLine("Grid Reference");
        NXFNormaliser normaliser = new NXFNormaliser(origHeader);
        
        //When
        NXFLine tl45 = normaliser.normalise(new NXFLine("TL 45"));
        NXFLine sn42WithHyphen = normaliser.normalise(new NXFLine("SN-42"));
        
        //Then
        assertEquals("Expected TL45 to be normalised", tl45.getLine(), "TL45");
        assertEquals("Expected TL45 to be normalised", sn42WithHyphen.getLine(), "SN42");
    }
    
    @Test
    public void checkThatZeroAbundanceAcceptsAbsenceValues() {
        //Given
        NXFLine origHeader = new NXFLine("Zero Abundance");
        NXFNormaliser normaliser = new NXFNormaliser(origHeader);
        
        //When
        NXFLine t = normaliser.normalise(new NXFLine("t"));
        NXFLine tru = normaliser.normalise(new NXFLine("tRuE"));
        NXFLine yes = normaliser.normalise(new NXFLine("yEs"));
        NXFLine absent = normaliser.normalise(new NXFLine("absent"));
        NXFLine absence = normaliser.normalise(new NXFLine("ABSENCE"));
        
        //Then
        assertEquals("Expected to read absence", t.getLine(), "absence");
        assertEquals("Expected to read absence", tru.getLine(), "absence");
        assertEquals("Expected to read absence", yes.getLine(), "absence");
        assertEquals("Expected to read absence", absent.getLine(), "absence");
        assertEquals("Expected to read absence", absence.getLine(), "absence");
    }
    
    @Test
    public void checkThatZeroAbundanceAcceptsPresenceValues() {
        //Given
        NXFLine origHeader = new NXFLine("Zero Abundance");
        NXFNormaliser normaliser = new NXFNormaliser(origHeader);
        
        //When
        NXFLine f = normaliser.normalise(new NXFLine("f"));
        NXFLine fals = normaliser.normalise(new NXFLine("false"));
        NXFLine no = normaliser.normalise(new NXFLine("no"));
        NXFLine present = normaliser.normalise(new NXFLine("present"));
        NXFLine presence = normaliser.normalise(new NXFLine("PRESENCE"));
        
        //Then
        assertEquals("Expected to read absence", f.getLine(), "presence");
        assertEquals("Expected to read absence", fals.getLine(), "presence");
        assertEquals("Expected to read absence", no.getLine(), "presence");
        assertEquals("Expected to read absence", present.getLine(), "presence");
        assertEquals("Expected to read absence", presence.getLine(), "presence");
    }
    
    @Test
    public void checkThatZeroAbundancePassesUnknownValuesThru() {
        //Given
        NXFLine origHeader = new NXFLine("Zero Abundance");
        NXFNormaliser normaliser = new NXFNormaliser(origHeader);
        
        //When
        NXFLine value = normaliser.normalise(new NXFLine("Some Unkown Value"));
        
        //Then
        assertEquals("Expected to gracefully handle unknown values", value.getLine(), "Some Unkown Value");
    }
    
    @Test
    public void checkThatPrecisionSnapsValues() {
        //Given
        NXFLine origHeader = new NXFLine("Precision");
        NXFNormaliser normaliser = new NXFNormaliser(origHeader);
        
        //When
        NXFLine below100 = normaliser.normalise(new NXFLine("99"));
        NXFLine below1000 = normaliser.normalise(new NXFLine("999"));
        NXFLine below2000 = normaliser.normalise(new NXFLine("1999"));
        NXFLine below10000 = normaliser.normalise(new NXFLine("9999"));
        
        //Then
        assertEquals("Snapped to 100", below100.getLine(), "100");
        assertEquals("Snapped to 1000", below1000.getLine(), "1000");
        assertEquals("Snapped to 2000", below2000.getLine(), "2000");
        assertEquals("Snapped to 10000", below10000.getLine(), "10000");
    }
    
    @Test
    public void checkThatPrecisionValuesRenameTheSame() {
        //Given
        NXFLine origHeader = new NXFLine("Precision");
        NXFNormaliser normaliser = new NXFNormaliser(origHeader);
        
        //When
        NXFLine exactly100 = normaliser.normalise(new NXFLine("10"));
        NXFLine exactly1000 = normaliser.normalise(new NXFLine("1000"));
        NXFLine exactly2000 = normaliser.normalise(new NXFLine("2000"));
        NXFLine exactly10000 = normaliser.normalise(new NXFLine("10000"));
        NXFLine gibberish = normaliser.normalise(new NXFLine("Some Unhandled Gibberish"));
        
        //Then
        assertEquals("100 remained unchanged", exactly100.getLine(), "100");
        assertEquals("1000 remained unchanged", exactly1000.getLine(), "1000");
        assertEquals("2000 remained unchanged", exactly2000.getLine(), "2000");
        assertEquals("10000 remained unchanged", exactly10000.getLine(), "10000");
        assertEquals("Gibberish remained unchanged", gibberish.getLine(), "Some Unhandled Gibberish");
    }
    
    @Test
    public void checkThatSensitiveAcceptsTrueValues() {
        //Given
        NXFLine origHeader = new NXFLine("SENSITIVE");
        NXFNormaliser normaliser = new NXFNormaliser(origHeader);
        
        //When
        NXFLine t = normaliser.normalise(new NXFLine("t"));
        NXFLine tru = normaliser.normalise(new NXFLine("tRuE"));
        NXFLine yes = normaliser.normalise(new NXFLine("yEs"));
        
        //Then
        assertEquals("Expected to read true", t.getLine(), "true");
        assertEquals("Expected to read true", tru.getLine(), "true");
        assertEquals("Expected to read true", yes.getLine(), "true");
    }
    
    @Test
    public void checkThatSensitiveAcceptsFalseValues() {
        //Given
        NXFLine origHeader = new NXFLine("SENSITIVE");
        NXFNormaliser normaliser = new NXFNormaliser(origHeader);
        
        //When
        NXFLine f = normaliser.normalise(new NXFLine("f"));
        NXFLine fals = normaliser.normalise(new NXFLine("fAlSe"));
        NXFLine no = normaliser.normalise(new NXFLine("no"));
        
        //Then
        assertEquals("Expected to read false", f.getLine(), "false");
        assertEquals("Expected to read false", fals.getLine(), "false");
        assertEquals("Expected to read false", no.getLine(), "false");
    }
    
    @Test
    public void checkThatSensitivePassesUnknownValuesThru() {
        //Given
        NXFLine origHeader = new NXFLine("SENSITIVE");
        NXFNormaliser normaliser = new NXFNormaliser(origHeader);
        
        //When
        NXFLine value = normaliser.normalise(new NXFLine("Some Unkown Value"));
        
        //Then
        assertEquals("Expected to gracefully handle unknown values", value.getLine(), "Some Unkown Value");
    }
}
