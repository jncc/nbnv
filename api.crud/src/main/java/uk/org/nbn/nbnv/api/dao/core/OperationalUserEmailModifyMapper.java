/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.org.nbn.nbnv.api.dao.core;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.One;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import uk.org.nbn.nbnv.api.model.User;
import uk.org.nbn.nbnv.api.model.UserEmailModify;

/**
 *
 * @author Matt Debont
 */
public interface OperationalUserEmailModifyMapper {
    
    @Insert("INSERT INTO UserEmailModify VALUES (#{user.id}, #{email}, #{activationKey}, GETDATE())")
    public int modifyEmail(@Param("user") User user, 
        @Param("email") String email, 
        @Param("activationKey") String activationKey);
    
    @Delete("DELETE FROM UserEmailModify WHERE userID = #{user.id}")
    public int removeModification(@Param("user") User user);
    
    @Select("SELECT * FROM UserEmailModify WHERE userID = #{user.id}")
    @Results(value = {
        @Result(property="user", column="userID", javaType=User.class, one=@One(select="uk.org.nbn.nbnv.api.dao.core.OperationalUserMapper.getUserById"))
    })    
    public UserEmailModify getModificationForUser(@Param("user") User user);
    
    @Update("UPDATE \"User\" SET email = (SELECT newEmail FROM UserEmailModify uem WHERE uem.userID = #{user.id}) WHERE id = #{user.id}")
    public int updateEmailAddress(@Param("user") User user);
}
