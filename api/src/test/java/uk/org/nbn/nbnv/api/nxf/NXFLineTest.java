package uk.org.nbn.nbnv.api.nxf;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import static org.junit.Assert.assertEquals;
import org.junit.Test;

/**
 *
 * @author cjohn
 */
public class NXFLineTest {
    @Test
    public void checkThatNXFLineCopiesTheContentOfTheValuesList() {
        //Given
        List<String> columns = new ArrayList<>();
        columns.add("test");
        NXFLine line = new NXFLine(columns);
        
        //When
        columns.add("some more data added later");
        
        //Then
        assertEquals("Expected the line to not have changed", "test", line.toString());
        assertEquals("Expected the line to not have changed", Arrays.asList("test"), line.getValues());
    }
    
    @Test
    public void checkThatAppendingColumnDoesNotMutateOriginal() {
        //Given
        NXFLine line = new NXFLine("hello");
        
        //When
        line.append("another column");
        
        //Then
        assertEquals("Expected line to stay the same", "hello", line.toString());
    }
    
    @Test
    public void checkThatAppendCreatesNewLineWithExtraColumn() {
        //Given
        NXFLine line = new NXFLine("first");
        
        //When
        NXFLine newLine = line.append("extra");
        
        //Then
        assertEquals("Expected extra column", "first\textra", newLine.toString());
    }
}
