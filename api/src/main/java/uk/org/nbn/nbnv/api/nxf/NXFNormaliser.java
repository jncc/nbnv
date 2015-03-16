package uk.org.nbn.nbnv.api.nxf;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
    private static final List<String> NXF_HEADINGS = Arrays.asList();
    private final List<String> origHeaders, nxfHeaders, attrHeaders;

    /**
     * Initialises the transform based off of a raw NXFLine as supplied from an
     * NXF file.
     * @param header the heading of the NBN Exchange format file which holds the
     *  column names
     */
    public NXFNormaliser(NXFLine header) {
        String cleanedHeader = header.getLine().toUpperCase().replaceAll(" |_|-", "");
        origHeaders = Arrays.asList(cleanedHeader.split(" \t"));
        nxfHeaders = new ArrayList<>(origHeaders);
        nxfHeaders.retainAll(NXF_HEADINGS);
        attrHeaders = new ArrayList<>(origHeaders);
        attrHeaders.removeAll(NXF_HEADINGS);
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
        return null;
    }

    /**
     * Simply take the given NXFLine and 'fix' any values. This will involve: -
     * Combining attributes in to one column - Replacing dodgy values with
     * standard ones - Removing bad characters from values
     * @param line to normalise
     * @return the normalised line
     */
    public NXFLine normalise(NXFLine line) {
        return null;
    }
}
