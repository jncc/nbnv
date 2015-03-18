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
    private static final int DEFAULT_LINE_LIMIT = 1048576;
    private final LineNumberReader reader;
    private final int limit;
    private int columns = -1;
    private String nextLine;
    
    public NXFReader(LineNumberReader reader, int limit) {
        this.reader = reader;
        this.limit = limit;
    }
    
    public NXFReader(LineNumberReader reader) {
        this(reader, DEFAULT_LINE_LIMIT);
    }
     
    public NXFReader(Reader reader) {
        this(new LineNumberReader(reader));
    }
    
    /**
     * Read the current line from the stream and return it as a NXFLine object.
     * If the amount of columns for this line differs to ones which have been 
     * previously read, then the method will throw an IOException as the NXF 
     * stream is corrupt.
     * 
     * An NXFLine may exist over multiple lines in an NXF file. This can happen
     * if there are carriage returns in a value. Any carriage returns in a given
     * value will be replaced with a 'space' character.
     * @return the current line which has been read or null if there is no line
     * @throws IOException if there was a problem reading the line, the line
     *  contained the wrong amount of columns or the length of a line which was
     *  concatenated together was too long.
     */
    public NXFLine readLine() throws IOException {
        String readLine = nextLine == null ? reader.readLine() : nextLine;        
        if(readLine != null) {
            // If we have not yet populated the columsn value then we haven't
            // yet read the Header line in. The header line is our reference to
            // how many columns are in the NXF input.
            if(columns == -1) {
                NXFLine firstLine = new NXFLine(readLine);
                columns = firstLine.getValues().size();
                return firstLine;
            }
            
            // Delimiter-separated values (such as the nbn exchange format) may
            // have values which contain carriage returns. Lists are commonly
            // presented in this format. In order to read in the full line, we
            // may need to read in multiple lines of data in order to get a 
            // complete NXF line of data. Keep reading until we get more than
            // enough columns.
            while( (nextLine = reader.readLine()) != null ) {
                NXFLine concat = new NXFLine(readLine + " " + nextLine);
                
                if(concat.getLine().length() > limit) {
                    throw new IOException("Line number " + reader.getLineNumber() + " contains too much data");
                }
                else if(concat.getValues().size() > columns) {
                    break;
                }
                else {
                    readLine = concat.getLine(); //Update the readLine and carry on
                }
            }
            
            // The loop has completed, readLine will either contain the correct
            // amount of columns or too little. 
            NXFLine line = new NXFLine(readLine);
            if(line.getValues().size() == columns) {
                return line;
            }
            else {
                throw new IOException("Line number " + reader.getLineNumber() + " contains the wrong amount of columns");
            }
        }
        else {
            return null;
        }
    }
    
    @Override
    public void close() throws IOException {
        reader.close();
    }
}
