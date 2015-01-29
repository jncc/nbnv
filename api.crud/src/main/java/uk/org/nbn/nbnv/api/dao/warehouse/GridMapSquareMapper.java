package uk.org.nbn.nbnv.api.dao.warehouse;

import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.SelectProvider;
import uk.org.nbn.nbnv.api.dao.providers.GridMapSquareProvider;
import uk.org.nbn.nbnv.api.model.GridMapSquare;
import uk.org.nbn.nbnv.api.model.TaxonDataset;
import uk.org.nbn.nbnv.api.model.User;

public interface GridMapSquareMapper {
    
    @SelectProvider(type=GridMapSquareProvider.class, method="gridMapSquares")
    List<GridMapSquare> getGridMapSquares(
            @Param("user") User user, 
            @Param("ptvk") String ptvk, 
            @Param("resolution") String resolution, 
            @Param("startYear") Integer startYear, 
            @Param("endYear") Integer endYear,
            @Param("datasetKey") List<String> datasetKey,
            @Param("viceCountyIdentifier") String viceCountyIdentifier,
            @Param("absence") boolean absence);
    
    @SelectProvider(type=GridMapSquareProvider.class, method="gridMapSquaresInspire")
    List<GridMapSquare> getGridMapSquaresLimitedFilter(
            @Param("user") User user,
            @Param("ptvk") String ptvk,
            @Param("resolution") String resolution);
    
    @SelectProvider(type=GridMapSquareProvider.class, method="gridMapDatasets")
    List<TaxonDataset> getGridMapDatasets(
            @Param("user") User user, 
            @Param("ptvk") String ptvk, 
            @Param("resolution") String resolution, 
            @Param("startYear") Integer startYear, 
            @Param("endYear") Integer endYear, 
            @Param("datasetKey") List<String> datasetKey,
            @Param("viceCountyIdentifier") String viceCountyIdentifier);
    
    @SelectProvider(type=GridMapSquareProvider.class, method="searchForMatchingResolutions")
    @Results({
        @Result(
            property="gridRef", 
            column="identifier", 
            javaType=String.class 
        )
    })
    List<GridMapSquare> searchForMatchingResolutions(
            @Param("term") String term,
            @Param("resolution") String resolution);
}
