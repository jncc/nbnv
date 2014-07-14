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
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration("classpath:/test-context.xml")
public class MetadataControllerTest {
    //    @SuppressWarnings("SpringJavaAutowiringInspection")
    @Autowired
    protected WebApplicationContext wac;
    private MockMvc mockMvc;

    @Before
    public void setup() {
        this.mockMvc = webAppContextSetup(this.wac).build();
    }

    @Test
    public void testUpload() throws Exception {
        String testJson = new MetadataForm().SampleTestData().toJson();
    //        Logger.info("test json is :" + testJson);

        mockMvc.perform(
                post(Url.uploadMetadata)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(testJson))
//                .andDo(print())
                .andExpect(status().isOk());

    }
    @Test
    public void testDownloadSample() throws Exception {
        mockMvc.perform(
                get(Url.downloadMetadataSample).accept(MediaType.APPLICATION_JSON))
                .andExpect(new DefaultResultMatcher(MetadataForm.class));
    }

    @Test
    public void testBasic() throws Exception {
        String testJson = "false";
        // just checking spring is set up ok
        mockMvc.perform(
                post(Url.basic)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(testJson))
                .andDo(print())
                .andExpect(status().isOk());

    }
}