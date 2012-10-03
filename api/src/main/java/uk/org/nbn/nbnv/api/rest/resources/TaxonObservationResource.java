/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.org.nbn.nbnv.api.rest.resources;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
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
import uk.org.nbn.nbnv.api.dao.mappers.DatasetMapper;
import uk.org.nbn.nbnv.api.dao.mappers.OrganisationMapper;
import uk.org.nbn.nbnv.api.rest.providers.annotations.TokenUser;
import uk.org.nbn.nbnv.api.dao.mappers.TaxonObservationMapper;
import uk.org.nbn.nbnv.api.model.*;

/**
 *
 * @author Administrator
 */
@Component
@Path("/taxonObservations")
public class TaxonObservationResource {

    @Autowired
    TaxonObservationMapper observationMapper;
    @Autowired
    OrganisationMapper organisationMapper;
    @Autowired
    DatasetMapper datasetMapper;

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
     *
     * @GET public List<TaxonObservation> getObservationsByFilter(@TokenUser()
     * User user, TaxonObservationFilter filter) { return
     * observationMapper.selectObservationRecordsByFilter(user.getId(), filter.getStartYear(),
     * filter.getEndYear()); }
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<TaxonObservation> getObservationsByFilter(
            @TokenUser() User user, @QueryParam("startYear") @DefaultValue("-1") int startYear, 
            @QueryParam("endYear") @DefaultValue("-1") int endYear, 
            @QueryParam("datasetKey") @DefaultValue("") List<String> datasetKeys, 
            @QueryParam("ptvk") @DefaultValue("") List<String> taxa, 
            @QueryParam("overlapSite") @DefaultValue("-1") Integer overlaps, 
            @QueryParam("withinSite") @DefaultValue("-1") Integer within, 
            @QueryParam("sensitive") @DefaultValue("1") Boolean sensitive, 
            @QueryParam("designation") @DefaultValue("") String designation, 
            @QueryParam("taxonOutputGroup") @DefaultValue("") String taxonOutputGroup, 
            @QueryParam("gridRef") @DefaultValue("") String gridRef) {
        //TODO: squareBlurring(?)
        return observationMapper.selectObservationRecordsByFilter(user, startYear, endYear, datasetKeys, taxa, overlaps, within, sensitive, designation, taxonOutputGroup, gridRef);
    }

    @GET
    @Path("/species")
    @Produces(MediaType.APPLICATION_JSON)
    public List<TaxonWithQueryStats> getObservationSpeciesByFilter(
            @TokenUser() User user, 
            @QueryParam("startYear") @DefaultValue("-1") int startYear, 
            @QueryParam("endYear") @DefaultValue("-1") int endYear, 
            @QueryParam("datasetKey") List<String> datasetKeys, 
            @QueryParam("ptvk") List<String> taxa, 
            @QueryParam("overlapSite") @DefaultValue("-1") Integer overlaps, 
            @QueryParam("withinSite") @DefaultValue("-1") Integer within, 
            @QueryParam("sensitive") @DefaultValue("1") Boolean sensitive, 
            @QueryParam("designation") @DefaultValue("") String designation, 
            @QueryParam("taxonOutputGroup") @DefaultValue("") String taxonOutputGroup, 
            @QueryParam("gridRef") @DefaultValue("") String gridRef) {
        //TODO: squareBlurring(?)
        List<TaxonWithQueryStats> toReturn = observationMapper.selectObservationSpeciesByFilter(user, startYear, endYear, datasetKeys, taxa, overlaps, within, sensitive, designation, taxonOutputGroup, gridRef);
        Collections.sort(toReturn);
        return toReturn;
    }

    @GET
    @Path("/groups")
    @Produces(MediaType.APPLICATION_JSON)
    public List<TaxonOutputGroupWithQueryStats> getObservationGroupsByFilter(
            @TokenUser() User user, 
            @QueryParam("startYear") @DefaultValue("-1") int startYear, 
            @QueryParam("endYear") @DefaultValue("-1") int endYear, 
            @QueryParam("datasetKey") List<String> datasetKeys, 
            @QueryParam("ptvk") List<String> taxa, 
            @QueryParam("overlapSite") @DefaultValue("-1") Integer overlaps, 
            @QueryParam("withinSite") @DefaultValue("-1") Integer within, 
            @QueryParam("sensitive") @DefaultValue("1") Boolean sensitive, 
            @QueryParam("designation") @DefaultValue("") String designation, 
            @QueryParam("taxonOutputGroup") @DefaultValue("") String taxonOutputGroup, 
            @QueryParam("gridRef") @DefaultValue("") String gridRef) {
        //TODO: squareBlurring(?)
        return observationMapper.selectObservationGroupsByFilter(user, startYear, endYear, datasetKeys, taxa, overlaps, within, sensitive, designation, taxonOutputGroup, gridRef);
    }

