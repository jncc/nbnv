/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.org.nbn.nbnv.api.rest.resources;

import java.io.IOException;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.codehaus.enunciate.jaxrs.ResponseCode;
import org.codehaus.enunciate.jaxrs.StatusCodes;
import org.codehaus.enunciate.jaxrs.TypeHint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import uk.org.nbn.nbnv.api.dao.core.OperationalOrganisationMapper;
import uk.org.nbn.nbnv.api.dao.warehouse.DatasetMapper;
import uk.org.nbn.nbnv.api.dao.warehouse.OrganisationMapper;
import uk.org.nbn.nbnv.api.model.Dataset;
import uk.org.nbn.nbnv.api.model.FriendlyResponse;
import uk.org.nbn.nbnv.api.model.Organisation;
import uk.org.nbn.nbnv.api.model.OrganisationMembership;
import uk.org.nbn.nbnv.api.model.User;
import uk.org.nbn.nbnv.api.model.meta.OpResult;
import uk.org.nbn.nbnv.api.rest.providers.annotations.TokenOrganisationAdminUser;
import uk.org.nbn.nbnv.api.rest.providers.annotations.TokenOrganisationUser;
import uk.org.nbn.nbnv.api.rest.providers.annotations.TokenUser;
import uk.org.nbn.nbnv.api.services.TaxonDatasetPlaceholderService;
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
    @Autowired TaxonDatasetPlaceholderService taxonDatasetPlaceholderService;

    /**
     * Get a list of all organisations from the data warehouse
     *
     * @return A list of all organisations from the data warehouse
     *
     * @response.representation.200.qname List<Organisation>
     * @response.representation.200.mediaType application/json
     */
    @GET
    @TypeHint(Organisation.class)
    @StatusCodes({
        @ResponseCode(code = 200, condition = "Successfully returned a list of all organisations")
    })
    @Produces(MediaType.APPLICATION_JSON)
    public List<Organisation> get() {
        return organisationMapper.selectAll();
    }
    
    /**
     * Get a list of all organisation from the data warehouse that have 
     * contributed at least 1 dataset
     * 
     * @return  A list of organisations that have at least 1 dataset
     * 
     * @response.representation.200.qname List<Organisation>
     * @response.representation.200.mediaType application/json* 
     */
    @GET
    @Path("/contributing")
    @TypeHint(Organisation.class)
    @StatusCodes({
        @ResponseCode(code = 200, condition = "Successfully returned a list of all contributing organisations")
    })
    @Produces(MediaType.APPLICATION_JSON)
    public List<Organisation> getContributingOrgs() {
        return organisationMapper.selectAllContributing();
    }

    /**
     * Search for organisations based on name and abbreviation
     * @param term Search term
     * @return List of organisations matching the search term
     * 
     * @response.representation.200.qname List<Organisation>
     * @response.representation.200.mediaType application/json
     */
    @GET
    @Path("/search")
    @TypeHint(Organisation.class)
    @StatusCodes({
        @ResponseCode(code = 200, condition = "Successfully returned a list of all organisations, matching the given search term")
    })
    @Produces(MediaType.APPLICATION_JSON)
    public List<Organisation> searchForOrgByPartial(@QueryParam("term") String term) {
       return oOrganisationMapper.searchForOrganisation("%" + term + "%");
    }
    
    /**
     * Return a list of organisations that the current user can join, so 
     * excludes any organisation of which the current user is already a member
     * or cannot join
     * 
     * @param user The current user (Injected Token no need to pass)
     * @return A list of organisations that the current user can join
     * 
     * @response.representation.200.qname List<Organisation>
     * @response.representation.200.mediaType application/json
     */
    @GET
    @Path("/joinable")
    @TypeHint(Organisation.class)
    @StatusCodes({
        @ResponseCode(code = 200, condition = "Successfully returned a list of all organisations that are joinable by the current user"),
        @ResponseCode(code = 403, condition = "The current user is not logged in")
    })    
    @Produces(MediaType.APPLICATION_JSON)
    public List<Organisation> getJoinableOrganisations(@TokenUser(allowPublic = false) User user) {
        return oOrganisationMapper.getJoinableOrganisations(user.getId());
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
    @TypeHint(Organisation.class)
    @StatusCodes({
        @ResponseCode(code = 200, condition = "Successfully returned the selected organisation")
    })  
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
     * organisation) (Injected Token no need to pass)
     * @param id An organisation ID
     *
     * @return A specified organisation from the core database
     *
     * @response.representation.200.qname Organisation
     * @response.representation.200.mediaType application/json
     */
    @GET
    @Path("/{id}/metadata")
    @TypeHint(Organisation.class)
    @StatusCodes({
        @ResponseCode(code = 200, condition = "Successfully returned the selected organisation"),
        @ResponseCode(code = 403, condition = "The current user is not and admin of the selected organisation")
    })    
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
     * (Injected Token no need to pass)
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
     * 
     * @return An OpResult detailing the success or failure of this operation
     * 
     * @response.representation.200.qname OpResult
     * @response.representation.200.mediaType application/json
     */
    @POST
    @Path("/{id}/metadata")
    @TypeHint(OpResult.class)
    @StatusCodes({
        @ResponseCode(code = 200, condition = "Successfully completed operation, may have failed check the OpResult for details"),
        @ResponseCode(code = 403, condition = "The current user is not and admin of the selected organisation")
    })   
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
            @FormParam("contactEmail") String contactEmail) {

        
        int response = oOrganisationMapper.updateOrganisationDetails(id, name,
                abbreviation, summary, address, postcode, phone, website,
                contactName, contactEmail);

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
    @TypeHint(Dataset.class)
    @StatusCodes({
        @ResponseCode(code = 200, condition = "Successfully returned a list of datasets owned by this organisation")
    })
    @Produces(MediaType.APPLICATION_JSON)
    public List<Dataset> getDatasetsByID(@PathParam("id") int id) {
        return datasetMapper.selectByOrganisationID(id);
    }
    
    /**
     * Allows for the creation of new taxon datasets given a Zip Archive which 
     * contains:
     *  - form.doc - A species metadata word document (http://www.nbn.org.uk/Share-Data/Providing-Data/Metadata-form-for-species-datasets.aspx)
     *  - additional.json - A json object which contains the information which 
     *      can not be read from a word document for technical reasons.
     * 
     * The additional.json requires three fields. This should look like
     *  {"resolution":"10km", "recorderNames": true, "recordAttributes": true}
     * 
     * Where:
     * - resolution is the geographic resolution which the general public 
     * will see records at. (i.e. one of 10km, 2km, 1km, 100m)
     * - recorderNames is a boolean indicating weather or not the recorder name(s)
     *  are made publicly available
     * - recorderAttributes is a boolean indicating weather or not the dataset 
     * attributes are made available 
     * 
     * @param user a provided user who is an administrator of the given organisation
     * @param id of an organisation stored on the NBN Gateway
     * @param request http request containing a zip archive
     * @return a new dataset key
     */
    @POST
    @Path("/{id}/datasets")
    @StatusCodes({
        @ResponseCode(code = 200, condition = "Successfully created a new dataset to import against"),
        @ResponseCode(code = 400, condition = "Failed to create the new dataset")
    })
    @Produces(MediaType.TEXT_PLAIN)
    @Consumes("application/zip")
    public Response storeNewDataset(@TokenOrganisationAdminUser(path = "id") User user, @PathParam("id") int id, @Context HttpServletRequest request) {
        try {
            String datasetKey = taxonDatasetPlaceholderService.storeMetadataWordDocument(id, request.getInputStream());
            return Response.ok().entity(datasetKey).build();
        }
        catch (IOException ex) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(new FriendlyResponse(false, ex.getMessage()))
                    .build();
        }
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
    @TypeHint(Dataset.class)
    @StatusCodes({
        @ResponseCode(code = 200, condition = "Successfully returned a list of datasets that this organisation contributes to")
    })
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
    @StatusCodes({
        @ResponseCode(code = 200, condition = "Successfully returned the logo for this organisation")
    })
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
    @StatusCodes({
        @ResponseCode(code = 200, condition = "Successfully returned the small version of the logo for this organisation")
    })    
    @Produces("image/png")
    public Object getLogoSmall(@PathParam("id") int id) {
        return organisationMapper.selectLogoSmallByOrganisationID(id);
    }
}