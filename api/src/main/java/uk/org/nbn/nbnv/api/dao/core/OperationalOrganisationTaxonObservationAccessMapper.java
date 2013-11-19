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
import uk.org.nbn.nbnv.api.model.Organisation;

/**
 *
 * @author paulbe
 */
public interface OperationalOrganisationTaxonObservationAccessMapper {
    @InsertProvider(type=AccessProvider.class, method="addOrgAccess")
    public void addOrganisationAccess(
            @Param("organisation") Organisation organisation
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

    @DeleteProvider(type=AccessProvider.class, method="removeOrgAccess")
    public void removeOrganisationAccess(
            @Param("organisation") Organisation organisation
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

    @Delete("DELETE FROM OrganisationTaxonObservationAccess WHERE observationID IN (SELECT obs.id FROM TaxonObservationDataEnhanced WHERE datasetKey = #{datasetKey})")
    public void removeAllOrganisationAccessForDataset(@Param("datasetKey") String datasetKey);}
