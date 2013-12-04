/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.org.nbn.nbnv.api.dao.warehouse;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;
import uk.org.nbn.nbnv.api.dao.providers.StatisticsProvider;

/**
 *
 * @author Matt Debont
 */
public interface StatisticsMapper {
    @SelectProvider(type = StatisticsProvider.class, method = "getRowCount")
    int getRowCount(@Param("table") String table);
}
