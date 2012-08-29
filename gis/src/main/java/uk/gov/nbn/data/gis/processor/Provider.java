package uk.gov.nbn.data.gis.processor;

import java.lang.annotation.Annotation;
import java.util.List;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author Chris Johnson
 */
public interface Provider {
    boolean isProviderFor(Class<?> clazz, MapServiceMethod method, HttpServletRequest request, List<Annotation> annotations) throws ProviderException;
    Object provide(Class<?> clazz, MapServiceMethod method, HttpServletRequest request, List<Annotation> annotations) throws ProviderException;
}
