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
import uk.org.nbn.nbnv.api.model.User;
import uk.org.nbn.nbnv.api.model.UserAccessRequest;
import uk.org.nbn.nbnv.api.model.UserAccessRequestAuditHistory;

/**
 *
 * @author paulbe
 */
public interface OperationalUserAccessRequestAuditHistoryMapper {
    @Insert("INSERT INTO UserAccessRequestAuditHistory (filterID, actionerID, [timestamp], [action]) VALUES (#{filter}, #{user}, CURRENT_TIMESTAMP, #{action})")
    public void addHistory(@Param("filter") int filterID, @Param("user") int userID, @Param("action") String action);
    
    @Select("SELECT uarah.* FROM UserAccessRequestAuditHistory uarah INNER JOIN UserAccessRequest uar ON uar.filterID = uarah.filterID WHERE uar.datasetKey = #{dataset}")
    @Results(value={
        @Result(property="actioner", column="actionerID", javaType=User.class, one=@One(select="uk.org.nbn.nbnv.api.dao.core.OperationalUserMapper.getUserById")),
        @Result(property="request", column="filterID", javaType=UserAccessRequest.class, one=@One(select="uk.org.nbn.nbnv.api.dao.core.OperationalUserAccessRequestMapper.getRequest"))
    })
    public List<UserAccessRequestAuditHistory> getHistory(@Param("dataset") String dataset);
}
