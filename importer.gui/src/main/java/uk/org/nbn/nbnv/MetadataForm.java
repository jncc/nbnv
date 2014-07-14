package uk.org.nbn.nbnv;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;


/**
 * @author stephen batty
 *         Date: 10/07/14
 *         Time: 14:54
 */
public class MetadataForm implements JsonBean {
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
    private String geographicResolution; // radio button
    private String recordAttributes; // radio button
    private String recorderNames; // radio button
    private String insertionType;
    private String adminName;
    private String adminPhone;
    private String adminEmail;

    public MetadataForm() {
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MetadataForm that = (MetadataForm) o;

        if (!accessConstraints.equals(that.accessConstraints)) return false;
        if (!additionalInfo.equals(that.additionalInfo)) return false;
        if (!adminEmail.equals(that.adminEmail)) return false;
        if (!adminName.equals(that.adminName)) return false;
        if (!adminPhone.equals(that.adminPhone)) return false;
        if (!dataQuality.equals(that.dataQuality)) return false;
        if (!description.equals(that.description)) return false;
        if (!geographicResolution.equals(that.geographicResolution)) return false;
        if (!geographicalCoverage.equals(that.geographicalCoverage)) return false;
        if (!insertionType.equals(that.insertionType)) return false;
        if (!methodsOfDataCapture.equals(that.methodsOfDataCapture)) return false;
        if (!organisation.equals(that.organisation)) return false;
        if (!purposeOfDataCapture.equals(that.purposeOfDataCapture)) return false;
        if (!recordAttributes.equals(that.recordAttributes)) return false;
        if (!recorderNames.equals(that.recorderNames)) return false;
        if (!temporalCoverage.equals(that.temporalCoverage)) return false;
        if (!title.equals(that.title)) return false;
        if (!useConstraints.equals(that.useConstraints)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = title.hashCode();
        result = 31 * result + organisation.hashCode();
        result = 31 * result + description.hashCode();
        result = 31 * result + methodsOfDataCapture.hashCode();
        result = 31 * result + purposeOfDataCapture.hashCode();
        result = 31 * result + geographicalCoverage.hashCode();
        result = 31 * result + temporalCoverage.hashCode();
        result = 31 * result + dataQuality.hashCode();
        result = 31 * result + additionalInfo.hashCode();
        result = 31 * result + useConstraints.hashCode();
        result = 31 * result + accessConstraints.hashCode();
        result = 31 * result + geographicResolution.hashCode();
        result = 31 * result + recordAttributes.hashCode();
        result = 31 * result + recorderNames.hashCode();
        result = 31 * result + insertionType.hashCode();
        result = 31 * result + adminName.hashCode();
        result = 31 * result + adminPhone.hashCode();
        result = 31 * result + adminEmail.hashCode();
        return result;
    }

    public MetadataForm(Builder builder) {
        this.accessConstraints = builder.accessConstraints;
        this.additionalInfo = builder.additionalInfo;
        this.adminEmail = builder.adminEmail;
        this.adminName = builder.adminName;
        this.adminPhone = builder.adminPhone;
        this.dataQuality = builder.dataQuality;
        this.description = builder.description;
        this.geographicalCoverage = builder.geographicalCoverage;
        this.geographicResolution = builder.geographicalResolution;
        this.insertionType = builder.insertionType;
        this.methodsOfDataCapture = builder.methodsOfDataCapture;
        this.purposeOfDataCapture = builder.purposeOfDataCapture;
        this.organisation = builder.organsiation;
        this.recordAttributes = builder.recordAttributes;
        this.recorderNames = builder.recorderNames;

        this.temporalCoverage = builder.temporalCoverage;
        this.title = builder.title;

    }

    /**
     * Returns test instance populated with dummy data
     * **/
    /*this would be better static but it is used in jsonbean in testing*/
    @Override
    public MetadataForm SampleTestData() {
        return new Builder()
                .access("test access")
                .accessConstraints("test access constraints")
                .additionalInfo("test additonal information")
                .adminEmail("test@googel.com")
                .adminName("samuel t herring")
                .adminPhone("0123456 (b)")
                .dataQuality("test data quality")
                .description("test description")
                .geographicalCoverage("test geo coverage")
                .geographicalResolution("test geo resolution")
                .insertionType("insertion type")
                .methodsOfDataCapture("mmethod of data capture")
                .purposeOfDataCapture("purporse data capture")
                .recordAttributes("record attr")
                .recorderNames("record names")
                .temporalCoverage("temporal coverage")
                .title("my title")
                .useContraints("use constraints")
                .build();

    }
    @Override
    public String toJson() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper(); // this would be better injcted but not a spring managed bean
        return mapper.writeValueAsString(this);
    }
    @Override
    public MetadataForm fromJson(String json) throws IOException {
        ObjectMapper mapper = new ObjectMapper(); // this would be better injcted but not a spring managed bean
        return mapper.readValue(json, MetadataForm.class);
    }


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

