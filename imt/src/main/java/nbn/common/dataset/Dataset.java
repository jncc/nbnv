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
public class Dataset {
    /** The unique identifier for the dataset. */
    protected String datasetKey;
    /** The resource datasetTitle. */
    protected String datasetTitle;
    /** The organisation owning the dataset. */
    protected Organisation datasetProvider;
//    /** The datasetKey identifying the datasetProvider organisation. */
//    protected int datasetProviderKey;

    /**
     * @return the datasetKey
     */
    public String getDatasetKey() {
        return datasetKey;
    }

    /**
     * @return the datasetTitle
     */
    public String getDatasetTitle() {
        return datasetTitle;
    }

    /**
     * @return the datasetProvider
     */
    public Organisation getDatasetProvider() {
        return datasetProvider;
    }

//    /**
//     * @return the datasetProviderKey
//     */
//    public int getDatasetProviderKey() {
//        return datasetProviderKey;
//    }

    Dataset(String key, String title, Organisation provider) {
        this.datasetKey = key;
        this.datasetTitle = title;
        this.datasetProvider = provider;
    }
}
