/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.org.nbn.nbnv.api.rest.resources;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import uk.org.nbn.nbnv.api.dao.warehouse.TaxonObservationMapper;
import uk.org.nbn.nbnv.api.model.TaxonDatasetWithQueryStats;
import uk.org.nbn.nbnv.api.model.User;
import uk.org.nbn.nbnv.api.model.meta.RequestDatasetFilterJSON;
import uk.org.nbn.nbnv.api.model.meta.RequestSpatialFilterJSON;
import uk.org.nbn.nbnv.api.model.meta.RequestTaxonFilterJSON;
import uk.org.nbn.nbnv.api.model.meta.RequestYearFilterJSON;

/**
 *
 * @author Matt Debont
 */
public class RequestResource extends AbstractResource {
    
    @Autowired TaxonObservationMapper observationMapper;
    
    protected void checkAccessJSONFilter(RequestDatasetFilterJSON datasetFilter,  
            RequestSpatialFilterJSON spatialFilter, 
            RequestTaxonFilterJSON taxonFilter) throws IllegalArgumentException {
        if (datasetFilter.isAll() && taxonFilter.isAll() && spatialFilter.isAll()) {
            throw new IllegalArgumentException("Must specify at least one of the following filters; Dataset, Spatial or Taxon");
        }
        
        // Check the dataset filter for validitiy
        if (!datasetFilter.isAll() 
                && (datasetFilter.getDatasets() == null 
                    || datasetFilter.getDatasets().isEmpty() 
                    || !listHasAtLeastOneText(datasetFilter.getDatasets()))) {
            throw new IllegalArgumentException("Cannot use a dataset filter with no datasets selected");
        }     
        
        // Check for a valid spatial filter
        if (!spatialFilter.isAll() 
                && !StringUtils.hasText(spatialFilter.getGridRef())
                && !(StringUtils.hasText(spatialFilter.getFeature()) 
                     && StringUtils.hasText(spatialFilter.getDataset()))) {
             throw new IllegalArgumentException("Cannot use a spatial filter without a gridRef or feature and dataset");
        } 
        
        // Check for a valid taxon filter
        if (!taxonFilter.isAll()
                && !StringUtils.hasText(taxonFilter.getTvk())
                && !StringUtils.hasText(taxonFilter.getOutput())
                && !StringUtils.hasText(taxonFilter.getDesignation())
                && taxonFilter.getOrgSuppliedList() == -1) {
            throw new IllegalArgumentException("Cannot use a taxon filter without a ptvk, a designation, an output group or an organisation supplied list");
        }                      
        
        // Check for valid combinations, dataset list may only come through if a
        // taxon or spatial filter is applied
        if ((!datasetFilter.isAll() && datasetFilter.getDatasets().size() > 1)
                && (taxonFilter.isAll() && spatialFilter.isAll())) {
            throw new IllegalArgumentException("Cannot specify more than one dataset without a valid taxon or spatial filter");
        }
    }    
    
    protected void validateCompleteDatasetFilter(List<TaxonDatasetWithQueryStats> datasets, RequestDatasetFilterJSON datasetsFilter) {
        if (!datasetsFilter.isAll() && datasetsFilter.getDatasets().size() > 1) {
            List <String> tDatasets = new ArrayList<String>();
            for (TaxonDatasetWithQueryStats dataset : datasets) {
                tDatasets.add(dataset.getDatasetKey());
            }

            for (String dataset : datasetsFilter.getDatasets()) {
                if (!tDatasets.contains(dataset)) {
                    throw new IllegalArgumentException("Request contains datasets, not matching the requestable datasets");
                }
            }
        }
    }
    
    protected void validateCompleteDatasetFilterSingle(List<TaxonDatasetWithQueryStats> datasets, String datasetKey) {
        List <String> tDatasets = new ArrayList<String>();
        for (TaxonDatasetWithQueryStats dataset : datasets) {
            tDatasets.add(dataset.getDatasetKey());
        }
        
        if (!tDatasets.contains(datasetKey)) {
            throw new IllegalArgumentException("Request contains datasets, not matching the requestable datasets");
        }
    }
    
    protected List<TaxonDatasetWithQueryStats> getRequestableDatasets(User user, 
            RequestDatasetFilterJSON datasetFilter,  
            RequestSpatialFilterJSON spatialFilter, 
            RequestTaxonFilterJSON taxonFilter,
            RequestYearFilterJSON yearFilter,
            String sensitiveStr, String datasetKey) {
        
        boolean sensitive = sensitiveStr.equals("sans");
        
        List<String> tvks = new ArrayList<String>();
        tvks.add(taxonFilter.getTvk()); 
        
        List<String> datasets = new ArrayList<String>();
        
        if (StringUtils.hasText(datasetKey)) {
            datasets.add(datasetKey);
        } else if (!datasetFilter.isAll() && datasetFilter.getDatasets().size() > 0) {
            datasets = datasetFilter.getDatasets();
        }
        
        return observationMapper.selectRequestableObservationDatasetsByFilter(user, 
                yearFilter.getStartYear(), yearFilter.getEndYear(), datasets, 
                tvks, spatialFilter.getMatch(), spatialFilter.getFeature(), 
                sensitive, taxonFilter.getDesignation(), taxonFilter.getOutput(), 
                spatialFilter.getGridRef(), "");
    }
    
    protected boolean doRecordsExistForThisRequest(List<TaxonDatasetWithQueryStats> datasets) {
        if (datasets.isEmpty()) {
            return false;
        } 
        
        return true;
    }      
}
