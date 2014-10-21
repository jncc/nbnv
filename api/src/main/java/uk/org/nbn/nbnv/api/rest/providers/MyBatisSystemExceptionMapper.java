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
import org.springframework.stereotype.Repository;

/**
 *
 * @author Matt Debont
 */
@Provider
@Repository
public class MyBatisSystemExceptionMapper implements ExceptionMapper<MyBatisSystemException> {

    @Override
    public Response toResponse(MyBatisSystemException e) {
        Map<String, Object> toReturn = new HashMap<String, Object>();
        toReturn.put("success", false);
        toReturn.put("status", e.getMessage());
        
        return Response.status(Response.Status.BAD_REQUEST)
            .type(MediaType.APPLICATION_JSON)
            .entity(toReturn)
            .build();
    }
}