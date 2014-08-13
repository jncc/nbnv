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
import uk.org.nbn.nbnv.api.model.meta.DownloadFilterJSON;

/**
 *
 * @author paulbe
 */
@Component
public class DownloadUtils {

    @Autowired TaxonObservationMapper taxonObservationMapper;
    @Autowired DownloadJSONToText downloadJSONToText;

    public TaxonObservationFilter createFilter(String json, DownloadFilterJSON download) {
        TaxonObservationFilter filter = new TaxonObservationFilter();
        filter.setFilterJSON(json);
        filter.setFilterText(downloadJSONToText.convert(download));

        return filter;
    }

    public List<String> createSpeciesList(DownloadFilterJSON download) {
        List<String> species = null;
        if (download.getTaxon().getTvk() != null && !download.getTaxon().getTvk().isEmpty()) {
            species = new ArrayList<String>();
            species.add(download.getTaxon().getTvk());
        }

        return species;
    }

    public List<String> createDatasetList(DownloadFilterJSON download, List<String> species, User user) {
        List<String> datasets;

        if (download.getDataset().isAll()) {
            List<TaxonDatasetWithQueryStats> selectRequestableObservationDatasetsByFilter = taxonObservationMapper.selectRequestableObservationDatasetsByFilter(user, download.getYear().getStartYear(), download.getYear().getEndYear(), new ArrayList<String>(), species, download.getSpatial().getMatch(), download.getSpatial().getFeature(), (download.getSensitive().equals("sans") ? true : false), download.getTaxon().getDesignation(), download.getTaxon().getOutput(), download.getTaxon().getOrgSuppliedList(), "", "");
            datasets = new ArrayList<String>();

            for (TaxonDatasetWithQueryStats tdwqs : selectRequestableObservationDatasetsByFilter) {
                datasets.add(tdwqs.getDatasetKey());
            }
        } else {
            datasets = download.getDataset().getDatasets();
        }

        return datasets;
    }

    public List<String> createSensitiveDatasetList(DownloadFilterJSON download, List<String> species, User user) {
        List<String> datasets = new ArrayList<String>();

        List<TaxonDatasetWithQueryStats> selectRequestableSensitiveObservationDatasetsByFilter = taxonObservationMapper.selectRequestableSensitiveObservationDatasetsByFilter(user, download.getYear().getStartYear(), download.getYear().getEndYear(), new ArrayList<String>(), species, download.getSpatial().getMatch(), download.getSpatial().getFeature(), (download.getSensitive().equals("sans") ? true : false), download.getTaxon().getDesignation(), download.getTaxon().getOutput(), download.getTaxon().getOrgSuppliedList(), "", "");
        for (TaxonDatasetWithQueryStats tdwqs : selectRequestableSensitiveObservationDatasetsByFilter) {
            datasets.add(tdwqs.getDatasetKey());
        }

        return datasets;
    }

    public List<String> createDatasetList(DownloadFilterJSON download, List<String> species, Organisation org) {
        List<String> datasets;

        if (download.getDataset().isAll()) {
            List<TaxonDatasetWithQueryStats> selectRequestableObservationDatasetsByFilter = taxonObservationMapper.selectRequestableObservationDatasetsByFilterOrganisation(org, download.getYear().getStartYear(), download.getYear().getEndYear(), new ArrayList<String>(), species, download.getSpatial().getMatch(), download.getSpatial().getFeature(), (download.getSensitive().equals("sans") ? true : false), download.getTaxon().getDesignation(), download.getTaxon().getOutput(), download.getTaxon().getOrgSuppliedList(), "", "");
            datasets = new ArrayList<String>();

            for (TaxonDatasetWithQueryStats tdwqs : selectRequestableObservationDatasetsByFilter) {
                datasets.add(tdwqs.getDatasetKey());
            }
        } else {
            datasets = download.getDataset().getDatasets();
        }

        return datasets;
    }

    public List<String> createSensitiveDatasetList(DownloadFilterJSON download, List<String> species, Organisation org) {
        List<String> datasets = new ArrayList<String>();

        List<TaxonDatasetWithQueryStats> selectRequestableSensitiveObservationDatasetsByFilter = taxonObservationMapper.selectRequestableSensitiveObservationDatasetsByFilterOrganisation(org, download.getYear().getStartYear(), download.getYear().getEndYear(), new ArrayList<String>(), species, download.getSpatial().getMatch(), download.getSpatial().getFeature(), (download.getSensitive().equals("sans") ? true : false), download.getTaxon().getDesignation(), download.getTaxon().getOutput(), download.getTaxon().getOrgSuppliedList(), "", "");
        for (TaxonDatasetWithQueryStats tdwqs : selectRequestableSensitiveObservationDatasetsByFilter) {
            datasets.add(tdwqs.getDatasetKey());
        }

        return datasets;
    }

    public List<Integer> getRecordSet(DownloadFilterJSON download, List<String> species, String dataset, User user) {
        List<String> datasets = new ArrayList<String>();
        datasets.add(dataset);
        return taxonObservationMapper.selectRequestableObservationRecordIDsByFilter(user, download.getYear().getStartYear(), download.getYear().getEndYear(), datasets, species, download.getSpatial().getMatch(), download.getSpatial().getFeature(), (download.getSensitive().equals("sans") ? true : false), download.getTaxon().getDesignation(), download.getTaxon().getOutput(), download.getTaxon().getOrgSuppliedList(), "", "");
    }

    public List<Integer> getRecordSet(DownloadFilterJSON download, List<String> species, String dataset, Organisation org) {
        List<String> datasets = new ArrayList<String>();
        datasets.add(dataset);
        return taxonObservationMapper.selectRequestableObservationRecordIDsByFilterOrganisation(org, download.getYear().getStartYear(), download.getYear().getEndYear(), datasets, species, download.getSpatial().getMatch(), download.getSpatial().getFeature(), (download.getSensitive().equals("sans") ? true : false), download.getTaxon().getDesignation(), download.getTaxon().getOutput(), download.getTaxon().getOrgSuppliedList(), "", "");
    }
}