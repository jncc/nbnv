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
        assertEquals("Expected header to be DynamicProperties", "DYNAMICPROPERTIES", header.toString());
        assertEquals("Expected json in attribute column", "{\"ATTRIBUTE1\":\"my data\"}", data.toString());
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
        assertEquals("Expected srs column created", "PROJECTION\tSRS", header.toString());
        assertEquals("Expected srs to be filled in", "WGS84\t4326", wgs84.toString());
        assertEquals("Expected srs to be left blank", "some unkown type\t", other.toString());
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
        assertEquals("Expected headers", "SRS\tPROJECTION", header.toString());
        assertEquals("Expected srs to be filled in", wgs84.toString(), "4326\tWGS84");
        assertEquals("Expected srs to be left as is", other.toString(), "leave me\thmm");
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
        assertEquals("Expected P to be turned to OO", pValue.toString(), "OO");
        assertEquals("Expected DD to have been left alone", ddValue.toString(), "DD");
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
        assertEquals("Expected TL45 to be normalised", tl45.toString(), "TL45");
        assertEquals("Expected TL45 to be normalised", sn42WithHyphen.toString(), "SN42");
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
        assertEquals("Expected to read absence", "absence", t.toString());
        assertEquals("Expected to read absence", "absence", tru.toString());
        assertEquals("Expected to read absence", "absence", yes.toString());
        assertEquals("Expected to read absence", "absence", absent.toString());
        assertEquals("Expected to read absence", "absence", absence.toString());
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
        assertEquals("Expected to read absence", "presence", f.toString());
        assertEquals("Expected to read absence", "presence", fals.toString());
        assertEquals("Expected to read absence", "presence", no.toString());
        assertEquals("Expected to read absence", "presence", present.toString());
        assertEquals("Expected to read absence", "presence", presence.toString());
    }
    
    @Test
    public void checkThatZeroAbundancePassesUnknownValuesThru() {
        //Given
        NXFLine origHeader = new NXFLine("Zero Abundance");
        NXFNormaliser normaliser = new NXFNormaliser(origHeader);
        
        //When
        NXFLine value = normaliser.normalise(new NXFLine("Some Unkown Value"));
        
        //Then
        assertEquals("Expected to gracefully handle unknown values", value.toString(), "Some Unkown Value");
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
        assertEquals("Snapped to 100", below100.toString(), "100");
        assertEquals("Snapped to 1000", below1000.toString(), "1000");
        assertEquals("Snapped to 2000", below2000.toString(), "2000");
        assertEquals("Snapped to 10000", below10000.toString(), "10000");
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
        assertEquals("100 remained unchanged", exactly100.toString(), "100");
        assertEquals("1000 remained unchanged", exactly1000.toString(), "1000");
        assertEquals("2000 remained unchanged", exactly2000.toString(), "2000");
        assertEquals("10000 remained unchanged", exactly10000.toString(), "10000");
        assertEquals("Gibberish remained unchanged", gibberish.toString(), "Some Unhandled Gibberish");
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
        assertEquals("Expected to read true", t.toString(), "true");
        assertEquals("Expected to read true", tru.toString(), "true");
        assertEquals("Expected to read true", yes.toString(), "true");
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
        assertEquals("Expected to read false", f.toString(), "false");
        assertEquals("Expected to read false", fals.toString(), "false");
        assertEquals("Expected to read false", no.toString(), "false");
    }
    
    @Test
    public void checkThatSensitivePassesUnknownValuesThru() {
        //Given
        NXFLine origHeader = new NXFLine("SENSITIVE");
        NXFNormaliser normaliser = new NXFNormaliser(origHeader);
        
        //When
        NXFLine value = normaliser.normalise(new NXFLine("Some Unkown Value"));
        
        //Then
        assertEquals("Expected to gracefully handle unknown values", value.toString(), "Some Unkown Value");
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
        assertEquals("Expected site key to be bundled up in the attribute column", "DYNAMICPROPERTIES", header.toString());
        assertEquals("Expected site key to be bundled up in the attribute column", "{\"SITEKEY\":\"my favourite location\"}", site.toString());
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
        assertEquals("Expected column heading not to have double quotes", "TAXONVERSIONKEY", header.toString());
        assertEquals("Expected data not to have double quotes", "NBNSYS0000000001", data.toString());
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
        assertEquals("Expected only a single colum", "TAXONVERSIONKEY", header.toString());
        assertEquals("Expected only a single piece of data", "NBNSYS0000000001", site.toString());
    }
    
    @Test
    public void checkThatMispelledSenitiveFixed() {
        //Given
        NXFLine origHeader = new NXFLine("senitive");
        NXFNormaliser normaliser = new NXFNormaliser(origHeader);
        
        //When
        NXFLine header = normaliser.header();
        
        //Then
        assertEquals("Expected correct spelling of senstivie column", "SENSITIVE", header.toString());
    }
    
    @Test
    public void checkThatMispelledSenstiveFixed() {
        //Given
        NXFLine origHeader = new NXFLine("senstive");
        NXFNormaliser normaliser = new NXFNormaliser(origHeader);
        
        //When
        NXFLine header = normaliser.header();
        
        //Then
        assertEquals("Expected correct spelling of senstivie column", "SENSITIVE", header.toString());
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
        assertEquals("Expected a precision without decimal places", "100", onedp.toString());
        assertEquals("Expected a precision without decimal places", "10000", fivedp.toString());
        assertEquals("Expected truncation of input to snap to a precision 'higher' rather than the normal case of 'lower'", "100", edgeCase.toString());
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
        assertEquals("Expected pure text to fail", "I'm not a number", veryWordy.toString());
        assertEquals("Expected number like thing to fail", "123L", numberLike.toString());
        assertEquals("Expected commas to fail", "12,3", comma.toString());
        assertEquals("Expected multiple dp to be thrown out", "10.1.2.3", multipldDp.toString());
        assertEquals("Expected scientific number to pass", "1000", scientificNumber.toString());
        assertEquals("Expected NaN to pass", "NaN", nan.toString());
        assertEquals("Expected NaN to pass", "Infinity", infinity.toString());
    }
    
    @Test
    public void checkThatNewLinesCharactersAreReplacedWithSpaces() {
        //Given
        NXFLine origHeader = new NXFLine("precision");
        NXFNormaliser normaliser = new NXFNormaliser(origHeader);
        
        //When
        NXFLine lines = normaliser.normalise(new NXFLine("I have\nnew\nlines in me"));
        
        //Then
        assertEquals("Expected new lines to be removed", "I have new lines in me", lines.toString());
    }
    
    @Test
    public void checkThatDynamicPropertiesGetsHandledAsANormalAttribute() {
        //Given
        NXFLine header = new NXFLine("DynamicProperties");
        NXFNormaliser normaliser = new NXFNormaliser(header);
        
        //When
        NXFLine line = normaliser.normalise(new NXFLine("some data"));
        
        //Then
        assertEquals("Expected dynamic properties gets wrapped like attribute", "{\"DYNAMICPROPERTIES\":\"some data\"}", line.toString());
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void checkThatDuplicateAttributePropertiesCausesAFailure() {
        //Given
        NXFLine header = new NXFLine("Duplicate \t Duplicate");
        
        //When
        NXFNormaliser normaliser = new NXFNormaliser(header);
        
        //Then
        fail("Expected to fail as the header has duplicate columns");
    }
}
