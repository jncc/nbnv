package uk.org.nbn.nbnv;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration("classpath:/test-context.xml")
public class MetadataControllerTest {
    private MockMvc mockMvc;

    @SuppressWarnings("SpringJavaAutowiringInspection")
    @Autowired
    protected WebApplicationContext wac;

    @Before
    public void setup() {
        this.mockMvc = webAppContextSetup(this.wac).build();
    }

    @Test
    public void testUpload() throws Exception {
        // just checking spring is set up ok
        mockMvc.perform(
                    post("/hello")
                    .accept(MediaType.APPLICATION_JSON)
                    .content(MediaType.APPLICATION_JSON_VALUE)
                    .content()
                )
                .andExpect(status().isOk())
                .andExpect(view().name("hello"));
    }
    /** there must be an easier way than this*/
    private String getMetadataJson(){
        return  "{\"title\":\"mytitle\"" +
                ",\"organisation\":\"myOrganisation\"" +
                ",\"description\":\"myDesc\"" +
                ",\"methodsOfDataCapture\":\"myMethods of data capture\"" +
                ",\"purposeOfDataCapture\":\"my puprose of data caprutre\"" +
                ",\"geographicalCoverage\":\"my geo coverage\"" +
                ",\"temporalCoverage\":\"my temp caoveral\"" +
                ",\"dataQuality\":\"my data quality\"" +
                ",\"additionalInfo\":\"my addtional info\"" +
                ",\"useConstraints\":\"my use constraints\"" +
                ",\"accessConstraints\":\"my access cosntraints \"" +
                ",\"geographicResolution\":\" my geo resolution\"" +
                ",\"adminDetails\":" +
                    "{\"name\":\"my admin name\"" +
                    ",\"phone\":\"my phone \"" +
                    ",\"email\":\"my tel 12345\"}" +
                ",\"access\":{}\"" +
                ",\"resolution\":\"\"" +
                ",\"recordAttributes\":\"\"" +
                ",\"recorderNames\":\"\"" +
                ",\"insertionType\":\"\"}";
    }
}
