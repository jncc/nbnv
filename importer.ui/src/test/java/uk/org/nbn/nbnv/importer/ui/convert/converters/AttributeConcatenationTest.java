/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.org.nbn.nbnv.importer.ui.convert.converters;

import java.util.ArrayList;
import java.util.List;
import junit.framework.Assert;
import org.json.JSONObject;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import uk.org.nbn.nbnv.importer.ui.parser.ColumnMapping;
import uk.org.nbn.nbnv.importer.ui.parser.DarwinCoreField;

/**
 *
 * @author Paul Gilbertson
 */
public class AttributeConcatenationTest {

    public AttributeConcatenationTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of getName method, of class AttributeConcatenation.
     */
    @Test
    public void testGetName() {
        System.out.println("getName");

        AttributeConcatenation instance = new AttributeConcatenation();
        String expResult = "Concatenate attributes";
        String result = instance.getName();
        assertEquals(expResult, result);
    }

    /**
     * Test of isStepNeeded method, of class AttributeConcatenation.
     */
    @Test
    public void testIsStepNeededTrue() {
        System.out.println("isStepNeeded");

        List<ColumnMapping> columns = new ArrayList<ColumnMapping>();
        columns.add(new ColumnMapping(0, "mapping", DarwinCoreField.ATTRIBUTE));

        AttributeConcatenation instance = new AttributeConcatenation();
        boolean expResult = true;
        boolean result = instance.isStepNeeded(columns);
        assertEquals(expResult, result);
    }

    @Test
    public void testIsStepNeededFalse() {
        System.out.println("isStepNeeded");

        List<ColumnMapping> columns = new ArrayList<ColumnMapping>();
        columns.add(new ColumnMapping(0, "mapping", DarwinCoreField.EVENTDATE));

        AttributeConcatenation instance = new AttributeConcatenation();
        boolean expResult = false;
        boolean result = instance.isStepNeeded(columns);
        assertEquals(expResult, result);
    }

    /**
     * Test of modifyHeader method, of class AttributeConcatenation.
     */
    @Test
    public void testModifyHeader() {
        System.out.println("modifyHeader");

        List<ColumnMapping> columns = new ArrayList<ColumnMapping>();
        columns.add(new ColumnMapping(0, "mapping", DarwinCoreField.ATTRIBUTE));

        AttributeConcatenation instance = new AttributeConcatenation();
        instance.modifyHeader(columns);
        
        Assert.assertEquals(2, columns.size());
        Assert.assertEquals(DarwinCoreField.DYNAMICPROPERTIES, columns.get(1).getField());
    }

    /**
     * Test of modifyRow method, of class AttributeConcatenation.
     */
    @Test
    public void testModifyRow() throws Exception {
        System.out.println("modifyRow");

        List<ColumnMapping> columns = new ArrayList<ColumnMapping>();
        columns.add(new ColumnMapping(0, "mapping", DarwinCoreField.ATTRIBUTE));

        AttributeConcatenation instance = new AttributeConcatenation();
        instance.isStepNeeded(columns);
        instance.modifyHeader(columns);
        
        List<String> row = new ArrayList<String>();
        row.add("test");
        
        instance.modifyRow(row);
        
        Assert.assertEquals("mapping", new JSONObject(row.get(1)).keys().next());
        
    }
}
