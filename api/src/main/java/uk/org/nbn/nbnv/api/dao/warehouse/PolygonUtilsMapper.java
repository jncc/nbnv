/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.org.nbn.nbnv.api.dao.warehouse;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 *
 * @author Matt Debont
 */
public interface PolygonUtilsMapper {
    @Select("SELECT geography::STGeomFromText('#{polygon}', 4326).STArea()")
    public double getAreaFromWKT(@Param("polygon") String polygon);
}
