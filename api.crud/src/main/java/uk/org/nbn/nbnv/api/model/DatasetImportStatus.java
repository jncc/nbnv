package uk.org.nbn.nbnv.api.model;

import java.util.Map;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class DatasetImportStatus {
    private boolean isOnQueue, isProcessing;
    private Map<String, ImportStatus> history;

    public DatasetImportStatus(boolean isOnQueue, boolean isProcessing, Map<String, ImportStatus> history) {
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

    public Map<String, ImportStatus> getHistory() {
        return history;
    }

    public void setHistory(Map<String, ImportStatus> history) {
        this.history = history;
    }
    
}
