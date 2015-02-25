package uk.org.nbn.nbnv.api.utils;

import freemarker.template.Configuration;
import freemarker.template.TemplateException;
import java.io.IOException;
import java.io.Writer;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import uk.org.nbn.nbnv.api.model.Dataset;

/**
 *
 * @author cjohn
 */
public class EMLWriter {
    private final Dataset dataset;
    private final Date startDate, endDate;

    private final Configuration configuration;
    
    public EMLWriter(Dataset dataset, Date startDate, Date endDate) {
        this.dataset = dataset;
        this.startDate = startDate;
        this.endDate = endDate;
        this.configuration = new Configuration();
        configuration.setClassForTemplateLoading(EMLWriter.class, "");
    }
    
    public void write(Writer writer) throws IOException, TemplateException {
        Map<String, Object> data = new HashMap<>();
        data.put("startDate", startDate);
        data.put("endDate", endDate);
        data.put("dataset", dataset);
        configuration.getTemplate("eml.xml.ftl").process(data, writer);
    }
}
