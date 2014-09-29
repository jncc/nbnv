package uk.org.nbn.nbnv.api.dao.core;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import uk.org.nbn.nbnv.api.model.Attribute;

/**
 *
 * @author Matt Debont
 */
public interface OperationalAttributeMapper {      

    @Select("SELECT * FROM Attribute WHERE id = #{attributeID}")
    Attribute getDatasetAttribute(@Param("attributeID") int attributeID);
    
    @Update("UPDATE Attribute SET description = #{description} WHERE id = #{attributeID}")
    int updateDatasetAttribute(@Param("attributeID") int attributeID, @Param("description") String description);
}
