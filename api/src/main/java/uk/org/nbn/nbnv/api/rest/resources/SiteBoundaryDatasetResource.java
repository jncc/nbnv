package uk.org.nbn.nbnv.api.rest.resources;

import java.util.List;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import org.codehaus.enunciate.jaxrs.ResponseCode;
import org.codehaus.enunciate.jaxrs.StatusCodes;
import org.codehaus.enunciate.jaxrs.TypeHint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import uk.org.nbn.nbnv.api.dao.warehouse.SiteBoundaryDatasetMapper;
import uk.org.nbn.nbnv.api.dao.warehouse.SiteBoundaryMapper;
import uk.org.nbn.nbnv.api.model.SiteBoundary;
import uk.org.nbn.nbnv.api.model.SiteBoundaryDataset;

@Component
@Path("/siteBoundaryDatasets")
public class SiteBoundaryDatasetResource extends AbstractResource {
    
    @Autowired SiteBoundaryDatasetMapper SiteBoundaryDatasetMapper;
    @Autowired SiteBoundaryMapper siteBoundaryMapper;
    
    /**
     * Return a list of all Site Boundary Datasets from the data warehouse
     * 
     * @return A list of all Site Boundary Datasets from the data warehouse
     * 
     * @response.representation.200.qname SiteBoundaryCategory
     * @response.representation.200.mediaType application/json
     */
    @GET
    @TypeHint(SiteBoundaryDataset.class)
    @StatusCodes({
        @ResponseCode(code = 200, condition = "Successfully returned list of all site boundary datasets")
    })
    @Produces(MediaType.APPLICATION_JSON)
    public List<SiteBoundaryDataset> get(){
        return SiteBoundaryDatasetMapper.get();
    }
    
    /**
     * Returns a specific Site Boundary Dataset from the data warehouse
     * 
     * @param id A Site Boundary Dataset ID
     * 
     * @return A specific Site Boundary Dataset from the data warehouse
     * 
     * @response.representation.200.qname SiteBoundaryCategory
     * @response.representation.200.mediaType application/json
     */
    @GET
    @Path("/{id}")
    @TypeHint(SiteBoundaryDataset.class)
    @StatusCodes({
        @ResponseCode(code = 200, condition = "Successfully returned the requested site boundary dataset")
    })  
    @Produces(MediaType.APPLICATION_JSON)
    public SiteBoundaryDataset getSiteBoundaryDataset(@PathParam("id") String id){
        return SiteBoundaryDatasetMapper.getByDatasetKey(id);
    }
    
    /**
     * Return a list of Site Boundaries associated with a specified Dataset
     * 
     * @param id A dataset Key
     * 
     * @return A list of Site Boundaries associated with a specified Dataset
     * 
     * @response.representation.200.qname List<SiteBoundary>
     * @response.representation.200.mediaType application/json
     */
    @GET
    @Path("/{id}/siteBoundaries")
    @TypeHint(SiteBoundary.class)
    @StatusCodes({
        @ResponseCode(code = 200, condition = "Successfully returned list of all site boundaries in this category")
    })    
    @Produces(MediaType.APPLICATION_JSON)
    public List<SiteBoundary> getSiteBoundariesByDataset(@PathParam("id") String id){
        return siteBoundaryMapper.getByDatasetKey(id);
    }
    
}