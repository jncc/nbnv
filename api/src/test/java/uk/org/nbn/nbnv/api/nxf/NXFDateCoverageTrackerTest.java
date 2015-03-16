package uk.org.nbn.nbnv.api.nxf;

import java.util.Calendar;
import static java.util.Calendar.DAY_OF_MONTH;
import static java.util.Calendar.MONTH;
import static java.util.Calendar.YEAR;
import java.util.Date;
import static org.junit.Assert.assertEquals;
import org.junit.Test;

/**
 * Beware, in java months start at 0, not 1.
 * @author cjohn
 */
public class NXFDateCoverageTrackerTest {
    @Test
    public void checkThatCanDetermineDateRangeWithStartDateAndEndDateColumns() {
        //Given
        NXFLine header = new NXFLine("StartDate\tEndDate");
        NXFDateCoverageTracker tracker = new NXFDateCoverageTracker(header);
        
        //When
        tracker.read(new NXFLine("03/10/2006\t07/11/2011"));
        tracker.read(new NXFLine("03/10/2004\t02/12/2006"));
        Calendar start = getCalendar(tracker.getEarliestDate());
        Calendar end = getCalendar(tracker.getLatestDate());
        
        //Then
        assertEquals("Expected to get start year",  2004, start.get(YEAR));
        assertEquals("Expected to get start month", 9,    start.get(MONTH));
        assertEquals("Expected to get start day",   3,    start.get(DAY_OF_MONTH));
        
        assertEquals("Expected to get end year",    2011, end.get(YEAR));
        assertEquals("Expected to get end month",   10,   end.get(MONTH));
        assertEquals("Expected to get end day",     7,    end.get(DAY_OF_MONTH));
    }
    
    @Test
    public void checkThatCanDetermineDataRangeWithSingleDateColumn() {
        //Given
        NXFLine header = new NXFLine("Date");
        NXFDateCoverageTracker tracker = new NXFDateCoverageTracker(header);
        
        //When
        tracker.read(new NXFLine("03/05/2006"));
        tracker.read(new NXFLine("23/06/2004"));
        Calendar start = getCalendar(tracker.getEarliestDate());
        Calendar end = getCalendar(tracker.getLatestDate());
        
        //Then
        assertEquals("Expected to get start year",  2004, start.get(YEAR));
        assertEquals("Expected to get start month", 5,    start.get(MONTH));
        assertEquals("Expected to get start day",   23,   start.get(DAY_OF_MONTH));
        
        assertEquals("Expected to get end year",    2006, end.get(YEAR));
        assertEquals("Expected to get end month",   4,    end.get(MONTH));
        assertEquals("Expected to get end day",     3,    end.get(DAY_OF_MONTH));
    }
    
    @Test
    public void checkThatSkipsOverInvalidDates() {
        //Given
        NXFLine header = new NXFLine("Date");
        NXFDateCoverageTracker tracker = new NXFDateCoverageTracker(header);
        
        //When
        tracker.read(new NXFLine("03/05/2006"));
        tracker.read(new NXFLine("This isn't a date?!?"));
        tracker.read(new NXFLine("23/06/2004"));
        Calendar start = getCalendar(tracker.getEarliestDate());
        Calendar end = getCalendar(tracker.getLatestDate());
        
        //Then
        assertEquals("Expected to get start year",  2004, start.get(YEAR));        
        assertEquals("Expected to get end year",    2006, end.get(YEAR));
    }
    
    private Calendar getCalendar(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar;
    }
}
