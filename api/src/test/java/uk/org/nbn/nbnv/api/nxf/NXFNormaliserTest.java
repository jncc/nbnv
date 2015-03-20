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
        NXFLine origHeader = new NXFLine("Record key\tattribute1");
        NXFNormaliser normaliser = new NXFNormaliser(origHeader);
        
        //When
        NXFLine header = normaliser.header();
        NXFLine data = normaliser.normalise(new NXFLine("1 \t my data"));
        
        //Then
        assertEquals("Expected header to be DynamicProperties", "RECORDKEY\tDYNAMICPROPERTIES", header.toString());
        assertEquals("Expected json in attribute column", "1\t{\"ATTRIBUTE1\":\"my data\"}", data.toString());
    }
    
    @Test
    public void checkThatFillsCreatesSRSColumnIfMissing() {
        //Given
        NXFLine origHeader = new NXFLine("RECORDKEY\tPROJECTION");
        NXFNormaliser normaliser = new NXFNormaliser(origHeader);
        
        //When
        NXFLine header = normaliser.header();
        NXFLine wgs84 = normaliser.normalise(new NXFLine("1\tWGS84"));
        NXFLine other = normaliser.normalise(new NXFLine("1\tsome unkown type"));
        
        //Then
        assertEquals("Expected srs column created", "RECORDKEY\tPROJECTION\tSRS", header.toString());
        assertEquals("Expected srs to be filled in", "1\tWGS84\t4326", wgs84.toString());
        assertEquals("Expected srs to be left blank", "1\tsome unkown type\t", other.toString());
    }
    
    @Test
    public void checkThatReplacesValuesInSRSColumnIfPresent() {
        //Given
        NXFLine origHeader = new NXFLine("Record key\tSrs\tProjection");
        NXFNormaliser normaliser = new NXFNormaliser(origHeader);
        
        //When
        NXFLine header = normaliser.header();
        NXFLine wgs84 = normaliser.normalise(new NXFLine("1\tWrong epsg code \t WGS84"));
        NXFLine other = normaliser.normalise(new NXFLine("1\tleave me \t hmm"));
        
        //Then
        assertEquals("Expected headers", "RECORDKEY\tSRS\tPROJECTION", header.toString());
        assertEquals("Expected srs to be filled in", wgs84.toString(), "1\t4326\tWGS84");
        assertEquals("Expected srs to be left as is", other.toString(), "1\tleave me\thmm");
    }
    
    @Test
    public void checkThatDateTypeValuesOfPGetChangedToOO() {
        //Given
        NXFLine origHeader = new NXFLine("Record key\tDate type");
        NXFNormaliser normaliser = new NXFNormaliser(origHeader);
        
        //When
        NXFLine pValue = normaliser.normalise(new NXFLine("1\tP"));
        NXFLine ddValue = normaliser.normalise(new NXFLine("1\tDD"));
        
        //Then
        assertEquals("Expected P to be turned to OO", pValue.toString(), "1\tOO");
        assertEquals("Expected DD to have been left alone", ddValue.toString(), "1\tDD");
    }
    
    @Test
    public void checkThatRubbishInGridRefGetsRemoved() {
        //Given
        NXFLine origHeader = new NXFLine("Record key\tGrid Reference");
        NXFNormaliser normaliser = new NXFNormaliser(origHeader);
        
        //When
        NXFLine tl45 = normaliser.normalise(new NXFLine("1\tTL 45"));
        NXFLine sn42WithHyphen = normaliser.normalise(new NXFLine("1\tSN-42"));
        
        //Then
        assertEquals("Expected TL45 to be normalised", tl45.toString(), "1\tTL45");
        assertEquals("Expected TL45 to be normalised", sn42WithHyphen.toString(), "1\tSN42");
    }
    
    @Test
    public void checkThatZeroAbundanceAcceptsAbsenceValues() {
        //Given
        NXFLine origHeader = new NXFLine("Record key\tZero Abundance");
        NXFNormaliser normaliser = new NXFNormaliser(origHeader);
        
        //When
        NXFLine t = normaliser.normalise(new NXFLine("1\tt"));
        NXFLine tru = normaliser.normalise(new NXFLine("1\ttRuE"));
        NXFLine yes = normaliser.normalise(new NXFLine("1\tyEs"));
        NXFLine absent = normaliser.normalise(new NXFLine("1\tabsent"));
        NXFLine absence = normaliser.normalise(new NXFLine("1\tABSENCE"));
        
        //Then
        assertEquals("Expected to read absence", "1\tabsence", t.toString());
        assertEquals("Expected to read absence", "1\tabsence", tru.toString());
        assertEquals("Expected to read absence", "1\tabsence", yes.toString());
        assertEquals("Expected to read absence", "1\tabsence", absent.toString());
        assertEquals("Expected to read absence", "1\tabsence", absence.toString());
    }
    
    @Test
    public void checkThatZeroAbundanceAcceptsPresenceValues() {
        //Given
        NXFLine origHeader = new NXFLine("Record key\tZero Abundance");
        NXFNormaliser normaliser = new NXFNormaliser(origHeader);
        
        //When
        NXFLine f = normaliser.normalise(new NXFLine("1\tf"));
        NXFLine fals = normaliser.normalise(new NXFLine("1\tfalse"));
        NXFLine no = normaliser.normalise(new NXFLine("1\tno"));
        NXFLine present = normaliser.normalise(new NXFLine("1\tpresent"));
        NXFLine presence = normaliser.normalise(new NXFLine("1\tPRESENCE"));
        
        //Then
        assertEquals("Expected to read absence", "1\tpresence", f.toString());
        assertEquals("Expected to read absence", "1\tpresence", fals.toString());
        assertEquals("Expected to read absence", "1\tpresence", no.toString());
        assertEquals("Expected to read absence", "1\tpresence", present.toString());
        assertEquals("Expected to read absence", "1\tpresence", presence.toString());
    }
    
    @Test
    public void checkThatZeroAbundancePassesUnknownValuesThru() {
        //Given
        NXFLine origHeader = new NXFLine("Record key\tZero Abundance");
        NXFNormaliser normaliser = new NXFNormaliser(origHeader);
        
        //When
        NXFLine value = normaliser.normalise(new NXFLine("1\tSome Unkown Value"));
        
        //Then
        assertEquals("Expected to gracefully handle unknown values", value.toString(), "1\tSome Unkown Value");
    }
    
    @Test
    public void checkThatPrecisionSnapsValues() {
        //Given
        NXFLine origHeader = new NXFLine("Record key\tPrecision");
        NXFNormaliser normaliser = new NXFNormaliser(origHeader);
        
        //When
        NXFLine below100 = normaliser.normalise(new NXFLine("1\t99"));
        NXFLine below1000 = normaliser.normalise(new NXFLine("1\t999"));
        NXFLine below2000 = normaliser.normalise(new NXFLine("1\t1999"));
        NXFLine below10000 = normaliser.normalise(new NXFLine("1\t9999"));
        
        //Then
        assertEquals("Snapped to 100", below100.toString(), "1\t100");
        assertEquals("Snapped to 1000", below1000.toString(), "1\t1000");
        assertEquals("Snapped to 2000", below2000.toString(), "1\t2000");
        assertEquals("Snapped to 10000", below10000.toString(), "1\t10000");
    }
    
    @Test
    public void checkThatPrecisionValuesRenameTheSame() {
        //Given
        NXFLine origHeader = new NXFLine("Record key\tPrecision");
        NXFNormaliser normaliser = new NXFNormaliser(origHeader);
        
        //When
        NXFLine exactly100 = normaliser.normalise(new NXFLine("1\t10"));
        NXFLine exactly1000 = normaliser.normalise(new NXFLine("1\t1000"));
        NXFLine exactly2000 = normaliser.normalise(new NXFLine("1\t2000"));
        NXFLine exactly10000 = normaliser.normalise(new NXFLine("1\t10000"));
        NXFLine gibberish = normaliser.normalise(new NXFLine("1\tSome Unhandled Gibberish"));
        
        //Then
        assertEquals("100 remained unchanged", exactly100.toString(), "1\t100");
        assertEquals("1000 remained unchanged", exactly1000.toString(), "1\t1000");
        assertEquals("2000 remained unchanged", exactly2000.toString(), "1\t2000");
        assertEquals("10000 remained unchanged", exactly10000.toString(), "1\t10000");
        assertEquals("Gibberish remained unchanged", gibberish.toString(), "1\tSome Unhandled Gibberish");
    }
    
    @Test
    public void checkThatSensitiveAcceptsTrueValues() {
        //Given
        NXFLine origHeader = new NXFLine("RECORDKEY\tSENSITIVE");
        NXFNormaliser normaliser = new NXFNormaliser(origHeader);
        
        //When
        NXFLine t = normaliser.normalise(new NXFLine("1\tt"));
        NXFLine tru = normaliser.normalise(new NXFLine("1\ttRuE"));
        NXFLine yes = normaliser.normalise(new NXFLine("1\tyEs"));
        
        //Then
        assertEquals("Expected to read true", t.toString(), "1\ttrue");
        assertEquals("Expected to read true", tru.toString(), "1\ttrue");
        assertEquals("Expected to read true", yes.toString(), "1\ttrue");
    }
    
    @Test
    public void checkThatSensitiveAcceptsFalseValues() {
        //Given
        NXFLine origHeader = new NXFLine("RECORDKEY\tSENSITIVE");
        NXFNormaliser normaliser = new NXFNormaliser(origHeader);
        
        //When
        NXFLine f = normaliser.normalise(new NXFLine("1\tf"));
        NXFLine fals = normaliser.normalise(new NXFLine("1\tfAlSe"));
        NXFLine no = normaliser.normalise(new NXFLine("1\tno"));
        
        //Then
        assertEquals("Expected to read false", f.toString(), "1\tfalse");
        assertEquals("Expected to read false", fals.toString(), "1\tfalse");
        assertEquals("Expected to read false", no.toString(), "1\tfalse");
    }
    
    @Test
    public void checkThatSensitivePassesUnknownValuesThru() {
        //Given
        NXFLine origHeader = new NXFLine("RECORDKEY\tSENSITIVE");
        NXFNormaliser normaliser = new NXFNormaliser(origHeader);
        
        //When
        NXFLine value = normaliser.normalise(new NXFLine("1\tSome Unkown Value"));
        
        //Then
        assertEquals("Expected to gracefully handle unknown values", value.toString(), "1\tSome Unkown Value");
    }
    
    @Test
    public void checkThatSiteKeyTreatedAsAnAttribute() {
        //Given
        NXFLine origHeader = new NXFLine("RECORDKEY\tSITEKEY");
        NXFNormaliser normaliser = new NXFNormaliser(origHeader);
        
        //When
        NXFLine header = normaliser.header();
        NXFLine site = normaliser.normalise(new NXFLine("1\tmy favourite location"));
        
        //Then
        assertEquals("Expected site key to be bundled up in the attribute column", "RECORDKEY\tDYNAMICPROPERTIES", header.toString());
        assertEquals("Expected site key to be bundled up in the attribute column", "1\t{\"SITEKEY\":\"my favourite location\"}", site.toString());
    }
    
    @Test
    public void checkThatDoubleQuotesRemoved() {
        //Given
        NXFLine origHeader = new NXFLine("\"RECORD\"KEY\"");
        NXFNormaliser normaliser = new NXFNormaliser(origHeader);
        
        //When
        NXFLine header = normaliser.header();
        NXFLine data = normaliser.normalise(new NXFLine("\"NBNSYS00\"00000001\""));
        
        //Then
        assertEquals("Expected column heading not to have double quotes", "RECORDKEY", header.toString());
        assertEquals("Expected data not to have double quotes", "NBNSYS0000000001", data.toString());
    }
    
    @Test
    public void checkThatUnwantedColumnsAreRemoved() {
        //Given
        NXFLine origHeader = new NXFLine("RECORDKEY\tTAXONNAME\tTAXONGROUP\tCOMMONNAME\tTAXONVERSIONKEY");
        NXFNormaliser normaliser = new NXFNormaliser(origHeader);
        
        //When
        NXFLine header = normaliser.header();
        NXFLine site = normaliser.normalise(new NXFLine("1\tHippocrepis comosa\tVascular plant\tHorseshoe vetch\tNBNSYS0000000001"));
        
        //Then
        assertEquals("Expected only a single colum", "RECORDKEY\tTAXONVERSIONKEY", header.toString());
        assertEquals("Expected only a single piece of data", "1\tNBNSYS0000000001", site.toString());
    }
    
    @Test
    public void checkThatMispelledSenitiveFixed() {
        //Given
        NXFLine origHeader = new NXFLine("recordkey\tsenitive");
        NXFNormaliser normaliser = new NXFNormaliser(origHeader);
        
        //When
        NXFLine header = normaliser.header();
        
        //Then
        assertEquals("Expected correct spelling of senstivie column", "RECORDKEY\tSENSITIVE", header.toString());
    }
    
    @Test
    public void checkThatMispelledSenstiveFixed() {
        //Given
        NXFLine origHeader = new NXFLine("recordkey\tsenstive");
        NXFNormaliser normaliser = new NXFNormaliser(origHeader);
        
        //When
        NXFLine header = normaliser.header();
        
        //Then
        assertEquals("Expected correct spelling of senstivie column", "RECORDKEY\tSENSITIVE", header.toString());
    }
    
    @Test
    public void checkThatDecimalRemovedFromPrecision() {
        //Given
        NXFLine origHeader = new NXFLine("recordkey\tprecision");
        NXFNormaliser normaliser = new NXFNormaliser(origHeader);
        
        //When
        NXFLine onedp = normaliser.normalise(new NXFLine("1\t100.0"));
        NXFLine fivedp = normaliser.normalise(new NXFLine("1\t4321.12345"));
        NXFLine edgeCase = normaliser.normalise(new NXFLine("1\t100.9999"));
        
        //Then
        assertEquals("Expected a precision without decimal places", "1\t100", onedp.toString());
        assertEquals("Expected a precision without decimal places", "1\t10000", fivedp.toString());
        assertEquals("Expected truncation of input to snap to a precision 'higher' rather than the normal case of 'lower'", "1\t100", edgeCase.toString());
    }
    
    @Test
    public void checkThatPrecisionHandlesInvalidNumbers() {
        //Given
        NXFLine origHeader = new NXFLine("recordkey\tprecision");
        NXFNormaliser normaliser = new NXFNormaliser(origHeader);
        
        //When
        NXFLine veryWordy = normaliser.normalise(new NXFLine("1\tI'm not a number"));
        NXFLine numberLike = normaliser.normalise(new NXFLine("1\t123L"));
        NXFLine comma = normaliser.normalise(new NXFLine("1\t12,3"));
        NXFLine multipldDp = normaliser.normalise(new NXFLine("1\t10.1.2.3"));
        NXFLine scientificNumber = normaliser.normalise(new NXFLine("1\t1.1E2"));
        NXFLine nan = normaliser.normalise(new NXFLine("1\tNaN"));
        NXFLine infinity = normaliser.normalise(new NXFLine("1\tInfinity"));
        
        //Then
        assertEquals("Expected pure text to fail", "1\tI'm not a number", veryWordy.toString());
        assertEquals("Expected number like thing to fail", "1\t123L", numberLike.toString());
        assertEquals("Expected commas to fail", "1\t12,3", comma.toString());
        assertEquals("Expected multiple dp to be thrown out", "1\t10.1.2.3", multipldDp.toString());
        assertEquals("Expected scientific number to pass", "1\t1000", scientificNumber.toString());
        assertEquals("Expected NaN to pass", "1\tNaN", nan.toString());
        assertEquals("Expected NaN to pass", "1\tInfinity", infinity.toString());
    }
    
    @Test
    public void checkThatNewLinesCharactersAreReplacedWithSpaces() {
        //Given
        NXFLine origHeader = new NXFLine("recordkey\tprecision");
        NXFNormaliser normaliser = new NXFNormaliser(origHeader);
        
        //When
        NXFLine lines = normaliser.normalise(new NXFLine("1\tI have\nnew\nlines in me"));
        
        //Then
        assertEquals("Expected new lines to be removed", "1\tI have new lines in me", lines.toString());
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void checkThatIllegalArgumentExceptionThrownWithAttsAndDynamicProperties() {
        //Given
        NXFLine origHeader = new NXFLine("RecordKey\tCustomColumn\tDynamicProperties");
        
        //When
        NXFNormaliser normaliser = new NXFNormaliser(origHeader);
        
        //Then
        fail("Expected it to fail because both custom attribute columns and a dynamic property column exist in the header");
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
    
    @Test
    public void checkThatMissingRecordKeyColumnGetsCreatedWithRollingNumber() {
        //Given
        NXFLine origHeader = new NXFLine("GridReference");
        NXFNormaliser normaliser = new NXFNormaliser(origHeader);
        
        //When
        NXFLine header = normaliser.header();
        NXFLine row1 = normaliser.normalise(new NXFLine("TL45"));
        NXFLine row2 = normaliser.normalise(new NXFLine("TL46"));
        
        //Then
        assertEquals("Expected row1 with record key", "GRIDREFERENCE\tRECORDKEY", header.toString());
        assertEquals("Expected row1 with record key", "TL45\t1", row1.toString());
        assertEquals("Expected row1 with record key", "TL46\t2", row2.toString());
    }
}
