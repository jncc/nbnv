package uk.gov.nbn.data.gis.processor;

import java.io.File;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.*;
import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

/**
 * The following is a factory for obtaining a MapServiceMethod for a given 
 * requested path
 * @author Christopher Johnson
 */
@Component
public class ProviderFactory {
    @Autowired ApplicationContext context;
    private Collection<? extends Provider> providers;
    
    @PostConstruct public void init() {
        providers = context.getBeansOfType(Provider.class).values();
    }

    /**
     * Resolves a MapServiceMethod parameter from one of the providers in the 
     * providers package
     * @param method
     * @param request
     * @param toReturn
     * @param paramAnnotations
     * @return A instantiated Object from a provider in the provider class
     * @throws ProviderException 
     */
    Object getProvidedForParameter(MapServiceMethod method, HttpServletRequest request, Class<?> toReturn, List<Annotation> paramAnnotations) throws ProviderException {
        for(Provider currProvider : providers) {
            if(currProvider.isProviderFor(toReturn, method, request, paramAnnotations)) {
                return currProvider.provide(toReturn, method, request, paramAnnotations);
            }
        }
        return null; //can't find a matching parameter
    }
}
