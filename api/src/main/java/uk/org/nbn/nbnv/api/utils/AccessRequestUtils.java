/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.org.nbn.nbnv.api.utils;

import uk.org.nbn.nbnv.api.utils.AccessRequestJSONToText;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import uk.org.nbn.nbnv.api.dao.warehouse.TaxonObservationMapper;
import uk.org.nbn.nbnv.api.model.TaxonDatasetWithQueryStats;
import uk.org.nbn.nbnv.api.model.TaxonObservationFilter;
import uk.org.nbn.nbnv.api.model.User;
import uk.org.nbn.nbnv.api.model.meta.AccessRequestJSON;

/**
 *
 * @author paulbe
 */
@Component
public class AccessRequestUtils {
    @Autowired TaxonObservationMapper taxonObservationMapper;
    @Autowired AccessRequestJSONToText accessRequestJSONToText;
        
    public TaxonObservationFilter createFilter(String json, AccessRequestJSON accessRequest) { 
        TaxonObservationFilter filter = new TaxonObservationFilter();
        filter.setFilterJSON(json);
        filter.setFilterText(accessRequestJSONToText.convert(accessRequest));

        return filter;
    }
    
    public List<String> createSpeciesList(AccessRequestJSON accessRequest) {
        List<String> species = null;
        if (accessRequest.getTaxon().getTvk() != null && !accessRequest.getTaxon().getTvk().isEmpty()) {
            species = new ArrayList<String>();
            species.add(accessRequest.getTaxon().getTvk());
        }

        return species;
    }
    
    public List<String> createDatasetList(AccessRequestJSON accessRequest, List<String> species, User user) {
        List<String> datasets;
        
        if (accessRequest.getDataset().isAll()) {
            List<TaxonDatasetWithQueryStats> selectRequestableObservationDatasetsByFilter = taxonObservationMapper.selectRequestableObservationDatasetsByFilter(user, accessRequest.getYear().getStartYear(), accessRequest.getYear().getEndYear(), new ArrayList<String>(), species, accessRequest.getSpatial().getMatch(), accessRequest.getSpatial().getFeature(), (accessRequest.getSensitive().equals("sans") ? true : false), accessRequest.getTaxon().getDesignation(), accessRequest.getTaxon().getOutput(), "");
            datasets = new ArrayList<String>();
            
            for (TaxonDatasetWithQueryStats tdwqs : selectRequestableObservationDatasetsByFilter) {
                datasets.add(tdwqs.getDatasetKey());
            }
        } else {
            datasets = accessRequest.getDataset().getDatasets();
            
            if (accessRequest.getDataset().isSecret()) {
                List<TaxonDatasetWithQueryStats> selectRequestableSensitiveObservationDatasetsByFilter = taxonObservationMapper.selectRequestableSensitiveObservationDatasetsByFilter(user, accessRequest.getYear().getStartYear(), accessRequest.getYear().getEndYear(), new ArrayList<String>(), species, accessRequest.getSpatial().getMatch(), accessRequest.getSpatial().getFeature(), (accessRequest.getSensitive().equals("sans") ? true : false), accessRequest.getTaxon().getDesignation(), accessRequest.getTaxon().getOutput(), "");
                for (TaxonDatasetWithQueryStats tdwqs : selectRequestableSensitiveObservationDatasetsByFilter) {
                    datasets.add(tdwqs.getDatasetKey());
                }
            }
        }

        return datasets;
    }
}
