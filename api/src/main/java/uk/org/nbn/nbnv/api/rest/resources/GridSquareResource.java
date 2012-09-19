package uk.org.nbn.nbnv.api.rest.resources;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import uk.org.nbn.nbnv.api.dao.mappers.GridSquareMapper;
import uk.org.nbn.nbnv.api.model.GridSquare;

/**
 * A jersey resource which provides bounding boxes for certain features on the 
 * gateway
 * @author Christopher Johnson
 */
@Component
@Path("/gridSquares")
public class GridSquareResource {
    @Autowired GridSquareMapper gridSquareMapper;
    
    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public GridSquare getGridSquare(@PathParam("id") String gridSquare){
        return gridSquareMapper.getGridSquare(gridSquare);
    }
}
