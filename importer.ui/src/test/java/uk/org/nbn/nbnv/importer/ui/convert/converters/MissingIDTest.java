/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.org.nbn.nbnv.importer.ui.convert.converters;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import uk.org.nbn.nbnv.importer.ui.convert.BadDataException;
import uk.org.nbn.nbnv.importer.ui.parser.ColumnMapping;
import uk.org.nbn.nbnv.importer.ui.parser.DarwinCoreField;

/**
 *
 * @author Matt Debont
 */
public class MissingIDTest {
    private List<ColumnMapping> mappings;
    private List<String> row;
    private MissingID step;
    
    @Before
    public void setup() {
        mappings = new ArrayList<ColumnMapping>();
        mappings.add(new ColumnMapping(0, "TaxonName", DarwinCoreField.TAXONNAME));
        mappings.add(new ColumnMapping(1, "SurveyKey", DarwinCoreField.COLLECTIONCODE));
        mappings.add(new ColumnMapping(2, "SiteKey", DarwinCoreField.LOCATIONID));

        row = new ArrayList<String>(Arrays.asList("CT0000TMA\tCS0000TMA\tSITE001".split("\t", -1)));
        
        step = new MissingID();
    }
    
    @Test
    public void testIsStepNeededTrue() {
        assertEquals(step.isStepNeeded(mappings), true);
    }
    
    @Test
    public void testIsStepNeededFalse() {
        mappings.set(0, new ColumnMapping(0, "RecordKey", DarwinCoreField.OCCURRENCEID));
        assertEquals(step.isStepNeeded(mappings), false);
    }
    
    @Test
    public void testModifyHeaders() {
        step.modifyHeader(mappings);
        assertEquals(mappings.size(), 4);
        assertEquals(mappings.get(3).getField(), DarwinCoreField.OCCURRENCEID);
    }
    
    @Test
    public void testModifyRow() {
        step.modifyHeader(mappings);
        try {
            step.modifyRow(row);
        } catch(BadDataException ex) {
            fail("Threw a BadDataException: " + ex.getMessage());
        }
        assertEquals(row.get(3), "uid:CS0000TMA:SITE001:1");
    }
}
