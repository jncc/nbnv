/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.org.nbn.nbnv.api.dao.mappers;

import java.util.List;
import org.apache.ibatis.annotations.One;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import uk.org.nbn.nbnv.api.model.Organisation;
import uk.org.nbn.nbnv.api.model.OrganisationMembership;
import uk.org.nbn.nbnv.api.model.User;

/**
 *
 * @author Paul Gilbertson
 */
public interface OrganisationMembershipMapper {
    @Select("SELECT userKey, organisationID, role FROM OrganisationMembershipData WHERE userKey = #{userKey}")
    @Results(value = {
        @Result(property="user", column="userKey", javaType=User.class, one=@One(select="uk.org.nbn.nbnv.api.dao.mappers.UserMapper.getUser")),
        @Result(property="organisation", column="organisationID", javaType=Organisation.class, one=@One(select="uk.org.nbn.nbnv.api.dao.mappers.OrganisationMapper.selectByID"))           
    })
    List<OrganisationMembership> getOrganisationMembershipsByUser(@Param("userKey") int userKey);

    @Select("SELECT userKey, organisationID, role FROM OrganisationMembershipData")
    @Results(value = {
        @Result(property="user", column="userKey", javaType=User.class, one=@One(select="uk.org.nbn.nbnv.api.dao.mappers.UserMapper.getUser")),
        @Result(property="organisation", column="organisationID", javaType=Organisation.class, one=@One(select="uk.org.nbn.nbnv.api.dao.mappers.OrganisationMapper.selectByID"))           
    })
    List<OrganisationMembership> selectAll();

    @Select("SELECT userKey, organisationID, role FROM OrganisationMembershipData WHERE userKey = #{userKey} AND organisationID = #{organisationID}")
    @Results(value = {
        @Result(property="user", column="userKey", javaType=User.class, one=@One(select="uk.org.nbn.nbnv.api.dao.mappers.UserMapper.getUser")),
        @Result(property="organisation", column="organisationID", javaType=Organisation.class, one=@One(select="uk.org.nbn.nbnv.api.dao.mappers.OrganisationMapper.selectByID")),
    })
    OrganisationMembership getOrganisationMembershipsByUserAndOrganisation(@Param("userKey") int userKey, @Param("organisationID") int organisationID);
}
