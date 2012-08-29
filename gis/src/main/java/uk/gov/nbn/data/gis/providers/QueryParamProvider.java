package uk.gov.nbn.data.gis.providers;

import java.lang.annotation.Annotation;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import uk.gov.nbn.data.gis.processor.MapServiceMethod;
import uk.gov.nbn.data.gis.processor.Provider;

/**
 *
 * @author Chris Johnson
 */
public class QueryParamProvider implements Provider<QueryParam, String> {

    @Override
    public QueryParam providesFor(MapServiceMethod method, HttpServletRequest request, Class<?> clazz, List<Annotation> annotations) {
        for(Annotation currAnnotation : annotations) {
            if(currAnnotation instanceof QueryParam) {
                return (QueryParam)currAnnotation;
            }
        }
        return null;
    }

    @Override
    public String process(QueryParam providesForResponse, MapServiceMethod method, HttpServletRequest request, Class<?> clazz, List<Annotation> annotations) {
        return request.getParameter(providesForResponse.value());
    }
}
