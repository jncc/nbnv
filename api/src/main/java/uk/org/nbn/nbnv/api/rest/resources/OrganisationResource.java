/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.org.nbn.nbnv.api.rest.resources;

import java.util.List;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import uk.org.nbn.nbnv.api.dao.core.OperationalOrganisationMapper;
import uk.org.nbn.nbnv.api.dao.warehouse.DatasetMapper;
import uk.org.nbn.nbnv.api.dao.warehouse.OrganisationMapper;
import uk.org.nbn.nbnv.api.model.Dataset;
import uk.org.nbn.nbnv.api.model.Organisation;
import uk.org.nbn.nbnv.api.model.OrganisationMembership;
import uk.org.nbn.nbnv.api.model.User;
import uk.org.nbn.nbnv.api.model.meta.OpResult;
import uk.org.nbn.nbnv.api.rest.providers.annotations.TokenOrganisationUser;
import uk.org.nbn.nbnv.api.solr.SolrResolver;

/**
 *
 * @author Paul Gilbertson
 */
@Component
@Path("/organisations")
public class OrganisationResource extends AbstractResource {

    @Autowired OrganisationMapper organisationMapper;
    @Autowired OperationalOrganisationMapper oOrganisationMapper;
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
     * @response.representation.200.qname Organisation
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
     * Return a specified organisation from the core database, if the user is a
     * administrator of the specified organisation or return a 403 Forbidden
     * error
     *
     * @param user The current user (Must be organisation admin of this
     * organisation)
     * @param id An organisation ID
     *
     * @return A specified organisation from the core database
     *
     * @response.representation.200.qname Organisation
     * @response.representation.200.mediaType application/json
     */
    @GET
    @Path("/{id}/metadata")
    @Produces(MediaType.APPLICATION_JSON)
    public Organisation getFullMetadata(
            @TokenOrganisationUser(path = "id", roles = OrganisationMembership.Role.administrator) User user,
            @PathParam("id") int id) {
        return oOrganisationMapper.selectByID(id);
    }

    /**
     * Update the details of an organisation, if the user is an admin of the 
     * specified organisation, otherwise return a 403 Forbidden error
     * 
     * @param user The current user (Must be an admin of this organisation)
     * @param id The id of an organisation
     * @param name The name of the organisation
     * @param abbreviation The abbreviation of the organisation
     * @param summary A summary about the organisation
     * @param address An address for the organisation
     * @param postcode A postcode for the organisation
     * @param phone A contact phone number for the organisation
     * @param website The organisations website address
     * @param contactName A contact for this organisation for the NBN
     * @param contactEmail A contact email for this organisation for the NBN
     * @param allowPublicRegistration Whether this organisation allows public
     * registration or not (is passed as the string 'on' due to jQuery 
     * serialisation)
     * 
     * @return An OpResult detailing the success or failure of this operation
     */
    @POST
    @Path("/{id}/metadata")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.APPLICATION_JSON)
    public OpResult updateOrganistionMetadata(
            @TokenOrganisationUser(path = "id", roles = OrganisationMembership.Role.administrator) User user,
            @PathParam("id") int id,
            @FormParam("name") String name,
            @FormParam("abbreviation") String abbreviation,
            @FormParam("summary") String summary,
            @FormParam("address") String address,
            @FormParam("postcode") String postcode,
            @FormParam("phone") String phone,
            @FormParam("website") String website,
            @FormParam("contactName") String contactName,
            @FormParam("contactEmail") String contactEmail,
            @FormParam("allowPublicRegistration") String allowPublicRegistration) {

        
        int response = oOrganisationMapper.updateOrganisationDetails(id, name,
                abbreviation, summary, address, postcode, phone, website,
                contactName, contactEmail, 
                (allowPublicRegistration != null && allowPublicRegistration.equals("on"))? 1 : 0);

        if (response == 1) {
            return new OpResult();
        } else if (response == 0) {
            return new OpResult("No matching organisation found!");
        } else {
            return new OpResult("Unknown issue");
        }
    }
    
//    @POST
//    @Path("/{id}/logo")
//    @Consumes(MediaType.MULTIPART_FORM_DATA)
//    @Produces(MediaType.APPLICATION_JSON)
//    public OpResult updateOrganisationLog(
//            @TokenOrganisationUser(path = "id", roles = OrganisationMembership.Role.administrator) User user, 
//            @PathParam("id") int id) {
//        int response = oOrganisationMapper.updateOrganisationLogo(null, null);
//        
//        if (response == 1) {
//            return new OpResult();
//        } else if (response == 0) {
//            return new OpResult("No matching organisation found!");
//        } else {
//            return new OpResult("Unknown issue");
//        }
//    }

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