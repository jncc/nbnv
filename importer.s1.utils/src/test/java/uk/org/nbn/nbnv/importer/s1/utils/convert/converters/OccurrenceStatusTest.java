/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.org.nbn.nbnv.importer.s1.utils.convert.converters;

import java.util.ArrayList;
import java.util.List;
import static org.junit.Assert.*;
import org.junit.Test;
import uk.org.nbn.nbnv.importer.s1.utils.errors.BadDataException;
import uk.org.nbn.nbnv.importer.s1.utils.parser.ColumnMapping;
import uk.org.nbn.nbnv.importer.s1.utils.parser.DarwinCoreField;

/**
 *
 * @author Paul Gilbertson
 */
public class OccurrenceStatusTest {
    
    public OccurrenceStatusTest() {
    }
    /**
     * Test of getName method, of class OccurrenceStatus.
     */
    @Test
    public void testGetName() {
        System.out.println("getName");
        OccurrenceStatus instance = new OccurrenceStatus();
        String expResult = "Convert ZeroAbundance T-F flag to controlled vocabulary";
        String result = instance.getName();
        assertEquals(expResult, result);
    }

    /**
     * Test of isStepNeeded method, of class OccurrenceStatus.
     */
    @Test
    public void testIsStepNeededTrue() {
        System.out.println("isStepNeededTrue");
        
        List<ColumnMapping> columns = new ArrayList<ColumnMapping>();
        ColumnMapping mapping = new ColumnMapping(0, "mapping", DarwinCoreField.OCCURRENCESTATUS);
        columns.add(mapping);

        OccurrenceStatus instance = new OccurrenceStatus();
        boolean expResult = true;
        boolean result = instance.isStepNeeded(columns);
        
        assertEquals(expResult, result);
    }

    /**
     * Test of isStepNeeded method, of class OccurrenceStatus.
     */
    @Test
    public void testIsStepNeededFalse() {
        System.out.println("isStepNeededFalse");
        
        List<ColumnMapping> columns = new ArrayList<ColumnMapping>();
        ColumnMapping mapping = new ColumnMapping(0, "mapping", DarwinCoreField.COLLECTIONCODE);
        columns.add(mapping);

        OccurrenceStatus instance = new OccurrenceStatus();
        boolean expResult = false;
        boolean result = instance.isStepNeeded(columns);
        
        assertEquals(expResult, result);
    }

    /**
     * Test of modifyRow method, of class OccurrenceStatus.
     */
    @Test
    public void testModifyRowT() throws BadDataException {
        System.out.println("modifyRowT");
        
        List<ColumnMapping> columns = new ArrayList<ColumnMapping>();
        ColumnMapping mapping = new ColumnMapping(0, "mapping", DarwinCoreField.OCCURRENCESTATUS);
        columns.add(mapping);

        OccurrenceStatus instance = new OccurrenceStatus();
        instance.isStepNeeded(columns);
        
        List<String> row = new ArrayList<String>();
        row.add("T");
        
        instance.modifyRow(row);
        
        assertEquals("absence", row.get(0));
    }

    /**
     * Test of modifyRow method, of class OccurrenceStatus.
     */
    @Test
    public void testModifyRowF() throws BadDataException {
        System.out.println("modifyRowF");
        
        List<ColumnMapping> columns = new ArrayList<ColumnMapping>();
        ColumnMapping mapping = new ColumnMapping(0, "mapping", DarwinCoreField.OCCURRENCESTATUS);
        columns.add(mapping);

        OccurrenceStatus instance = new OccurrenceStatus();
        instance.isStepNeeded(columns);
        
        List<String> row = new ArrayList<String>();
        row.add("F");
        
        instance.modifyRow(row);
        
        assertEquals("presence", row.get(0));
    }
    
        /**
     * Test of modifyRow method, of class OccurrenceStatus.
     */
    @Test(expected=BadDataException.class)
    public void testModifyRowBadData() throws BadDataException {
        System.out.println("modifyRowBadData");
        
        List<ColumnMapping> columns = new ArrayList<ColumnMapping>();
        ColumnMapping mapping = new ColumnMapping(0, "mapping", DarwinCoreField.OCCURRENCESTATUS);
        columns.add(mapping);

        OccurrenceStatus instance = new OccurrenceStatus();
        instance.isStepNeeded(columns);
        
        List<String> row = new ArrayList<String>();
        row.add("Crap");
        
        instance.modifyRow(row);
    }

}
