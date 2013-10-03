/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.org.nbn.nbnv.api.dao.core;

import java.util.List;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import uk.org.nbn.nbnv.api.model.Organisation;

/**
 *
 * @author paulbe
 */
public interface OperationalDatasetAdministratorMapper {
    @Insert("INSERT INTO DatasetAdministrator VALUES (#{userKey}, #{datasetKey})")
    int insertNewDatasetAdministrator(@Param("userKey") int userKey, @Param("datasetKey") String datasetKey);
    
    @Delete("DELETE FROM DatasetAdministrator WHERE userID = #{userKey} AND datasetKey = #{datasetKey}")
    int removeDatasetAdministrator(@Param("userKey") int userKey, @Param("datasetKey") String datasetKey);
}
