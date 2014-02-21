package uk.org.nbn.nbnv.api.model;

import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Matt Debont
 */
@XmlRootElement
public class DownloadStat {
    private int id;
    private String name;
    private String extra;
    private int total;
    private int totalAlt;

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

    public String getExtra() {
        return extra;
    }

    public void setExtra(String extra) {
        this.extra = extra;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }
    
    public int getTotalAlt() {
        return totalAlt;
    }

    public void setTotalAlt(int totalAlt) {
        this.totalAlt = totalAlt;
    }
}
