/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.org.nbn.nbnv.api.rest.providers;

import java.util.HashMap;
import java.util.Map;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import org.mybatis.spring.MyBatisSystemException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.org.nbn.nbnv.api.rest.resources.utils.throwables.CorelessException;

/**
 *
 * @author Matt Debont
 */
@Provider
public class MyBatisSystemExceptionMapper implements ExceptionMapper<MyBatisSystemException> {
    private static final Logger logger = LoggerFactory.getLogger(MyBatisSystemExceptionMapper.class);

    @Override
    public Response toResponse(MyBatisSystemException e) {
        Map<String, Object> toReturn = new HashMap<String, Object>();
        toReturn.put("success", false);
        
        if (e.getRootCause() instanceof CorelessException) {
            toReturn.put("status", "Core Database is offline, please try again later");
            
            return Response.status(Response.Status.SERVICE_UNAVAILABLE)
                    .type(MediaType.APPLICATION_JSON)
                    .entity(toReturn)
                    .build();
        }
        
        toReturn.put("status", e.getMessage());
        
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
            .type(MediaType.APPLICATION_JSON)
            .entity(toReturn)
            .build();
    }
}