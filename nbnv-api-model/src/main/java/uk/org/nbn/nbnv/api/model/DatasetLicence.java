package uk.org.nbn.nbnv.api.model;

import com.sun.jersey.server.linking.Ref;
import java.net.URI;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class DatasetLicence {

    private int id;
    private String abbreviation;
    private String name;
    private String summary;
    private String href;
    
    private boolean hasImg;
    @Ref(value="datasetLicence/${instance.id}/img", condition="${instance.hasImg}", 
            style=Ref.Style.ABSOLUTE) 
    private URI img_href;

    public DatasetLicence() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAbbreviation() {
        return abbreviation;
    }

    public void setAbbreviation(String abbreviation) {
        this.abbreviation = abbreviation;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getHref() {
        return href;
    }

    public void setHref(String href) {
        this.href = href;
    }

    public boolean isHasImg() {
        return hasImg;
    }

    public void setHasImg(boolean hasImg) {
        this.hasImg = hasImg;
    }

    public URI getImg_href() {
        return img_href;
    }

    public void setImg_href(URI img_href) {
        this.img_href = img_href;
    }
}
