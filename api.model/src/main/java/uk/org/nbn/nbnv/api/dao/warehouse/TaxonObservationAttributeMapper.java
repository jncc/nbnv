/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.org.nbn.nbnv.api.dao.warehouse;

import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.SelectProvider;
import uk.org.nbn.nbnv.api.dao.providers.TaxonObservationProvider;
import uk.org.nbn.nbnv.api.model.Attribute;
import uk.org.nbn.nbnv.api.model.TaxonObservationAttribute;
import uk.org.nbn.nbnv.api.model.User;

/**
 *
 * @author Matt Debont
 */
public interface TaxonObservationAttributeMapper {
    
    @SelectProvider(type=TaxonObservationProvider.class, method="filterSelectedAttributesForDownload")
    public List<Attribute> getAttributeListForObservations(
            @Param("user") User user
            , @Param("startYear") Integer startYear
            , @Param("endYear") Integer endYear
            , @Param("datasetKey") List<String> datasetKey
            , @Param("ptvk") List<String> ptvk
            , @Param("spatialRelationship") String spatialRelationship
            , @Param("featureID") String featureId
            , @Param("sensitive") Boolean sensitive
            , @Param("designation") String designation
            , @Param("taxonOutputGroup") String taxonOutputGroup
            , @Param("organisationList") int organisationList
            , @Param("gridRef") String gridRef
            , @Param("polygon") String polygon);
    
    @SelectProvider(type=TaxonObservationProvider.class, method="filterSelectedAttributeDataForDownload")
    public List<TaxonObservationAttribute> getAttributesForObservations(
            @Param("user") User user
            , @Param("startYear") Integer startYear
            , @Param("endYear") Integer endYear
            , @Param("datasetKey") List<String> datasetKey
            , @Param("ptvk") List<String> ptvk
            , @Param("spatialRelationship") String spatialRelationship
            , @Param("featureID") String featureId
            , @Param("sensitive") Boolean sensitive
            , @Param("designation") String designation
            , @Param("taxonOutputGroup") String taxonOutputGroup
            , @Param("gridRef") String gridRef
            , @Param("polygon") String polygon);
}
