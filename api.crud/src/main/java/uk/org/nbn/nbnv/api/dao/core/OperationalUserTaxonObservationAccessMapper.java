/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.org.nbn.nbnv.api.dao.core;

import java.util.List;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.DeleteProvider;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Param;
import uk.org.nbn.nbnv.api.dao.providers.AccessProvider;
import uk.org.nbn.nbnv.api.model.User;

/**
 *
 * @author paulbe
 */
public interface OperationalUserTaxonObservationAccessMapper {
    @InsertProvider(type=AccessProvider.class, method="addUserAccess")
    public void addUserAccess(
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
            , @Param("orgSuppliedList") int orgSuppliedList
            , @Param("gridRef") String gridRef
            , @Param("polygon") String polygon);

    @DeleteProvider(type=AccessProvider.class, method="removeUserAccess")
    public void removeUserAccess(
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
            , @Param("orgSuppliedList") int orgSuppliedList
            , @Param("gridRef") String gridRef
            , @Param("polygon") String polygon);
    
    @Delete("DELETE FROM UserTaxonObservationAccess WHERE observationID IN (SELECT obs.id FROM TaxonObservationDataEnhanced obs WHERE datasetKey = #{datasetKey}) AND userID > 1")
    public void removeAllUserAccessForDataset(@Param("datasetKey") String datasetKey);
}
