/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.org.nbn.nbnv.importer.ui.convert.converters;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import static org.junit.Assert.*;
import org.junit.Test;
import uk.org.nbn.nbnv.importer.ui.convert.BadDataException;
import uk.org.nbn.nbnv.importer.ui.parser.ColumnMapping;
import uk.org.nbn.nbnv.importer.ui.parser.DarwinCoreField;

/**
 *
 * @author Paul Gilbertson
 */
public class SensitiveBooleanTest {
    
    public SensitiveBooleanTest() {
    }
    /**
     * Test of getName method, of class SensitiveBoolean.
     */
    @Test
    public void testGetName() {
        System.out.println("getName");
        SensitiveBoolean instance = new SensitiveBoolean();
        String expResult = "Convert Sensitive T-F flag to boolean";
        String result = instance.getName();
        assertEquals(expResult, result);
    }

    /**
     * Test of isStepNeeded method, of class SensitiveBoolean.
     */
    @Test
    public void testIsStepNeededTrue() {
        System.out.println("isStepNeededTrue");
        
        List<ColumnMapping> columns = new ArrayList<ColumnMapping>();
        ColumnMapping mapping = new ColumnMapping(0, "mapping", DarwinCoreField.SENSITIVEOCCURRENCE);
        columns.add(mapping);

        SensitiveBoolean instance = new SensitiveBoolean();
        boolean expResult = true;
        boolean result = instance.isStepNeeded(columns);
        
        assertEquals(expResult, result);
    }

    /**
     * Test of isStepNeeded method, of class SensitiveBoolean.
     */
    @Test
    public void testIsStepNeededFalse() {
        System.out.println("isStepNeededFalse");
        
        List<ColumnMapping> columns = new ArrayList<ColumnMapping>();
        ColumnMapping mapping = new ColumnMapping(0, "mapping", DarwinCoreField.COLLECTIONCODE);
        columns.add(mapping);

        SensitiveBoolean instance = new SensitiveBoolean();
        boolean expResult = false;
        boolean result = instance.isStepNeeded(columns);
        
        assertEquals(expResult, result);
    }

    /**
     * Test of modifyRow method, of class SensitiveBoolean.
     */
    @Test
    public void testModifyRowT() throws BadDataException {
        System.out.println("modifyRowT");
        
        List<ColumnMapping> columns = new ArrayList<ColumnMapping>();
        ColumnMapping mapping = new ColumnMapping(0, "mapping", DarwinCoreField.SENSITIVEOCCURRENCE);
        columns.add(mapping);

        SensitiveBoolean instance = new SensitiveBoolean();
        instance.isStepNeeded(columns);
        
        List<String> row = new ArrayList<String>();
        row.add("T");
        
        instance.modifyRow(row);
        
        assertEquals("true", row.get(0));
    }

    /**
     * Test of modifyRow method, of class SensitiveBoolean.
     */
    @Test
    public void testModifyRowF() throws BadDataException {
        System.out.println("modifyRowF");
        
        List<ColumnMapping> columns = new ArrayList<ColumnMapping>();
        ColumnMapping mapping = new ColumnMapping(0, "mapping", DarwinCoreField.SENSITIVEOCCURRENCE);
        columns.add(mapping);

        SensitiveBoolean instance = new SensitiveBoolean();
        instance.isStepNeeded(columns);
        
        List<String> row = new ArrayList<String>();
        row.add("F");
        
        instance.modifyRow(row);
        
        assertEquals("false", row.get(0));
    }
    
        /**
     * Test of modifyRow method, of class SensitiveBoolean.
     */
    @Test(expected=BadDataException.class)
    public void testModifyRowBadData() throws BadDataException {
        System.out.println("modifyRowBadData");
        
        List<ColumnMapping> columns = new ArrayList<ColumnMapping>();
        ColumnMapping mapping = new ColumnMapping(0, "mapping", DarwinCoreField.SENSITIVEOCCURRENCE);
        columns.add(mapping);

        SensitiveBoolean instance = new SensitiveBoolean();
        instance.isStepNeeded(columns);
        
        List<String> row = new ArrayList<String>();
        row.add("Crap");
        
        instance.modifyRow(row);
    }

}
