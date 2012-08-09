package uk.org.nbn.nbnv.api.rest.resources;

import java.util.List;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import uk.org.nbn.nbnv.api.dao.mappers.SiteBoundaryDatasetMapper;
import uk.org.nbn.nbnv.api.dao.mappers.SiteBoundaryMapper;
import uk.org.nbn.nbnv.api.model.SiteBoundary;
import uk.org.nbn.nbnv.api.model.SiteBoundaryDataset;

@Component
@Path("/siteBoundaryDatasets")
public class SiteBoundaryDatasetResource {
    
    @Autowired SiteBoundaryDatasetMapper SiteBoundaryDatasetMapper;
    @Autowired SiteBoundaryMapper siteBoundaryMapper;
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<SiteBoundaryDataset> get(){
        return SiteBoundaryDatasetMapper.get();
    }
    
    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public SiteBoundaryDataset getSiteBoundaryDataset(@PathParam("id") String id){
        return SiteBoundaryDatasetMapper.getByDatasetKey(id);
    }
    
    @GET
    @Path("/{id}/siteBoundaries")
    @Produces(MediaType.APPLICATION_JSON)
    public List<SiteBoundary> getSiteBoundariesByDataset(@PathParam("id") String id){
        return siteBoundaryMapper.getByDatasetKey(id);
    }
    
}
