package uk.gov.nbn.data.gis.processor;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Component;

/**
 * The following class represents a factory which produces an ordered list of 
 * method arguments which need to be provided in order to invoke a method. 
 * The argument class (@see Argument) enables access to the annotations array 
 * provided by the java reflection api to be read as a ClassMap.
 * 
 * This simplifies the obtaining of a parameter when used in Provider methods
 * @see Provider
 * @author Christopher Johnson
 */
@Component
public class MethodArgumentFactory {
    private final Map<Method, Argument[]> processedMethods;
    
    public MethodArgumentFactory() {
        processedMethods = new HashMap<Method, Argument[]>();
    }
    
    public Argument[] getArguments(Method method) {
        if(!processedMethods.containsKey(method)) {
            Class<?>[] parameterClasses = method.getParameterTypes();
            Type[] parameterTypes = method.getGenericParameterTypes();
            Argument[] arguments = new Argument[parameterClasses.length];
            Annotation[][] parameterAnnotations = method.getParameterAnnotations();

            for(int i=0; i<parameterTypes.length; i++) {
                arguments[i] = new Argument(parameterClasses[i], parameterTypes[i], Arrays.asList(parameterAnnotations[i]));
            }
            processedMethods.put(method, arguments);
        }
        return processedMethods.get(method);
    }
    
    public static class Argument {
        private Class<?> parameterClass;
        private Type parameterType;
        private Annotations annotationMap;
        
        private Argument(Class<?> parameterClass, Type parameterType, List<Annotation> annotations) {
            this.parameterClass = parameterClass;
            this.parameterType = parameterType;
            this.annotationMap = new Annotations(annotations);
        }

        public Class<?> getParameterClass() {
            return parameterClass;
        }
        
        public Type getParameterType() {
            return parameterType;
        }

        public Annotations getAnnotationMap() {
            return annotationMap;
        }
    }
}