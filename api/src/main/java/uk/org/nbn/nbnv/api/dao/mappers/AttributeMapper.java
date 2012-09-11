package uk.org.nbn.nbnv.api.dao.mappers;

import java.util.List;
import org.apache.ibatis.annotations.Select;
import uk.org.nbn.nbnv.api.model.Attribute;

public interface AttributeMapper {
    
    @Select("SELECT attributeID, label, description FROM DatasetAttributeData WHERE datasetKey = #{datasetKey}")
    List<Attribute> selectAttributesByDatasetKey(String datasetKey);

}
