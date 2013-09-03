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
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import uk.org.nbn.nbnv.api.dao.warehouse.OrganisationSuppliedListMapper;
import uk.org.nbn.nbnv.api.dao.warehouse.TaxonMapper;
import uk.org.nbn.nbnv.api.dao.warehouse.TaxonNavigationGroupMapper;
import uk.org.nbn.nbnv.api.model.OrganisationSuppliedList;
import uk.org.nbn.nbnv.api.model.Taxon;
import uk.org.nbn.nbnv.api.model.TaxonNavigationGroup;

/**
 *
 * @author Matt Debont
 */
@Component
@Path("/organisationList")
public class OrganisationSuppliedListResource extends AbstractResource {
    
    @Autowired OrganisationSuppliedListMapper organisationSuppliedListMapper;
    @Autowired TaxonNavigationGroupMapper taxonNavigationGroupMapper;
    @Autowired TaxonMapper taxonMapper;
    
    /**
     * Returns information about all current organisation lists
     * 
     * @return A list of organisation supplied taxon list
     * 
     * @response.representation.200.qname List<OrganisationSuppliedList>
     * @response.representation.200.mediaType application/json
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<OrganisationSuppliedList> getAllOrgSuppliedLists() {
        return organisationSuppliedListMapper.selectAll();
    }
    
    /**
     * Returns ones Organisation Supplied Taxon List by a given ID
     * 
     * @param id The id of the organisation list 
     * @return The specified organisation list
     * 
     * @response.representation.200.qname OrganisationSuppliedList
     * @response.representation.200.mediaType application/json
     */
    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public OrganisationSuppliedList getOrgSuppliedListByID(@PathParam("id") int id) {
        return organisationSuppliedListMapper.selectByID(id);
    }
      
    /**
     * Returns ones Organisation Supplied Taxon List by a given code / 
     * abbreviation
     * 
     * @param code The code (abbreviation) of the organisation list 
     * @return The specified organisation list
     * 
     * @response.representation.200.qname OrganisationSuppliedList
     * @response.representation.200.mediaType application/json
     */
    @GET
    @Path("/code/{code}")
    @Produces(MediaType.APPLICATION_JSON)
    public OrganisationSuppliedList getOrgSuppliedListByCode(@PathParam("code") String code) {
        return organisationSuppliedListMapper.selectByCode(code);
    }
    
    /**
     * Returns a Taxon Navigation Group specified by an organisation list and 
     * taxon navigation group id
     * 
     * @param id A Organisation Supplied List ID
     * @param taxonNavigationGroupId A Taxon Navigation Group ID
     * 
     * @return A Taxon Navigation Group specified by a organisation list and 
     * taxon navigation group ID
     * 
     * @response.representation.200.qname TaxonNavigationGroup
     * @response.representation.200.mediaType application/json
     */
    @GET
    @Path("{id}/taxonNavigationGroups/{taxonNavigationGroupId}")
    @Produces(MediaType.APPLICATION_JSON)
    public TaxonNavigationGroup getTaxonNavigationGroupByOrganisationList(@PathParam("id") int id, @PathParam("taxonNavigationGroupId") String taxonNavigationGroupId){
        TaxonNavigationGroup toReturn = taxonNavigationGroupMapper.getTaxonNavigationGroup(taxonNavigationGroupId);
        toReturn.setChildren(taxonNavigationGroupMapper.getChildrenByOrganisationList(taxonNavigationGroupId, id));
        return toReturn;
    }
    
    /**
     * Returns a list of Taxons specified by an organisation taxon list and a 
     * taxon navigation group (or just by a designation id if no taxon 
     * navigation group id is supplied)
     * 
     * @param id An Organisation List ID
     * @param taxonNavigationGroupId A Taxon Navigation Group ID (Optional) 
     * supplied via querystring
     * 
     * @return A list of Taxons specified by designation and a taxon navigation
     * group (or just by an organisation taxon list if no taxon navigation group 
     * id is supplied)
     * 
     * @response.representation.200.qname List<Taxon>
     * @response.representation.200.mediaType application/json
     */
    @GET
    @Path("/{id}/species")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Taxon> getSpeciesByDesignationAndTaxonNavigationGroup(@PathParam("id") int id, @QueryParam("taxonNavigationGroupId") String taxonNavigationGroupId) {
        if(taxonNavigationGroupId != null){
            return taxonMapper.selectByOrganisationListIDAndTaxonNavigationGroup(id, taxonNavigationGroupId);
        }else{
            return taxonMapper.selectByOrganisationListID(id);
        }
    }
}
