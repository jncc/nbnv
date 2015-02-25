package uk.org.nbn.nbnv.api.utils;

import java.io.IOException;
import java.io.LineNumberReader;
import java.util.Arrays;
import static org.junit.Assert.assertEquals;
import org.junit.Test;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import uk.org.nbn.nbnv.api.utils.NXFReader.NXFLine;

/**
 *
 * @author cjohn
 */
public class NXFReaderTest {
    
    @Test
    public void checkThatFailsToReadLineWhichHasTheWrongAmountOfColumns() throws IOException {
        //Given
        LineNumberReader input = mock(LineNumberReader.class);
        when(input.readLine())
                .thenReturn("First\tLine")
                .thenReturn("One column");
        
        when(input.getLineNumber()).thenReturn(300);
        NXFReader reader = new NXFReader(input);
        
        //When
        NXFLine header = reader.readLine();
        try {
            NXFLine data = reader.readLine();
        }
        
        //Then
        catch(IOException io) {
            assertEquals("Expected a error exception with line number", io.getMessage(), "Line number 300 contains the wrong amount of columns");
        }
    }
    
    @Test
    public void checkThatCanReadLineOfGoodData() throws IOException {
        //Given
        LineNumberReader input = mock(LineNumberReader.class);
        when(input.readLine())
                .thenReturn("First      \t Line")
                .thenReturn("One column \t Two column");
        NXFReader reader = new NXFReader(input);
        
        //When
        NXFLine header = reader.readLine();
        NXFLine data = reader.readLine();

        //Then
        assertEquals("Expected the correct data in the header", header.getColumns(), Arrays.asList("First", "Line"));
        assertEquals("Expected the correct data in the first line", data.getColumns(), Arrays.asList("One column", "Two column"));
    }
}
