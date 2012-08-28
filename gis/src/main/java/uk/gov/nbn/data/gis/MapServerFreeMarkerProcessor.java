/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package uk.gov.nbn.data.gis;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 *
 * @author Christopher Johnson
 */
public class MapServerFreeMarkerProcessor {
    private static Configuration cfg; 
    
    static {
        cfg= new Configuration();
    }
    
    public static File executeTemplateToTempFile(String templateName) throws IOException, TemplateException {
        File toReturn = File.createTempFile("ms", "ftlOut");
        FileWriter out = new FileWriter(toReturn);
        try {
            Template template = cfg.getTemplate(templateName);
            template.process(null, out);
            return toReturn;
        }
        finally{
            out.flush();
            out.close();
        }
    }
}
