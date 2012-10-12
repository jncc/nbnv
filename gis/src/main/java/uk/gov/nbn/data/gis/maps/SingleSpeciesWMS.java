package uk.gov.nbn.data.gis.maps;

import java.util.HashMap;
import java.util.List;
import java.util.Properties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import uk.gov.nbn.data.gis.processor.MapFileModel;
import uk.gov.nbn.data.gis.processor.MapService;
import uk.gov.nbn.data.gis.processor.MapContainer;
import uk.gov.nbn.data.gis.processor.GridMap;
import uk.gov.nbn.data.gis.processor.GridMap.Layer;
import uk.gov.nbn.data.gis.processor.GridMap.GridLayer;
import uk.gov.nbn.data.gis.processor.GridMap.Resolution;
import uk.gov.nbn.data.gis.providers.annotations.PathParam;
import uk.gov.nbn.data.gis.providers.annotations.QueryParam;
import uk.gov.nbn.data.gis.providers.annotations.ServiceURL;
import uk.org.nbn.nbnv.api.model.User;

/**
 * The following represents a Map service for SingleSpecies
 * 
 * It is configured to take the following filters :
 *  startyear
 *  endyear
 *  datasets
 *  taxonVersionKey (As part of the url call)
 * @author Christopher Johnson
 */
@Component
@MapContainer("SingleSpecies")
public class SingleSpeciesWMS {
    private static final String QUERY = "geom from ("
            + "SELECT f.geom, o.observationID, f.label "
            + "FROM [dbo].[UserTaxonObservationData] o "
            + "INNER JOIN [dbo].[GridTree] gt ON gt.featureID = o.featureID "
            + "INNER JOIN [dbo].[FeatureData] f ON f.featureID = gt.parentFeatureID "
            + "WHERE pTaxonVersionKey = '%s' "
            + "AND userKey = %s "
            + "AND resolutionID = %d "
            + "%s " //place for dataset filter
            + "%s " //place for start year filter
            + "%s " //place for end year filter
        + ") AS foo USING UNIQUE observationID USING SRID=4326";
    @Autowired Properties properties;
    
    @MapService("{taxonVersionKey}")
    @GridMap(
        layers={
            @GridLayer(name="10km",     layer="Grid-10km",      resolutions=Resolution.TEN_KM),
            @GridLayer(name="2km",      layer="Grid-2km",       resolutions=Resolution.TWO_KM),
            @GridLayer(name="1km",      layer="Grid-1km",       resolutions=Resolution.ONE_KM),
            @GridLayer(name="100m",     layer="Grid-100m",      resolutions=Resolution.ONE_HUNDRED_METERS),
            @GridLayer(name="10km-2km", layer="Grid-10km-2km",  resolutions={Resolution.TEN_KM, Resolution.TWO_KM}),
            @GridLayer(name="10km-1km", layer="Grid-10km-1km",  resolutions={Resolution.TEN_KM, Resolution.TWO_KM, Resolution.ONE_KM}),
            @GridLayer(name="10km-100m",layer="Grid-10km-100m", resolutions={Resolution.TEN_KM, Resolution.TWO_KM, Resolution.ONE_KM, Resolution.ONE_HUNDRED_METERS}),
            @GridLayer(name="2km-1km",  layer="Grid-2km-1km",   resolutions={Resolution.TWO_KM, Resolution.ONE_KM}),
            @GridLayer(name="2km-100m", layer="Grid-2km-100m",  resolutions={Resolution.TWO_KM, Resolution.ONE_KM, Resolution.ONE_HUNDRED_METERS}),
            @GridLayer(name="1km-100m", layer="Grid-1km-100m",  resolutions={Resolution.ONE_KM, Resolution.ONE_HUNDRED_METERS})
        },
        defaultLayer="10km",
        backgrounds=@Layer(name="os", layer="OS-Scale-Dependent" ),
        overlays=@Layer(name="feature", layer="Selected-Feature" )
    )
    public MapFileModel getSingleSpeciesModel(
            final User user,
            @ServiceURL String mapServiceURL,
            @PathParam(key="taxonVersionKey", validation="^[A-Z]{6}[0-9]{10}$") final String key,
            @QueryParam(key="datasets", validation="^[A-Z0-9]{8}$") final List<String> datasetKeys,
            @QueryParam(key="startyear", validation="[0-9]{4}") final String startYear,
            @QueryParam(key="endyear", validation="[0-9]{4}") final String endYear,
            @QueryParam(key="feature", validation="[0-9]*") String featureID
            ) {
        
        
        HashMap<String, Object> data = new HashMap<String, Object>();
        data.put("mapServiceURL", mapServiceURL);
        data.put("featureID", featureID);
        data.put("properties", properties);
        data.put("layerGenerator", new ResolutionDataGenerator() {
                @Override
                public String getData(int resolution) {
                    return String.format(QUERY, key, user.getId(), resolution, 
                        MapHelper.createInDatasetsSegment(datasetKeys),
                        MapHelper.createStartYearSegment(startYear),
                        MapHelper.createEndYearSegment(endYear));
                }
        });
        return new MapFileModel("SingleSpeciesWMS.map",data);
    }
}
