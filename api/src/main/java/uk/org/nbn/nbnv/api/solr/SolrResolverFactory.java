package uk.org.nbn.nbnv.api.solr;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import org.apache.solr.common.SolrDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

/**
 * A simple class which holds all the solr resolver methods which are held on
 * beans held in spring
 * @author cjohn
 */
@Component
public class SolrResolverFactory {
    private Map<String, InstanceMethod> resolvers;
    
    /**
     * The following constructor will scan all the beans held in Spring and look
     * for methods which are annotated with solr resolver. This will be stored
     * in this object for resolving nbn concepts held in the solr index
     * @param context 
     */
    public @Autowired SolrResolverFactory(ApplicationContext context) {
        resolvers = new HashMap<String, InstanceMethod>();
        //get all the beans held in spring
        for(Object bean : context.getBeansOfType(Object.class).values()) {
            for(Method method : bean.getClass().getMethods()) {
                SolrResolver annotation = method.getAnnotation(SolrResolver.class);
                if(annotation != null) {
                    InstanceMethod instanceMethod = new InstanceMethod(bean, method);
                    for(String type : annotation.value()) {
                        resolvers.put(type, instanceMethod);
                    }
                }
            }
        }
    }
    
    public Object resolve(SolrDocument solr) {
        String[] record_idParts = ((String)solr.get("record_id")).split("-", 2);
        InstanceMethod resolverMethod = resolvers.get(record_idParts[0]);
        if(resolverMethod != null) {
            return resolverMethod.execute(record_idParts[1]);
        }
        else {
            throw new IllegalArgumentException("The solr response contained results on an unkown type");
        }
    }
    
    /**
     * The following class holds a instance and method combo. The method must only
     * take one parameter (An identifer specific for that type)
     */
    private static class InstanceMethod {
        private final Method method;
        private final Object instance;
        private final Class<?> identifierType;
        
        public InstanceMethod(Object instance, Method method) {
            this.instance = instance;
            this.method = method;
            Class<?>[] parameterTypes = method.getParameterTypes();
            this.identifierType = parameterTypes[0];
            if(parameterTypes.length != 1) {
                throw new IllegalArgumentException("The annotated method must only take one argument");
            }
        }
        
        /**
         * Execute the solr resolver method with a string representation of the
         * unique identifer
         * @param id A unique identifer for that type
         * @return The response of the solr resolver method
         */
        public Object execute(String id) {
            try {
                if(identifierType == String.class) {
                    return method.invoke(instance, id); //normal string just invoke
                }
                else if(identifierType.isPrimitive()) { //type is primative, must parse
                    if (identifierType == int.class)
                        return method.invoke(instance, Integer.parseInt(id));
                    else if (identifierType == double.class)
                        return method.invoke(instance, Double.parseDouble(id));
                    else if (identifierType == float.class)
                        return method.invoke(instance, Float.parseFloat(id));
                    else
                        throw new IllegalArgumentException("Unhandled primative type parse");
                }
                throw new IllegalArgumentException("The solr resolver parameter type is not handled");
            } catch (IllegalAccessException ex) {
                throw new RuntimeException(ex);
            } catch (InvocationTargetException ex) {
                throw new RuntimeException(ex);
            }
        }
    }
}
