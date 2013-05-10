/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.org.nbn.nbnv.api.model.meta;

import java.util.List;

/**
 *
 * @author Paul Gilbertson
 */
public class AccessRequestDatasetFilterJSON extends AccessRequestFilterJSON {
    private List<String> datasets;
    private boolean secret;

    /**
     * @return the datasets
     */
    public List<String> getDatasets() {
        return datasets;
    }

    /**
     * @param datasets the datasets to set
     */
    public void setDatasets(List<String> datasets) {
        this.datasets = datasets;
    }

    /**
     * @return the secret
     */
    public boolean isSecret() {
        return secret;
    }

    /**
     * @param secret the secret to set
     */
    public void setSecret(boolean secret) {
        this.secret = secret;
    }
}
