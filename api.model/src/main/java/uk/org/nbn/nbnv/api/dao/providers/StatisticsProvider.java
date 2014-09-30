/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.org.nbn.nbnv.api.dao.providers;

import java.util.Map;
import static org.apache.ibatis.jdbc.SelectBuilder.*;

/**
 *
 * @author Matt Debont
 */
public class StatisticsProvider {
    
    
    public String getRowCount(Map<String, Object> params) {
        BEGIN();
        SELECT("COUNT(*)");
        FROM((String) params.get("table"));
        return SQL();
    }
    
    public String getRowCountAdvanced(Map<String, Object> params) {
        BEGIN();
        SELECT("SUM(row_count)");
        FROM("sys.dm_db_partition_stats");
        WHERE("object_id=OBJECT_ID('" + params.get("table") + "')");
        WHERE("(index_id=0 OR index_id=1)");
        String sql = SQL();
        return sql;
    }
}
