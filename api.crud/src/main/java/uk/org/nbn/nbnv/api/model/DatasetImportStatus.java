package uk.org.nbn.nbnv.api.model;

import java.util.List;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class DatasetImportStatus {
    private boolean isOnQueue, isProcessing;
    private List<ImporterResult> history;
    
    public DatasetImportStatus() {}

    public DatasetImportStatus(boolean isOnQueue, boolean isProcessing, List<ImporterResult> history) {
        this.isOnQueue = isOnQueue;
        this.isProcessing = isProcessing;
        this.history = history;
    }

    public boolean isIsOnQueue() {
        return isOnQueue;
    }

    public void setIsOnQueue(boolean isOnQueue) {
        this.isOnQueue = isOnQueue;
    }

    public boolean isIsProcessing() {
        return isProcessing;
    }

    public void setIsProcessing(boolean isProcessing) {
        this.isProcessing = isProcessing;
    }

    public List<ImporterResult> getHistory() {
        return history;
    }

    public void setHistory(List<ImporterResult> history) {
        this.history = history;
    }
}
