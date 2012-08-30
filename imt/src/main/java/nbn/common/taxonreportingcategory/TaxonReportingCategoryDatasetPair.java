/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package nbn.common.taxonreportingcategory;

import nbn.common.dataset.TaxonDataset;

/**
 *
 * @author Administrator
 */
public class TaxonReportingCategoryDatasetPair {
    private TaxonReportingCategory _trc;
    private TaxonDataset _dataset;

    /**
     * @return the _trc
     */
    public TaxonReportingCategory getTaxonReportingCategory() {
        return _trc;
    }

    /**
     * @return the _dataset
     */
    public TaxonDataset getDataset() {
        return _dataset;
    }

    TaxonReportingCategoryDatasetPair(TaxonReportingCategory trc, TaxonDataset dataset) {
        this._trc = trc;
        this._dataset = dataset;
    }


}
