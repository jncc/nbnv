/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.gov.nbn.data.powerless.mvc;

import freemarker.template.TemplateModelException;
import java.io.File;
import java.util.Properties;
import javax.annotation.PostConstruct;
import javax.servlet.ServletContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;
import uk.gov.nbn.data.powerless.PowerlessHelper;

/**
 * The following Spring Bean will configure the FreeMarkerConfigurer with any 
 * Powerless Global properties which it may require.
 * 
 * @see PowerlessHelper for details on this 
 * @author Christopher Johnson
 */
@Component
public class PowerlessSpringMVCSetup {
    @Autowired FreeMarkerConfigurer configurer;
    @Autowired Properties properties;
    @Autowired ServletContext context;
    
    @PostConstruct
    public void setup() throws TemplateModelException {        
        PowerlessHelper.setUpConfiguration(
                    configurer.getConfiguration(), 
                    properties, 
                    new File(context.getRealPath(PowerlessHelper.DEFAULT_FREEMARKER_TEMPLATE_LIBRARIES)), 
                    "libraries");
    }
}
