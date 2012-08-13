package uk.gov.nbn.data.powerless.request;

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
 * Will be readable as a sequence in freemarker. If only one value is specified
 * then that value can be accessed as a scalar or as an array with a single 
 * element.
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
        if(parameterValues == null) 
            return new StringArraySequence(new String[0]); //return an empty array so that ?seq_contains can always be called   
        else if(parameterValues.length==1)
            return new TraditionalHttpRequestParameterModel(parameterValues[0]); //case that a single param exists
        else
            return new StringArraySequence(parameterValues); //multiple params for this key return as array       
    }
    
    /**
     * The following class will wrap up a String and present it as a Scalar and
     * a single element array
     */
    private static class TraditionalHttpRequestParameterModel extends 
        StringArraySequence implements TemplateScalarModel {
        
        private final String parameter;
        TraditionalHttpRequestParameterModel(String value) {
            super(new String[]{value});
            parameter = value;
        }

        @Override
        public String getAsString() throws TemplateModelException {
            return parameter;
        }
    }
}
