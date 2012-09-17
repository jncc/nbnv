/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.org.nbn.nbnv.api.rest.resources;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import uk.org.nbn.nbnv.api.rest.providers.annotations.TokenUser;
import uk.org.nbn.nbnv.api.dao.mappers.TaxonObservationMapper;
import uk.org.nbn.nbnv.api.model.TaxonObservation;
import uk.org.nbn.nbnv.api.model.User;

/**
 *
 * @author Administrator
 */
@Component
@Path("/taxonObservations")
public class TaxonObservationResource {

    @Autowired
    TaxonObservationMapper observationMapper;

    @GET
    @Path("/{id : \\d+}")
    @Produces(MediaType.APPLICATION_JSON)
    public TaxonObservation getObservation(@TokenUser() User user, @PathParam("id") int id) {
        return observationMapper.selectById(id, user.getId());
    }

    @GET
    @Path("/{id : [A-Z][A-Z0-9]{7}}")
    @Produces(MediaType.APPLICATION_JSON)
    public List<TaxonObservation> getObservationsByDataset(@TokenUser() User user, @PathParam("id") String id) {
        return observationMapper.selectByDataset(id, user.getId());
    }

    @GET
    @Path("/{id : [A-Z]{3}SYS[0-9]{10}}")
    @Produces(MediaType.APPLICATION_JSON)
    public List<TaxonObservation> getObservationsByTaxon(@TokenUser() User user, @PathParam("id") String id) {
        return observationMapper.selectByPTVK(id, user.getId());
    }

    /*
     * Needs InjectorProvider to work
     
     @GET
     public List<TaxonObservation> getObservationsByFilter(@TokenUser() User user, TaxonObservationFilter filter) {
     return observationMapper.selectByFilter(user.getId(), filter.getStartYear(), filter.getEndYear());
     }
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<TaxonObservation> getObservationsByFilter(
            @TokenUser() User user
            , @QueryParam("startYear") @DefaultValue("-1") int startYear
            , @QueryParam("endYear") @DefaultValue("-1") int endYear
            , @QueryParam("datasetKey") @DefaultValue("") String datasetKey
            , @QueryParam("ptvk") @DefaultValue("") String ptvk
            , @QueryParam("overlapSite") @DefaultValue("-1") Integer overlaps
            , @QueryParam("withinSite") @DefaultValue("-1") Integer within
            , @QueryParam("sensitive") @DefaultValue("1") Boolean sensitive
            , @QueryParam("designation") @DefaultValue("") String designation) {
        //TODO: TaxonGroup, designation, squareBlurring(?)
        List<String> datasets = null;
        List<String> taxa = null;

        if (!"".equalsIgnoreCase(datasetKey)) {
            datasets = Arrays.asList(datasetKey.split(","));
        }

        if (!"".equalsIgnoreCase(ptvk)) {
            taxa = Arrays.asList(ptvk.split(","));
        }

        return observationMapper.selectByFilter(user.getId(), startYear, endYear, datasets, taxa, overlaps, within, sensitive, designation);
    }
}
