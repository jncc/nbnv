/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package nbn.common.taxon;

import nbn.common.dataset.TaxonDataset;
import nbn.common.util.Pair;

/**
 *
 * @author Administrator
 */
public class TaxonDatasetPair {
    private Pair<Taxon,TaxonDataset> taxonDatasetPair;

    /**
     * @return the _taxon
     */
    public Taxon getTaxon() {
        return taxonDatasetPair.getA();
    }

    /**
     * @return the _dataset
     */
    public TaxonDataset getDataset() {
        return taxonDatasetPair.getB();
    }

    TaxonDatasetPair(Taxon taxon, TaxonDataset dataset) {
        taxonDatasetPair = new Pair<Taxon,TaxonDataset>(taxon,dataset);
    }


}