    @GET
    @Path("/datasets")
    @Produces(MediaType.APPLICATION_JSON)
    public List<DatasetWithQueryStats> getObservationDatasetsByFilter(
            @TokenUser() User user, @QueryParam("startYear") @DefaultValue("-1") int startYear, 
            @QueryParam("endYear") @DefaultValue("-1") int endYear, 
            @QueryParam("datasetKey") @DefaultValue("") List<String> datasetKeys, 
            @QueryParam("ptvk") @DefaultValue("") List<String> taxa, 
            @QueryParam("overlapSite") @DefaultValue("-1") Integer overlaps, 
            @QueryParam("withinSite") @DefaultValue("-1") Integer within, 
            @QueryParam("sensitive") @DefaultValue("1") Boolean sensitive, 
            @QueryParam("designation") @DefaultValue("") String designation, 
            @QueryParam("taxonOutputGroup") @DefaultValue("") String taxonOutputGroup, 
            @QueryParam("gridRef") @DefaultValue("") String gridRef) {
        //TODO: squareBlurring(?)
        return observationMapper.selectObservationDatasetsByFilter(user, startYear, endYear, datasetKeys, taxa, overlaps, within, sensitive, designation, taxonOutputGroup, gridRef);
    }

    @GET
    @Path("/datasets/observations")
    @Produces(MediaType.APPLICATION_JSON)
    public List<TaxonDataset> getDatasetsWithObservationsByFilter(
            @TokenUser() User user, @QueryParam("startYear") @DefaultValue("-1") int startYear, 
            @QueryParam("endYear") @DefaultValue("-1") int endYear, 
            @QueryParam("datasetKey") @DefaultValue("") List<String> datasetKeys, 
            @QueryParam("ptvk") @DefaultValue("") List<String> taxa, 
            @QueryParam("overlapSite") @DefaultValue("-1") Integer overlaps, 
            @QueryParam("withinSite") @DefaultValue("-1") Integer within, 
            @QueryParam("sensitive") @DefaultValue("1") Boolean sensitive, 
            @QueryParam("designation") @DefaultValue("") String designation, 
            @QueryParam("taxonOutputGroup") @DefaultValue("") String taxonOutputGroup, 
            @QueryParam("gridRef") @DefaultValue("") String gridRef) {
        //TODO: squareBlurring(?)
        List<TaxonObservation> taxonObservationsOrderedByDataset = observationMapper.selectObservationsByFilterOrderedByDataset(user, startYear, endYear, datasetKeys, taxa, overlaps, within, sensitive, designation, taxonOutputGroup, gridRef);
        return getDatasetsWithObservations(taxonObservationsOrderedByDataset);
//        return taxonObservationsOrderedByDataset;
    }

    @GET
    @Path("/providers")
    @Produces(MediaType.APPLICATION_JSON)
    public List<ProviderWithQueryStats> getObservationProvidersByFilter(
            @TokenUser() User user, @QueryParam("startYear") @DefaultValue("-1") int startYear, 
            @QueryParam("endYear") @DefaultValue("-1") int endYear, 
            @QueryParam("datasetKey") @DefaultValue("") List<String> datasetKeys, 
            @QueryParam("ptvk") @DefaultValue("") List<String> taxa, 
            @QueryParam("overlapSite") @DefaultValue("-1") Integer overlaps, 
            @QueryParam("withinSite") @DefaultValue("-1") Integer within, 
            @QueryParam("sensitive") @DefaultValue("1") Boolean sensitive, 
            @QueryParam("designation") @DefaultValue("") String designation, 
            @QueryParam("taxonOutputGroup") @DefaultValue("") String taxonOutputGroup, 
            @QueryParam("gridRef") @DefaultValue("") String gridRef) {
        //TODO: squareBlurring(?)
        List<DatasetWithQueryStats> datasetsWithQueryStats = observationMapper.selectObservationDatasetsByFilter(user, startYear, endYear, datasetKeys, taxa, overlaps, within, sensitive, designation, taxonOutputGroup, gridRef);

        return groupDatasetsByProvider(datasetsWithQueryStats);
    }

