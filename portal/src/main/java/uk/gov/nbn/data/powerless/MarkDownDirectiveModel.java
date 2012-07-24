/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.gov.nbn.data.powerless;

import freemarker.core.Environment;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateDirectiveModel;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModel;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.Map;
import org.tautua.markdownpapers.Markdown;
import org.tautua.markdownpapers.parser.ParseException;

/**
 *
 * @author Administrator
 */
public class MarkDownDirectiveModel implements TemplateDirectiveModel {
    public void execute(Environment env, Map params, TemplateModel[] loopVars, TemplateDirectiveBody body) throws TemplateException, IOException {
        try {
            Reader reader = getReader(env, params, body);
            try {
                new Markdown().transform(reader, env.getOut());
            }
            finally {
                reader.close();
            }
        }
        catch(ParseException ex) {
            throw new TemplateException("An exception occurred when trying to process template", ex, env);
        }
    }
    
    public static Reader getReader(Environment env, Map params, TemplateDirectiveBody body) throws TemplateException, IOException {
        if(params.containsKey("file")) {
            String filename = params.get("file").toString();
            return FreeMarkerHelper.getReaderForTemplateFile(env, filename, shouldProcess(params, filename));
        }
        else {
            StringWriter writer = new StringWriter();
            body.render(writer);
            return new StringReader(writer.toString());
        }
    }
    
    private static boolean shouldProcess(Map params, String file) {
        return params.containsKey("process") ? (Boolean)params.get("process") : file.endsWith(".ftl");
    }
}
