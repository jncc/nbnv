package uk.org.nbn.nbnv.api.utils;

import java.io.IOException;
import java.io.Reader;

/**
 * The BufferedReader class allows reading a stream in terms on lines. These 
 * lines are then returned as Strings. When dealing with external streams
 * (and potentially malicious) we may want to limit the amount of memory used 
 * to read a line. If a line is too long, we can assume that the data is incorrect
 * 
 * @see NXFReader
 * @see java.io.BufferedReader
 * @author cjohn
 */
public class LimitedLineLengthReader extends Reader {
    private static final int DEFAULT_LINE_LIMIT = 1048576;
    private int count;
    private final Reader reader;
    private final int limit;
    
    /**
     * Constructs a Limited Line Length Reader, which will only allow up to 
     * 1 Mebibyte of data to be present on a single line
     * @param reader to read from
     */
    public LimitedLineLengthReader(Reader reader) {
        this(reader, DEFAULT_LINE_LIMIT);
    }
    
    /**
     * Allows the construction of a Limited Line Length Reader which has a custom
     * line limit. Perhaps you want to enforce the 80 character limit
     * @param reader to read from
     * @param limit the maximum readable amount of characters on a line
     */
    public LimitedLineLengthReader(Reader reader, int limit) {
        this.reader = reader;
        this.limit = limit;
        this.count = 0;
    }
    
    @Override
    public int read(char[] buf, int off, int len) throws IOException {
        int read = reader.read(buf,off,len);
        for(int i=off; i<read+off; i++){
            if(buf[i] == '\n' || buf[i] == '\r'){
                count=0;
            }
            else if(++count > limit){
                throw new IOException("Line too long");
            }
        }
        return read;
    }
    
    @Override
    public boolean ready() throws IOException {
        return reader.ready();
    }
    
    @Override
    public void close() throws IOException {
        reader.close();
    }
}
