package uk.org.nbn.nbnv.api.utils;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author cjohn
 */
public class NXFReader implements Closeable {
    private final LineNumberReader reader;
    private int columns = -1;
    
    public NXFReader(InputStream in) {
        this.reader = new LineNumberReader(new InputStreamReader(in));
    }
    
    public NXFLine readLine() throws IOException {
        NXFLine line = new NXFLine(reader.readLine());
        int currLineColumns = line.getColumns().size();
        if(columns != -1 && columns != line.getColumns().size()) {
            throw new IOException("Line number " + reader.getLineNumber() + " contains the wrong amount of columns");
        }
        columns = currLineColumns;
        return line;
    }
    
    public boolean ready() throws IOException {
        return reader.ready();
    }
    
    @Override
    public void close() throws IOException {
        reader.close();
    }
    
    public static class NXFLine {
        private final String line;
        private NXFLine(String line) {
            this.line = line;
        }
        
        /**
         * @return the original line read (with tabs in place) from the nxf
         */
        @Override
        public String toString() {
            return line;
        }
        
        /**
         * Grabs the columns of this line, splits them and trims the content.
         * Optionally uppercasing all of the values in the list
         * @param uppercase if the text in the line should be uppercased
         * @return the tabbed line split and optionally uppercased
         */
        public List<String> getColumns(boolean uppercase) {
            return splitLine( uppercase ? line.toUpperCase() : line);
        }
        
        /**
         * Get the columns of data
         * @return the tabbed line split
         */
        public List<String> getColumns() {
            return getColumns(false);
        }
        
        private List<String> splitLine(String line) {
            List<String> toReturn = new ArrayList<>();
            for(String part: line.split("\t")) {
                toReturn.add(part.trim());
            }
            return toReturn;
        }
    }
}
