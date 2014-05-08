/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.org.nbn.nbnv.api.poker.model;

//import java.sql.Date;
import java.util.Date;
import javax.xml.bind.annotation.XmlRootElement;
import uk.org.nbn.nbnv.api.model.*;

/**
 *
 * @author Matt Debont
 */
@XmlRootElement
public class AccessRequest {
    private TaxonObservationFilter filter;
    private Dataset dataset;
    private int requestPurposeID;
    private String requestPurposeLabel;
    private String requestReason;
    private Date requestDate;
    private int responseTypeID;
    private String responseReason;
    private Date responseDate;
    private Date accessExpires;
    private String datasetKey;
    private boolean sensitiveRequest;

    /**
     * @return the filter
     */
    public TaxonObservationFilter getFilter() {
        return filter;
    }

    /**
     * @param filter the filter to set
     */
    public void setFilter(TaxonObservationFilter filter) {
        this.filter = filter;
    }

    /**
     * @return the requestReason
     */
    public String getRequestReason() {
        return requestReason;
    }

    /**
     * @param requestReason the requestReason to set
     */
    public void setRequestReason(String requestReason) {
        this.requestReason = requestReason;
    }

    /**
     * @return the requestDate
     */
    public Date getRequestDate() {
        return requestDate;
    }

    /**
     * @param requestDate the requestDate to set
     */
    public void setRequestDate(Date requestDate) {
        this.requestDate = requestDate;
    }

    /**
     * @return the responseTypeID
     */
    public int getResponseTypeID() {
        return responseTypeID;
    }

    /**
     * @param responseTypeID the responseTypeID to set
     */
    public void setResponseTypeID(int responseTypeID) {
        this.responseTypeID = responseTypeID;
    }

    /**
     * @return the responseReason
     */
    public String getResponseReason() {
        return responseReason;
    }

    /**
     * @param responseReason the responseReason to set
     */
    public void setResponseReason(String responseReason) {
        this.responseReason = responseReason;
    }

    /**
     * @return the responseDate
     */
    public Date getResponseDate() {
        return responseDate;
    }

    /**
     * @param responseDate the responseDate to set
     */
    public void setResponseDate(Date responseDate) {
        this.responseDate = responseDate;
    }

    /**
     * @return the accessExpires
     */
    public Date getAccessExpires() {
        return accessExpires;
    }

    /**
     * @param accessExpires the accessExpires to set
     */
    public void setAccessExpires(Date accessExpires) {
        this.accessExpires = accessExpires;
    }

    /**
     * @return the dataset
     */
    public Dataset getDataset() {
        return dataset;
    }

    /**
     * @param dataset the dataset to set
     */
    public void setDataset(Dataset dataset) {
        this.dataset = dataset;
    }

    /**
     * @return the datasetKey
     */
    public String getDatasetKey() {
        return datasetKey;
    }

    /**
     * @param datasetKey the datasetKey to set
     */
    public void setDatasetKey(String datasetKey) {
        this.datasetKey = datasetKey;
    }

    /**
     * @return the requestPurposeID
     */
    public int getRequestPurposeID() {
        return requestPurposeID;
    }

    /**
     * @param requestPurposeID the requestPurposeID to set
     */
    public void setRequestPurposeID(int requestPurposeID) {
        this.requestPurposeID = requestPurposeID;
    }

    /**
     * @return the sensitiveRequest
     */
    public boolean isSensitiveRequest() {
        return sensitiveRequest;
    }

    /**
     * @param sensitiveRequest the sensitiveRequest to set
     */
    public void setSensitiveRequest(boolean sensitiveRequest) {
        this.sensitiveRequest = sensitiveRequest;
    }

    /**
     * @return the requestPurposeLabel
     */
    public String getRequestPurposeLabel() {
        return requestPurposeLabel;
    }

    /**
     * @param requestPurposeLabel the requestPurposeLabel to set
     */
    public void setRequestPurposeLabel(String requestPurposeLabel) {
        this.requestPurposeLabel = requestPurposeLabel;
    }

}
