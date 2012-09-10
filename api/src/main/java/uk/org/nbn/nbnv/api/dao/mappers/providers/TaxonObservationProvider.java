/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.org.nbn.nbnv.api.dao.mappers.providers;

import java.util.List;
import java.util.Map;
import static org.apache.ibatis.jdbc.SelectBuilder.BEGIN;
import static org.apache.ibatis.jdbc.SelectBuilder.FROM;
import static org.apache.ibatis.jdbc.SelectBuilder.SELECT;
import static org.apache.ibatis.jdbc.SelectBuilder.SQL;
import static org.apache.ibatis.jdbc.SelectBuilder.WHERE;
import org.springframework.util.StringUtils;
//import static org.apache.ibatis.jdbc.SelectBuilder.AND;
/**
 *
 * @author Paul Gilbertson
 */
public class TaxonObservationProvider {
    public String filteredSelect(Map<String, Object> params) {
        BEGIN();
        SELECT("*");
        FROM("UserTaxonObservationData");
        WHERE("userKey = #{userKey}");
        
        if ((Integer)params.get("startYear") > -1) {
            WHERE("YEAR(endDate) >= #{startYear}");
        }
        
        if ((Integer)params.get("endYear") > -1) {
            WHERE("YEAR(startDate) <= #{endYear}");
        }
        
        for (String dataset : (List<String>) params.get("datasetKey")) {
            
            //WHERE("datasetKey IN (#{datasetKey})");
        }
        
        return SQL();
    }
}
