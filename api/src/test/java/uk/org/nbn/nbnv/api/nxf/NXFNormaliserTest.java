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
        assertEquals("Expected header to be DynamicProperties", "DYNAMICPROPERTIES", header.getLine());
        assertEquals("Expected json in attribute column", "{\"ATTRIBUTE1\":\"my data\"}", data.getLine());
    }
    
    @Test
    public void checkThatFillsCreatesSRSColumnIfMissing() {
        //Given
        NXFLine origHeader = new NXFLine("PROJECTION");
        NXFNormaliser normaliser = new NXFNormaliser(origHeader);
        
        //When
        NXFLine header = normaliser.header();
        NXFLine wgs84 = normaliser.normalise(new NXFLine("WGS84"));
        NXFLine other = normaliser.normalise(new NXFLine("some unkown type"));
        
        //Then
        assertEquals("Expected srs column created", "PROJECTION\tSRS", header.getLine());
        assertEquals("Expected srs to be filled in", "WGS84\t4326", wgs84.getLine());
        assertEquals("Expected srs to be left blank", "some unkown type\t", other.getLine());
    }
    
    @Test
    public void checkThatReplacesValuesInSRSColumnIfPresent() {
        //Given
        NXFLine origHeader = new NXFLine("Srs\tProjection");
        NXFNormaliser normaliser = new NXFNormaliser(origHeader);
        
        //When
        NXFLine header = normaliser.header();
        NXFLine wgs84 = normaliser.normalise(new NXFLine("Wrong epsg code \t WGS84"));
        NXFLine other = normaliser.normalise(new NXFLine("leave me \t hmm"));
        
        //Then
        assertEquals("Expected headers", "SRS\tPROJECTION", header.getLine());
        assertEquals("Expected srs to be filled in", wgs84.getLine(), "4326\tWGS84");
        assertEquals("Expected srs to be left as is", other.getLine(), "leave me\thmm");
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
        assertEquals("Expected to read absence", "absence", t.getLine());
        assertEquals("Expected to read absence", "absence", tru.getLine());
        assertEquals("Expected to read absence", "absence", yes.getLine());
        assertEquals("Expected to read absence", "absence", absent.getLine());
        assertEquals("Expected to read absence", "absence", absence.getLine());
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
        assertEquals("Expected to read absence", "presence", f.getLine());
        assertEquals("Expected to read absence", "presence", fals.getLine());
        assertEquals("Expected to read absence", "presence", no.getLine());
        assertEquals("Expected to read absence", "presence", present.getLine());
        assertEquals("Expected to read absence", "presence", presence.getLine());
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
    
    @Test
    public void checkThatSiteKeyTreatedAsAnAttribute() {
        //Given
        NXFLine origHeader = new NXFLine("SITEKEY");
        NXFNormaliser normaliser = new NXFNormaliser(origHeader);
        
        //When
        NXFLine header = normaliser.header();
        NXFLine site = normaliser.normalise(new NXFLine("my favourite location"));
        
        //Then
        assertEquals("Expected site key to be bundled up in the attribute column", "DYNAMICPROPERTIES", header.getLine());
        assertEquals("Expected site key to be bundled up in the attribute column", "{\"SITEKEY\":\"my favourite location\"}", site.getLine());
    }
    
    @Test
    public void checkThatDoubleQuotesRemoved() {
        //Given
        NXFLine origHeader = new NXFLine("\"TAXONVERSION\"KEY\"");
        NXFNormaliser normaliser = new NXFNormaliser(origHeader);
        
        //When
        NXFLine header = normaliser.header();
        NXFLine data = normaliser.normalise(new NXFLine("\"NBNSYS00\"00000001\""));
        
        //Then
        assertEquals("Expected column heading not to have double quotes", "TAXONVERSIONKEY", header.getLine());
        assertEquals("Expected data not to have double quotes", "NBNSYS0000000001", data.getLine());
    }
    
    @Test
    public void checkThatUnwantedColumnsAreRemoved() {
        //Given
        NXFLine origHeader = new NXFLine("TAXONNAME\tTAXONGROUP\tCOMMONNAME\tTAXONVERSIONKEY");
        NXFNormaliser normaliser = new NXFNormaliser(origHeader);
        
        //When
        NXFLine header = normaliser.header();
        NXFLine site = normaliser.normalise(new NXFLine("Hippocrepis comosa\tVascular plant\tHorseshoe vetch\tNBNSYS0000000001"));
        
        //Then
        assertEquals("Expected only a single colum", "TAXONVERSIONKEY", header.getLine());
        assertEquals("Expected only a single piece of data", "NBNSYS0000000001", site.getLine());
    }
    
    @Test
    public void checkThatMispelledSenitiveFixed() {
        //Given
        NXFLine origHeader = new NXFLine("senitive");
        NXFNormaliser normaliser = new NXFNormaliser(origHeader);
        
        //When
        NXFLine header = normaliser.header();
        
        //Then
        assertEquals("Expected correct spelling of senstivie column", "SENSITIVE", header.getLine());
    }
    
    @Test
    public void checkThatMispelledSenstiveFixed() {
        //Given
        NXFLine origHeader = new NXFLine("senstive");
        NXFNormaliser normaliser = new NXFNormaliser(origHeader);
        
        //When
        NXFLine header = normaliser.header();
        
        //Then
        assertEquals("Expected correct spelling of senstivie column", "SENSITIVE", header.getLine());
    }
    
    @Test
    public void checkThatDecimalRemovedFromPrecision() {
        //Given
        NXFLine origHeader = new NXFLine("precision");
        NXFNormaliser normaliser = new NXFNormaliser(origHeader);
        
        //When
        NXFLine onedp = normaliser.normalise(new NXFLine("100.0"));
        NXFLine fivedp = normaliser.normalise(new NXFLine("4321.12345"));
        NXFLine edgeCase = normaliser.normalise(new NXFLine("100.9999"));
        
        //Then
        assertEquals("Expected a precision without decimal places", "100", onedp.getLine());
        assertEquals("Expected a precision without decimal places", "10000", fivedp.getLine());
        assertEquals("Expected truncation of input to snap to a precision 'higher' rather than the normal case of 'lower'", "100", edgeCase.getLine());
    }
    
    @Test
    public void checkThatPrecisionHandlesInvalidNumbers() {
        //Given
        NXFLine origHeader = new NXFLine("precision");
        NXFNormaliser normaliser = new NXFNormaliser(origHeader);
        
        //When
        NXFLine veryWordy = normaliser.normalise(new NXFLine("I'm not a number"));
        NXFLine numberLike = normaliser.normalise(new NXFLine("123L"));
        NXFLine comma = normaliser.normalise(new NXFLine("12,3"));
        NXFLine multipldDp = normaliser.normalise(new NXFLine("10.1.2.3"));
        NXFLine scientificNumber = normaliser.normalise(new NXFLine("1.1E2"));
        NXFLine nan = normaliser.normalise(new NXFLine("NaN"));
        NXFLine infinity = normaliser.normalise(new NXFLine("Infinity"));
        
        //Then
        assertEquals("Expected pure text to fail", "I'm not a number", veryWordy.getLine());
        assertEquals("Expected number like thing to fail", "123L", numberLike.getLine());
        assertEquals("Expected commas to fail", "12,3", comma.getLine());
        assertEquals("Expected multiple dp to be thrown out", "10.1.2.3", multipldDp.getLine());
        assertEquals("Expected scientific number to pass", "1000", scientificNumber.getLine());
        assertEquals("Expected NaN to pass", "NaN", nan.getLine());
        assertEquals("Expected NaN to pass", "Infinity", infinity.getLine());
    }
    
    @Test
    public void checkThatNewLinesCharactersAreReplacedWithSpaces() {
        //Given
        NXFLine origHeader = new NXFLine("precision");
        NXFNormaliser normaliser = new NXFNormaliser(origHeader);
        
        //When
        NXFLine lines = normaliser.normalise(new NXFLine("I have\nnew\nlines in me"));
        
        //Then
        assertEquals("Expected new lines to be removed", "I have new lines in me", lines.getLine());
    }
}