    public String getGeographicResolution() {
        return geographicResolution;
    }

    public void setGeographicResolution(String geographicResolution) {
        this.geographicResolution = geographicResolution;
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

    public String getName() {
        return adminName;
    }

    public void setName(String name) {
        this.adminName = name;
    }

    public String getPhone() {
        return adminPhone;
    }

    public void setPhone(String adminPhone) {
        this.adminPhone = adminPhone;
    }

    public String getAdminEmail() {
        return adminEmail;
    }

    public void setAdminEmail(String adminEmail) {
        this.adminEmail = adminEmail;
    }

    private static class Builder {
        private String title;
        private String organsiation;
        private String description;
        private String methodsOfDataCapture;
        private String purposeOfDataCapture;
        private String geographicalCoverage;
        private String temporalCoverage;
        private String dataQuality;
        private String additionalInfo;
        private String useContraints;
        private String accessConstraints;
        private String geographicalResolution;
        private String adminName;
        private String adminPhone;
        private String adminEmail;
        private String access;
        private String recordAttributes;
        private String recorderNames;
        private String insertionType;

        public Builder() {
        }

        public Builder title(String title) {
            this.title = title;
            return this;
        }

        public Builder organsiation(String organsiation) {
            this.organsiation = organsiation;
            return this;
        }

        public Builder description(String description) {
            this.description = description;
            return this;
        }

        public Builder methodsOfDataCapture(String methodsOfDataCapture) {
            this.methodsOfDataCapture = methodsOfDataCapture;
            return this;
        }

        public Builder purposeOfDataCapture(String purposeOfDataCapture) {
            this.purposeOfDataCapture = purposeOfDataCapture;
            return this;
        }

        public Builder geographicalCoverage(String geographicalCoverage) {
            this.geographicalCoverage = geographicalCoverage;
            return this;
        }

        public Builder temporalCoverage(String temporalCoverage) {
            this.temporalCoverage = temporalCoverage;
            return this;
        }

        public Builder dataQuality(String dataQuality) {
            this.dataQuality = dataQuality;
            return this;
        }

        public Builder additionalInfo(String additionalInfo) {
            this.additionalInfo = additionalInfo;
            return this;
        }

        public Builder useContraints(String useContraints) {
            this.useContraints = useContraints;
            return this;
        }

        public Builder accessConstraints(String accessConstraints) {
            this.accessConstraints = accessConstraints;
            return this;
        }

        public Builder geographicalResolution(String geographicalResolution) {
            this.geographicalResolution = geographicalResolution;
            return this;
        }

        public Builder adminName(String adminName) {
            this.adminName = adminName;
            return this;
        }

        public Builder adminPhone(String adminPhone) {
            this.adminPhone = adminPhone;
            return this;
        }

        public Builder adminEmail(String adminEmail) {
            this.adminEmail = adminEmail;
            return this;
        }

        public Builder access(String access) {
            this.access = access;
            return this;
        }

        public Builder recordAttributes(String recordAttributes) {
            this.recordAttributes = recordAttributes;
            return this;
        }

        public Builder recorderNames(String recorderNames) {
            this.recorderNames = recorderNames;
            return this;
        }

        public Builder insertionType(String insertionType) {
            this.insertionType = insertionType;
            return this;
        }

        public MetadataForm build() {
            return new MetadataForm(this);
        }
    }

}
