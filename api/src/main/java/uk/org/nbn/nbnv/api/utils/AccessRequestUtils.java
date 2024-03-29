/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.org.nbn.nbnv.api.utils;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import uk.org.nbn.nbnv.api.dao.warehouse.TaxonObservationMapper;
import uk.org.nbn.nbnv.api.model.Organisation;
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
            boolean sensitiveRecords = accessRequest.getSensitive().equals("sans") ? true : false;
            if (!accessRequest.getSpatial().isAll()) { sensitiveRecords = false; }
            List<TaxonDatasetWithQueryStats> selectRequestableObservationDatasetsByFilter = 
                    taxonObservationMapper.selectRequestableObservationDatasetsByFilter(
                        user, accessRequest.getYear().getStartYear(), accessRequest.getYear().getEndYear(), 
                        new ArrayList<String>(), species, accessRequest.getSpatial().getMatch(), 
                        accessRequest.getSpatial().getFeature(), sensitiveRecords, 
                        accessRequest.getTaxon().getDesignation(), accessRequest.getTaxon().getOutput(), 
                        accessRequest.getTaxon().getOrgSuppliedList(), accessRequest.getSpatial().getGridRef(), 
                        "");
            datasets = new ArrayList<String>();

            for (TaxonDatasetWithQueryStats tdwqs : selectRequestableObservationDatasetsByFilter) {
                datasets.add(tdwqs.getDatasetKey());
            }
        } else {
            datasets = accessRequest.getDataset().getDatasets();
        }

        return datasets;
    }

    public List<String> createSensitiveDatasetList(AccessRequestJSON accessRequest, List<String> species, User user) {
        List<String> datasets = new ArrayList<String>();

        List<TaxonDatasetWithQueryStats> selectRequestableSensitiveObservationDatasetsByFilter = 
                taxonObservationMapper.selectRequestableSensitiveObservationDatasetsByFilter(
                user, accessRequest.getYear().getStartYear(), accessRequest.getYear().getEndYear(), 
                new ArrayList<String>(), species, accessRequest.getSpatial().getMatch(), 
                accessRequest.getSpatial().getFeature(), 
                (accessRequest.getSensitive().equals("sans") ? true : false), accessRequest.getTaxon().getDesignation(), 
                accessRequest.getTaxon().getOutput(), accessRequest.getTaxon().getOrgSuppliedList(), 
                accessRequest.getSpatial().getGridRef(), "");
        for (TaxonDatasetWithQueryStats tdwqs : selectRequestableSensitiveObservationDatasetsByFilter) {
            datasets.add(tdwqs.getDatasetKey());
        }

        return datasets;
    }

    public List<String> createDatasetList(AccessRequestJSON accessRequest, List<String> species, Organisation org) {
        List<String> datasets;

        if (accessRequest.getDataset().isAll()) {
            boolean sensitiveRecords = accessRequest.getSensitive().equals("sans") ? true : false;
            if (!accessRequest.getSpatial().isAll()) { sensitiveRecords = false; }
            List<TaxonDatasetWithQueryStats> selectRequestableObservationDatasetsByFilter = taxonObservationMapper.selectRequestableObservationDatasetsByFilterOrganisation(org, accessRequest.getYear().getStartYear(), accessRequest.getYear().getEndYear(), new ArrayList<String>(), species, accessRequest.getSpatial().getMatch(), accessRequest.getSpatial().getFeature(), sensitiveRecords, accessRequest.getTaxon().getDesignation(), accessRequest.getTaxon().getOutput(), accessRequest.getTaxon().getOrgSuppliedList(), accessRequest.getSpatial().getGridRef(), "");
            datasets = new ArrayList<String>();

            for (TaxonDatasetWithQueryStats tdwqs : selectRequestableObservationDatasetsByFilter) {
                datasets.add(tdwqs.getDatasetKey());
            }
        } else {
            datasets = accessRequest.getDataset().getDatasets();
        }

        return datasets;
    }

    public List<String> createSensitiveDatasetList(AccessRequestJSON accessRequest, List<String> species, Organisation org) {
        List<String> datasets = new ArrayList<String>();

        List<TaxonDatasetWithQueryStats> selectRequestableSensitiveObservationDatasetsByFilter = taxonObservationMapper.selectRequestableSensitiveObservationDatasetsByFilterOrganisation(org, accessRequest.getYear().getStartYear(), accessRequest.getYear().getEndYear(), new ArrayList<String>(), species, accessRequest.getSpatial().getMatch(), accessRequest.getSpatial().getFeature(), (accessRequest.getSensitive().equals("sans") ? true : false), accessRequest.getTaxon().getDesignation(), accessRequest.getTaxon().getOutput(), accessRequest.getTaxon().getOrgSuppliedList(), accessRequest.getSpatial().getGridRef(), "");
        for (TaxonDatasetWithQueryStats tdwqs : selectRequestableSensitiveObservationDatasetsByFilter) {
            datasets.add(tdwqs.getDatasetKey());
        }

        return datasets;
    }

    public List<Integer> getRecordSet(AccessRequestJSON accessRequest, List<String> species, String dataset, User user) {
        List<String> datasets = new ArrayList<String>();
        datasets.add(dataset);
        return taxonObservationMapper.selectRequestableObservationRecordIDsByFilter(user, accessRequest.getYear().getStartYear(), accessRequest.getYear().getEndYear(), datasets, species, accessRequest.getSpatial().getMatch(), accessRequest.getSpatial().getFeature(), (accessRequest.getSensitive().equals("sans") ? true : false), accessRequest.getTaxon().getDesignation(), accessRequest.getTaxon().getOutput(), accessRequest.getTaxon().getOrgSuppliedList(), accessRequest.getSpatial().getGridRef(), "");
    }

    public List<Integer> getRecordSet(AccessRequestJSON accessRequest, List<String> species, String dataset, Organisation org) {
        List<String> datasets = new ArrayList<String>();
        datasets.add(dataset);
        return taxonObservationMapper.selectRequestableObservationRecordIDsByFilterOrganisation(org, accessRequest.getYear().getStartYear(), accessRequest.getYear().getEndYear(), datasets, species, accessRequest.getSpatial().getMatch(), accessRequest.getSpatial().getFeature(), (accessRequest.getSensitive().equals("sans") ? true : false), accessRequest.getTaxon().getDesignation(), accessRequest.getTaxon().getOutput(), accessRequest.getTaxon().getOrgSuppliedList(), accessRequest.getSpatial().getGridRef(), "");
    }
}
