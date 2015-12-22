/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.org.nbn.nbnv.api.model.meta;

import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Matt Debont
 */
@XmlRootElement
public class DatasetRecordCount {

    private String datasetKey;
    private int count;

    public DatasetRecordCount() {
    }

    public String getDatasetKey() {
        return datasetKey;
    }

    public void setDatasetKey(String datasetKey) {
        this.datasetKey = datasetKey;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}