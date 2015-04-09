package uk.org.nbn.nbnv.api.nxf;

import java.io.IOException;
import java.io.LineNumberReader;
import java.util.Arrays;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;
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
                .thenReturn("One column")
                .thenReturn(null);
        
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
                .thenReturn("One column \t Two column")
                .thenReturn(null);
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
    
    @Test(expected=IOException.class)
    public void checkThatTooManyColumnsCausesAnError() throws IOException {
        //Given
        LineNumberReader input = mock(LineNumberReader.class);
        when(input.readLine())
                .thenReturn("First      \t Line")
                .thenReturn("One column \t Two column \t Three column")
                .thenReturn(null);
        NXFReader reader = new NXFReader(input);
        
        //When
        NXFLine header = reader.readLine();
        NXFLine data = reader.readLine();
        
        //Then
        fail("Expected to fail with an ioexception");
    }
    
    @Test(expected=IOException.class)
    public void checkThatTooFewColumnsCausesAnError() throws IOException {
        //Given
        LineNumberReader input = mock(LineNumberReader.class);
        when(input.readLine())
                .thenReturn("First      \t Line")
                .thenReturn("One column")
                .thenReturn(null);
        NXFReader reader = new NXFReader(input);
        
        //When
        NXFLine header = reader.readLine();
        NXFLine data = reader.readLine();
        
        //Then
        fail("Expected to fail with an ioexception");
    }
    
    @Test
    public void checkThatCanReadValuesOverMultipleLines() throws IOException {
        //Given
        LineNumberReader input = mock(LineNumberReader.class);
        when(input.readLine())
                .thenReturn("First      \t Line")
                .thenReturn("Some data")
                .thenReturn("Split over")
                .thenReturn("Multiple")
                .thenReturn("Lines \t other")
                .thenReturn("Next \t Line")
                .thenReturn(null);
        NXFReader reader = new NXFReader(input);
        
        //When
        NXFLine header = reader.readLine();
        NXFLine data1 = reader.readLine();
        NXFLine data2 = reader.readLine();
        
        //Then
        assertEquals("Expected the correct data in the header", header.getValues(), Arrays.asList("First", "Line"));
        assertEquals("Expected the correct data in the first line", data1.getValues(), Arrays.asList("Some data\nSplit over\nMultiple\nLines", "other"));
        assertEquals("Expected the correct data in the second line", data2.getValues(), Arrays.asList("Next", "Line"));
    }
    
    @Test
    public void checkThatDataCanBeReadFromMultipleLinesInLastColumn() throws IOException {
        //Given
        LineNumberReader input = mock(LineNumberReader.class);
        when(input.readLine())
                .thenReturn("First      \t Line")
                .thenReturn("other \t Some data")
                .thenReturn("Split over")
                .thenReturn("Multiple")
                .thenReturn("Lines")
                .thenReturn("Next \t Line")
                .thenReturn(null);
        NXFReader reader = new NXFReader(input);
        
        //When
        NXFLine header = reader.readLine();
        NXFLine data1 = reader.readLine();
        NXFLine data2 = reader.readLine();
        
        //Then
        assertEquals("Expected the correct data in the header", header.getValues(), Arrays.asList("First", "Line"));
        assertEquals("Expected the correct data in the first line", data1.getValues(), Arrays.asList("other", "Some data\nSplit over\nMultiple\nLines"));
        assertEquals("Expected the correct data in the second line", data2.getValues(), Arrays.asList("Next", "Line"));
    }
    
    @Test(expected=IOException.class)
    public void checkThatNXFLineWhichAppearsOverMultipleLinesAndIsTooLongCausesException() throws IOException {
        //Given
        LineNumberReader input = mock(LineNumberReader.class);
        when(input.readLine())
                .thenReturn("Heading")
                .thenReturn("A value which is")
                .thenReturn("seperated over many lines")
                .thenReturn(null);
        NXFReader reader = new NXFReader(input, 40);
        
        //When
        NXFLine header = reader.readLine();
        NXFLine data = reader.readLine();
        
        //Then
        fail("Expected to fail with IOException as the line was too long");
    }
}
