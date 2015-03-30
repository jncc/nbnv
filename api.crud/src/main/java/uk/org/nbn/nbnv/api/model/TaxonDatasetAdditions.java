package uk.org.nbn.nbnv.api.model;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * The following class represents the parts of a TaxonDataset which can not be
 * reliably read from a TaxonDataset metadata form.
 * 
 * The word document can be supplied in addition an instance of this class
 * to create a new TaxonDataset
 * @author cjohn
 */
@XmlRootElement
public class TaxonDatasetAdditions {
    private String resolution;
    
    private boolean recordAttributes, recorderNames;

    public String getResolution() {
        return resolution;
    }

    public void setResolution(String resolution) {
        this.resolution = resolution;
    }

    public boolean isRecordAttributes() {
        return recordAttributes;
    }

    public void setRecordAttributes(boolean recordAttributes) {
        this.recordAttributes = recordAttributes;
    }

    public boolean isRecorderNames() {
        return recorderNames;
    }

    public void setRecorderNames(boolean recorderNames) {
        this.recorderNames = recorderNames;
    }
    
}
