package uk.org.nbn.nbnv;

import org.junit.Assert;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultMatcher;

/**
 * @author stephen batty
 *         Date: 14/07/14
 *         Time: 16:27
 */
public class DefaultResultMatcher<T extends JsonBean> implements ResultMatcher {
        private final Class<T> clazz;
    public DefaultResultMatcher(Class<T> clazz){
        this.clazz = clazz;

    }
    @Override
    public void match(MvcResult result) throws Exception {
        HttpStatus actualStatus = HttpStatus.valueOf(result.getResponse().getStatus());
        Assert.assertTrue("Status is not 200", actualStatus.is2xxSuccessful());
        String json = result.getResponse().getContentAsString();
        JsonBean<T> jsonBean = clazz.newInstance();
        T actualResult = jsonBean.fromJson(json);
        T expectedResult = jsonBean.SampleTestData();
        Assert.assertEquals("Serialized json is not as expected", expectedResult, actualResult);

    }
}
