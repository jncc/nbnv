package uk.gov.nbn.data.gis.maps.context;

import com.sun.jersey.api.client.GenericType;
import com.sun.jersey.api.client.WebResource;
import java.util.List;
import org.jooq.util.sqlserver.SQLServerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import static uk.gov.nbn.data.dao.jooq.Tables.*;
import uk.gov.nbn.data.gis.maps.MapHelper;
import uk.org.nbn.nbnv.api.model.ContextLayer;

/**
 *
 * @author Administrator
 */
@Component
public class ContextLayerDataGenerator {
    @Autowired WebResource resource;
    
    public String getContextLayerData(ContextLayer layer) {
        SQLServerFactory create = new SQLServerFactory();
        
        return MapHelper.getMapData("geom", "id", create
                .select(CONTEXTLAYERFEATUREDATA.ID, CONTEXTLAYERFEATUREDATA.GEOM)
                .from(CONTEXTLAYERFEATUREDATA)
                .where(CONTEXTLAYERFEATUREDATA.CONTEXTLAYERID.eq(layer.getId()))
            , layer.getSrid());
    }
    
    public List<ContextLayer> getContextLayers() {
        GenericType<List<ContextLayer>> type = new GenericType<List<ContextLayer>>(){};
        return resource
            .path("contextLayers")
            .get(type);
    }
}
