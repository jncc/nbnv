package uk.org.nbn.nbnv.api.dao.warehouse;

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
    @Select("SELECT userID, organisationID, role FROM OrganisationMembershipData")
    @Results(value = {
        @Result(property="user", column="userID", javaType=User.class, one=@One(select="uk.org.nbn.nbnv.api.dao.warehouse.UserMapper.getUserById")),
        @Result(property="organisation", column="organisationID", javaType=Organisation.class, one=@One(select="uk.org.nbn.nbnv.api.dao.warehouse.OrganisationMapper.selectByID"))           
    })
    List<OrganisationMembership> selectAll();

    @Select("SELECT userID, organisationID, role FROM OrganisationMembershipData WHERE userID = #{userKey}")
    @Results(value = {
        @Result(property="user", column="userID", javaType=User.class, one=@One(select="uk.org.nbn.nbnv.api.dao.warehouse.UserMapper.getUserById")),
        @Result(property="organisation", column="organisationID", javaType=Organisation.class, one=@One(select="uk.org.nbn.nbnv.api.dao.warehouse.OrganisationMapper.selectByID"))           
    })
    List<OrganisationMembership> selectByUser(@Param("userKey") int userKey);

    @Select("SELECT userID, organisationID, role FROM OrganisationMembershipData WHERE organisationID = #{organisationID}")
    @Results(value = {
        @Result(property="user", column="userID", javaType=User.class, one=@One(select="uk.org.nbn.nbnv.api.dao.warehouse.UserMapper.getUserById")),
        @Result(property="organisation", column="organisationID", javaType=Organisation.class, one=@One(select="uk.org.nbn.nbnv.api.dao.warehouse.OrganisationMapper.selectByID"))
    })
    List<OrganisationMembership> selectByOrganisation(@Param("organisationID") int organisationID);

    @Select("SELECT userID, organisationID, role FROM OrganisationMembershipData WHERE userID = #{userKey} AND organisationID = #{organisationID}")
    @Results(value = {
        @Result(property="user", column="userID", javaType=User.class, one=@One(select="uk.org.nbn.nbnv.api.dao.warehouse.UserMapper.getUserById")),
        @Result(property="organisation", column="organisationID", javaType=Organisation.class, one=@One(select="uk.org.nbn.nbnv.api.dao.warehouse.OrganisationMapper.selectByID"))
    })
    OrganisationMembership selectByUserAndOrganisation(@Param("userKey") int userKey, @Param("organisationID") int organisationID);
    
    @Select("SELECT COUNT(*) FROM OrganisationMembershipData WHERE userID = #{userKey} AND organisationID = #{organisationID}")
    boolean isUserMemberOfOrganisation(@Param("userKey") int userKey, @Param("organisationID") int organisationID);
    
    @Select("SELECT COUNT(*) FROM OrganisationMembershipData WHERE userID = #{userKey} AND organisationID = #{organisationID} AND (role = 'lead' OR role = 'administrator')")
    boolean isUserOrganisationAdmin(@Param("userKey") int userKey, @Param("organisationID") int organisationID);
    
    
}
