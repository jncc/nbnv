package uk.gov.nbn.data.gis.maps;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.HashMap;
import java.util.List;
import org.springframework.stereotype.Component;
import uk.gov.nbn.data.gis.processor.MapObject;
import uk.gov.nbn.data.gis.processor.MapService;
import uk.gov.nbn.data.gis.providers.annotations.MapFile;
import uk.gov.nbn.data.gis.providers.annotations.PathParam;
import uk.gov.nbn.data.gis.providers.annotations.QueryParam;
import uk.org.nbn.nbnv.api.model.User;

/**
 * The following represents a Map service for DesignationSpeciesDensity
 * 
 * It is configured to take the following filters :
 *  startyear
 *  endyear
 *  datasets
 *  designationKey (As part of the url call)
 * @author Christopher Johnson
 */
@Component
@MapService("DesignationSpeciesDensity")
public class DesignationSpeciesDensityWMS {
    private static final String QUERY = "geom from ("
            + "SELECT geom, species, label "
            + "FROM ("
                + "SELECT o.userKey, td.code, gt.parentFeatureID AS featureID, "
                    + "COUNT(DISTINCT o.pTaxonVersionKey) AS species "
                + "FROM [dbo].[UserTaxonObservationData] o " 
                + "INNER JOIN [dbo].[GridTree] gt ON gt.featureID = o.featureID "
                + "INNER JOIN [dbo].[DesignationTaxonData] td ON td.pTaxonVersionKey = o.pTaxonVersionKey "
                + "WHERE code = '%s' "
                + "AND userKey = %s "
                + "%s " //placeholder for dataset filter
                + "%s " //startyear for dataset filter
                + "%s " //endyear for dataset filter
                + "GROUP BY gt.parentFeatureID, td.code, o.userKey"
            + ") a "
            + "INNER JOIN [dbo].[FeatureData] f ON f.featureID = a.featureID "
            + "WHERE resolutionID = %d "
        + ") AS foo USING UNIQUE label USING SRID=4326";
    
 
    @MapObject("{designationKey}")
    public File getTaxonMap(
            @MapFile("DesignationSpeciesDensityWMS.map") String mapFile,
            final User user,
            @QueryParam(key="datasets", validation="^[A-Z0-9]{8}$") final List<String> datasetKeys,
            @QueryParam(key="startyear", validation="[0-9]{4}") final String startYear,
            @QueryParam(key="endyear", validation="[0-9]{4}") final String endYear,
            @PathParam(key="designationKey", validation="^[A-Z0-9.()/_\\-]+$") final String key) throws IOException, TemplateException {
        
        HashMap<String, Object> data = new HashMap<String, Object>();
        data.put("layerGenerator", new ResolutionDataGenerator() {
                @Override
                public String getData(int resolution) {
                    return String.format(QUERY, key, user.getId(), 
                        MapHelper.createInDatasetsSegment(datasetKeys),
                        MapHelper.createStartYearSegment(startYear),
                        MapHelper.createEndYearSegment(endYear),
                        resolution);
                }
        });
        Configuration cfg = new Configuration();
        File parentFile = new File(mapFile).getParentFile();
        cfg.setDirectoryForTemplateLoading(new File(mapFile).getParentFile());
        Template template = cfg.getTemplate("DesignationSpeciesDensityWMS.map");
        // File output
        File file = File.createTempFile("tempMap", ".map", parentFile);
        Writer out = new FileWriter (file);
        
        template.process(data, out);
        out.flush();
        out.close();
        return file;
    }
}
