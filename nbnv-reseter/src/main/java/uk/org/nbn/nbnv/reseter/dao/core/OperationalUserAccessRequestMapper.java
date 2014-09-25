/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.org.nbn.nbnv.reseter.dao.core;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.List;
import org.apache.ibatis.annotations.*;
import uk.org.nbn.nbnv.api.model.Dataset;
import uk.org.nbn.nbnv.api.model.TaxonObservationFilter;
import uk.org.nbn.nbnv.api.model.User;
import uk.org.nbn.nbnv.api.model.UserAccessRequest;

/**
 *
 * @author Paul Gilbertson
 */
public interface OperationalUserAccessRequestMapper {
    @Select("SELECT uar.* FROM UserAccessRequest uar "
            + "WHERE uar.datasetKey = #{dataset} AND responseTypeID = 1")
    @Results(value = {
        @Result(property="filter", column="filterID", javaType=TaxonObservationFilter.class, one=@One(select="uk.org.nbn.nbnv.api.dao.core.OperationalTaxonObservationFilterMapper.selectById")),
        @Result(property="user", column="userID", javaType=User.class, one=@One(select="uk.org.nbn.nbnv.api.dao.core.OperationalUserMapper.getUserById")),
        @Result(property="dataset", column="datasetKey", javaType=Dataset.class, one=@One(select="uk.org.nbn.nbnv.api.dao.core.OperationalDatasetMapper.selectByDatasetKey")),
        @Result(property="requestPurposeLabel", column="requestPurposeID", javaType=String.class, one=@One(select="uk.org.nbn.nbnv.api.dao.core.AccessRequestPurposeMapper.getPurposeText")),
        @Result(property="datasetKey", column="datasetKey")
    })
    public List<UserAccessRequest> getGrantedRequestsByDataset(@Param("dataset") String datasetKey);
}
