package uk.org.nbn.nbnv.api.dao.warehouse;

import java.util.List;
import org.apache.ibatis.annotations.Select;
import uk.org.nbn.nbnv.api.model.HabitatDataset;
import uk.org.nbn.nbnv.api.model.HabitatFeature;

public interface HabitatFeatureMapper {

    @Select("SELECT identifier, HabitatDatasetKey, providerKey, uploadDate, title as datasetTitle " +
                "FROM HabitatFeatureData hfd " +
                "INNER JOIN HabitatFeatureFeatureData hffd ON hfd.featureID = hffd.id " +
                "INNER JOIN HabitatDatasetData hdd ON hfd.habitatDatasetKey = hdd.datasetKey " +
                "WHERE identifier = #{identifier}")
    HabitatFeature getByIdentifier(String identifier);

}
