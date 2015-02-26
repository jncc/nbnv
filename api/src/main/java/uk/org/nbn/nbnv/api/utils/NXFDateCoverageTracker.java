package uk.org.nbn.nbnv.api.utils;


import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import uk.org.nbn.nbnv.api.utils.NXFReader.NXFLine;

/**
 *
 * @author cjohn
 */
public class NXFDateCoverageTracker {
    private static final List<SimpleDateFormat> VALID_DATE_FORMATS = Arrays.asList(
            new SimpleDateFormat("dd/MM/yyyy"),
            new SimpleDateFormat("dd-MM-yyyy"),
            new SimpleDateFormat("yyyy/MM/dd"),
            new SimpleDateFormat("yyyy-MM-dd"),
            new SimpleDateFormat("dd MMM yyyy"),
            new SimpleDateFormat("MMM yyyy"),
            new SimpleDateFormat("yyyy"));
    
    private final int startDateCol;
    private final int endDateCol;
    private Date earliestDate;
    private Date latestDate;
    
    public NXFDateCoverageTracker(NXFLine header) {
        List<String> columns = header.getColumns(true);
        if(columns.contains("STARTDATE") && columns.contains("ENDDATE")) {
            this.startDateCol = columns.indexOf("STARTDATE");
            this.endDateCol   = columns.indexOf("ENDDATE");
        }
        else if(columns.contains("DATE")) {
            this.startDateCol = columns.indexOf("DATE");
            this.endDateCol = columns.indexOf("DATE");
        }
        else {
            throw new IllegalArgumentException("Either Date or StartDate and EndDate must be provided");
        }
        
        this.earliestDate = new Date();
        this.latestDate = new Date(Long.MIN_VALUE);
    }

    public Date getEarliestDate() {
        return earliestDate;
    }
    
    public Date getLatestDate() {
        return latestDate;
    }
    
    public void read(NXFLine line) {
        List<String> data = line.getColumns();
        Date startDate = parseDate(data.get(startDateCol), earliestDate);
        if(startDate.before(earliestDate)) {
            earliestDate = startDate;
        }
        Date endDate = parseDate(data.get(endDateCol), latestDate);
        if(endDate.after(latestDate)) {
            latestDate = endDate;
        }
    }
    
    private Date parseDate(String date, Date defaultVal) {
        for(DateFormat format: VALID_DATE_FORMATS) {
            try {
                return format.parse(date);
            } catch (ParseException ex) {} //Failed this date format, try next
        }
        return defaultVal;
    }
}
