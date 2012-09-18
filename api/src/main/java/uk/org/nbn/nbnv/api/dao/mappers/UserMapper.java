/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.org.nbn.nbnv.api.dao.mappers;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
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
}
