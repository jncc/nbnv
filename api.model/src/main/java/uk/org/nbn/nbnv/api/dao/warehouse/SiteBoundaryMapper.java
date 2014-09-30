package uk.org.nbn.nbnv.api.dao.warehouse;

import java.util.List;
import org.apache.ibatis.annotations.One;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;
import uk.org.nbn.nbnv.api.dao.providers.TaxonObservationProvider;
import uk.org.nbn.nbnv.api.model.SiteBoundary;
import uk.org.nbn.nbnv.api.model.SiteBoundaryCategory;
import uk.org.nbn.nbnv.api.model.SiteBoundaryDataset;
import uk.org.nbn.nbnv.api.model.User;

public interface SiteBoundaryMapper {
    @Select("SELECT sbd.*, sbfd.identifier FROM SiteBoundaryData sbd INNER JOIN SiteBoundaryFeatureData sbfd ON sbd.featureID = sbfd.id ORDER BY name ASC")
    @Results(value = {
        @Result(property = "siteBoundaryDataset", column = "siteBoundaryDatasetKey", javaType = SiteBoundaryDataset.class, one = @One(select = "uk.org.nbn.nbnv.api.dao.warehouse.SiteBoundaryDatasetMapper.getByDatasetKey")),
        @Result(property = "siteBoundaryDatasetKey", column = "siteBoundaryDatasetKey"),
        @Result(property = "siteBoundaryCategory", column = "siteBoundaryCategoryID", javaType = SiteBoundaryCategory.class, one = @One(select = "uk.org.nbn.nbnv.api.dao.warehouse.SiteBoundaryCategoryMapper.getByIDDatasetsNotInstantiated")),
        @Result(property = "siteBoundaryCategoryId", column = "siteBoundaryCategoryID")
    })
    List<SiteBoundary> getAll();
    
    @Select("SELECT sbd.*, fId.identifier FROM SiteBoundaryData sbd INNER JOIN FeatureIdentifierData fid ON sbd.featureID = fid.id WHERE fid.identifier = #{id}")
    @Results(value = {
        @Result(property = "siteBoundaryDataset", column = "siteBoundaryDatasetKey", javaType = SiteBoundaryDataset.class, one = @One(select = "uk.org.nbn.nbnv.api.dao.warehouse.SiteBoundaryDatasetMapper.getByDatasetKey")),
        @Result(property = "siteBoundaryDatasetKey", column = "siteBoundaryDatasetKey"),
        @Result(property = "siteBoundaryCategory", column = "siteBoundaryCategoryID", javaType = SiteBoundaryCategory.class, one = @One(select = "uk.org.nbn.nbnv.api.dao.warehouse.SiteBoundaryCategoryMapper.getByIDDatasetsNotInstantiated")),
        @Result(property = "siteBoundaryCategoryId", column = "siteBoundaryCategoryID")
    })
    SiteBoundary getById(@Param("id") String identifier);

    @Select("SELECT sbd.*, sbfd.identifier FROM SiteBoundaryData sbd INNER JOIN SiteBoundaryFeatureData sbfd ON sbd.featureID = sbfd.id WHERE siteBoundaryDatasetKey = #{datasetKey} ORDER BY name ASC")
    List<SiteBoundary> getByDatasetKey(String datasetKey);

    @SelectProvider(type = TaxonObservationProvider.class, method = "filteredSelectSitesForTVK")
    @Results(value = {
        @Result(property = "siteBoundaryDataset", column = "siteBoundaryDatasetKey", javaType = SiteBoundaryDataset.class, one = @One(select = "uk.org.nbn.nbnv.api.dao.warehouse.SiteBoundaryDatasetMapper.getByDatasetKey")),
        @Result(property = "siteBoundaryDatasetKey", column = "siteBoundaryDatasetKey"),
        @Result(property = "siteBoundaryCategory", column = "siteBoundaryCategoryID", javaType = SiteBoundaryCategory.class, one = @One(select = "uk.org.nbn.nbnv.api.dao.warehouse.SiteBoundaryCategoryMapper.getByIDDatasetsNotInstantiated")),
        @Result(property = "siteBoundaryCategoryId", column = "siteBoundaryCategoryID")
    })
    public List<SiteBoundary> getByTaxonVersionKey(
            @Param("user") User user, 
            @Param("startYear") Integer startYear, 
            @Param("endYear") Integer endYear, 
            @Param("datasetKey") List<String> datasetKey, 
            @Param("ptvk") List<String> ptvk, 
            @Param("spatialRelationship") String spatialRelationship, 
            @Param("featureID") String featureId, 
            @Param("sensitive") Boolean sensitive, 
            @Param("designation") String designation, 
            @Param("taxonOutputGroup") String taxonOutputGroup, 
            @Param("orgSuppliedList") int orgSuppliedList, 
            @Param("gridRef") String gridRef,
            @Param("polygon") String polygon,
            @Param("absence") boolean absence);
}
