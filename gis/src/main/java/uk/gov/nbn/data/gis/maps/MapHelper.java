package uk.gov.nbn.data.gis.maps;

import java.util.List;
import org.jooq.Query;
import org.jooq.Condition;
import org.jooq.DatePart;
import static uk.gov.nbn.data.dao.jooq.Tables.*;
import static org.jooq.impl.Factory.*;
import org.jooq.util.sqlserver.SQLServerFactory;

/**
 * The following class provides methods which can be used to inject SQL filters
 * into a SQL statement.
 * @author Chris Johnson
 */
public class MapHelper {
    
    public static String getMapData(String geomField, String uniqueField, int srid, Query query) {
        return new StringBuilder(geomField)
                .append(" from (")
                .append(query.getSQL(true))
                .append(") AS foo USING UNIQUE ")
                .append(uniqueField)
                .append(" USING SRID=")
                .append(srid)
                .toString();
    }
    
    /**The following interface enables anonymous implementations for creating
     * SQL Expressions in Map Server Templates
     **/
    public interface ResolutionDataGenerator {
        String getData(int resolution);
    }
    
    static Condition createTemporalSegment(Condition currentCond, String startYear, String endYear) {
        return createEndYearSegment(createStartYearSegment(currentCond, startYear), endYear);
    }
    
    private static Condition createStartYearSegment(Condition currentCond, String startYear) {
        if(startYear != null) {
            return currentCond.and(
                extract(USERTAXONOBSERVATIONDATA.STARTDATE,DatePart.YEAR)
                .greaterOrEqual(Integer.parseInt(startYear)));
        }
        else {
            return currentCond;
        }
    }
    
    private static Condition createEndYearSegment(Condition currentCond, String endYear) {
        if(endYear != null) {
            return currentCond.and(
                extract(USERTAXONOBSERVATIONDATA.ENDDATE,DatePart.YEAR)
                .lessOrEqual(Integer.parseInt(endYear)));
        }
        else {
            return currentCond;
        }
    }
    
    static Condition createInDatasetsSegment(Condition currentCond, List<String> datasetKeys) {
        if(datasetKeys !=null && !datasetKeys.isEmpty()) {
            return currentCond.and(USERTAXONOBSERVATIONDATA.DATASETKEY.in(datasetKeys));
        }
        else {
            return currentCond;
        }
    }
    
    static String getSelectedFeatureData(String selectedFeature) {
        SQLServerFactory create = new SQLServerFactory();
        return MapHelper.getMapData("geom", "id", 4326, create
            .select(FEATUREDATA.ID, FEATUREDATA.GEOM)
            .from(FEATUREDATA)
            .where(FEATUREDATA.IDENTIFIER.eq(selectedFeature))
        );
    }
}
