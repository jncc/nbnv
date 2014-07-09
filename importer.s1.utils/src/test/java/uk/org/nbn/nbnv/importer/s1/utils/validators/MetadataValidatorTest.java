/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.org.nbn.nbnv.importer.s1.utils.validators;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import uk.org.nbn.nbnv.importer.s1.utils.model.Metadata;
import uk.org.nbn.nbnv.jpa.nbncore.Organisation;

/**
 *
 * @author Matt Debont
 */
public class MetadataValidatorTest {
    
    public MetadataValidatorTest() {
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
     * Test of supports method, of class MetadataValidator.
     */
    @Test
    public void testSupports() {
        Class<?> type = Organisation.class;
        MetadataValidator instance = new MetadataValidator();
        boolean expResult = false;
        boolean result = instance.supports(type);
        assertEquals(expResult, result);
        
        type = Metadata.class;
        expResult = true;
        result = instance.supports(type);
        assertEquals(expResult, result);   
    }

    /**
     * Test of validate method, of class MetadataValidator.
     */
    @Test
    public void testValidate() {

    }
}
