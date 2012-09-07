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
        @Result(property="user", column="userKey", javaType=User.class, one=@One(select="uk.org.nbn.nbnv.api.dao.mappers.UserMapper.getUser")),
        @Result(property="dataset", column="datasetKey", javaType=Dataset.class, one=@One(select="uk.org.nbn.nbnv.api.dao.mappers.DatasetMapper.selectByDatasetKey"))
    })
    List<DatasetAdministrator> selectAll();

    @Select("SELECT * FROM DatasetAdministrator WHERE userKey = #{userKey}")
    @Results(value={
        @Result(property="user", column="userKey", javaType=User.class, one=@One(select="uk.org.nbn.nbnv.api.dao.mappers.UserMapper.getUser")),
        @Result(property="dataset", column="datasetKey", javaType=Dataset.class, one=@One(select="uk.org.nbn.nbnv.api.dao.mappers.DatasetMapper.selectByDatasetKey"))
    })
    List<DatasetAdministrator> selectByUser(@Param("userKey") int userKey);

    @Select("SELECT * FROM DatasetAdministrator WHERE datasetKey = #{datasetKey}")
    @Results(value={
        @Result(property="user", column="userKey", javaType=User.class, one=@One(select="uk.org.nbn.nbnv.api.dao.mappers.UserMapper.getUser")),
        @Result(property="dataset", column="datasetKey", javaType=Dataset.class, one=@One(select="uk.org.nbn.nbnv.api.dao.mappers.DatasetMapper.selectByDatasetKey"))
    })
    List<DatasetAdministrator> selectByDataset(@Param("datasetKey") String datasetKey);
    
    @Select("SELECT * FROM DatasetAdministrator WHERE userKey = #{userKey} AND datasetKey = #{datasetKey}")
    @Results(value={
        @Result(property="user", column="userKey", javaType=User.class, one=@One(select="uk.org.nbn.nbnv.api.dao.mappers.UserMapper.getUser")),
        @Result(property="dataset", column="datasetKey", javaType=Dataset.class, one=@One(select="uk.org.nbn.nbnv.api.dao.mappers.DatasetMapper.selectByDatasetKey"))
    })
    DatasetAdministrator selectByUserAndDataset(@Param("userKey") int userKey, @Param("datasetKey") String datasetKey);

}
