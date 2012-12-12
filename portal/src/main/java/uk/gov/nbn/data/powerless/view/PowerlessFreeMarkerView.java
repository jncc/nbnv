package uk.gov.nbn.data.powerless.view;

import freemarker.template.TemplateException;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.view.freemarker.FreeMarkerView;
import uk.gov.nbn.data.powerless.json.JSONReaderStatusException;

/**
 * The following is an extension of the freemarkerview. It was created specifically
 * to handle authentication status codes from the json reader
 * 
 * TODO : review this!!!!
 * @author Christopher Johnson
 */
public class PowerlessFreeMarkerView extends FreeMarkerView {
    @Override
    protected void doRender(Map<String, Object> model, HttpServletRequest request, HttpServletResponse response) 
            throws Exception {
        try {
            super.doRender(model, request, response);
        }
        catch(TemplateException te) {
            handleThrowable(request, response, te);
        }
    }
    
    private void handleThrowable(HttpServletRequest request, HttpServletResponse response, TemplateException ex) throws TemplateException, IOException {
        if(ex.getCauseException() instanceof JSONReaderStatusException) {
            JSONReaderStatusException jsonException = (JSONReaderStatusException)ex.getCauseException();
            if(jsonException.getStatusCode() == 401) {
                response.sendRedirect("/User/SSO/Unauthorized?redirect=" + URLEncoder.encode(request.getRequestURL().toString()));
            }
        }
        else {
            throw ex; //rethrow the original exception
        }
    }
}
