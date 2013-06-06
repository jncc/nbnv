package uk.org.nbn.nbnv.api.rest.resources;

import java.util.List;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import org.apache.solr.client.solrj.SolrServerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import uk.org.nbn.nbnv.api.dao.warehouse.TaxonNavigationGroupMapper;
import uk.org.nbn.nbnv.api.model.TaxonNavigationGroup;
import uk.org.nbn.nbnv.api.solr.Solr;
import uk.org.nbn.nbnv.api.solr.SolrResponse;

@Component
@Path("/taxonNavigationGroups")
public class TaxonNavigationGroupResource extends AbstractResource {

    @Autowired TaxonNavigationGroupMapper mapper;
    @Autowired Solr solr;
    
    /**
     * Return a list of all Taxon Navigation Groups from the data warehouse
     * 
     * @response.representation.200.qname List<TaxonNavigationGroup>
     * @response.representation.200.mediaType application/json
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<TaxonNavigationGroup> getTaxonNavigationGroups() {
        return mapper.selectAll();
    }

    /**
     * Return a specific Taxon Navigation Group from the data warehouse
     * 
     * @param id A Taxon Navigation Group ID
     * 
     * @return A specific Taxon Navigation Group from the data warehouse
     * 
     * @response.representation.200.qname TaxonNavigationGroup
     * @response.representation.200.mediaType application/json
     */
    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public TaxonNavigationGroup getTaxonNavigationGroup(@PathParam("id") String id) {
        return mapper.getTaxonNavigationGroup(id);
    }

    /**
     * Return the Top Levels of Taxon Navigation Groups, optionally associated
     * with a Designation TODO: Do we need this? Have another endpoint which does
     * this below
     * 
     * @param designationId A Designation ID (Optional)
     * 
     * @return The Top Levels of Taxon Navigation Groups
     *  
     * @response.representation.200.qname List<TaxonNavigationGroup>
     * @response.representation.200.mediaType application/json
     */
    @GET
    @Path("/topLevels")
    @Produces(MediaType.APPLICATION_JSON)
    public List<TaxonNavigationGroup> getTopLevelTaxonNavigationGroups(@QueryParam("designationId") String designationId) {
        if (designationId != null) {
            return mapper.getTopLevelsByDesignationID(designationId);
        } else {
            return mapper.getTopLevels();
        }
    }

    /**
     * Return the Top Levels of Taxon Navigation Groups associated with a 
     * Designation
     * 
     * @param id A Designation ID
     * 
     * @return The Top Levels of Taxon Navigation Groups associated with a 
     * Designation
     * 
     * @response.representation.200.qname List<TaxonNavigationGroup>
     * @response.representation.200.mediaType application/json
     */
    @GET
    @Path("/topLevels/designations/{designationId}")
    @Produces(MediaType.APPLICATION_JSON)
    public List<TaxonNavigationGroup> getTopLevelTaxonNavigationGroupsByDesignation(@PathParam("designationId") String id) {
        return mapper.getTopLevelsByDesignationID(id);
    }

    /**
     * Return a solr response returning Taxa within a Taxon Navigation Group
     * 
     * @param taxonGroup A Taxon Group ID
     * @param rows The number of rows
     * @param start The page to start at
     * 
     * @returns A SOLR response to this search
     * 
     * @throws SolrServerException 
     * 
     * @response.representation.200.qname SolrResponse
     * @response.representation.200.mediaType application/json
     */
    @GET
    @Path("/{id}/species")
    @Produces(MediaType.APPLICATION_JSON)
    public SolrResponse getTaxa(
            @PathParam("id") String taxonGroup,
            @QueryParam("rows") @DefaultValue("20") int rows,
            @QueryParam("start") @DefaultValue("0") int start) throws SolrServerException {
        
        return solr
                .create()
                .query("navigationGroupKey:" + taxonGroup)
                .rows(rows)
                .start(start)
                .response();
    }
    
}
