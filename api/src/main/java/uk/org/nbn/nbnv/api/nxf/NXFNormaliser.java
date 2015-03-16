package uk.org.nbn.nbnv.api.nxf;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The NBN Exchange Format files which are submitted to the NBN Gateway don't
 * always conform the the NBN Exchange format standard. Common issues (such as
 * representing boolean values as T or F) can be trivially normalised. It is the
 * responsibility of this class to transform raw NXFLines (read from a file)
 * into strict NXFLines which can be submitted to the NBN importer for
 * processing.
 * 
* The NBN importer expects things like attributes to be presented in a single
 * column (DynamicProperties) where as the NXF specifies that these can be
 * supplied as individual columns. This class will transform attribute columns
 * in to one.
 *
 * @author Jon Cooper
 * @author Christopher Johnson
 */
public class NXFNormaliser {
    private final List<String> origHeaders, nxfHeaders, attrHeaders;

    /**
     * Initialises the transform based off of a raw NXFLine as supplied from an
     * NXF file.
     * @param header the heading of the NBN Exchange format file which holds the
     *  column names
     */
    public NXFNormaliser(NXFLine header) {
        if( header == null ) {
            throw new IllegalArgumentException("The NBN Exchange file was empty");
        }
        String cleanedHeader = header.getLine().toUpperCase().replaceAll(" |_|-", "");
        origHeaders = Arrays.asList(cleanedHeader.split("\t"));
        nxfHeaders = new ArrayList<>(origHeaders);
        nxfHeaders.retainAll(NXFHeading.stringValues());
        attrHeaders = new ArrayList<>(origHeaders);
        attrHeaders.removeAll(NXFHeading.stringValues());
    }
    
    /**
     * This #normalize(NXFLine) method transforms a line into strict NBN Exchange
     * format. As such the amount and order of the values in the columns may 
     * change. This method will return the headings for the new columns.
     * The headings will be returned in a standardised form. That is capitalised
     * with spaces, underscores and other characters removed
     * 
     * @return the nxf line representing the header with values in a 
     *  standardised form
    */
    public NXFLine header() {
        List<String> toReturn = new ArrayList<>(nxfHeaders);
        if(addSRSColumn()) {
            toReturn.add("SRS");
        }
        if(!attrHeaders.isEmpty()) {
            toReturn.add("DYNAMICPROPERTIES");
        }
        return new NXFLine(toReturn);
    }

    /**
     * Simply take the given NXFLine and 'fix' any values. This will involve: -
     * Combining attributes in to one column - Replacing dodgy values with
     * standard ones - Removing bad characters from values
     * @param line to normalise
     * @return the normalised line
     */
    public NXFLine normalise(NXFLine line) {
        Map<String, String> data = getData(line);
        data.put(NXFHeading.SENSITIVE.name(), doBoolean(data.get(NXFHeading.SENSITIVE.name())));
        return null;
    }
    
    private Map<String,String> getData(NXFLine line) {
        Map<String,String> data = new HashMap<>();
        List<String> values = line.getValues();
        for(int i=0; i<values.size(); i++) {
            data.put(origHeaders.get(i), values.get(i));
        }
        return data;
    }
    
    private boolean addSRSColumn() {
        return nxfHeaders.contains("GRIDREFCOL") && !nxfHeaders.contains("SRS");
    }
    
    /**
     * Normalises a string that is masquerading as a boolean
     * @param toTidy the string that needs normalising to true/false
     * @return the normalised string
     */
    private String doBoolean(String toTidy){
        if (toTidy.equalsIgnoreCase("T") || toTidy.equalsIgnoreCase("true") || toTidy.equalsIgnoreCase("yes")) {
            return "true";
        } else if (toTidy.equalsIgnoreCase("F") || toTidy.equalsIgnoreCase("false") || toTidy.equalsIgnoreCase("no")) {
            return "false";
        } else {
            return toTidy;
        }
    }
}
