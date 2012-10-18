/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.org.nbn.nbnv.api.rest.resources;

import com.sun.jersey.core.util.Base64;
import java.io.ByteArrayInputStream;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import uk.org.nbn.nbnv.api.dao.mappers.DatasetMapper;
import uk.org.nbn.nbnv.api.dao.mappers.OrganisationMapper;
import uk.org.nbn.nbnv.api.model.Dataset;
import uk.org.nbn.nbnv.api.model.Organisation;

/**
 *
 * @author Paul Gilbertson
 */
@Component
@Path("/organisations")
public class OrganisationResource {
    @Autowired OrganisationMapper organisationMapper;
    @Autowired DatasetMapper datasetMapper;
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Organisation> get() {
        return organisationMapper.selectAll();
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Organisation getByID(@PathParam("id") int id) {
        return organisationMapper.selectByID(id);
    }

    @GET
    @Path("/{id}/datasets")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Dataset> getDatasetsByID(@PathParam("id") int id) {
        return datasetMapper.selectByOrganisationID(id);
    }

    @GET
    @Path("/{id}/logo")    
    @Produces("image/jpg")
    public Object getLogo(@PathParam("id") int id) {
        return organisationMapper.selectLogoByOrganisationID(id);
    }

    @GET
    @Path("/{id}/logosmall")    
    @Produces("image/png")
    public Object getLogoSmall(@PathParam("id") int id) {
        return organisationMapper.selectLogoSmallByOrganisationID(id);
    }
}
