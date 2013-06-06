package uk.org.nbn.nbnv.api.dao.core;

import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import uk.org.nbn.nbnv.api.model.Survey;

/**
 *
 * @author Matt Debont
 */
public interface OperationalSurveyMapper {
    @Select("SELECT * FROM Survey WHERE id = #{id} AND datasetKey = #{datasetKey}")
    public Survey getSurveyById(int id, String datasetKey);
    
    @Update("UPDATE Survey SET providerKey = #{providerKey}, title = #{title}, description = #{description}, geographicalCoverage = #{geographicalCoverage}, temporalCoverage = #{temporalCoverage}, dataQuality = #{dataQuality}, dataCaptureMethod = #{dataCaptureMethod}, purpose = #{purpose}, additionalInformation = #{additionalInformation} WHERE id = #{id}")
    public int updateSurveyById(Survey survey);
}
