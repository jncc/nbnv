/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package nbn.common.taxon;

import java.util.List;
import nbn.common.dataset.TaxonDataset;
import nbn.common.util.Pair;

/**
 *
 * @author Administrator
 */
public class DatasetTaxonObservationListPair<T extends TaxonObservation> {
    private Pair<TaxonDataset, List<T >> datasetTaxonObservationListPair;

    /**
     * @return the _taxon
     */
    public List<T> getTaxonObservationList() {
        return datasetTaxonObservationListPair.getB();
    }

    /**
     * @return the _dataset
     */
    public TaxonDataset getDataset() {
        return datasetTaxonObservationListPair.getA();
    }

    DatasetTaxonObservationListPair(TaxonDataset dataset, List<T > obsList) {
        datasetTaxonObservationListPair = new Pair<TaxonDataset, List<T>>(dataset,obsList);
    }
}
