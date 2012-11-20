/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.org.nbn.nbnv.api.dao.core;

import java.sql.Date;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;

/**
 *
 * @author Paul Gilbertson
 */
public interface OperationalUserAccessRequestMapper {
    @Insert("INSERT INTO UserAccessRequest (filterID, userID, datasetKey, requestRoleID, requestTypeID, requestReason, requestDate)"
            + " VALUES (#{filterID}, #{userID}, #{datasetKey}, #{requestRoleID}, #{requestTypeID}, #{requestReason}, #{requestDate})")
    public int createRequest(
            @Param("filterID") int filterID
            , @Param("userID") int userID
            , @Param("datasetKey") String datasetKey
            , @Param("requestRoleID") int requestRoleID
            , @Param("requestTypeID") int requestTypeID
            , @Param("requestReason") String requestReason
            , @Param("requestDate") Date requestDate);
}
