package uk.gov.nbn.data.gis.processor;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.*;
import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import uk.gov.nbn.data.gis.processor.MethodArgumentFactory.Argument;

/**
 * The following is a factory for obtaining a MapServiceMethod for a given 
 * requested path
 * @author Christopher Johnson
 */
@Component
public class ProviderFactory {
    @Autowired ApplicationContext context;
    @Autowired MethodArgumentFactory methodArgumentFactory;
    
    private Collection<? extends Provider> providers;
    
    @PostConstruct public void init() {
        providers = context.getBeansOfType(Provider.class).values();
    }

    public Object provideForMethodAndExecute(Object instance, Method method, MapServiceMethod mapServiceMethod, HttpServletRequest request) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, ProviderException {
        Argument[] arguments = methodArgumentFactory.getArguments(method);
        Object[] parameters = new Object[arguments.length];
        for(int i=0; i<parameters.length; i++) {
            parameters[i] = getProviderFor(arguments[i]).provide(
                                            arguments[i].getParameterClass(), 
                                            arguments[i].getParameterType(),
                                            mapServiceMethod, 
                                            request, 
                                            arguments[i].getAnnotationMap());
        }
        
        return method.invoke(instance, parameters);
    }
    
    /**
     * The following method will obtain a provider from those which are configured in spring
     * which can cope with the given request
     * @param method
     * @param request
     * @param toReturn
     * @param paramAnnotations
     * @return A provider which can cope with the parameter
     * @throws IllegalArgumentException If no provider exists
     */
    public Provider getProviderFor(Argument argument) {
        for(Provider currProvider : providers) {
            if(currProvider.isProviderFor(argument.getParameterClass(), argument.getParameterType(), argument.getAnnotationMap())) {
                return currProvider;
            }
        }
        throw new IllegalArgumentException("There is no provider configured which can provider for this argument");
    }
}
