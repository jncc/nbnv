package uk.org.nbn.nbnv.api.dao.core;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import uk.org.nbn.nbnv.api.model.Survey;

/**
 *
 * @author Matt Debont
 */
public interface OperationalSurveyMapper {
    @Select("SELECT * FROM Survey WHERE id = #{survey}")
    public Survey getSurveyById(@Param("survey") int id);
    
    @Update("UPDATE Survey SET title = #{title}, description = #{description}, geographicalCoverage = #{geographicalCoverage}, temporalCoverage = #{temporalCoverage}, dataQuality = #{dataQuality}, dataCaptureMethod = #{dataCaptureMethod}, purpose = #{purpose}, additionalInformation = #{additionalInformation} WHERE id = #{id}")
    public int updateSurveyById(Survey survey);
}
