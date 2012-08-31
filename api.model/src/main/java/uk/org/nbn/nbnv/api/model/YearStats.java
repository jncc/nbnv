package uk.org.nbn.nbnv.api.model;

public class YearStats {
    
    private int year, recordCount;
    
    public YearStats(){}
    
    public YearStats(int year, int recordCount){
        this.year = year;
        this.recordCount = recordCount;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getRecordCount() {
        return recordCount;
    }

    public void setRecordCount(int recordCount) {
        this.recordCount = recordCount;
    }
    
}
