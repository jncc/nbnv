package uk.org.nbn.nbnv.api.model;

import java.util.Date;
import java.util.List;

/**
 *
 * @author Matt Debont
 */
public class OrganisationSuppliedList {
    private int id;
    private String organisationName;
    private String name;
    private String code;
    private Date dateUploaded;
    private String description;
    private List<String> pTVKs;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getOrganisationName() {
        return organisationName;
    }

    public void setOrganisationName(String organisationName) {
        this.organisationName = organisationName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Date getDateUploaded() {
        return dateUploaded;
    }

    public void setDateUploaded(Date dateUploaded) {
        this.dateUploaded = dateUploaded;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<String> getpTVKs() {
        return pTVKs;
    }

    public void setpTVKs(List<String> pTVKs) {
        this.pTVKs = pTVKs;
    }

}
