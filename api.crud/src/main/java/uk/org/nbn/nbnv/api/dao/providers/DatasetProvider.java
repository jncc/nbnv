package uk.org.nbn.nbnv.api.dao.providers;

import java.util.List;
import java.util.Map;
import org.springframework.util.StringUtils;
import static org.apache.ibatis.jdbc.SqlBuilder.*;

/**
 * key
 *
 * @author Matt Debont
 */
public class DatasetProvider {

//    @Update("UPDATE Dataset SET title = #{title}, description = #{description}, "
//			+ "dataCaptureMethod = #{captureMethod}, purpose = #{purpose}, "
//			+ "geographicalCoverage = #{geographicalCoverage}, "
//			+ "dataQuality = #{quality}, additionalInformation = #{additionalInformation}, "
//			+ "accessConstraints = #{accessConstraints}, useConstraints = #{useConstraints}, "
//			+ "temporalCoverage = #{temporalCoverage}, licenceID = #{licenceID}, "
//			+ "metadataLastEdited = GETDATE() WHERE [key] = #{key}")	
	
	public String updateDataset(Map<String, Object> params) {
		BEGIN();
		UPDATE("Dataset");
		SET("title = #{title}");
		SET("description = #{description}");
		SET("dataCaptureMethod = #{captureMethod}");
		SET("purpose = #{purpose}");
		SET("geographicalCoverage = #{geographicalCoverage}");
		SET("dataQuality = #{quality}");
		SET("additionalInformation = #{additionalInformation}");
		SET("accessConstraints = #{accessConstraints}");
		SET("useConstraints = #{useConstraints}");
		SET("temporalCoverage = #{temporalCoverage}");
		if (params.containsKey("licenceID") && params.get("licenceID") != null) {
			SET("licenceID = #{licenceID}");
		} else {
			SET("licenceID = NULL");
		}
		SET("metadataLastEdited = GETDATE()");
		WHERE("[key] = #{datasetKey}");
		
		return SQL();
	}
	
    public String getAttributesByID(Map<String, Object> params) {
        BEGIN();
        SELECT("id AS attributeID, label, description");
        FROM("AttributeData");
        WHERE("id IN " + intListToCommaList((List<Integer>) params.get("attribs")));
        return SQL();
    }

    private String intListToCommaList(List<Integer> list) {
        return "('" + StringUtils.collectionToDelimitedString(list, "','") + "')";
    }
}
