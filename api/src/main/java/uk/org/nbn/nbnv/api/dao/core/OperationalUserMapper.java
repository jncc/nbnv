/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.org.nbn.nbnv.api.dao.core;

import java.util.Date;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import uk.org.nbn.nbnv.api.model.User;

/**
 *
 * @author Paul Gilbertson
 */
public interface OperationalUserMapper {
    @Update("UPDATE \"User\" " +
        "SET password_sha1 = #{passwordSHA1}, " +
            "password_md5_sha1 = #{md5PasswordSHA1} " +
        "WHERE id=#{user.id}")
    void setUserPassword( @Param("user") User user,  
                                @Param("passwordSHA1") byte[] passwordHash, 
                                @Param("md5PasswordSHA1") byte[] md5passwordHash); 
    
    @Insert("INSERT INTO \"User\"" +
                                "(username, password_sha1, password_md5_sha1, " + 
                                "userTypeID, forename, surname, phone, email," +
                                "active, activationKey, invalidEmail, allowEmailAlerts," +
                                "subscribedToAdminMails, subscribedToNBNMarketting, " +
                                "bannedFromValidation, englishNameOrder, registrationDate)" +
                        "VALUES (#{user.username}, #{passwordSHA1}, #{md5PasswordSHA1}," +
                                "2, #{user.forename}, #{user.surname}, #{user.phone}," +
                                "#{user.email}, 0, #{activationKey}, 0, 0, 0, 0, " +
                                "0, 0, #{registrationDate})")
    void registerNewUser(   @Param("user") User user,
                            @Param("registrationDate") Date registrationDate,
                            @Param("activationKey") String activationKey,
                            @Param("passwordSHA1") byte[] passwordHash, 
                            @Param("md5PasswordSHA1") byte[] md5passwordHash);
    
    @Insert("UPDATE \"User\" SET active = 1 WHERE username = #{username} AND active = 0 AND activationKey = #{code}")
    int activateNewUser( @Param("username") String username, @Param("code") String code);
    
    @Select("SELECT * from UserData WHERE id = #{id}")
    public User getUserById(@Param("id") int id);

}
