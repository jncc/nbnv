package uk.org.nbn.nbnv.api.dao.mappers;

import org.apache.ibatis.annotations.One;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import uk.org.nbn.nbnv.api.model.BoundingBox;
import uk.org.nbn.nbnv.api.model.GridSquare;

/**
 * The following mapper will return information regarding grid
 * @author Christopher Johnson
 */
public interface GridSquareMapper {    
    @Select("SELECT * FROM GridSquareFeatureData WHERE label = #{gridSquare}")
    @Results(value = {
        @Result(property="boundingBox", column="featureID", javaType=BoundingBox.class, one=@One(select="uk.org.nbn.nbnv.api.dao.mappers.BoundingBoxMapper.getBoundingBoxForGridSquare")),
    })
    GridSquare getGridSquare(@Param("gridSquare") String gridSquare);
}
