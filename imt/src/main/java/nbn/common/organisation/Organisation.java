package nbn.common.organisation;

/**
*
* @author	    :- Christopher Johnson
* @date		    :- 10-Jan-2011
* @description	    :- The following class represents the structure of an Organisation
*/
public class Organisation {
    private int organisationKey;
    private boolean allowPublicRegistration;
    private int emailAlertType;
    private String abbreviation, postcode,  contactEmail, name, contactName, address, summary;
    private String website, logoURL;

    Organisation(int organisationKey) {
	this.organisationKey = organisationKey;
    }

    void setAbbreviation(String abbreviation) {
	this.abbreviation = abbreviation;
    }

    void setAddress(String address) {
	this.address = address;
    }

    void setAllowPublicRegistration(boolean allowPublicRegistration) {
	this.allowPublicRegistration = allowPublicRegistration;
    }

    void setContactEmail(String contactEmail) {
	this.contactEmail = contactEmail;
    }

    void setContactName(String contactName) {
	this.contactName = contactName;
    }

    void setEmailAlertType(int emailAlertType) {
	this.emailAlertType = emailAlertType;
    }

    void setLogoURL(String logoURL) {
	this.logoURL = logoURL;
    }

    void setOrganisationName(String name) {
	this.name = name;
    }

    void setPostcode(String postcode) {
	this.postcode = postcode;
    }

    void setSummary(String summary) {
	this.summary = summary;
    }

    void setWebsite(String website) {
	this.website = website;
    }

    public String getAbbreviation() {
	return abbreviation;
    }

    public String getAddress() {
	return address;
    }

    public boolean isAllowPublicRegistration() {
	return allowPublicRegistration;
    }

    public String getContactEmail() {
	return contactEmail;
    }

    public String getContactName() {
	return contactName;
    }

    public int getEmailAlertType() {
	return emailAlertType;
    }

    public String getLogoURL() {
	return logoURL;
    }

    public String getOrganisationName() {
	return name;
    }

    public int getOrganisationKey() {
	return organisationKey;
    }

    public String getPostcode() {
	return postcode;
    }

    public String getSummary() {
	return summary;
    }

    public String getWebsite() {
	return website;
    }

    public String getThumbnailLogoUrl() {
        String fileExtension;
        String filename;
        String filepath;
        String[] filetypeFinder = logoURL.split("\\.");

        if (filetypeFinder.length > 1) {
            fileExtension = filetypeFinder[(filetypeFinder.length - 1)];
            String[] fileNameFinder = filetypeFinder[(filetypeFinder.length - 2)].split("\\/");
            if (fileNameFinder.length > 1) {
                filename = fileNameFinder[(fileNameFinder.length - 1)];
                filepath = logoURL.substring(0, (logoURL.length() - fileExtension.length() - filename.length() - 1)); //length of the full URL, - length of filename, -1 because of ., - fileextension
                return "http://data.nbn.org.uk/" + filepath + "th_" + filename + "." + fileExtension;
            }
        }
        return null;
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof Organisation) {
            if (((Organisation)o).getOrganisationKey() == this.organisationKey)
                return true;
        }

        return false;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 71 * hash + this.organisationKey;
        return hash;
    }
}
