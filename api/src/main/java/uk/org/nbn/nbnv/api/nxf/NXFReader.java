package uk.org.nbn.nbnv.api.nxf;

import java.io.Closeable;
import java.io.IOException;
import java.io.LineNumberReader;
import java.io.Reader;

/**
 * The following class will read in a NBN Exchange formated file line by line.
 * It will ensure that the amount of values read per line are the same.
 * @author cjohn
 */
public class NXFReader implements Closeable {
    private final LineNumberReader reader;
    private int columns = -1;
    
    public NXFReader(LineNumberReader reader) {
        this.reader = reader;
    }
     
    public NXFReader(Reader reader) {
        this.reader = new LineNumberReader(reader);
    }    
    
    /**
     * Read the current line from the stream and return it as a NXFLine object.
     * If the amount of columns for this line differs to ones which have been 
     * previously read, then the method will throw an IOException as the NXF 
     * stream is corrupt
     * @return the current line which has been read or null if there is no line
     * @throws IOException if there was a problem reading the line or the line
     *  contained the wrong amount of columns
     */
    public NXFLine readLine() throws IOException {
        String readLine = reader.readLine();
        if(readLine != null) {
            NXFLine line = new NXFLine(readLine);
            int currLineColumns = line.getValues().size();
            if(columns != -1 && columns != line.getValues().size()) {
                throw new IOException("Line number " + reader.getLineNumber() + " contains the wrong amount of columns");
            }
            columns = currLineColumns;
            return line;
        }
        else {
            return null;
        }
    }
    
    /**
     * Check if there is any content on the stream to read
     * @return true if there are more lines to read
     * @throws IOException if there was a problem reading from the stream
     */
    public boolean ready() throws IOException {
        return reader.ready();
    }
    
    @Override
    public void close() throws IOException {
        reader.close();
    }
}
