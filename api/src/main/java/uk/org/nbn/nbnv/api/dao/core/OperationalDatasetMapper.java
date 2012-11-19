/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.org.nbn.nbnv.api.dao.core;

import org.apache.ibatis.annotations.Update;
import uk.org.nbn.nbnv.api.model.Dataset;

/**
 *
 * @author Paul Gilbertson
 */
public interface OperationalDatasetMapper {
    @Update("UPDATE Dataset SET title = #{title}, description = #{description}, dataCaptureMethod = #{captureMethod}, purpose = #{purpose}, geographicalCoverage = #{geographicalCoverage}, "
            + " dataQuality = #{quality}, additionalInformation = #{additionalInformation}, accessConstraints = #{accessConstraints}, useConstraints = #{useConstraints}, temporalCoverage = #{temporalCoverage} "
            + " WHERE [key] = #{key}")
    public int updateDataset(Dataset dataset);

}
