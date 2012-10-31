package uk.org.nbn.nbnv.api.dao.warehouse;

import java.util.List;
import org.apache.ibatis.annotations.Select;
import uk.org.nbn.nbnv.api.model.ContextLayer;


public interface ContextLayerMapper {
    @Select(
        "SELECT contextLayerID as id, contextLayer as name, geom.STSrid as srid "
        + "FROM ContextLayerFeatureData GROUP BY contextLayerID, contextLayer, geom.STSrid")
    List<ContextLayer> getAllContextLayers();
}
