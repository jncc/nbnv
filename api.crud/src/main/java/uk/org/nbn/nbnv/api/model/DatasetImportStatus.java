package uk.org.nbn.nbnv.api.model;

import java.util.Map;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class DatasetImportStatus {
    private boolean isOnQueue, isProcessing;
    private Map<String, ImporterResult> history;

    public DatasetImportStatus(boolean isOnQueue, boolean isProcessing, Map<String, ImporterResult> history) {
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

    public Map<String, ImporterResult> getHistory() {
        return history;
    }

    public void setHistory(Map<String, ImporterResult> history) {
        this.history = history;
    }
    
}
