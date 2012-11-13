/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.org.nbn.nbnv.importer.ui.convert.converters;

import java.util.ArrayList;
import java.util.List;
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
 * @author Matt Debont
 */
public class GridRefTypeStepTest {
    
    public GridRefTypeStepTest() {
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
     * Test of getName method, of class GridRefTypeStep.
     */
    @Test
    public void testGetName() {
        GridRefTypeStep instance = new GridRefTypeStep();
        assertEquals(instance.getName(),
                "Check Grid Reference Projection for non Grid Reference Values");
    }

    /**
     * Test of isStepNeeded method, of class GridRefTypeStep.
     */
    @Test
    public void testIsStepNeeded() {
        GridRefTypeStep instance = new GridRefTypeStep();
        
        List<ColumnMapping> mappings = new ArrayList<ColumnMapping>();
        mappings.add(new ColumnMapping(0, "Projection", DarwinCoreField.GRIDREFERENCETYPE));
        
        assertEquals(true, instance.isStepNeeded(mappings));
        
        mappings = new ArrayList<ColumnMapping>();
        mappings.add(new ColumnMapping(0, "Projection", DarwinCoreField.GRIDREFERENCETYPE));
        mappings.add(new ColumnMapping(1, "SRS", DarwinCoreField.VERBATIMSRS));
        
        assertEquals(true, instance.isStepNeeded(mappings));
        
        mappings = new ArrayList<ColumnMapping>();
        mappings.add(new ColumnMapping(0, "SRS", DarwinCoreField.VERBATIMSRS));
        
        assertEquals(false, instance.isStepNeeded(mappings));
    }

    /**
     * Test of modifyHeader method, of class GridRefTypeStep.
     */
    @Test
    public void testModifyHeader() {
        GridRefTypeStep instance = new GridRefTypeStep();
        
        List<ColumnMapping> mappings = new ArrayList<ColumnMapping>();
        mappings.add(new ColumnMapping(0, "Projection", DarwinCoreField.GRIDREFERENCETYPE));
        
        instance.modifyHeader(mappings);
        
        assertEquals(mappings.size(), 2);
        assertEquals(mappings.get(1).getColumnLabel(), "SRS");
        assertEquals(mappings.get(1).getField(), DarwinCoreField.VERBATIMSRS);
        
        mappings = new ArrayList<ColumnMapping>();
        mappings.add(new ColumnMapping(0, "Projection", DarwinCoreField.GRIDREFERENCETYPE));
        mappings.add(new ColumnMapping(1, "SRS", DarwinCoreField.VERBATIMSRS));
        
        assertEquals(mappings.size(), 2);
        assertEquals(mappings.get(0).getField(), DarwinCoreField.GRIDREFERENCETYPE);
        assertEquals(mappings.get(1).getField(), DarwinCoreField.VERBATIMSRS);
    }

    /**
     * Test of modifyRow method, of class GridRefTypeStep.
     */
    @Test
    public void testModifyRow() throws Exception {
        GridRefTypeStep instance = new GridRefTypeStep();
        
        List<ColumnMapping> mappings = new ArrayList<ColumnMapping>();
        mappings.add(new ColumnMapping(0, "Projection", DarwinCoreField.GRIDREFERENCETYPE));
        
        if (instance.isStepNeeded(mappings)) {
            instance.modifyHeader(mappings);
            
            List<String> row = new ArrayList<String>();
            row.add("WGS84");
            
            instance.modifyRow(row);
            
            assertEquals(row.get(0), "");
            assertEquals(row.get(1), "4326");
            
            row = new ArrayList<String>();
            row.add("OSGB");
            
            instance.modifyRow(row);
            
            assertEquals(row.get(0), "OSGB");
            assertEquals(row.get(1), "");
        } else {
            fail("Step should be required");
        }
        
        instance = new GridRefTypeStep();
        
        mappings = new ArrayList<ColumnMapping>();
        mappings.add(new ColumnMapping(0, "Projection", DarwinCoreField.GRIDREFERENCETYPE));
        mappings.add(new ColumnMapping(1, "TaxonVersionKey", DarwinCoreField.TAXONID));
        mappings.add(new ColumnMapping(2, "SRS", DarwinCoreField.VERBATIMSRS));
        
        if (instance.isStepNeeded(mappings)) {
            instance.modifyHeader(mappings);
            
            List<String> row = new ArrayList<String>();
            row.add("WGS84");
            row.add("");
            row.add("");
            
            instance.modifyRow(row);
            
            assertEquals(row.get(0), "");
            assertEquals(row.get(1), "");
            assertEquals(row.get(2), "4326");
            
            row = new ArrayList<String>();
            row.add("OSGB");
            row.add("");
            row.add("");
            
            instance.modifyRow(row);
            
            assertEquals(row.get(0), "OSGB");
            assertEquals(row.get(1), "");
            assertEquals(row.get(2), "");
            
            row = new ArrayList<String>();
            row.add("");
            row.add("");
            row.add("4326");
            
            instance.modifyRow(row);
            
            assertEquals(row.get(0), "");
            assertEquals(row.get(1), "");
            assertEquals(row.get(2), "4326");
        } else {
            fail("Step should be required");
        }
    }
}
