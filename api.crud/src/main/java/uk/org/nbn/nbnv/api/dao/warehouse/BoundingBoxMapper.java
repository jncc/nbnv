package uk.org.nbn.nbnv.api.dao.warehouse;

import org.apache.ibatis.annotations.Select;
import uk.org.nbn.nbnv.api.model.BoundingBox;

/**
 * The following mapper will return bounding boxes for various features held
 * on the NBN Gateway
 * @author Christopher Johnson
 */
public interface BoundingBoxMapper {
    @Select("SELECT " + 
                "geom.STEnvelope().STPointN(1).STX as minX, " + 
                "geom.STEnvelope().STPointN(1).STY as minY, " + 
                "geom.STEnvelope().STPointN(3).STX as maxX, " + 
                "geom.STEnvelope().STPointN(3).STY as maxY, " +
                "'EPSG:' + cast(geom.STSrid as nvarchar) as epsgCode " + 
            "FROM FeatureData " + 
            "WHERE id = #{id}")
    BoundingBox getWorldBoundingBox(int id);
    
    @Select("SELECT " + 
                "originalGeom.STEnvelope().STPointN(1).STX as minX, " + 
                "originalGeom.STEnvelope().STPointN(1).STY as minY, " + 
                "originalGeom.STEnvelope().STPointN(3).STX as maxX, " + 
                "originalGeom.STEnvelope().STPointN(3).STY as maxY, " +
                "'EPSG:' + cast(originalGeom.STSrid as nvarchar) as epsgCode " + 
            "FROM FeatureData " + 
            "WHERE id = #{id}")
    BoundingBox getNativeBoundingBox(int id);
}
