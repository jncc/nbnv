package uk.gov.nbn.data.portal.config;

import freemarker.template.Configuration;
import freemarker.template.TemplateExceptionHandler;
import freemarker.template.TemplateModelException;
import java.io.File;
import java.io.FilenameFilter;
import java.util.Map;
import java.util.Properties;
import javax.annotation.PostConstruct;
import javax.servlet.ServletContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;
import uk.gov.nbn.data.powerless.MarkDownDirectiveModel;

/**
 * The following Spring Bean will configure the FreeMarkerConfigurer with any 
 * Powerless Global properties which it may require.
 * 
 * @see PowerlessHelper for details on this 
 * @author Christopher Johnson
 */
public class PowerlessSpringMVCConfig {
    public static final String DEFAULT_FREEMARKER_TEMPLATE_LIBRARIES = "/WEB-INF/ftl/libraries/";
    
    private FreeMarkerConfigurer configurer;
    @Autowired Properties properties;
    @Autowired ServletContext context;
    
    public PowerlessSpringMVCConfig(FreeMarkerConfigurer configurer) {
        this.configurer = configurer;
    }
    
    @PostConstruct
    public void setup() throws TemplateModelException {      
        Configuration config = configurer.getConfiguration();
        
        importLibraries(config, new File(context.getRealPath(DEFAULT_FREEMARKER_TEMPLATE_LIBRARIES)));
        
        for(Map.Entry currEntry : properties.entrySet()){
            config.setSharedVariable((String)currEntry.getKey(), currEntry.getValue());
        }
        config.setSharedVariable("markdown", new MarkDownDirectiveModel());
        config.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
    }    
    
    private static void importLibraries(Configuration config, File freemarkerLibraries) {
        for(File currFile : freemarkerLibraries.listFiles(new FreeMarkerTemplateLibraryFileFilter())) {
            String libraryName = currFile.getName().substring(0, currFile.getName().lastIndexOf('.'));
            config.addAutoImport(libraryName, "libraries/" + currFile.getName());
        }
    }    
    
    private static class FreeMarkerTemplateLibraryFileFilter implements FilenameFilter{
        @Override public boolean accept(File dir, String name) {
            return !name.startsWith("_") && name.endsWith(".ftl");
        }
    }
}
