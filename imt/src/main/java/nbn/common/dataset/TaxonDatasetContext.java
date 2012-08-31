/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package nbn.common.dataset;

import nbn.common.organisation.Organisation;

/**
 *
 * @author Administrator
 */
public class TaxonDatasetContext<T> extends TaxonDataset {
    private int recordCount;
    private T context;

    TaxonDatasetContext(String key, String title, Organisation provider, T context, int recordCount) {
        super(key, title, provider);
        this.context = context;
        this.recordCount = recordCount;
    }

    public T getContext() {
        return context;
    }

    public int getRecordCount() {
        return recordCount;
    }
}
