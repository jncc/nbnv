/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.org.nbn.nbnv.api.dao.warehouse;

import java.util.List;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.One;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import uk.org.nbn.nbnv.api.model.Dataset;
import uk.org.nbn.nbnv.api.model.DatasetAdministrator;
import uk.org.nbn.nbnv.api.model.User;

/**
 *
 * @author Paul Gilbertson
 */
public interface DatasetAdministratorMapper {
    @Select("SELECT * FROM DatasetAdministrator")
    @Results(value={
        @Result(property="user", column="userID", javaType=User.class, one=@One(select="uk.org.nbn.nbnv.api.dao.warehouse.UserMapper.getUserById")),
        @Result(property="dataset", column="datasetKey", javaType=Dataset.class, one=@One(select="uk.org.nbn.nbnv.api.dao.warehouse.DatasetMapper.selectByDatasetKey"))
    })
    List<DatasetAdministrator> selectAll();

    @Select("SELECT * FROM DatasetAdministrator WHERE userID = #{userKey}")
    @Results(value={
        @Result(property="user", column="userID", javaType=User.class, one=@One(select="uk.org.nbn.nbnv.api.dao.warehouse.UserMapper.getUserById")),
        @Result(property="dataset", column="datasetKey", javaType=Dataset.class, one=@One(select="uk.org.nbn.nbnv.api.dao.warehouse.DatasetMapper.selectByDatasetKey"))
    })
    List<DatasetAdministrator> selectByUser(@Param("userKey") int userKey);

    @Select("SELECT d.* FROM DatasetAdministrator da INNER JOIN DatasetData d ON da.datasetKey = d.[key] WHERE da.userID = #{userKey}")
    List<Dataset> selectDatasetsByUser(@Param("userKey") int userKey);

    @Select("SELECT * FROM DatasetAdministrator WHERE datasetKey = #{datasetKey}")
    @Results(value={
        @Result(property="user", column="userID", javaType=User.class, one=@One(select="uk.org.nbn.nbnv.api.dao.warehouse.UserMapper.getUserById")),
        @Result(property="dataset", column="datasetKey", javaType=Dataset.class, one=@One(select="uk.org.nbn.nbnv.api.dao.warehouse.DatasetMapper.selectByDatasetKey"))
    })
    List<DatasetAdministrator> selectByDataset(@Param("datasetKey") String datasetKey);
    
    @Select("SELECT * FROM DatasetAdministrator WHERE userID = #{userKey} AND datasetKey = #{datasetKey}")
    @Results(value={
        @Result(property="user", column="userID", javaType=User.class, one=@One(select="uk.org.nbn.nbnv.api.dao.warehouse.UserMapper.getUserById")),
        @Result(property="dataset", column="datasetKey", javaType=Dataset.class, one=@One(select="uk.org.nbn.nbnv.api.dao.warehouse.DatasetMapper.selectByDatasetKey"))
    })
    DatasetAdministrator selectByUserAndDataset(@Param("userKey") int userKey, @Param("datasetKey") String datasetKey);

    @Select("SELECT COUNT(*) FROM DatasetAdministrator WHERE userID = #{userKey} AND datasetKey = #{datasetKey}")
    boolean isUserDatasetAdministrator(@Param("userKey") int userKey, @Param("datasetKey") String datasetKey);
    
    @Select("SELECT DISTINCT dd.* FROM ((SELECT dd.[key] FROM DatasetData dd INNER JOIN OrganisationMembershipData omd ON omd.organisationID = dd.organisationID WHERE omd.userID = #{userID} AND omd.[role] = 'administrator') UNION ALL (SELECT da.[datasetKey] as [key] FROM DatasetAdministrator da WHERE da.userID = #{userID})) keys INNER JOIN DatasetData dd ON dd.[key] = keys.[key]")
    List<Dataset> getAdminableDatasetsByUserAndOrgs(@Param("userID") int userID);
}