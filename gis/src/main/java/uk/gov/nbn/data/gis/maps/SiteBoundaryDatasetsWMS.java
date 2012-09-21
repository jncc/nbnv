package uk.gov.nbn.data.gis.maps;

import com.sun.jersey.api.client.GenericType;
import com.sun.jersey.api.client.WebResource;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;
import javax.ws.rs.core.MediaType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import uk.gov.nbn.data.gis.processor.MapObject;
import uk.gov.nbn.data.gis.processor.MapService;
import uk.gov.nbn.data.gis.providers.annotations.MapFile;
import uk.org.nbn.nbnv.api.model.SiteBoundaryDataset;

/**
 * The following map service will make a call to the data api as defined in
 * the gis.properties to retrieve the most upto-date list of site boundary 
 * datasets. This will then be used to create the layers for the map file
 * @author Christopher Johnson
 */
@Component
@MapService("SiteBoundaryDatasets")
public class SiteBoundaryDatasetsWMS {
    @Autowired Properties properties;
    @Autowired WebResource dataApi;
    
    private static final String DATA = "geom from ("
            + "SELECT geom, sbfd.featureID "
            + "FROM SiteBoundaryFeatureData sbfd "
            + "INNER JOIN SiteBoundaryData sbd ON sbd.featureID = sbfd.featureID "
            + "WHERE siteBoundaryDatasetKey = '%s'"
        + ") AS foo USING UNIQUE featureID USING SRID=4326";
    
    @MapObject
    public File getTaxonMap(@MapFile("SiteBoundaryDatasetsWMS.map") String mapFile) throws IOException, TemplateException{
        List<SiteBoundaryDataset> datasets = dataApi
                        .path("siteBoundaryDatasets")
                        .accept(MediaType.APPLICATION_JSON) 
                        .get(new GenericType<List<SiteBoundaryDataset>>() { });
        
        HashMap<String, Object> data = new HashMap<String, Object>();
        data.put("siteBoundaries", datasets);
        Configuration cfg = new Configuration();
        File parentFile = new File(mapFile).getParentFile();
        cfg.setDirectoryForTemplateLoading(new File(mapFile).getParentFile());
        Template template = cfg.getTemplate("SiteBoundaryDatasetsWMS.map");
        // File output
        File file = File.createTempFile("tempMap", ".map", parentFile);
        Writer out = new FileWriter (file);
        
        template.process(data, out);
        out.flush();
        out.close();
        return file;
    }
}
