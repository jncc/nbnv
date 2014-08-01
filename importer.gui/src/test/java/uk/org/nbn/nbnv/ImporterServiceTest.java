package uk.org.nbn.nbnv;

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
import uk.org.nbn.nbnv.service.ImporterService;

import java.io.File;
import java.io.IOException;

import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

/**
 * @author Stephen Batty
 *         Date: 30/07/14
 *         Time: 16:45
 */
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration("classpath:/test-context.xml")
public class ImporterServiceTest {

    @Autowired
    protected WebApplicationContext wac;

    @Autowired
    private ImporterService importerService;

    private MockMvc mockMvc;
    private File testFile;


    @Before
    public void setup() throws IOException {
        this.mockMvc = webAppContextSetup(this.wac).build();
//        Resource resource = new ClassPathResource("CapercaillieBroodCountData.txt");
//        Resource resource = new ClassPathResource("importer-data.7z");
        Resource resource = new ClassPathResource("importer-data.zip");

        testFile = resource.getFile();

    }

    @Test
    public void testImportOK() throws Exception {
        Assert.assertTrue("can't read test file", testFile.canRead());
        boolean result = importerService.doImport(testFile);
        Assert.assertTrue("import not ok", result);

    }

    @Test
    public void testImportFail() throws Exception {

        Assert.assertTrue("can't read test file", testFile.canRead());
        boolean result = importerService.doImport(testFile);
        Assert.assertTrue("import not ok", result);

    }
}
