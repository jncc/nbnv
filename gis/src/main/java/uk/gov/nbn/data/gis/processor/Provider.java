package uk.gov.nbn.data.gis.processor;

import java.lang.annotation.Annotation;
import java.util.List;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author Chris Johnson
 */
public interface Provider<T,R> {
    T providesFor(MapServiceMethod method, HttpServletRequest request, Class<?> clazz, List<Annotation> annotations);
    R process(T providesForResponse, MapServiceMethod method, HttpServletRequest request, Class<?> clazz, List<Annotation> annotations);
}
