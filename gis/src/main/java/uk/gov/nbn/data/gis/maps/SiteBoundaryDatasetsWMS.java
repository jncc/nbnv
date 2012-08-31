package uk.gov.nbn.data.gis.maps;

import com.sun.jersey.api.client.GenericType;
import com.sun.jersey.api.client.WebResource;
import edu.umn.gis.mapscript.*;
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
    public mapObj getTaxonMap(@MapFile("SiteBoundaryDatasetsWMS.map") String mapFile){
        mapObj toReturn = new mapObj(mapFile);
        for(SiteBoundaryDataset currSiteBoundaryDataset : dataApi
                        .path("siteBoundaryDatasets")
                        .accept(MediaType.APPLICATION_JSON)
                        .get(new GenericType<List<SiteBoundaryDataset>>() { })) {
            createLayer(currSiteBoundaryDataset.getDatasetKey(), toReturn);
        }
        return toReturn;
    }
    
    private void createLayer(String datasetKey, mapObj toAddTo) {
        layerObj toReturn = new layerObj(toAddTo);
        toReturn.setName(datasetKey);
        toReturn.setType(MS_LAYER_TYPE.MS_LAYER_POLYGON);
        toReturn.setStatus(mapscriptConstants.MS_OFF);
        toReturn.setPlugin_library("msplugin_mssql2008.dll");
        toReturn.setConnectiontype(MS_CONNECTION_TYPE.MS_PLUGIN);
        toReturn.setConnection(properties.getProperty("spatialConnection"));
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
