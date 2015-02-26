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
        configuration.setDateFormat("yyyy-MM-dd");
        template = configuration.getTemplate("eml.xml.ftl");
        
        this.writer = writer;
    }
    
    public void write(TaxonDataset dataset, Date startDate, Date endDate, boolean isUpsert) throws IOException, TemplateException {
        Map<String, Object> data = new HashMap<>();
        data.put("startDate", startDate);
        data.put("endDate", endDate);
        data.put("isUpsert", isUpsert);
        data.put("dataset", dataset);
        data.put("publicPrecision", getPrecision(dataset.getPublicResolution()));
        template.process(data, writer);
    }
    
    protected String getPrecision(String resolution) {
        switch(resolution) {
            case "10km": return "10000";
            case "2km":  return "2000";
            case "1km":  return "1000";
            case "100m": return "100";
            default:     return "null";
        }
    }
}
