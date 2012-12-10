package uk.org.nbn.nbnv.api.rest.providers;

import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import javax.validation.ConstraintViolation;
import javax.validation.Valid;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyReader;
import javax.ws.rs.ext.Provider;
import javax.ws.rs.ext.Providers;
import org.springframework.stereotype.Component;

/**
 * The following provider will perform bean validation (JSR 303) on inputs annotated
 * with the Valid annotation.
 */
@Component
@Provider
public class JSR303ValidProvider implements MessageBodyReader<Object> {
    @Context Providers providers;
    
    private final Validator validator;
    
    public JSR303ValidProvider() {
        this.validator = Validation.buildDefaultValidatorFactory().getValidator();
    }
    
    @Override public boolean isReadable(Class<?> type, Type type1, Annotation[] antns, MediaType mt) {
        return getAnnotationLookup(antns).containsKey(Valid.class);
    }

    @Override public Object readFrom(Class<Object> type, Type type1, Annotation[] antns, MediaType mt, MultivaluedMap<String, String> mm, InputStream in) throws IOException, WebApplicationException {
        Annotation[] annotations = removeValidAnnotation(antns); //remove the valid annotation so I can get the next best message body reader
        MessageBodyReader<Object> messageBodyReader = providers.getMessageBodyReader(type, type1, annotations, mt);
        Object toValidate = messageBodyReader.readFrom(type, type1, annotations, mt, mm, in);
        Set<ConstraintViolation<Object>> validationErrors = validator.validate(toValidate);
        if(!validationErrors.isEmpty()) {
            throw new IllegalArgumentException(createValidationErrorMessage(validationErrors));
        }
        return toValidate;
    }
    
    /**
     * Forms a validation message from all constraint errors
     * @param errors
     * @return 
     */
    private static String createValidationErrorMessage(Set<ConstraintViolation<Object>> errors) {
        StringBuilder toReturn = new StringBuilder("The supplied input is invalid :");
        for(ConstraintViolation<Object> violation : errors) {
            toReturn.append(violation.getMessage())
                    .append("\n");
                    
        }
        return toReturn.toString();
    }
    
    /**
     * Gets an array without the Valid annotation in. 
     * @param annotations This must contain an instance of Valid annotation
     */
    private static Annotation[] removeValidAnnotation(Annotation[] annotations) {
        Annotation[] toReturn = new Annotation[annotations.length -1];
        Map<Class<? extends Annotation>, Annotation> validAnnotation = getAnnotationLookup(annotations);
        validAnnotation.remove(Valid.class);
        return validAnnotation.values().toArray(toReturn);
    }
    
    /* Converts the annotations array into a lookup map     */
    private static Map<Class<? extends Annotation>, Annotation> getAnnotationLookup(Annotation[] annotations) {
        Map<Class<? extends Annotation>, Annotation> annotationMap = new HashMap<Class<? extends Annotation>, Annotation>();
        for(Annotation currAnnotation : annotations) {
            annotationMap.put(currAnnotation.annotationType(), currAnnotation);
        }
        return annotationMap;
    } 
}
