/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.org.nbn.nbnv.api.dao.core;

import java.util.List;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.One;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import uk.org.nbn.nbnv.api.model.OrganisationAccessRequest;
import uk.org.nbn.nbnv.api.model.OrganisationAccessRequestAuditHistory;
import uk.org.nbn.nbnv.api.model.User;

/**
 *
 * @author paulbe
 */
public interface OperationalOrganisationAccessRequestAuditHistoryMapper {
    @Insert("INSERT INTO OrganisationAccessRequestAuditHistory (filterID, actionerID, [timestamp], [action]) VALUES (#{filter}, #{user}, CURRENT_TIMESTAMP, #{action})")
    public void addHistory(@Param("filter") int filterID, @Param("user") int userID, @Param("action") String action);
    
    @Select("SELECT oarah.* FROM OrganisationAccessRequestAuditHistory oarah INNER JOIN OrganisationAccessRequest oar ON oar.filterID = oarah.filterID WHERE oar.datasetKey = #{dataset}")
    @Results(value={
        @Result(property="actioner", column="actionerID", javaType=User.class, one=@One(select="uk.org.nbn.nbnv.api.dao.core.OperationalUserMapper.getUserById")),
        @Result(property="request", column="filterID", javaType=OrganisationAccessRequest.class, one=@One(select="uk.org.nbn.nbnv.api.dao.core.OperationalOrganisationAccessRequestMapper.getRequest"))
    })
    public List<OrganisationAccessRequestAuditHistory> getHistory(@Param("dataset") String dataset);
}
