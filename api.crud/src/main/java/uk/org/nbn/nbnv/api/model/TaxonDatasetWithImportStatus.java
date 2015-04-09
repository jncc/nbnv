package uk.org.nbn.nbnv.api.model;

import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author cjohn
 */
@XmlRootElement
public class TaxonDatasetWithImportStatus {
    private TaxonDataset dataset;
    private DatasetImportStatus importStatus;
    
    public TaxonDatasetWithImportStatus() {}

    public TaxonDatasetWithImportStatus(TaxonDataset dataset, DatasetImportStatus importStatus) {
        this.dataset = dataset;
        this.importStatus = importStatus;
    }

    public TaxonDataset getDataset() {
        return dataset;
    }

    public void setDataset(TaxonDataset dataset) {
        this.dataset = dataset;
    }

    public DatasetImportStatus getImportStatus() {
        return importStatus;
    }

    public void setImportStatus(DatasetImportStatus importStatus) {
        this.importStatus = importStatus;
    }
    
}
