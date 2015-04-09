package uk.org.nbn.nbnv.api.nxf;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.apache.commons.lang.StringUtils;

/**
 * A representation of a single line from an NBN Exchange formatted file.
 * This line could be either the Heading line (which states the column names)
 * or a values line.
 * 
 * An NXF Line is immutable, meaning that a given instance once constructed 
 * should not change.
 * @author cjohn
 */
public class NXFLine {
    private final String line;
    private final List<String> values;
    
    public NXFLine(String line) {
        this.line = line;
        values = Arrays.asList(StringUtils.stripAll(line.split("\t")));
    }
    
    public NXFLine(List<String> values) {
        this.values = new ArrayList<>(values);
        line = StringUtils.join(values, '\t');
    }
    
    public List<String> getValues() {
        return values;
    }
    
    /**
     * Creates a new instance of an NXFLine which appends the given column on to
     * the columns of this NXFLine.
     * @param column to append to the columns in this line
     * @return a new NXFLine which the column added
     */
    public NXFLine append(String column) {
        List<String> columns = new ArrayList<>(values);
        columns.add(column);
        return new NXFLine(columns);
    }
    
    @Override
    public String toString() {
        return line;
    }
}
