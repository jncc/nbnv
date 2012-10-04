package uk.org.nbn.nbnv.api.dao.mappers;

import org.apache.ibatis.annotations.One;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import uk.org.nbn.nbnv.api.model.BoundingBox;
import uk.org.nbn.nbnv.api.model.Feature;

/**
 * The following mapper will return information regarding grid
 * @author Christopher Johnson
 */
public interface FeatureMapper {    
    @Select("SELECT * FROM FeatureData WHERE featureID = #{id}")
    @Results({
        @Result(
            property="worldBoundingBox", 
            column="featureID", 
            javaType=BoundingBox.class, 
            one=@One(select="uk.org.nbn.nbnv.api.dao.mappers.BoundingBoxMapper.getWorldBoundingBox")
        ),
        @Result(
            property="nativeBoundingBox", 
            column="featureID", 
            javaType=BoundingBox.class, 
            one=@One(select="uk.org.nbn.nbnv.api.dao.mappers.BoundingBoxMapper.getNativeBoundingBox")
        )
    })
    Feature getFeature(int featureId);
}
