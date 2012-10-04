package uk.org.nbn.nbnv.api.dao.mappers;

import org.apache.ibatis.annotations.Select;
import uk.org.nbn.nbnv.api.model.BoundingBox;

/**
 * The following mapper will return bounding boxes for various features held
 * on the NBN Gateway
 * @author Christopher Johnson
 */
public interface BoundingBoxMapper {
    @Select("SELECT " + 
                "MIN(geom.STEnvelope().STPointN(1).STX) as minX, " + 
                "MIN(geom.STEnvelope().STPointN(1).STY) as minY, " + 
                "MAX(geom.STEnvelope().STPointN(3).STX) as maxX, " + 
                "MAX(geom.STEnvelope().STPointN(3).STY) as maxY, " +
                "'EPSG:4326' as epsgCode " + 
            "FROM FeatureData " + 
            "WHERE featureID = #{id}")
    BoundingBox getWorldBoundingBox(int id);
    
    @Select("SELECT " + 
                "MIN(geom.STEnvelope().STPointN(1).STX) as minX, " + 
                "MIN(geom.STEnvelope().STPointN(1).STY) as minY, " + 
                "MAX(geom.STEnvelope().STPointN(3).STX) as maxX, " + 
                "MAX(geom.STEnvelope().STPointN(3).STY) as maxY, " +
                "'EPSG:4326' as epsgCode " + 
            "FROM FeatureData " + 
            "WHERE featureID = #{id}")
    BoundingBox getNativeBoundingBox(int id);
}
