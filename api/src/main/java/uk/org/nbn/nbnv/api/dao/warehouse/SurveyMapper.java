package uk.org.nbn.nbnv.api.dao.warehouse;

import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import uk.org.nbn.nbnv.api.model.*;

public interface SurveyMapper {

    @Select("SELECT * FROM SurveySpeciesRecordCountData WHERE datasetKey = #{datasetKey}")
    @Results(value = {
        @Result(property="id", column="surveyID", javaType=Integer.class)
    })
    List<Survey> selectSurveysByDatasetKey(String datasetKey);
    
    @Select("SELECT * FROM SurveySpeciesRecordCountData WHERE datasetKey = #{datasetKey} AND surveyID = #{surveyID}")
    @Results(value = {
        @Result(property="id", column="surveyID", javaType=Integer.class)
    })
    List<Survey> selectSurveyByIdAndDatasetKey(String datasetKey, int surveyID);
    
    @Select("SELECT * FROM SurveySpeciesRecordCountData WHERE datasetKey = #{datasetKey} AND providerKey = #{providerKey}")
    @Results(value = {
        @Result(property="id", column="surveyID", javaType=Integer.class)
    })
    Survey selectSurveyByProviderKeyAndDatasetKey(@Param("datasetKey") String datasetKey, @Param("providerKey") String providerKey);
    
}
