package uk.org.nbn.nbnv.api.dao.warehouse;

import java.util.List;
import org.apache.ibatis.annotations.One;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import uk.org.nbn.nbnv.api.model.SiteBoundaryCategory;

public interface SiteBoundaryCategoryMapper {
    
    @Select("SELECT * FROM SiteBoundaryCategoryData")
    @Results(value={
        @Result(property="siteBoundaryDatasets", column="id", javaType=List.class, one=@One(select="uk.org.nbn.nbnv.api.dao.warehouse.SiteBoundaryDatasetMapper.getBySiteBoundaryCategoryID")),
        @Result(property="id", column="id")
    })
    List<SiteBoundaryCategory> get();
    
    @Select("SELECT * FROM SiteBoundaryCategoryData where id = #{id}")
    @Results(value={
        @Result(property="siteBoundaryDatasets", column="id", javaType=List.class, one=@One(select="uk.org.nbn.nbnv.api.dao.warehouse.SiteBoundaryDatasetMapper.getBySiteBoundaryCategoryID")),
        @Result(property="id", column="id")
    })
    SiteBoundaryCategory getByID(int id);
    
    @Select("SELECT * FROM SiteBoundaryCategoryData where id = #{id}")
    SiteBoundaryCategory getByIDDatasetsNotInstantiated(int id);

}
