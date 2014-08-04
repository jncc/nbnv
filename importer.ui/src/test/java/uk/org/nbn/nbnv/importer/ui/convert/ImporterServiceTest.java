package uk.org.nbn.nbnv.importer.ui.convert;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import java.io.File;
import java.io.IOException;

import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

/**
 * @author Stephen Batty
 *         Date: 30/07/14
 *         Time: 16:45
 */
public class ImporterServiceTest extends BaseIntegrationTest {

    @Autowired
    private ImporterService importerService;


    private File testValidFile;
    private File testBadFile;



    @Before
    public void setup() throws IOException {
        baseSetup();
        Resource validResource = new ClassPathResource("valid.zip");
        validResource.getFile().getParent();
        Resource badResource = new ClassPathResource("justsomerandomfile.zip");
        testValidFile = validResource.getFile();
        testBadFile = badResource.getFile();

    }

    @Test
    public void testImportOK() throws Exception {
        Assert.assertTrue("can't read test file", testValidFile.canRead());
        boolean result = importerService.doImport(testValidFile);
        Assert.assertTrue("import not ok", result);

    }

    @Test
    public void testImportFail() throws Exception {
        Assert.assertTrue("can't read test file", testBadFile.canRead());
        boolean result = importerService.doImport(testBadFile);
        Assert.assertFalse("import not ok", result);

    }
}
