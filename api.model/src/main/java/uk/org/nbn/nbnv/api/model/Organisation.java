package uk.org.nbn.nbnv.api.model;

import com.sun.jersey.server.linking.Ref;
import java.net.URI;

public class Organisation {
    private int id;
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
    
    private boolean hasLogo, hasSmallLogo;
    @Ref(value="organisations/${instance.id}/logo", condition="${instance.hasLogo}", style=Ref.Style.ABSOLUTE) 
    private URI logo;
    @Ref(value="organisations/${instance.id}/logosmall", condition="${instance.hasSmallLogo}", style=Ref.Style.ABSOLUTE)
    private URI smallLogo;
    
    @Ref(value="${resource.portalUrl}/Organisations/${instance.id}", style=Ref.Style.RELATIVE_PATH) 
    private URI href;

    public URI getHref() {
        return href;
    }

    public void setHref(URI href) {
        this.href = href;
    }
    
    public void setHasLogo(boolean hasLogo) {
        this.hasLogo = hasLogo;
    }

    public void setHasSmallLogo(boolean hasSmallLogo) {
        this.hasSmallLogo = hasSmallLogo;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAbbreviation() {
        return abbreviation;
    }

    public void setAbbreviation(String abbreviation) {
        this.abbreviation = abbreviation;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPostcode() {
        return postcode;
    }

    public void setPostcode(String postcode) {
        this.postcode = postcode;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public String getContactEmail() {
        return contactEmail;
    }

    public void setContactEmail(String contactEmail) {
        this.contactEmail = contactEmail;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public boolean isAllowPublicRegistration() {
        return allowPublicRegistration;
    }

    public void setAllowPublicRegistration(boolean allowPublicRegistration) {
        this.allowPublicRegistration = allowPublicRegistration;
    }

    public URI getLogo() {
        return logo;
    }

    public void setLogo(URI logo) {
        this.logo = logo;
    }

    public URI getSmallLogo() {
        return smallLogo;
    }

    public void setSmallLogo(URI smallLogo) {
        this.smallLogo = smallLogo;
    }

    public boolean isHasLogo() {
        return hasLogo;
    }

    public boolean isHasSmallLogo() {
        return hasSmallLogo;
    }
}
