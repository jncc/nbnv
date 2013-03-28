package uk.org.nbn.nbnv.api.dao.warehouse;

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
    @Select("SELECT fd.id, label, fd.identifier, type FROM FeatureIdentifierData fid INNER JOIN FeatureData fd ON fd.id = fid.id WHERE fid.identifier = #{id}")
    @Results({
        @Result(
            property="worldBoundingBox", 
            column="id", 
            javaType=BoundingBox.class, 
            one=@One(select="uk.org.nbn.nbnv.api.dao.warehouse.BoundingBoxMapper.getWorldBoundingBox")
        ),
        @Result(
            property="nativeBoundingBox", 
            column="id", 
            javaType=BoundingBox.class, 
            one=@One(select="uk.org.nbn.nbnv.api.dao.warehouse.BoundingBoxMapper.getNativeBoundingBox")
        )
    })
    Feature getFeature(String featureId);

    @Select("SELECT id, label, identifier, type FROM FeatureData WHERE id = #{id}")
    @Results({
        @Result(
            property="worldBoundingBox", 
            column="id", 
            javaType=BoundingBox.class, 
            one=@One(select="uk.org.nbn.nbnv.api.dao.warehouse.BoundingBoxMapper.getWorldBoundingBox")
        ),
        @Result(
            property="nativeBoundingBox", 
            column="id", 
            javaType=BoundingBox.class, 
            one=@One(select="uk.org.nbn.nbnv.api.dao.warehouse.BoundingBoxMapper.getNativeBoundingBox")
        )
    })
    Feature getFeatureID(int featureId);
}
