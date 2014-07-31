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
public class OrganisationValidatorTest {
    
    public OrganisationValidatorTest() {
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
    public void testSupports() {
        Class<?> type = Metadata.class;
        OrganisationValidator instance = new OrganisationValidator();
        boolean expResult = false;
        boolean result = instance.supports(type);
        assertEquals(expResult, result);
        
        type = Organisation.class;
        expResult = true;
        result = instance.supports(type);
        assertEquals(expResult, result);       
    }
    
    @Test
    public void testValidate() {
//        Organisation o = new Organisation();
//        
//        o.setAbbreviation("");
//        o.setAddress("");
//        o.setAllowPublicRegistration(true);
//        o.setContactEmail("");
//        o.setLogo(null);
//        o.setLogoSmall(null);
//        o.setId(1);
//        o.setName("");
//        o.setPhone("");
//        o.setPostcode("");
//        o.setSummary("");
//        o.setWebsite("");
//        
//        Errors errors = new BeanPropertyBindingResult(o, "o");
//        OrganisationValidator instance = new OrganisationValidator();
//        instance.validate(o, errors);
//            
//        assertTrue(errors.hasErrors());
//        
//        // Organisation Name is required
//        assertNotNull(errors.getFieldError("organisationName"));
//        assertEquals(errors.getFieldError("organisationName").getCode(), "organisationName.required");
//        
//        // Contact Name is required
//        assertNotNull(errors.getFieldError("contactName"));
//        assertEquals(errors.getFieldError("contactName").getCode(), "contactName.required");
//        
//        // Contact Email is required
//        assertNotNull(errors.getFieldError("contactEmail"));
//        assertEquals(errors.getFieldError("contactEmail").getCode(), "contactEmail.required");
//        
//        // Contact Phone is required
//        assertNotNull(errors.getFieldError("phone"));
//        assertEquals(errors.getFieldError("phone").getCode(), "phone.required");
//        
//        // TODO: Left intentionally out due to confusion with address requirements
//        // Address is required
//        //assertNotNull(errors.getFieldError("address"));
//        //assertEquals(errors.getFieldError("address").getCode(), "address.required");
//        
//        // Postcode is required
//        //assertNotNull(errors.getFieldError("postcode"));
//        //assertEquals(errors.getFieldError("postcode").getCode(), "postcode.required");
//        
//        // Summary is required
//        assertNotNull(errors.getFieldError("summary"));
//        assertEquals(errors.getFieldError("summary").getCode(), "summary.required");
//        
//        o.setName("Very Unique Society of Testing Initiatives");
//        o.setContactName("I. A.M. Real");
//        o.setContactEmail("test@test.co.uk");
//        o.setPhone("04442 214445");
//        o.setAddress("123 Fake St, Fakeington");
//        o.setPostcode("TE1 1ST");
//        o.setSummary("This is a real summary of an important and most importantly real organisation");
//        
//        // Organisation should not already exist TODO: causes build failure on the build server
//        o.setName("Conchological Society of Great Britain & Ireland");
//        errors = new BeanPropertyBindingResult(o, "o");
//        instance.validate(o, errors);
//        
//        assertTrue(errors.hasErrors());
//        
//        assertNotNull(errors.getFieldError("organisationName"));
//        assertEquals(errors.getFieldError("organisationName").getCode(), "organisationName.exists");
//        
//        o.setName("Very Unique Society of Testing Initiatives");
//        
//        // Email should be of a correct format
//        o.setContactEmail("incorrectemail");
//        errors = new BeanPropertyBindingResult(o, "o");
//        instance.validate(o, errors);
//        
//        assertTrue(errors.hasErrors());
//        
//        assertNotNull(errors.getFieldError("contactEmail"));
//        assertEquals(errors.getFieldError("contactEmail").getCode(), "contactEmail.invalid");
//        
//        o.setContactEmail("test@test.co.uk");
//        
//        // Phone number should be of a correct format
//        o.setPhone("This is not a phone number?");
//        errors = new BeanPropertyBindingResult(o, "o");
//        instance.validate(o, errors);
//        
//        assertTrue(errors.hasErrors());
//        
//        assertNotNull(errors.getFieldError("phone"));
//        assertEquals(errors.getFieldError("phone").getCode(), "phone.invalid");
//        
//        o.setPhone("04442 214445");
//        errors = new BeanPropertyBindingResult(o, "o");
//        instance.validate(o, errors);
//        
//        assertTrue(!errors.hasErrors());
//        
//        o.setPhone("+44 4442 214445");
//        errors = new BeanPropertyBindingResult(o, "o");
//        instance.validate(o, errors);
//        
//        assertTrue(!errors.hasErrors());
//        
//        o.setPhone("0044 4442 214445");
//        errors = new BeanPropertyBindingResult(o, "o");
//        instance.validate(o, errors);
//        
//        assertTrue(!errors.hasErrors());
//        
//        o.setPhone("+44 4442 21 x4445");
//        errors = new BeanPropertyBindingResult(o, "o");
//        instance.validate(o, errors);
//        
//        assertTrue(!errors.hasErrors());
//        
//        errors = new BeanPropertyBindingResult(o, "o");
//        instance.validate(o, errors);
//        
//        assertTrue(!errors.hasErrors());
        
        // Postcode Validator
        // TODO:
        
        // Valid Organisation Valid?
        
        
        
//        for (FieldError error : errors.getFieldErrors()) {
//            System.out.print(error.getField() + " = ");
//            for (String code : error.getCodes()) {
//                System.out.print(code + " ");
//            }
//            System.out.println();
//        }        
        
    }
}
