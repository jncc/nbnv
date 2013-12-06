package uk.org.nbn.nbnv.api.rest.resources;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import uk.org.nbn.nbnv.api.dao.warehouse.TaxonObservationMapper;
import uk.org.nbn.nbnv.api.model.TaxonDatasetWithQueryStats;
import uk.org.nbn.nbnv.api.model.User;
import uk.org.nbn.nbnv.api.model.meta.BaseFilterJSON;

/**
 *
 * @author Matt Debont
 */
public class RequestResource extends AbstractResource {
    
    @Autowired TaxonObservationMapper taxonObservationMapper;
    
    /**
     * Check a JSON filter for basic structural validity, throws an error if the
     * filter does not pass the basic checks
     * 
     * @param filter The JSON filter to check for validity
     * @throws IllegalArgumentException Contains the error that caused the 
     * filter to fail its validation
     */
    protected void checkJSONFilterForValidity(BaseFilterJSON filter) throws IllegalArgumentException {
        if (filter.getDataset().isAll() 
                && filter.getSpatial().isAll() 
                && filter.getTaxon().isAll()) {
            throw new IllegalArgumentException("Cannot use a filter without at least ONE of the following filters; dataset, spatial or taxon");
        }
        
        if (!filter.getDataset().isAll() 
                && (filter.getDataset().getDatasets() == null
                    || filter.getDataset().getDatasets().isEmpty())) {
            throw new IllegalArgumentException("Cannot use a dataset filter without supplying at least one dataset key");
        }
        
        if (!filter.getSpatial().isAll()
                && !StringUtils.hasText(filter.getSpatial().getGridRef())
                && !(StringUtils.hasText(filter.getSpatial().getFeature()) && StringUtils.hasText(filter.getSpatial().getDataset()))) {
            throw new IllegalArgumentException("Cannot use a spatial filter without supplying at least a grid reference or a feature ID and its dataset");
        }
        
        if (!filter.getTaxon().isAll()
                && !StringUtils.hasText(filter.getTaxon().getTvk())
                && !StringUtils.hasText(filter.getTaxon().getDesignation())
                && !StringUtils.hasText(filter.getTaxon().getOutput())
                && filter.getTaxon().getOrgSuppliedList() == -1) {
            throw new IllegalArgumentException("Cannot use a taxon filter without supplying at least a TVK, designation, output group or an organisation supplied list ID");
        }
        
        if ((!filter.getDataset().isAll() && filter.getDataset().getDatasets().size() > 1)
                && filter.getTaxon().isAll()
                && filter.getSpatial().isAll()) {
            throw new IllegalArgumentException("Cannot select more than one dataset without a valid taxon or spatial filter");
        }
    }
    
    /**
     * Checks a request to see if it would return any records, 
     * 
     * @param user
     * @param filter
     * @param datasetKey 
     */
    protected void checkForRecordsReturnedSingleDataset(User user, BaseFilterJSON filter, String datasetKey) {
        List<String> tvks = new ArrayList<String>();
        tvks.add(filter.getTaxon().getTvk());
        List<String> datasets = new ArrayList<String>();
        datasets.add(datasetKey);
        
        List<TaxonDatasetWithQueryStats> stats = taxonObservationMapper
                .selectRequestableObservationDatasetsByFilter(user, 
                filter.getYear().getStartYear(), filter.getYear().getEndYear(), 
                datasets, tvks, filter.getSpatial().getMatch(), 
                filter.getSpatial().getFeature(), 
                filter.getSensitive().equals("sans"), 
                filter.getTaxon().getDesignation(), 
                filter.getTaxon().getOutput(), filter.getSpatial().getGridRef(), 
                "");
        
        if (stats.isEmpty()) {
            throw new IllegalArgumentException("This action would grant access to zero records, we recommend closing this request");
        }
    }
}
