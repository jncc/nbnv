/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.org.nbn.nbnv.api.model;

/**
 *
 * @author Paul Gilbertson
 */
public class Organisation {
    private int organisationID;
    private String name;
    private String abbreviation;
    private String summary;
    private String address;
    private String postcode;
    private String phone;
    private String contactName;
    private String contactEmail;
    private String website;
    private boolean allowPublicRegistration;
    private String logo;
    private String logoSmall;

    /**
     * @return the organisationID
     */
    public int getOrganisationID() {
        return organisationID;
    }

    /**
     * @param organisationID the organisationID to set
     */
    public void setOrganisationID(int organisationID) {
        this.organisationID = organisationID;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the abbreviation
     */
    public String getAbbreviation() {
        return abbreviation;
    }

    /**
     * @param abbreviation the abbreviation to set
     */
    public void setAbbreviation(String abbreviation) {
        this.abbreviation = abbreviation;
    }

    /**
     * @return the summary
     */
    public String getSummary() {
        return summary;
    }

    /**
     * @param summary the summary to set
     */
    public void setSummary(String summary) {
        this.summary = summary;
    }

    /**
     * @return the address
     */
    public String getAddress() {
        return address;
    }

    /**
     * @param address the address to set
     */
    public void setAddress(String address) {
        this.address = address;
    }

    /**
     * @return the postcode
     */
    public String getPostcode() {
        return postcode;
    }

    /**
     * @param postcode the postcode to set
     */
    public void setPostcode(String postcode) {
        this.postcode = postcode;
    }

    /**
     * @return the phone
     */
    public String getPhone() {
        return phone;
    }

    /**
     * @param phone the phone to set
     */
    public void setPhone(String phone) {
        this.phone = phone;
    }

    /**
     * @return the contactName
     */
    public String getContactName() {
        return contactName;
    }

    /**
     * @param contactName the contactName to set
     */
    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    /**
     * @return the contactEmail
     */
    public String getContactEmail() {
        return contactEmail;
    }

    /**
     * @param contactEmail the contactEmail to set
     */
    public void setContactEmail(String contactEmail) {
        this.contactEmail = contactEmail;
    }

    /**
     * @return the website
     */
    public String getWebsite() {
        return website;
    }

    /**
     * @param website the website to set
     */
    public void setWebsite(String website) {
        this.website = website;
    }

    /**
     * @return the allowPublicRegistration
     */
    public boolean isAllowPublicRegistration() {
        return allowPublicRegistration;
    }

    /**
     * @param allowPublicRegistration the allowPublicRegistration to set
     */
    public void setAllowPublicRegistration(boolean allowPublicRegistration) {
        this.allowPublicRegistration = allowPublicRegistration;
    }

    /**
     * @return the logo
     */
    public String getLogo() {
        return logo;
    }

    /**
     * @param logo the logo to set
     */
    public void setLogo(String logo) {
        this.logo = logo;
    }

    /**
     * @return the logoSmall
     */
    public String getLogoSmall() {
        return logoSmall;
    }

    /**
     * @param logoSmall the logoSmall to set
     */
    public void setLogoSmall(String logoSmall) {
        this.logoSmall = logoSmall;
    }
}
