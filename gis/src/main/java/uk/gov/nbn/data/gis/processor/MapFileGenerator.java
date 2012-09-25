package uk.gov.nbn.data.gis.processor;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.lang.reflect.InvocationTargetException;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * The following bean is responsible for generating Map files which can then
 * be used to generate maps in MapServer
 * @author Christopher Johnson
 */
@Component
public class MapFileGenerator {
    @Autowired MapServiceMethodFactory mapServiceMethodFactory;
    private Configuration config;
    private File templateDirectory;
    
    public MapFileGenerator() {
        config = new Configuration();
    }
    
    /**
     * Adapt the template folder setting method of the Freemarker configuration
     * @param mapTemplates Folder for the map templates
     * @throws IOException If an issues occurs in setting the template folder
     */
    public void setMapTemplateDirectory(File templateDirectory) throws IOException {
        this.templateDirectory = templateDirectory;
        config.setDirectoryForTemplateLoading(templateDirectory);
    }
    
    public File getMapFile(HttpServletRequest request, MapServiceMethod method) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, ProviderException, IOException, TemplateException {
        return getMapFile(method.createMapModel(request));
    }
    
    public File getMapFile(MapFileModel createMapModel) throws IOException, TemplateException {
        Template template = config.getTemplate(createMapModel.getMapFile());
        // File output
        File file = File.createTempFile("generated", ".map", templateDirectory);
        Writer out = new FileWriter (file);
        
        template.process(createMapModel.getModel(), out);
        out.flush();
        out.close();
        return file;
    }
}
