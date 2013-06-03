package uk.gov.nbn.data.portal.config;

import freemarker.template.Configuration;
import freemarker.template.TemplateExceptionHandler;
import freemarker.template.TemplateModelException;
import java.io.File;
import java.io.FilenameFilter;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.servlet.ServletContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
    @Value("${api}") String api;
    @Value("${gis}") String gis;
    @Autowired ServletContext context;
    
    public PowerlessSpringMVCConfig(FreeMarkerConfigurer configurer) {
        this.configurer = configurer;
    }
    
    @PostConstruct
    public void setup() throws TemplateModelException {      
        Configuration config = configurer.getConfiguration();
        
        importLibraries(config, new File(context.getRealPath(DEFAULT_FREEMARKER_TEMPLATE_LIBRARIES)));
        
        config.setSharedVariable("api", api);
        config.setSharedVariable("gis", gis);
        config.setSharedVariable("markdown", new MarkDownDirectiveModel());
        // TODO: Need to make a decision if we need commas, etc... in numbers as 
        // its all to easy to have an error creep up on us much later on, this 
        // will fix it globally for powerless controllers at least
        //config.setNumberFormat("0.##########");
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
