package uk.org.nbn.nbnv.api.dao.warehouse;

import java.util.List;
import org.apache.ibatis.annotations.Select;
import uk.org.nbn.nbnv.api.model.Sample;

public interface SampleMapper {
        
    //TODO this needs turning into a warehouse schema bound view when it is stable
    @Select("SELECT * from SampleData where surveyID = #{surveyID} ORDER BY sampleKey")
    List<Sample> selectSamplesBySurveyID(int surveyID);

}