    private List<ProviderWithQueryStats> groupDatasetsByProvider(List<DatasetWithQueryStats> datasetsWithQueryStats) {
        HashMap<Integer, ProviderWithQueryStats> providers = new HashMap<Integer, ProviderWithQueryStats>();
        for (DatasetWithQueryStats datasetWithQueryStats : datasetsWithQueryStats) {
            Integer providerKey = datasetWithQueryStats.getDataset().getOrganisationID();
            if (providers.containsKey(providerKey)) {
                appendDatasetToProvider(providers, datasetWithQueryStats);
            } else {
                providers.put(providerKey, getNewProviderWithQueryStats(datasetWithQueryStats));
            }
        }
        List<ProviderWithQueryStats> toReturn = new ArrayList<ProviderWithQueryStats>(providers.values());
        sortByProviderAndDataset(toReturn);
        return toReturn;
    }

    private ProviderWithQueryStats getNewProviderWithQueryStats(DatasetWithQueryStats datasetWithQueryStats) {
        int organisationID = datasetWithQueryStats.getDataset().getOrganisationID();
        List<DatasetWithQueryStats> datasets = new ArrayList<DatasetWithQueryStats>();
        datasets.add(datasetWithQueryStats);

        ProviderWithQueryStats toReturn = new ProviderWithQueryStats();
        toReturn.setOrganisationID(organisationID);
        toReturn.setQuerySpecificObservationCount(datasetWithQueryStats.getQuerySpecificObservationCount());
        toReturn.setOrganisation(organisationMapper.selectByID(organisationID));
        toReturn.setDatasetsWithQueryStats(datasets);
        return toReturn;
    }

    private void appendDatasetToProvider(HashMap<Integer, ProviderWithQueryStats> providers, DatasetWithQueryStats datasetWithQueryStats) {
        ProviderWithQueryStats provider = providers.get(datasetWithQueryStats.getDataset().getOrganisationID());
        provider.setQuerySpecificObservationCount(provider.getQuerySpecificObservationCount() + datasetWithQueryStats.getQuerySpecificObservationCount());
        provider.getDatasetsWithQueryStats().add(datasetWithQueryStats);
    }
    
    private void sortByProviderAndDataset(List<ProviderWithQueryStats> providersToSort){
        Collections.sort(providersToSort,Collections.reverseOrder());
        for(ProviderWithQueryStats providerWithQueryStats : providersToSort){
            Collections.sort(providerWithQueryStats.getDatasetsWithQueryStats());
        }
    }

    /*
     * This takes a list of observations in dataset order and returns a list
     * of datasets with their observations
     */
    private List<TaxonDataset>getDatasetsWithObservations(List<TaxonObservation> taxonObservationsOrderedByDataset){
        List<TaxonDataset> toReturn = new ArrayList<TaxonDataset>();
        List<TaxonObservation> taxonObservationsForDataset = null;
        String currentDatasetKey = "";
        String previousDatasetKey = "";
        for(TaxonObservation taxonObservation : taxonObservationsOrderedByDataset){
            currentDatasetKey = taxonObservation.getDatasetKey();
            if(currentDatasetKey.equals(previousDatasetKey)){
                taxonObservationsForDataset.add(taxonObservation);
            }else{
                if(!"".equals(previousDatasetKey)){
                    appendTaxonDataset(taxonObservationsForDataset, previousDatasetKey, toReturn);
                }
                taxonObservationsForDataset = new ArrayList<TaxonObservation>();
                taxonObservationsForDataset.add(taxonObservation);
            }
            previousDatasetKey = currentDatasetKey;
        }
        appendTaxonDataset(taxonObservationsForDataset, currentDatasetKey, toReturn);
        return toReturn;
    }
    
    private void appendTaxonDataset(List<TaxonObservation> taxonObservations, String taxonDatasetKey, List<TaxonDataset> taxonDatasets){
        TaxonDataset taxonDataset = datasetMapper.selectTaxonDatasetByID(taxonDatasetKey);
        taxonDataset.setObservations(taxonObservations);
        taxonDatasets.add(taxonDataset);
    }
    
    
}
