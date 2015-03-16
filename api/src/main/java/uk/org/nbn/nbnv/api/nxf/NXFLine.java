package uk.org.nbn.nbnv.api.nxf;

import java.util.Arrays;
import java.util.List;
import org.apache.commons.lang.StringUtils;

/**
 * A representation of a single line from an NBN Exchange formatted file.
 * This line could be either the Heading line (which states the column names)
 * or a values line.
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
        this.values = values;
        line = StringUtils.join(values, '\t');
    }
    
    public String getLine() {
        return line;
    }
    
    public List<String> getValues() {
        return values;
    }
}
