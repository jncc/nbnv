/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.org.nbn.nbnv.api.rest.resources;

import java.util.List;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import uk.org.nbn.nbnv.api.dao.core.OperationalOrganisationJoinRequestMapper;
import uk.org.nbn.nbnv.api.dao.core.OperationalOrganisationMembershipMapper;
import uk.org.nbn.nbnv.api.dao.warehouse.DatasetMapper;
import uk.org.nbn.nbnv.api.dao.warehouse.OrganisationMapper;
import uk.org.nbn.nbnv.api.dao.warehouse.OrganisationMembershipMapper;
import uk.org.nbn.nbnv.api.model.Dataset;
import uk.org.nbn.nbnv.api.model.Organisation;
import uk.org.nbn.nbnv.api.solr.SolrResolver;

/**
 *
 * @author Paul Gilbertson
 */
@Component
@Path("/organisations")
public class OrganisationResource extends AbstractResource {
    @Autowired OrganisationMapper organisationMapper;
    @Autowired DatasetMapper datasetMapper;
    
    /**
     * Get a list of all organisations from the data warehouse
     * 
     * @return A list of all organisations from the data warehouse
     * 
     * @response.representation.200.qname List<Organisation>
     * @response.representation.200.mediaType application/json
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Organisation> get() {
        return organisationMapper.selectAll();
    }

    /**
     * Return a specified organisation
     * 
     * @param id An organisation ID
     * 
     * @return A specified organisation
     * 
     * @response.representation.200.qname List<Organisation>
     * @response.representation.200.mediaType application/json
     */
    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @SolrResolver("ORGANISATION")
    public Organisation getByID(@PathParam("id") int id) {
        return organisationMapper.selectByID(id);
    }

    /**
     * Return a list of all datasets from a particular organisation
     * 
     * @param id An organisation ID
     * 
     * @return A list of all datasets from a particular organisation
     * 
     * @response.representation.200.qname List<Dataset>
     * @response.representation.200.mediaType application/json
     */
    @GET
    @Path("/{id}/datasets")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Dataset> getDatasetsByID(@PathParam("id") int id) {
        return datasetMapper.selectByOrganisationID(id);
    }

    /**
     * Return a list of all datasets that a specified organisation has 
     * contributed to
     * 
     * @param id An organisation ID
     * 
     * @return a list of all datasets that a specified organisation has 
     * contributed to
     * 
     * @response.representation.200.qname List<Dataset>
     * @response.representation.200.mediaType application/json
     */
    @GET
    @Path("/{id}/contributedDatasets")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Dataset> getContributedDatasetsByID(@PathParam("id") int id) {
        return datasetMapper.selectContributedByOrganisationID(id);
    }

    /**
     * Return the full sized logo of a specified organisation
     * 
     * @param id An organisation ID
     * 
     * @return The full sized logo of a specified organisation
     * 
     * @response.representation.200.qname Object
     * @response.representation.200.mediaType application/json
     */
    @GET
    @Path("/{id}/logo")    
    @Produces("image/jpg")
    public Object getLogo(@PathParam("id") int id) {
        return organisationMapper.selectLogoByOrganisationID(id);
    }

    /**
     * Return the small logo for a specified organisation
     * 
     * @param id An organisation ID
     * 
     * @return The small logo for a specified organisation
     * 
     * @response.representation.200.qname Object
     * @response.representation.200.mediaType application/json
     */
    @GET
    @Path("/{id}/logosmall")    
    @Produces("image/png")
    public Object getLogoSmall(@PathParam("id") int id) {
        return organisationMapper.selectLogoSmallByOrganisationID(id);
    }
}