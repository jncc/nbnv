
package nbn.common.taxon;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import nbn.common.dataset.TaxonDataset;

public abstract class TaxonObservation {
    private int _observationID;
    private String _providerID;
    private TaxonDataset _dataset;
    private Taxon _taxon;
    private TaxonObservationSite _site;
    private int _surveyKey;
    private boolean _sensitiveRecord;
    private Date _startDate;
    private Date _endDate;
    private DateType _dateType;
    private String _recorder;
    private String _determiner;
    private List<TaxonObservationAttribute> _attributes;

    public abstract Abundance getAbundance();
    /**
     * @return the _observationID
     */
    public int getObservationID() {
        return _observationID;
    }

    /**
     * @return the _providerID
     */
    public String getProviderID() {
        return _providerID;
    }

    /**
     * @return the _dataset
     */
    public TaxonDataset getDataset() {
        return _dataset;
    }

    /**
     * @return the _taxon
     */
    public Taxon getTaxon() {
        return _taxon;
    }

    /**
     * @return the _site
     */
    public TaxonObservationSite<?> getSite() {
        return _site;
    }

    /**
     * @return the _surveyKey
     */
    public int getSurveyKey() {
        return _surveyKey;
    }

    /**
     * @return the _sensitiveRecord
     */
    public boolean isSensitiveRecord() {
        return _sensitiveRecord;
    }

    /**
     * @return the _startDate
     */
    public Date getStartDate() {
        return _startDate;
    }

    /**
     * @return the _endDate
     */
    public Date getEndDate() {
        return _endDate;
    }

    /**
     * @return the _dateType
     */
    public DateType getDateType() {
        return _dateType;
    }

    /**
     * @return the _recorder
     */
    public String getRecorder() {
        return _recorder;
    }

    /**
     * @return the _determiner
     */
    public String getDeterminer() {
        return _determiner;
    }

    /**
     * @return the _attributes
     */
    public List<TaxonObservationAttribute> getAttributes() {
        return _attributes;
    }

    /**
     * @param providerID the _providerID to set
     */
    void setProviderID(String providerID) {
        this._providerID = providerID;
    }

    /**
     * @param dataset the _dataset to set
     */
    void setDataset(TaxonDataset dataset) {
        this._dataset = dataset;
    }

    /**
     * @param taxon the _taxon to set
     */
    void setTaxon(Taxon taxon) {
        this._taxon = taxon;
    }

    /**
     * @param site the _site to set
     */
    void setSite(TaxonObservationSite site) {
        this._site = site;
    }

    /**
     * @param surveyKey the _surveyKey to set
     */
    void setSurveyKey(int surveyKey) {
        this._surveyKey = surveyKey;
    }

    /**
     * @param sensitiveRecord the _sensitiveRecord to set
     */
    void setSensitiveRecord(boolean sensitiveRecord) {
        this._sensitiveRecord = sensitiveRecord;
    }

    /**
     * @param startDate the _startDate to set
     */
    void setStartDate(Date startDate) {
        this._startDate = startDate;
    }

    /**
     * @param endDate the _endDate to set
     */
    void setEndDate(Date endDate) {
        this._endDate = endDate;
    }

    /**
     * @param dateType the _dateType to set
     */
    void setDateType(DateType dateType) {
        this._dateType = dateType;
    }

    /**
     * @param recorder the _recorder to set
     */
    void setRecorder(String recorder) {
        this._recorder = recorder;
    }

    /**
     * @param determiner the _determiner to set
     */
    void setDeterminer(String determiner) {
        this._determiner = determiner;
    }

    /**
     * @param attributes the _attributes to set
     */
    void setAttributes(List<TaxonObservationAttribute> attributes) {
        this._attributes = attributes;
    }

    void addAttribute(TaxonObservationAttribute attribute) {
        this._attributes.add(attribute);
    }

    TaxonObservation(int key) {
        this._observationID = key;
        this._attributes = new ArrayList<TaxonObservationAttribute>();
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 47 * hash + this._observationID;
        return hash;
    }

    @Override
    public boolean equals(Object o) {
        if(o instanceof TaxonPresence)
            return _observationID == ((TaxonObservation)o)._observationID;
        else
            return super.equals(o);
    }

}
