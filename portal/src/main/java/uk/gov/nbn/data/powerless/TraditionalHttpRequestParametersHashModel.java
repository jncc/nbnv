package uk.gov.nbn.data.powerless;

import freemarker.core.StringArraySequence;
import freemarker.ext.servlet.HttpRequestParametersHashModel;
import freemarker.template.TemplateModel;
import freemarker.template.TemplateModelException;
import freemarker.template.TemplateScalarModel;
import javax.servlet.http.HttpServletRequest;

/**
 * The following class extends the functionality of the 
 * HttpRequestParametersHashModel to enable reading of parameters which may have
 * multiple values. A query string such as :
 * <code>
 *  ?filter=firstFilter&filter=secondFilter
 * </code>
 * 
 * Will be readable as a sequence in freemarker. The first value will also be
 * accessible as a freemarker scalar.
 * @author Christopher Johnson
 */
public class TraditionalHttpRequestParametersHashModel  extends HttpRequestParametersHashModel {
    private final HttpServletRequest request;
    public TraditionalHttpRequestParametersHashModel(HttpServletRequest request) {
        super(request);
        this.request = request;
    }

    @Override
    public TemplateModel get(String key) {
        String[] parameterValues = request.getParameterValues(key);
        if(parameterValues != null) {
            if(parameterValues.length==1)
                return new TraditionalHttpRequestParameterModel(parameterValues);
            else
                return new StringArraySequence(parameterValues);
        }
        return TemplateModel.NOTHING;            
    }
    
    private static class TraditionalHttpRequestParameterModel extends 
        StringArraySequence implements TemplateScalarModel {
        
        private final String parameter;
        TraditionalHttpRequestParameterModel(String[] stringArr) {
            super(stringArr);
            parameter = stringArr[0];
        }

        @Override
        public String getAsString() throws TemplateModelException {
            return parameter;
        }
    }
}
