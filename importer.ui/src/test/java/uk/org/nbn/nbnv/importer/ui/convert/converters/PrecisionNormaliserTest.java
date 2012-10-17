/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.org.nbn.nbnv.importer.ui.convert.converters;

import java.util.LinkedList;
import java.util.List;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import uk.org.nbn.nbnv.importer.ui.convert.BadDataException;
import uk.org.nbn.nbnv.importer.ui.parser.ColumnMapping;
import uk.org.nbn.nbnv.importer.ui.parser.DarwinCoreField;

/**
 *
 * @author Matt Debont
 */
public class PrecisionNormaliserTest {
    List<ColumnMapping> mappings;
    
    @Before
    public void setUp() {
        mappings = new LinkedList<ColumnMapping>();
        mappings.add(new ColumnMapping(0, "Precision", DarwinCoreField.GRIDREFERENCEPRECISION));
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of isStepNeeded method, of class PrecisionNormaliser.
     */
    @Test
    public void testIsStepNeeded() {
        PrecisionNormaliser instance = new PrecisionNormaliser();
        assertEquals(instance.isStepNeeded(mappings), true);
        
        mappings.set(0, new ColumnMapping(0, "RecordKey", DarwinCoreField.OCCURRENCEID));
        assertEquals(instance.isStepNeeded(mappings), false);
        
    }

    /**
     * Test of modifyHeader method, of class PrecisionNormaliser.
     */
    @Test
    public void testModifyHeader() {
        List<ColumnMapping> temp = mappings.subList(0, 1);
        PrecisionNormaliser instance = new PrecisionNormaliser();
        instance.modifyHeader(mappings);
        
        assertEquals(mappings, temp);
    }

    /**
     * Test of modifyRow method, of class PrecisionNormaliser.
     */
    @Test
    public void testModifyRow() {
        List<String> row = new LinkedList<String>();
        PrecisionNormaliser instance = new PrecisionNormaliser();
        
        instance.isStepNeeded(mappings);
        instance.modifyHeader(mappings);
        
        try {
            row.add("50");
            instance.modifyRow(row);
            assertEquals(row.get(0), "100");
        } catch (BadDataException ex) {
            fail("Caught BadDataException on intput '50'");
        }
        
        try {
            row.set(0, "150");
            instance.modifyRow(row);
            assertEquals(row.get(0), "1000");
        } catch (BadDataException ex) {
            fail("Caught BadDataException on intput '150'");
        }
        
        try {
            row.set(0, "1500");
            instance.modifyRow(row);
            assertEquals(row.get(0), "2000");
        } catch (BadDataException ex) {
            fail("Caught BadDataException on intput '1500'");
        }
        
        try {
            row.set(0, "2500");
            instance.modifyRow(row);
            assertEquals(row.get(0), "10000");
        } catch (BadDataException ex) {
            fail("Caught BadDataException on intput '2500'");
        }            
        
        boolean error = false;
        try {
            row.set(0, "150000");
            instance.modifyRow(row);
        } catch (BadDataException ex) {
            error = true;
        }
        
        if (!error) {
            fail("Expected BadDataException on input '150000' but instead got a value of " + row.get(0));
        }
    }
}
