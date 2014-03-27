package uk.gov.nbn.data.gis.maps;

import java.sql.Date;
import java.util.List;
import org.jooq.Condition;
import org.jooq.DSLContext;
import org.jooq.DatePart;
import org.jooq.Field;
import org.jooq.Query;
import org.jooq.SQLDialect;
import org.jooq.conf.ParamType;
import org.jooq.impl.DSL;
import static org.jooq.impl.DSL.*;
import static uk.gov.nbn.data.dao.jooq.Tables.*;

/**
 * The following class provides methods which can be used to inject SQL filters
 * into a SQL statement.
 * @author Chris Johnson
 */
public class MapHelper {
    
    public static String getMapData(Field<?> geomField, Field<?> uniqueField, int srid, Query query) {
        return query.getSQL(ParamType.INLINED);
    }
    
    /**Get the DSLContext for the dialect of the sqlserver**/
    public static DSLContext getContext() {
        return DSL.using(SQLDialect.SQLSERVER);
    }
    
    /**The following interface enables anonymous implementations for creating
     * SQL Expressions in Map Server Templates
     **/
    public interface ResolutionDataGenerator {
        String getData(String layerName);
    }
    
    static Condition createTemporalSegment(Condition currentCond, String startYear, String endYear, Field<? extends Date> startDateField, Field<? extends Date> endDateField) {
        return createEndYearSegment(createStartYearSegment(currentCond, startDateField, startYear), endDateField, endYear);
    }
    
    private static Condition createStartYearSegment(Condition currentCond, Field<? extends Date> startDateField, String startYear) {
        if(startYear != null) {
            
            return currentCond.and(
                extract(startDateField,DatePart.YEAR)
                .greaterOrEqual(Integer.parseInt(startYear)));
        }
        else {
            return currentCond;
        }
    }
    
    private static Condition createEndYearSegment(Condition currentCond, Field<? extends Date> endDateField, String endYear) {
        if(endYear != null) {
            return currentCond.and(
                extract(endDateField,DatePart.YEAR)
                .lessOrEqual(Integer.parseInt(endYear)));
        }
        else {
            return currentCond;
        }
    }
    
    static Condition createInDatasetsSegment(Condition currentCond, Field<String> datasetField, List<String> datasetKeys) {
        if(datasetKeys !=null && !datasetKeys.isEmpty()) {
            return currentCond.and(datasetField.in(datasetKeys));
        }
        else {
            return currentCond;
        }
    }
    
    static Condition createInIntegerFieldSegment(Condition currentCond, Field<Integer> field, List<Integer> values){
	if(values != null && !values.isEmpty()){
	    return currentCond.and(field.in(values));
	}
	else {
	    return currentCond;
	}
    }
    
    static String getSelectedFeatureData(String selectedFeature) {
        if(selectedFeature != null) {
            return MapHelper.getMapData(FEATUREDATA.GEOM, FEATUREDATA.ID, 4326, getContext()
                .select(FEATUREDATA.ID, FEATUREDATA.GEOM)
                .from(FEATUREDATA)
                .where(FEATUREDATA.IDENTIFIER.eq(selectedFeature))
            );
        }
        else {
            return null;
        }
    }
}
