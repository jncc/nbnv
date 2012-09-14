/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.org.nbn.nbnv.api.dao.mappers;

import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;
import uk.org.nbn.nbnv.api.model.TaxonObservation;

/**
 *
 * @author Paul Gilbertson
 */
public interface TaxonObservationMapper {
    @Select("SELECT * FROM UserTaxonObservationData WHERE observationID = #{id} AND userKey = #{userKey}")
    public TaxonObservation selectById(@Param("id") int id, @Param("userKey") int userKey);

    @Select("SELECT * FROM UserTaxonObservationData WHERE datasetKey = #{id} AND userKey = #{userKey}")
    public List<TaxonObservation> selectByDataset(@Param("id") String id, @Param("userKey") int userKey);

    @Select("SELECT * FROM UserTaxonObservationData WHERE pTaxonVersionKey = #{id} AND userKey = #{userKey}")
    public List<TaxonObservation> selectByPTVK(@Param("id") String id, @Param("userKey") int userKey);
    
    @SelectProvider(type=uk.org.nbn.nbnv.api.dao.mappers.providers.TaxonObservationProvider.class, method="filteredSelect")
    public List<TaxonObservation> selectByFilter(
            @Param("userKey") int userKey
            , @Param("startYear") Integer startYear
            , @Param("endYear") Integer endYear
            , @Param("datasetKey") List<String> datasetKey
            , @Param("ptvk") List<String> ptvk
            , @Param("overlaps") Integer overlaps
            , @Param("within") Integer within
            , @Param("sensitive") Boolean sensitive
            , @Param("designation") String designation);
}
