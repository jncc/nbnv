package uk.org.nbn.nbnv.api.dao.mappers;

import java.util.List;
import org.apache.ibatis.annotations.Many;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import uk.org.nbn.nbnv.api.model.Sample;

public interface SampleMapper {
        
    //TODO this needs turning into a warehouse schema bound view when it is stable
    @Select("SELECT * from NBNCore.dbo.Sample where surveyID = #{surveyID} ORDER BY sampleKey")
    List<Sample> selectSamplesBySurveyID(int surveyID);

}
