package uk.org.nbn.nbnv.api.dao.warehouse;

import java.util.List;
import org.apache.ibatis.annotations.Select;
import uk.org.nbn.nbnv.api.model.SiteBoundary;

public interface SiteBoundaryMapper {
    
    @Select("SELECT sbd.*, sbfd.identifier FROM SiteBoundaryData sbd INNER JOIN SiteBoundaryFeatureData sbfd ON sbd.featureID = sbfd.id WHERE siteBoundaryDatasetKey = #{datasetKey} ORDER BY name ASC")
    List<SiteBoundary> getByDatasetKey(String datasetKey);

}
