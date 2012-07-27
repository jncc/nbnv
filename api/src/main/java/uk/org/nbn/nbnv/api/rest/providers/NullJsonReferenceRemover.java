/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.org.nbn.nbnv.api.rest.providers;

import javax.ws.rs.ext.ContextResolver;
import javax.ws.rs.ext.Provider;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.annotate.JsonSerialize;

/**
 *
 * @author Administrator
 */
@Provider 
public class NullJsonReferenceRemover implements ContextResolver<ObjectMapper>{ 

     @Override 
     public ObjectMapper getContext(Class<?> type) { 
         final ObjectMapper objecMapper = new ObjectMapper(); 
         objecMapper.setSerializationInclusion(JsonSerialize.Inclusion.NON_NULL);
         return objecMapper; 
     } 
} 