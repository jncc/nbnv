/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package nbn.common.taxon;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import nbn.common.dataset.TaxonDataset;
import nbn.common.feature.recorded.GridSquareRecordedSite;

/**
 *
 * @author Administrator
 */
public class TaxonObservationGridSquareSiteAggregateData extends TaxonObservationSite<GridSquareRecordedSite> {
    private boolean _containsSensitiveRecords = false;
    private boolean _containsNonSensitiveRecords = false;
    private int _recordCount = 0;
    private Map<String, Taxon> _speciesList = null;
    private Map<String, TaxonDataset> _datasetList = null;
    private int _startYear = -1;
    private int _endYear = -1;

    TaxonObservationGridSquareSiteAggregateData(int locationId) {
        super(locationId);

        this._speciesList = new HashMap<String, Taxon>();
        this._datasetList = new HashMap<String, TaxonDataset>();
    }

    public void addRecordToSite(Taxon taxon, TaxonDataset dataset, boolean sensitive, int startYear, int endYear) {
        this._recordCount++;

        if (!this._speciesList.containsKey(taxon.getTaxonVersionKey()))
            this._speciesList.put(taxon.getTaxonVersionKey(), taxon);

        if (!this._datasetList.containsKey(dataset.getDatasetKey()))
            this._datasetList.put(dataset.getDatasetKey(), dataset);

        if (sensitive)
            this._containsSensitiveRecords = true;
        else
            this._containsNonSensitiveRecords = true;

        if ((startYear < this._startYear || this._startYear == -1) && startYear > 0)
            this._startYear = startYear;

        if ((endYear > this._endYear || this._endYear == -1) && endYear > 0)
            this._endYear = endYear;
    }
    /**
     * @return the _containsSensitiveRecords
     */
    public boolean containsSensitiveRecords() {
        return _containsSensitiveRecords;
    }

    /**
     * @return the _containsNonSensitiveRecords
     */
    public boolean containsNonSensitiveRecords() {
        return _containsNonSensitiveRecords;
    }

    /**
     * @return the _recordCount
     */
    public int getRecordCount() {
        return _recordCount;
    }

    /**
     * @return the _startYear
     */
    public int getStartYear() {
        return _startYear;
    }

    /**
     * @return the _endYear
     */
    public int getEndYear() {
        return _endYear;
    }

    public int getSpeciesCount() {
        return this._speciesList.size();
    }

    public Collection<Taxon> getSpeciesList() {
        return this._speciesList.values();
    }

    public int getDatasetCount() {
        return this._datasetList.size();
    }

    public Collection<TaxonDataset> getDatasetList() {
        return this._datasetList.values();
    }

    /**
     * @param containsSensitiveRecords the _containsSensitiveRecords to set
     */
    void setContainsSensitiveRecords(boolean containsSensitiveRecords) {
        this._containsSensitiveRecords = containsSensitiveRecords;
    }

    /**
     * @param containsNonSensitiveRecords the _containsNonSensitiveRecords to set
     */
    void setContainsNonSensitiveRecords(boolean containsNonSensitiveRecords) {
        this._containsNonSensitiveRecords = containsNonSensitiveRecords;
    }

    /**
     * @param recordCount the _recordCount to set
     */
    void setRecordCount(int recordCount) {
        this._recordCount = recordCount;
    }

    /**
     * @param startYear the _startYear to set
     */
    void setStartYear(int startYear) {
        this._startYear = startYear;
    }

    /**
     * @param endYear the _endYear to set
     */
    void setEndYear(int endYear) {
        this._endYear = endYear;
    }


}
