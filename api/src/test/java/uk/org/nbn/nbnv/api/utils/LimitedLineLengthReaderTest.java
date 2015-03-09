package uk.org.nbn.nbnv.api.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import org.junit.Test;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 *
 * @author cjohn
 */
public class LimitedLineLengthReaderTest {
    @Test(expected=IOException.class)
    public void checkThatFailsWhenLineContainsTooManyCharacters() throws IOException {
        //Given (Line with 41 chars)
        String data = "AbAbAbAbAbAbAbAbAbAbAbAbAbAbAbAbAbAbAbAbA\n" +
                      "NextLine\n";
        BufferedReader reader = new BufferedReader(new LimitedLineLengthReader(new StringReader(data), 40));
        
        //When
        String line = reader.readLine(); 
        
        //Then
        fail("Expected to fail with IOException");
    }
    
    @Test
    public void checkDoesntFailWhenLineLengthIsUnderLimit() throws IOException {
        //Given
        String data = "This line is under 40 chars long\n" +
                      "NextLine\n";
        BufferedReader reader = new BufferedReader(new LimitedLineLengthReader(new StringReader(data), 40));
        
        //When
        String line1 = reader.readLine(); 
        String line2 = reader.readLine(); 
        
        //Then
        assertEquals("Expected first line", line1, "This line is under 40 chars long");
        assertEquals("Expected second line", line2, "NextLine");
    }
    
    @Test
    public void checkThatDelegatesCloseToReader() throws IOException {
        //Given
        Reader reader = mock(Reader.class);
        LimitedLineLengthReader lineReader = new LimitedLineLengthReader(reader);
        
        //When
        lineReader.close();
        
        //Then
        verify(reader).close();
    }
    
    @Test
    public void checkThatDelegatesReadyToReader() throws IOException {
        //Given
        Reader reader = mock(Reader.class);
        when(reader.ready()).thenReturn(true);
        LimitedLineLengthReader lineReader = new LimitedLineLengthReader(reader);
        
        //When
        boolean ready = lineReader.ready();
        
        //Then
        verify(reader).ready();
        assertTrue("Expected value from mock", ready);
    }
}
