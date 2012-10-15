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
public class PreferredTaxonVersionKeyTest {
    List<ColumnMapping> mappings;
    PreferredTaxonVersionKey step;
    
    @Before
    public void setUp() {
        step = new PreferredTaxonVersionKey();
        
        mappings = new ArrayList<ColumnMapping>();
        mappings.add(new ColumnMapping(0, "TaxonVersionKey", DarwinCoreField.TAXONID));
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
    public void testModifyHeader() {
        step.isStepNeeded(mappings);
        List<ColumnMapping> temp = mappings;
        step.modifyHeader(mappings);
        assertEquals(mappings, temp);
    }
    
    @Ignore
    public void testModifyRows() {
        step.isStepNeeded(mappings);
        step.modifyHeader(mappings);
        
        List<String> row = new ArrayList<String>();
        row.add("NBNSYS0000006638");
        
        try {
            step.modifyRow(row);
        } catch (BadDataException ex) {
            fail("Got a BadDataException: " + ex.getMessage());
        }
        assertEquals(row.get(0), "NHMSYS0020528265");
        
        // Branch coverage, should just lookup this time
        row.set(0, "NBNSYS0000006638");
        try {
            step.modifyRow(row);
        } catch (BadDataException ex) {
            fail("Got a BadDataException: " + ex.getMessage());
        }        
        assertEquals(row.get(0), "NHMSYS0020528265");
    }
    
    @Ignore
    public void testModifyRowsBad() {
        step.isStepNeeded(mappings);
        step.modifyHeader(mappings);
        
        List<String> row = new ArrayList<String>();
        row.add("A Bunch of Rubbish");
        
        boolean passed = false;
        
        try {
            step.modifyRow(row);
        } catch (BadDataException ex) {
            passed = true;
        }
        
        if (!passed) {
            fail("Should have got a BadDataException, we did not");
        }
    }
}
