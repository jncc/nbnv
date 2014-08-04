package uk.org.nbn.nbnv.importer.ui.convert;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import uk.org.nbn.nbnv.importer.ui.email.EmailService;

import java.io.File;
import java.io.IOException;

/**
 * @author Stephen Batty
 *         Date: 04/08/14
 *         Time: 15:14
 */
public class EmailServiceTest extends BaseIntegrationTest{

    @Autowired
    private EmailService emailService;

    private static final String sendTo = "stephen.batty@jncc.gov.uk";
    private File testFileAttachment;

    @Before
    public void setup() throws IOException {
        baseSetup();
        Resource resource = new ClassPathResource("justsomerandomfile.zip");
        testFileAttachment = resource.getFile();
    }

    @Test
    public void testSendLog() throws Exception {
        emailService.sendLog(sendTo, testFileAttachment);
    }
}
