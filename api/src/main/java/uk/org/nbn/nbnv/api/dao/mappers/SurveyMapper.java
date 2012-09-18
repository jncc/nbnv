package uk.org.nbn.nbnv.api.dao.mappers;

import java.util.List;
import org.apache.ibatis.annotations.Select;
import uk.org.nbn.nbnv.api.model.*;

public interface SurveyMapper {
    
    @Select("SELECT * FROM SurveySpeciesRecordCountData WHERE datasetKey = #{datasetKey}")
    List<Survey> selectSurveysByDatasetKey(String datasetKey);

}
