package uk.gov.nbn.data.gis.processor;

import javax.servlet.http.HttpServletRequest;

/**
 * The following is the interface which should be implemented in order to be a 
 * provider to a MapServiceMethod
 * @author Chris Johnson
 */
public interface Provider {
    boolean isProviderFor(Class<?> clazz, Annotations annotations);
    Object provide(Class<?> clazz, MapServiceMethod method, HttpServletRequest request, Annotations annotations) throws ProviderException;
}
