/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.gov.nbn.data.powerless;

import freemarker.template.Configuration;
import freemarker.template.TemplateExceptionHandler;
import freemarker.template.TemplateModelException;
import java.io.File;
import java.io.FilenameFilter;
import java.util.Map;
import java.util.Properties;

/**
 * The following helper class sets up the Freemarker configuration in order to
 * be powerless
 * @author Christopher Johnson
 */
public class PowerlessHelper {
    public static final String DEFAULT_FREEMARKER_TEMPLATE_LIBRARIES = "/WEB-INF/ftl/libraries/";
    
    public static void setUpConfiguration(  Configuration config, 
                                            Properties properties, 
                                            File freemarkerLibraries, 
                                            String templatePathToLibraries) throws TemplateModelException {
        importLibraries(config, freemarkerLibraries,templatePathToLibraries);
        for(Map.Entry currEntry : properties.entrySet()){
            config.setSharedVariable((String)currEntry.getKey(), currEntry.getValue());
        }
        config.setSharedVariable("markdown", new MarkDownDirectiveModel());
        config.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
    }
    
    
    private static void importLibraries(Configuration config, File freemarkerLibraries, String templatePathToLibraries) {
        for(File currFile : freemarkerLibraries.listFiles(new FreeMarkerTemplateLibraryFileFilter())) {
            String libraryName = currFile.getName().substring(0, currFile.getName().lastIndexOf('.'));
            config.addAutoImport(libraryName, templatePathToLibraries + "/" + currFile.getName());
        }
    }    
    
    private static class FreeMarkerTemplateLibraryFileFilter implements FilenameFilter{
        @Override public boolean accept(File dir, String name) {
            return !name.startsWith("_") && name.endsWith(".ftl");
        }
    }
}
