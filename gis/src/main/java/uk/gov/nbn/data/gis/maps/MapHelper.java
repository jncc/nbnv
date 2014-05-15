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
import uk.ac.ceh.dynamo.bread.BreadException;
import static uk.gov.nbn.data.dao.jooq.Tables.*;
import uk.gov.nbn.data.gis.maps.colour.Band;

/**
 * The following class provides methods which can be used to inject SQL filters
 * into a SQL statement.
 * @author Chris Johnson
 */
public class MapHelper {

    private static final List<Integer> DEFAULT_VERIFICATION_KEYS = Arrays.asList(1,3,4);
    
    public static String getMapData(Query query) {
        return query.getSQL(ParamType.INLINED);
    }
    
    /**Get the DSLContext for the dialect of the sqlserver**/
    public static DSLContext getContext() {
        return DSL.using(SQLDialect.SQLSERVER);
    }
    
    /**The following interface enables anonymous implementations for creating
     * SQL Expressions in Map Server Templates
     **/
    public interface LayerDataGenerator {
        String getData(String layerName) throws BreadException;
    }
    

    static Condition createTemporalSegments(Condition currentCond, List<Band> dates, Field<? extends Date> startDateField, Field<? extends Date> endDateField) {
	if(dates != null && !dates.isEmpty()){
	    Condition a = getTemporalSegment(dates.get(0).getStartYear(),dates.get(0).getEndYear(),startDateField,endDateField);
	    Condition b = (dates.get(1) != null) ? getTemporalSegment(dates.get(1).getStartYear(),dates.get(1).getEndYear(),startDateField,endDateField) : null;
	    Condition c = (dates.get(2) != null) ? getTemporalSegment(dates.get(2).getStartYear(),dates.get(2).getEndYear(),startDateField,endDateField) : null;
	    switch (dates.size()){
		case 1:
		    currentCond = currentCond.and(a);
		    break;
		case 2:
		    currentCond = currentCond.and(a.or(b));
		    break;
		case 3:
		    currentCond = currentCond.and(a.or(b).or(c));
		    break;
	    }
	    return currentCond;
	}else{
	    return currentCond;
	}
    }
        
    private static Condition getTemporalSegment(String startYear, String endYear, Field<? extends Date> startDateField, Field<? extends Date> endDateField) {
	Condition startYearCond = extract(startDateField,DatePart.YEAR)
	    .greaterOrEqual(Integer.parseInt(startYear));
	return createEndYearSegment(startYearCond, endDateField, endYear);
    }
        
    static Condition createTemporalSegment(Condition currentCond, String startYear, String endYear, Field<? extends Date> startDateField, Field<? extends Date> endDateField) {
        return createEndYearSegment(createStartYearSegment(currentCond, startDateField, startYear), endDateField, endYear);
    }
    
    private static Condition createStartYearSegment(Condition currentCond, Field<? extends Date> endDateField, String startYear) {
        if(startYear != null) {
            return currentCond.and(
                extract(endDateField,DatePart.YEAR)
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
    
    static Condition createInVerificationKeysSegment(Condition currentCond, Field<Integer> field, List<Integer> values){
	if(values != null && !values.isEmpty()){
	    return currentCond.and(field.in(values));
	}
	else {
	    return currentCond.and(field.in(DEFAULT_VERIFICATION_KEYS));
	}
    }
    
    static String getSelectedFeatureData(String selectedFeature) {
        if(selectedFeature != null) {
            return MapHelper.getMapData(getContext()
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
