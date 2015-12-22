package uk.org.nbn.nbnv.api.model;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class DateTypeStats {
    private String dateTypeName;
    private int recordCount;
    
    public DateTypeStats(){}
    
    public DateTypeStats(String dateTypeName, int recordCount){
        this.dateTypeName = dateTypeName;
        this.recordCount = recordCount;
    }

    public String getDateTypeName() {
        return dateTypeName;
    }

    public void setDateTypeName(String dateTypeName) {
        this.dateTypeName = dateTypeName;
    }

    public int getRecordCount() {
        return recordCount;
    }

    public void setRecordCount(int recordCount) {
        this.recordCount = recordCount;
    }
}
