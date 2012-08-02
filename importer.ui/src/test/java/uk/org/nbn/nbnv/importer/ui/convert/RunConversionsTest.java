/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.org.nbn.nbnv.importer.ui.convert;

import java.util.ArrayList;
import java.util.List;
import junit.framework.Assert;
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
public class RunConversionsTest {

    public RunConversionsTest() {
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

    @Test
    public void testGetStepsOne() {
        RunConversions rc = new RunConversions();

        List<ColumnMapping> columns = new ArrayList<ColumnMapping>();
        columns.add(new ColumnMapping(0, "mapping", DarwinCoreField.OCCURRENCESTATUS));
        List<ConverterStep> steps = rc.getSteps(columns);

        Assert.assertEquals(1, steps.size());
    }

    @Test
    public void testGetStepsMany() {
        RunConversions rc = new RunConversions();

        List<ColumnMapping> columns = new ArrayList<ColumnMapping>();
        columns.add(new ColumnMapping(0, "mapping", DarwinCoreField.OCCURRENCESTATUS));
        columns.add(new ColumnMapping(1, "mapping", DarwinCoreField.SENSITIVEOCCURRENCE));
        columns.add(new ColumnMapping(2, "mapping", DarwinCoreField.ATTRIBUTE));
        List<ConverterStep> steps = rc.getSteps(columns);

        Assert.assertEquals(3, steps.size());
    }

    @Test
    public void testModifyColumns() {
        RunConversions rc = new RunConversions();

        List<ColumnMapping> columns = new ArrayList<ColumnMapping>();
        columns.add(new ColumnMapping(0, "mapping", DarwinCoreField.OCCURRENCESTATUS));
        columns.add(new ColumnMapping(1, "mapping", DarwinCoreField.SENSITIVEOCCURRENCE));
        columns.add(new ColumnMapping(2, "mapping", DarwinCoreField.ATTRIBUTE));
        List<ConverterStep> steps = rc.getSteps(columns);

        rc.modifyColumns(steps, columns);
        
        Assert.assertEquals(4, columns.size());
        Assert.assertEquals(3, columns.get(3).getColumnNumber());
    }
    
    @Test
    public void testModifyRowOccurrence() throws BadDataException {
        RunConversions rc = new RunConversions();
        List<ColumnMapping> columns = new ArrayList<ColumnMapping>();
        columns.add(new ColumnMapping(0, "mapping", DarwinCoreField.OCCURRENCESTATUS));
        List<String> row = new ArrayList<String>();
        row.add("T");
        
        List<ConverterStep> steps = rc.getSteps(columns);
        rc.modifyColumns(steps, columns);
        rc.modifyRow(steps, row);
        
        Assert.assertEquals("absent", row.get(0));
    }
}
