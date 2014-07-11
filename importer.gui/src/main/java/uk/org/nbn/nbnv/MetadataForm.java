package uk.org.nbn.nbnv;

/**
 * @author stephen batty
 *         Date: 10/07/14
 *         Time: 14:54
 */
public class MetadataForm {

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getOrganisation() {
        return organisation;
    }

    public void setOrganisation(String organisation) {
        this.organisation = organisation;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getMethodsOfDataCapture() {
        return methodsOfDataCapture;
    }

    public void setMethodsOfDataCapture(String methodsOfDataCapture) {
        this.methodsOfDataCapture = methodsOfDataCapture;
    }

    public String getPurposeOfDataCapture() {
        return purposeOfDataCapture;
    }

    public void setPurposeOfDataCapture(String purposeOfDataCapture) {
        this.purposeOfDataCapture = purposeOfDataCapture;
    }

    public String getGeographicalCoverage() {
        return geographicalCoverage;
    }

    public void setGeographicalCoverage(String geographicalCoverage) {
        this.geographicalCoverage = geographicalCoverage;
    }

    public String getTemporalCoverage() {
        return temporalCoverage;
    }

    public void setTemporalCoverage(String temporalCoverage) {
        this.temporalCoverage = temporalCoverage;
    }

    public String getDataQuality() {
        return dataQuality;
    }

    public void setDataQuality(String dataQuality) {
        this.dataQuality = dataQuality;
    }

    public String getAdditionalInfo() {
        return additionalInfo;
    }

    public void setAdditionalInfo(String additionalInfo) {
        this.additionalInfo = additionalInfo;
    }

    public String getUseConstraints() {
        return useConstraints;
    }

    public void setUseConstraints(String useConstraints) {
        this.useConstraints = useConstraints;
    }

    public String getAccessConstraints() {
        return accessConstraints;
    }

    public void setAccessConstraints(String accessConstraints) {
        this.accessConstraints = accessConstraints;
    }

    public String getResolution() {
        return resolution;
    }

    public void setResolution(String resolution) {
        this.resolution = resolution;
    }

    public String getRecordAttributes() {
        return recordAttributes;
    }

    public void setRecordAttributes(String recordAttributes) {
        this.recordAttributes = recordAttributes;
    }

    public String getRecorderNames() {
        return recorderNames;
    }

    public void setRecorderNames(String recorderNames) {
        this.recorderNames = recorderNames;
    }

    public String getInsertionType() {
        return insertionType;
    }

    public void setInsertionType(String insertionType) {
        this.insertionType = insertionType;
    }

    private String title;
    private String organisation;
    private String description;
    private String methodsOfDataCapture;
    private String purposeOfDataCapture;
    private String geographicalCoverage;
    private String temporalCoverage;
    private String dataQuality;
    private String additionalInfo;
    private String useConstraints;
    private String accessConstraints;
    private String resolution; // radio button
    private String recordAttributes; // radio button
    private String recorderNames; // radio button
    private String insertionType;
    private AdminDetails adminDetails;

    public AdminDetails getAdminDetails() {
        return adminDetails;
    }

    public void setAdminDetails(AdminDetails adminDetails) {
        this.adminDetails = adminDetails;
    }

    class AdminDetails {
        private String name;
        private String phone;
        private String email;

        String getName() {
            return name;
        }

        void setName(String name) {
            this.name = name;
        }

        String getPhone() {
            return phone;
        }

        void setPhone(String phone) {
            this.phone = phone;
        }

        String getEmail() {
            return email;
        }

        void setEmail(String email) {
            this.email = email;
        }
    }


}
