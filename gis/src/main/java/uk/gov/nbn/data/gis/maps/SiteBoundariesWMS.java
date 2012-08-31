package uk.gov.nbn.data.gis.maps;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.GenericType;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.api.json.JSONConfiguration;
import edu.umn.gis.mapscript.*;
import java.util.List;
import javax.ws.rs.core.MediaType;
import uk.gov.nbn.data.gis.processor.MapObject;
import uk.gov.nbn.data.gis.processor.MapService;
import uk.gov.nbn.data.gis.providers.annotations.MapFile;
import uk.org.nbn.nbnv.api.model.SiteBoundary;
import uk.org.nbn.nbnv.api.model.SiteBoundaryDataset;

/**
 *
 * @author Christopher Johnson
 */
@MapService("SiteBoundaries")
public class SiteBoundariesWMS {

    private static final String DATA = "geom from ("
            + "SELECT geom, sbfd.featureID "
            + "FROM SiteBoundaryFeatureData sbfd "
            + "INNER JOIN SiteBoundaryData sbd ON sbd.featureID = sbfd.featureID "
            + "WHERE siteBoundaryDatasetKey = '%s'"
        + ") AS foo USING UNIQUE featureID USING SRID=4326";
    
    @MapObject
    public mapObj getTaxonMap(@MapFile("SiteBoundariesWMS.map") String mapFile){
        mapObj toReturn = new mapObj(mapFile);
        DefaultClientConfig config = new DefaultClientConfig();
        config.getFeatures()
            .put(JSONConfiguration.FEATURE_POJO_MAPPING, Boolean.TRUE);
        Client client = Client.create(config);
        WebResource resource = client.resource("http://staging.testnbn.net/api/");
        GenericType<List<SiteBoundaryDataset>> gt = new GenericType<List<SiteBoundaryDataset>>() { };
        List<SiteBoundaryDataset> siteBoundaryDatasets = resource
                .path("siteBoundaryDatasets")
                .accept(MediaType.APPLICATION_JSON)
                .get(gt);
        createLayers(siteBoundaryDatasets, toReturn);  
        return toReturn;
    }
    
    private void createLayers(List<SiteBoundaryDataset> siteBoundaryDatasets, mapObj toAddTo) {
        for(SiteBoundaryDataset currSiteBoundaryDataset : siteBoundaryDatasets) {
            createLayer(currSiteBoundaryDataset.getDatasetKey(), toAddTo);
        }
    }
    
    private void createLayer(String datasetKey, mapObj toAddTo) {
        layerObj toReturn = new layerObj(toAddTo);
        toReturn.setName(datasetKey);
        toReturn.setType(MS_LAYER_TYPE.MS_LAYER_POLYGON);
        toReturn.setStatus(mapscriptConstants.MS_OFF);
        toReturn.setPlugin_library("msplugin_mssql2008.dll");
        toReturn.setConnectiontype(MS_CONNECTION_TYPE.MS_PLUGIN);
        toReturn.setConnection("Server=NBNSQL-B;Database=NBNWarehouse;uid=NBNImporter;pwd=Ecowaswashere9;Integrated Security=True");
        toReturn.setData(String.format(DATA, datasetKey));
        toReturn.setProjection("init=epsg:4326");
        addDefaultClass(toReturn);
    }
    
    private static void addDefaultClass(layerObj toAddClassTo) {
        classObj stylingClass = new classObj(toAddClassTo);
        stylingClass.setName("default");
        styleObj stylingObj = new styleObj(stylingClass);
        stylingObj.setColor(new colorObj(255,255,0, 1));
        stylingObj.setOutlinecolor(new colorObj(0,0,0, 1));
        stylingObj.setWidth(0.5);
    } 
}
