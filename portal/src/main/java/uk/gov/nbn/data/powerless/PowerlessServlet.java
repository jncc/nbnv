/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.gov.nbn.data.powerless;

import freemarker.ext.servlet.FreemarkerServlet;
import freemarker.template.Configuration;
import freemarker.template.TemplateExceptionHandler;
import freemarker.template.TemplateModelException;
import java.io.IOException;
import java.util.Map;
import javax.servlet.ServletException;
import uk.gov.nbn.data.powerless.json.JSONReaderForFreeMarker;

/**
 *
 * @author Administrator
 */
public class PowerlessServlet extends FreemarkerServlet{
    private static final String FREEMARKER_GLOBAL_VARIABLES = "powerless-global.properties";
    
    @Override public void init() throws ServletException {
        super.init();
        try {
            Configuration config = getConfiguration();
            for(Map.Entry currEntry : PropertiesReader.getEffectiveProperties(FREEMARKER_GLOBAL_VARIABLES).entrySet()){
                config.setSharedVariable((String)currEntry.getKey(), currEntry.getValue());
            }
            config.setSharedVariable("markdown", new MarkDownDirectiveModel());
            config.setSharedVariable("json", new JSONReaderForFreeMarker());
            config.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
        } catch (TemplateModelException ex) {
            throw new ServletException(ex);
        } catch (IOException io) {
            throw new ServletException(io);
        }
    }
}
