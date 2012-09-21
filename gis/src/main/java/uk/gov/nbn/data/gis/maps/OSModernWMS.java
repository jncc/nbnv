package uk.gov.nbn.data.gis.maps;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.Properties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import uk.gov.nbn.data.gis.processor.MapObject;
import uk.gov.nbn.data.gis.processor.MapService;
import uk.gov.nbn.data.gis.providers.annotations.MapFile;

/**
 * The following represents a Map service for providing Ordinance Survey Maps
 * @author Christopher Johnson
 */
@Component
@MapService("OS-Modern")
public class OSModernWMS { 
    @Autowired Properties properties;
    
    @MapObject
    public File getTaxonMap(@MapFile("OS-Modern.map") String mapFile) throws IOException, TemplateException {  
        Configuration cfg = new Configuration();
        File parentFile = new File(mapFile).getParentFile();
        cfg.setDirectoryForTemplateLoading(new File(mapFile).getParentFile());
        Template template = cfg.getTemplate("OS-Modern.map");
        // File output
        File file = File.createTempFile("tempMap", ".map", parentFile);
        Writer out = new FileWriter (file);
        template.process(properties, out);
        out.flush();
        out.close();
        return file;
    }
}
