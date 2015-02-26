package uk.org.nbn.nbnv.api.utils;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import java.io.IOException;
import java.io.Writer;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import uk.org.nbn.nbnv.api.model.Dataset;
import uk.org.nbn.nbnv.api.model.TaxonDataset;

/**
 * The following writer enables writing of eml.xml files to a provided writer.
 * @author cjohn
 */
public class EMLWriter {
    private final Writer writer;
    private final Template template;
    
    public EMLWriter(Writer writer) throws IOException {
        Configuration configuration = new Configuration();
        configuration.setClassForTemplateLoading(EMLWriter.class, "");
        template = configuration.getTemplate("eml.xml.ftl");
        
        this.writer = writer;
    }
    
    public void write(TaxonDataset dataset, Date startDate, Date endDate, boolean isUpsert) throws IOException, TemplateException {
        Map<String, Object> data = new HashMap<>();
        data.put("startDate", startDate);
        data.put("endDate", endDate);
        data.put("isUpsert", isUpsert);
        data.put("dataset", dataset);
        template.process(data, writer);
    }
}
