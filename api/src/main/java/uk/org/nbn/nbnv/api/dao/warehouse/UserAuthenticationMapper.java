package uk.org.nbn.nbnv.api.dao.core;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import uk.org.nbn.nbnv.api.model.User;

/**
 *
 * @author Christopher Johnson
 */
public interface UserAuthenticationMapper {
    
    @Select("SELECT count(*) from UserTokenAuthenticationData WHERE username_sha1 = #{u} AND password_sha1=#{p}")
    boolean isUser(@Param("u") byte[] username_SHA1, @Param("p") byte[] password_SHA1);
    
    
    @Select("SELECT count(*) from UserTokenAuthenticationData WHERE username_sha1 = #{u} AND password_md5_sha1=#{p}")
    boolean isUserMD5(@Param("u") byte[] username_SHA1, @Param("p") byte[] password_MD5_SHA1);
    
    @Update("UPDATE User " +
        "SET password_sha1 = #{passwordSHA1}, " +
            "password_md5_sha1 = #{md5PasswordSHA1} " +
        "WHERE id=#{user.id}")
    void setUserPassword( @Param("user") User user,  
                                @Param("passwordSHA1") byte[] passwordHash, 
                                @Param("md5PasswordSHA1") byte[] md5passwordHash);
    
    @Select("SELECT * FROM UserData WHERE (" +
        "   SELECT id FROM UserTokenAuthenticationData" +
        "   WHERE username_sha1 = #{u}" +
        ") = id"
    )
    User getUser(@Param("u") byte[] username_SHA1);
}
