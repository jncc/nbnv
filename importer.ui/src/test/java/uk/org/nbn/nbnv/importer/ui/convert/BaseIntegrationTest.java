package uk.org.nbn.nbnv.importer.ui.convert;

import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

/**
 * @author Stephen Batty
 *         Date: 04/08/14
 *         Time: 15:14
 */
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration("classpath:/test-context.xml")
public abstract class BaseIntegrationTest  {

    @Autowired
    protected WebApplicationContext wac;

    protected MockMvc mockMvc;

    /** must be called to set up webcontext before each test*/
    protected void baseSetup(){
        this.mockMvc = webAppContextSetup(this.wac).build();
    }

}
