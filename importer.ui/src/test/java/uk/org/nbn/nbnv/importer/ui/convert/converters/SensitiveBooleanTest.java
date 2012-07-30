/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.org.nbn.nbnv.importer.ui.convert.converters;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
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
        
        Map<DarwinCoreField, String> row = new EnumMap<DarwinCoreField, String>(DarwinCoreField.class);
        row.put(DarwinCoreField.SENSITIVEOCCURRENCE, "T");
        
        SensitiveBoolean instance = new SensitiveBoolean();
        instance.modifyRow(row);
        
        assertEquals("true", row.get(DarwinCoreField.SENSITIVEOCCURRENCE));
    }

    /**
     * Test of modifyRow method, of class SensitiveBoolean.
     */
    @Test
    public void testModifyRowF() throws BadDataException {
        System.out.println("modifyRowF");
        
        Map<DarwinCoreField, String> row = new EnumMap<DarwinCoreField, String>(DarwinCoreField.class);
        row.put(DarwinCoreField.SENSITIVEOCCURRENCE, "F");
        
        SensitiveBoolean instance = new SensitiveBoolean();
        instance.modifyRow(row);
        
        assertEquals("false", row.get(DarwinCoreField.SENSITIVEOCCURRENCE));
    }
    
        /**
     * Test of modifyRow method, of class SensitiveBoolean.
     */
    @Test(expected=BadDataException.class)
    public void testModifyRowBadData() throws BadDataException {
        System.out.println("modifyRowBadData");
        
        Map<DarwinCoreField, String> row = new EnumMap<DarwinCoreField, String>(DarwinCoreField.class);
        row.put(DarwinCoreField.SENSITIVEOCCURRENCE, "Crud");
        
        SensitiveBoolean instance = new SensitiveBoolean();
        instance.modifyRow(row);
    }

}
