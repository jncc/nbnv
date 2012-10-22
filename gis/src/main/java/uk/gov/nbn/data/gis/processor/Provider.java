package uk.gov.nbn.data.gis.processor;

import java.lang.reflect.Type;

/**
 * The following is the interface which should be implemented in order to be a 
 * provider to a MapServiceMethod
 * @author Chris Johnson
 */
public interface Provider {
    boolean isProviderFor(Class<?> clazz, Type type, Annotations annotations);
    Object provide(Class<?> clazz, Type type, MapServiceMethod.Call method, Annotations annotations) throws ProviderException;
}
