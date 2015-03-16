package uk.org.nbn.nbnv.api.nxf;

import java.io.IOException;
import java.io.LineNumberReader;
import java.util.Arrays;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import org.junit.Test;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

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
        assertEquals("Expected the correct data in the header", header.getValues(), Arrays.asList("First", "Line"));
        assertEquals("Expected the correct data in the first line", data.getValues(), Arrays.asList("One column", "Two column"));
    }
    
    @Test
    public void checkThatReaderReturnsNullLineWhenNoDataIsRead() throws IOException {
        //Given
        LineNumberReader reader = mock(LineNumberReader.class);
        NXFReader nxfReader = new NXFReader(reader);
        when(reader.readLine()).thenReturn(null);
        
        //When
        NXFLine line = nxfReader.readLine();
        
        //Then
        assertNull("Expected no data to be read", line);
    }
}
