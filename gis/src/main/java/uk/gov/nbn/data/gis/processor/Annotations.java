package uk.gov.nbn.data.gis.processor;

import java.lang.annotation.Annotation;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The following class defines a HashMap which has been specialised for annotation
 * lookups. This class is extensively used in providers as a means of looking up 
 * an annotations for the argument which the provider is to provide for.
 * 
 * @see MethodArgumentFactory.Argument
 * @author Christopher Johnson
 */
public class Annotations extends HashMap<Class<? extends Annotation>, Annotation> {
   
    public Annotations(List<Annotation> annotations) {
        super(Collections.unmodifiableMap(createAnnotationMap(annotations)));
    }
    
    public <T extends Annotation> T get(Class<T> annotationClass) {
        return (T)super.get(annotationClass);
    }
    
    private static Map<Class<? extends Annotation>, Annotation> createAnnotationMap(List<Annotation> annotations) {
        Map<Class<? extends Annotation>, Annotation> annotationMap = new HashMap<Class<? extends Annotation>, Annotation>();
        for(Annotation currAnnotation : annotations) {
            annotationMap.put(currAnnotation.annotationType(), currAnnotation);
        }
        return annotationMap;
    }
}
