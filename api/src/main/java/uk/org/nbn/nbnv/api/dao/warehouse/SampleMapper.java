package uk.org.nbn.nbnv.api.dao.warehouse;

import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import uk.org.nbn.nbnv.api.model.Sample;

public interface SampleMapper {
        
    //TODO this needs turning into a warehouse schema bound view when it is stable
    @Select("SELECT * from SampleData where surveyID = #{surveyID} ORDER BY sampleKey")
    List<Sample> selectSamplesBySurveyID(int surveyID);
    
    @Select("SELECT smd.* FROM SampleData smd INNER JOIN SurveyData svd ON svd.id = smd.surveyId WHERE svd.providerKey = #{providerKey} AND svd.datasetKey = #{datasetKey}")
    List<Sample> selectSamplesBySurveyProviderKey(@Param("datasetKey") String datasetKey, @Param("providerKey") String providerKey);
    
    @Select("SELECT smd.* FROM SampleData smd INNER JOIN SurveyData svd ON svd.id = smd.surveyId WHERE svd.providerKey = #{surveyKey} AND svd.datasetKey = #{datasetKey} AND smd.providerKey = #{sampleKey}")
    Sample selectSampleBySampleProviderKey(@Param("datasetKey") String datasetKey, @Param("surveyKey") String surveyKey, @Param("sampleKey") String sampleKey);

}
