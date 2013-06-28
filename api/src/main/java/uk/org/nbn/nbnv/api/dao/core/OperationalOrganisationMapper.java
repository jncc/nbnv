/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.org.nbn.nbnv.api.dao.core;

import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import uk.org.nbn.nbnv.api.model.Organisation;

/**
 *
 * @author paulbe
 */
public interface OperationalOrganisationMapper {
    @Select("SELECT CASE WHEN logo IS NULL THEN 0 ELSE 1 END as hasLogo, CASE WHEN logoSmall IS NULL THEN 0 ELSE 1 END as hasSmallLogo, id, name, abbreviation, summary, address, postcode, contactName, contactEmail, phone, website, allowPublicRegistration FROM Organisation WHERE id = #{id}")
    Organisation selectByID(int id);
    
    @Select("SELECT org.* FROM Organisation org INNER JOIN OrganisationMembership omd ON omd.organisationID = org.id WHERE omd.userID = #{id}")
    List<Organisation> selectByUser(int id);

    @Select("SELECT org.* FROM Organisation org INNER JOIN OrganisationMembership omd ON omd.organisationID = org.id WHERE omd.userID = #{id} AND (omd.role = 'administrator' OR omd.role = 'lead')")
    List<Organisation> selectByAdminUser(int id);
    
    @Select("SELECT org.* FROM Organisation org WHERE name LIKE #{term} OR abbreviation LIKE #{term}")
    List<Organisation> searchForOrganisation(@Param("term") String term);
    
    @Select("SELECT org.* FROM Organisation org WHERE org.id NOT IN (SELECT omd.organisationID FROM UserOrganisationMembership omd WHERE omd.userID = #{userID})")
    List<Organisation> getJoinableOrganisations(@Param("userID") int userID);
    
    @Update("UPDATE Organisation SET name = #{name}, abbreviation = #{abbreviation}, "
            + "summary = #{summary}, address = #{address}, postcode = #{postcode}, "
            + "phone = #{phone}, website = #{website}, contactName = #{contactName}, "
            + "contactEmail = #{contactEmail} WHERE id = #{id}")
    int updateOrganisationDetails(
            @Param("id") int id,
            @Param("name") String name,
            @Param("abbreviation") String abbreviation,
            @Param("summary") String summary,
            @Param("address") String address,
            @Param("postcode") String postcode,
            @Param("phone") String phone,
            @Param("website") String website,
            @Param("contactName") String contactName,
            @Param("contactEmail") String contactEmail);
    
    @Update("UPDATE Organisation SET logo = #{logo}, smallLogo = #{smallLogo}")
    int updateOrganisationLogo(
            @Param("logo") byte[] logo,
            @Param("smallLogo") byte[] smallLogo);
}
