/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.org.nbn.nbnv.api.dao.core;

import java.util.Date;
import java.util.List;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import uk.org.nbn.nbnv.api.model.Dataset;
import uk.org.nbn.nbnv.api.model.User;

/**
 *
 * @author Paul Gilbertson
 */
public interface OperationalUserMapper {
    
    @Select("SELECT * from \"User\" WHERE id = #{id}")
    public User getUserById(@Param("id") int id);
    
    @Select("Select * from \"User\" where username = #{username}")
    public User getUser(@Param("username") String username);

    @Select("Select * from \"User\" where email = #{email}")
    public User getUserFromEmail(@Param("email") String username);

    //@Select("SELECT COUNT(*) FROM UserRoleSystemAdministrator WHERE userKey = #{id}") TABLE DOES NOT EXIST
    @Select("SELECT COUNT(*) FROM \"User\" WHERE id = #{id} AND userTypeID = 3")
    public boolean isUserSystemAdministrator(@Param("id") int id);

    @Select("SELECT d.* FROM DatasetData d INNER JOIN DatasetAdministrator da ON da.datasetKey = d.[key] WHERE da.userID = #{id}")
    public List<Dataset> getDatasetsUserAdmins(@Param("id") int id);

    @Select("SELECT u.id, u.forename, u.surname, u.email from \"User\" u WHERE forename LIKE #{term} OR surname LIKE #{term} OR email LIKE #{term} OR (forename + ' ' + surname) LIKE #{term} ORDER BY forename, surname")
    public List<User> searchForUser(@Param("term") String term);
    
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
    
    @Update("UPDATE \"User\" SET forename = #{forename}, surname = #{surname}, email = #{email}, phone = #{phone} WHERE id = #{id}")
    public void updateUserDetails(@Param("id") int id, @Param("forename") String forename, @Param("surname") String surname, @Param("email") String email, @Param("phone") String phone);
    
    @Update("UPDATE \"User\" SET subscribedToNBNMarketting = #{nbnMarketing} WHERE id = #{id}") 
    public void updateUserEmailSettings(@Param("id") int id, @Param("nbnMarketing") int nbnMarketing);
    
    @Select("SELECT u.id, u.forename, u.surname, u.email FROM (SELECT * from \"User\" WHERE forename LIKE #{term} OR surname LIKE #{term} OR email LIKE #{term} OR (forename + ' ' + surname) LIKE #{term}) AS u WHERE NOT EXISTS (SELECT 1 FROM UserOrganisationMembership WHERE userID = id AND organisationID = #{organisation}) ORDER BY forename, surname")
    public List<User> searchForUserExcludeOrganisationMembers(@Param("term") String term, @Param("organisation") int organisationId);

    @Select("SELECT u.id, u.forename, u.surname, u.email FROM (SELECT * from \"User\" WHERE forename LIKE #{term} OR surname LIKE #{term} OR email LIKE #{term} OR (forename + ' ' + surname) LIKE #{term}) AS u WHERE NOT EXISTS (SELECT 1 FROM DatasetAdministrator WHERE userID = id AND datasetKey = #{datasetKey}) ORDER BY forename, surname")
    public List<User> searchForUserExcludeDatasetAdmins(@Param("term") String term, @Param("datasetKey") String datasetKey);
    
    @Update("UPDATE \"User\" SET lastLoggedIn = GETDATE() WHERE username = #{username}")
    public void userLoggedIn(@Param("username") String username);
}
