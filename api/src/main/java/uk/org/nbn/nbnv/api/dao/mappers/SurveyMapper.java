package uk.org.nbn.nbnv.api.dao.mappers;

import java.util.List;
import org.apache.ibatis.annotations.Select;
import uk.org.nbn.nbnv.api.model.*;

public interface SurveyMapper {
    
    //TODO this needs turning into a warehouse schema bound view when it is stable
    @Select("SELECT a.surveyID, datasetKey, surveyKey, title, description, geographicalCoverage, temporalCoverage, b.speciesCount, b.sampleCount, b.recordCount " +
                "FROM NBNCore.dbo.Survey a " +
                "INNER JOIN " +
                "(SELECT su.surveyID surveyID, COUNT(distinct prefnameTaxonVersionKey) speciesCount, COUNT(distinct sa.sampleID) sampleCount, COUNT(observationID) recordCount " +
                "FROM NBNCore.dbo.Survey su INNER JOIN NBNCore.dbo.Sample sa ON su.surveyID = sa.surveyID " +
                "INNER JOIN NBNCore.dbo.TaxonObservation tob ON sa.sampleID = tob.sampleID " +
                "INNER JOIN NBNCore.dbo.Taxon t ON tob.taxonVersionKey = t.taxonVersionKey " +
                "GROUP BY su.surveyID) b " +
                "ON a.surveyID = b.surveyID " +
                "WHERE a.datasetKey = #{datasetKey}"

            )
    List<Survey> selectSurveysByDatasetKey(String datasetKey);

}
