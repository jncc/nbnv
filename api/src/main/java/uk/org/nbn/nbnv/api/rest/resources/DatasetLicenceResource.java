/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
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
import uk.org.nbn.nbnv.api.dao.warehouse.DatasetLicenceMapper;
import uk.org.nbn.nbnv.api.model.DatasetLicence;

/**
 *
 * @author Matt Debont
 */
@Component
@Path("/datasetLicence")
public class DatasetLicenceResource extends AbstractResource {
    
    @Autowired DatasetLicenceMapper datasetLicenceMapper;
    
    @GET
    @TypeHint(DatasetLicence.class)
    @StatusCodes({
        @ResponseCode(code = 200, condition = "Successfully returned a list of all dataset licences")
    })
    @Produces(MediaType.APPLICATION_JSON)
    public List<DatasetLicence> get() {
        return datasetLicenceMapper.selectAllDatasetLicences();
    }    

    @GET
    @Path("/{id}")
    @TypeHint(DatasetLicence.class)
    @StatusCodes({
        @ResponseCode(code = 200, condition = "Successfully returned the requested dataset licence")
    })
    @Produces(MediaType.APPLICATION_JSON)
    public DatasetLicence get(@PathParam("id") int id) {
        return datasetLicenceMapper.getDatasetLicenceByID(id);
    }        

    @GET
    @Path("/{abbrv}")
    @TypeHint(DatasetLicence.class)
    @StatusCodes({
        @ResponseCode(code = 200, condition = "Successfully returned the requested dataset licence")
    })
    @Produces(MediaType.APPLICATION_JSON)
    public DatasetLicence get(@PathParam("abbrv") String abbrv) {
        return datasetLicenceMapper.getDatasetLicenceByAbbrv(abbrv);
    }
    
    @GET
    @Path("/{id}/img")
    @StatusCodes({
        @ResponseCode(code = 200, condition = "Successfully returned the image for this dataset licence")
    })
    @Produces("image/jpg")
    public Object getLogo(@PathParam("id") int id) {
        return datasetLicenceMapper.selectImgByDatasetLicenceDataID(id);
    }    
}
