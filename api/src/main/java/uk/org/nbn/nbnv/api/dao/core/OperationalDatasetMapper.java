/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.org.nbn.nbnv.api.dao.core;

import java.util.List;
import org.apache.ibatis.annotations.Many;
import org.apache.ibatis.annotations.One;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import uk.org.nbn.nbnv.api.model.Dataset;
import uk.org.nbn.nbnv.api.model.Organisation;

/**
 *
 * @author Paul Gilbertson
 */
public interface OperationalDatasetMapper {
    @Update("UPDATE Dataset SET title = #{title}, description = #{description}, dataCaptureMethod = #{captureMethod}, purpose = #{purpose}, geographicalCoverage = #{geographicalCoverage}, "
            + " dataQuality = #{quality}, additionalInformation = #{additionalInformation}, accessConstraints = #{accessConstraints}, useConstraints = #{useConstraints}, temporalCoverage = #{temporalCoverage} "
            + " WHERE [key] = #{key}")
    public int updateDataset(Dataset dataset);

    @Select("SELECT * FROM DatasetData WHERE DatasetData.\"key\" = #{key}")
    @Results(value = {
        //@Result(property="organisation", column="organisationID", javaType=Organisation.class, one=@One(select="uk.org.nbn.nbnv.api.dao.warehouse.OrganisationMapper.selectByID")),
        @Result(property="organisationID", column="organisationID"),
        @Result(property="key", column="key")//,
        //@Result(property="contributingOrganisations", column="key", javaType=List.class, many=@Many(select="uk.org.nbn.nbnv.api.dao.warehouse.DatasetContributingOrganisationMapper.selectOrganisationsByDataset"))
    })
    Dataset selectByDatasetKey(String key);

}
