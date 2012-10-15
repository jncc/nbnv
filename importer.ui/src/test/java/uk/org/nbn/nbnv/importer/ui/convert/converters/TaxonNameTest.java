/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.org.nbn.nbnv.importer.ui.convert.converters;

import java.util.ArrayList;
import java.util.List;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import uk.org.nbn.nbnv.importer.ui.convert.BadDataException;
import uk.org.nbn.nbnv.importer.ui.parser.ColumnMapping;
import uk.org.nbn.nbnv.importer.ui.parser.DarwinCoreField;

/**
 *
 * @author Matt Debont
 */
public class TaxonNameTest {
    List<ColumnMapping> mappings;
    TaxonName step;
    
    @Before
    public void setUp() {
        step = new TaxonName();
        
        mappings = new ArrayList<ColumnMapping>();
        mappings.add(new ColumnMapping(0, "TaxonName", DarwinCoreField.TAXONNAME));
    }
    
    @Test
    public void testIsStepNeededTrue() {
        // Should work if TaxonName field is detected
        assertEquals(step.isStepNeeded(mappings), true);

//        // Should work if we find an attribute field with an appropriate name
//        mappings.set(0, new ColumnMapping(0, "TaxonName", DarwinCoreField.ATTRIBUTE));
//        assertEquals(step.isStepNeeded(mappings), true);
    }
    
    @Test
    public void testIsStepNeededFalse() {
        // Should not perform step if TaxonVersionKey heading (as a TaxonID field)
        // is present
        mappings.add(new ColumnMapping(1, "TaxonVersionKey", DarwinCoreField.TAXONID));
        assertEquals(step.isStepNeeded(mappings), false);
    }
    
    @Test
    public void testModifyHeader() {
        step.isStepNeeded(mappings);
        step.modifyHeader(mappings);
        
        assertEquals(mappings.size(), 2);
        assertEquals(mappings.get(1).getField(), DarwinCoreField.TAXONID);
    }
    
    @Ignore
    public void testModifyRowRecordingEntity() {
        step.isStepNeeded(mappings);
        step.modifyHeader(mappings);
        
        // Check as RecordingEntity Fetch
        List<String> row = new ArrayList<String>();
        row.add("\"magna\" Bluethroat");
        try {
            step.modifyRow(row);
        } catch (BadDataException ex) {
            fail("Got a BadDataException: " + ex.getMessage());
        }
        assertEquals(row.get(1), "NHMSYS0020316779");
        
        // Check as qualified scientific name
        row.set(0, "Luscinia svecica subsp. magna");
        try {
            step.modifyRow(row);
        } catch (BadDataException ex) {
            fail("Got a BadDataException: " + ex.getMessage());
        }
        assertEquals(row.get(1), "NHMSYS0020316779");
        
        // Check as TaxonVersionKey
        row.set(0, "NHMSYS0020316841");
        try {
            step.modifyRow(row);
        } catch (BadDataException ex) {
            fail("Got a BadDataException: " + ex.getMessage());
        }
        assertEquals(row.get(1), "NHMSYS0020316779");
    }
}
