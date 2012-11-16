/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.org.nbn.nbnv.api.dao.warehouse;

import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import uk.org.nbn.nbnv.api.model.Dataset;
import uk.org.nbn.nbnv.api.model.User;

/**
 *
 * @author Paul Gilbertson
 */
public interface UserMapper {
    @Select("SELECT * from UserData WHERE id = #{id}")
    public User getUser(@Param("id") int id);
    
    @Select("SELECT COUNT(*) FROM UserRoleSystemAdministratorData WHERE userKey = #{id}")
    public boolean isUserSystemAdministrator(@Param("id") int id);
    
    @Select("SELECT d.* FROM DatasetData d INNER JOIN DatasetAdministrator da ON da.datasetKey = d.[key] WHERE da.userID = #{id}")
    public List<Dataset> getDatasetsUserAdmins(@Param("id") int id);
}
