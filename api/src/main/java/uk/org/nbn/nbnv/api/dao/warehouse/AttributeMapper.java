package uk.org.nbn.nbnv.api.dao.warehouse;

import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import uk.org.nbn.nbnv.api.model.Attribute;

public interface AttributeMapper {
    
    @Select("SELECT COUNT(*) FROM DatasetAttributeData WHERE attributeID = #{attributeID} AND datasetKey = #{datasetKey}")
    boolean isAttributePartOfDataset(@Param("datasetKey") String datasetKey, @Param("attributeID") int attributeID);
    
    @Select("SELECT attributeID, label, description FROM DatasetAttributeData WHERE datasetKey = #{datasetKey}")
    List<Attribute> selectAttributesByDatasetKey(String datasetKey);
}
