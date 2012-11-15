/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.org.nbn.nbnv.api.dao.core;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;
import uk.org.nbn.nbnv.api.model.User;

/**
 *
 * @author Paul Gilbertson
 */
public interface OperationalUserMapper {
    @Update("UPDATE User " +
        "SET password_sha1 = #{passwordSHA1}, " +
            "password_md5_sha1 = #{md5PasswordSHA1} " +
        "WHERE id=#{user.id}")
    void setUserPassword( @Param("user") User user,  
                                @Param("passwordSHA1") byte[] passwordHash, 
                                @Param("md5PasswordSHA1") byte[] md5passwordHash);    
}
