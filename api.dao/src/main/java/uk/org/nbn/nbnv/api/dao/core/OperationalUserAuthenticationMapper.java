/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.org.nbn.nbnv.api.dao.core;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import uk.org.nbn.nbnv.api.model.User;

/**
 *
 * @author Matt Debont
 */
public interface OperationalUserAuthenticationMapper {

        @Select("SELECT * FROM \"User\" WHERE (" +
                "SELECT id from UserTokenAuthenticationData WHERE username_sha1 = #{u} AND password_sha1=#{p}" +
            ") = id")
    User getUser(@Param("u") byte[] username_SHA1, @Param("p") byte[] password_SHA1);
    
    
    @Select("SELECT * FROM \"User\" WHERE (" +
                "SELECT id from UserTokenAuthenticationData WHERE username_sha1 = #{u} AND password_md5_sha1=#{p}" +
            ") = id")
    User getUserMD5(@Param("u") byte[] username_SHA1, @Param("p") byte[] password_MD5_SHA1);
    
    @Select("SELECT password_sha1 FROM UserTokenAuthenticationData WHERE username_sha1 = #{u}")
    Object getUsersPassHash(@Param("u") byte[] username_SHA1); 
}
