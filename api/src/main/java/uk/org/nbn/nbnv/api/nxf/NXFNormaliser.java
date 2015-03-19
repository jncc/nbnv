package uk.org.nbn.nbnv.api.nxf;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

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
        List<String> toRemoveFromAttrs = new ArrayList<>();
        toRemoveFromAttrs.add("TAXONNAME");
        toRemoveFromAttrs.add("TAXONGROUP");
        toRemoveFromAttrs.add("COMMONNAME");
        if( header == null ) {
            throw new IllegalArgumentException("The NBN Exchange file was empty");
        }
        String cleanedHeader = header.getLine().toUpperCase().replaceAll(" |_|-|\"", "");
        origHeaders = new ArrayList(Arrays.asList(cleanedHeader.split("\t")));
        if(origHeaders.indexOf("SENSTIVE")!=-1){
            origHeaders.set(origHeaders.indexOf("SENSTIVE"), "SENSITIVE");
        }
        if(origHeaders.indexOf("SENITIVE")!=-1){
            origHeaders.set(origHeaders.indexOf("SENITIVE"), "SENSITIVE");
        }
        nxfHeaders = new ArrayList<>(origHeaders);
        nxfHeaders.retainAll(NXFHeading.stringValues());
        attrHeaders = new ArrayList<>(origHeaders);
        attrHeaders.removeAll(NXFHeading.stringValues());
        attrHeaders.removeAll(toRemoveFromAttrs);
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
            toReturn.add(NXFHeading.SRS.name());
        }
        if(!attrHeaders.isEmpty()) {
            toReturn.add(NXFHeading.DYNAMICPROPERTIES.name());
        }
        return new NXFLine(toReturn);
    }
    
    /**
     * This takes a map of normalised NXF data and turns it into a tab delimited
     * row.  The data is in the correct order to match the headings that this.header()
     * returns.
     * @param data the normalised data
     * @return the normalised data as an NXFLine
     */
    public NXFLine data(Map<String,String> data) {
        List<String> toReturn = new ArrayList<>(getHeadings(nxfHeaders, data));
        if(addSRSColumn()) {
            toReturn.add(data.get(NXFHeading.SRS.name()));
        }
        if(!attrHeaders.isEmpty()){
            try {
                toReturn.add(getAttributes(attrHeaders, data));
            } catch (JSONException ex) {
                Logger.getLogger(NXFNormaliser.class.getName()).log(Level.SEVERE, null, ex);
            }
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
        Map<String,String> data = getData(line);
        if(data.containsKey(NXFHeading.SENSITIVE.name())){
            data.put(NXFHeading.SENSITIVE.name(), getSensitive(data.get(NXFHeading.SENSITIVE.name())));
        }
        if(data.containsKey(NXFHeading.DATETYPE.name())){
            data.put(NXFHeading.DATETYPE.name(), getDateTypePToOO(data.get(NXFHeading.DATETYPE.name())));
        }
        if(data.containsKey(NXFHeading.GRIDREFERENCE.name())){
            data.put(NXFHeading.GRIDREFERENCE.name(), getGridRef(data.get(NXFHeading.GRIDREFERENCE.name())));
        }
        if(data.containsKey(NXFHeading.ZEROABUNDANCE.name())){
            data.put(NXFHeading.ZEROABUNDANCE.name(), getZeroAbundance(data.get(NXFHeading.ZEROABUNDANCE.name())));
        }
        if(data.containsKey(NXFHeading.PRECISION.name())){
            data.put(NXFHeading.PRECISION.name(), getPrecision(data.get(NXFHeading.PRECISION.name())));
        }
        data.put(NXFHeading.SRS.name(), getSRSValue(data));
        return data(data);
    }
    
    private List<String> getHeadings(List<String> headings, Map<String,String> data) {
        List<String> toReturn = new ArrayList<>();
        for(String heading : headings){
            toReturn.add(data.get(heading));
        }
        return toReturn;
    }
    
    private Map<String,String> getData(NXFLine line) {
        Map<String,String> data = new HashMap<>();
        List<String> values = line.getValues();
        for(int i=0; i<values.size(); i++) {
            data.put(origHeaders.get(i), values.get(i).replaceAll("\"", ""));
        }
        return data;
    }
    
    private boolean addSRSColumn() {
        return nxfHeaders.contains("PROJECTION") && !nxfHeaders.contains("SRS");
    }
    
    private String getSensitive(String toTidy){
        if (toTidy.equalsIgnoreCase("T") || toTidy.equalsIgnoreCase("true") || toTidy.equalsIgnoreCase("yes")) {
            toTidy = "true";
        } else if (toTidy.equalsIgnoreCase("F") || toTidy.equalsIgnoreCase("false") || toTidy.equalsIgnoreCase("no")) {
            toTidy = "false";
        }
        return toTidy;
    }
    
    private String getDateTypePToOO(String toTidy){
        if(toTidy.endsWith("P")){
            toTidy = "OO";
        }
        return toTidy;
    }
    
    private String getGridRef(String toTidy){
        return toTidy.replaceAll(" ", "").replaceAll("-", "");
    }
    
    private String getSRSValue(Map<String,String> data){
        String toReturn = (data.get(NXFHeading.SRS.name())!=null) ? data.get(NXFHeading.SRS.name()) : "";
        if(data.containsKey(NXFHeading.PROJECTION.name())){
            if(data.get(NXFHeading.PROJECTION.name()).equals("WGS84")){
                toReturn = "4326";
            }
        }
        return toReturn;
    }
    
    private String getZeroAbundance(String toTidy) {
        if (toTidy.equalsIgnoreCase("T") || toTidy.equalsIgnoreCase("true") || toTidy.equalsIgnoreCase("yes") || toTidy.equalsIgnoreCase("absent") || toTidy.equalsIgnoreCase("absence")) {
            toTidy = "absence";
        } else if (toTidy.equalsIgnoreCase("F") || toTidy.equalsIgnoreCase("false") || toTidy.equalsIgnoreCase("no") || toTidy.equalsIgnoreCase("present") || toTidy.equalsIgnoreCase("presence")) {
            toTidy = "presence";
        }
        return toTidy;
    }
    
    private String getPrecision(String toTidy) {
        toTidy = toTidy.trim();
        if (!toTidy.isEmpty()) {
            try{
                int indexOfDecimal = toTidy.indexOf(".");
                if(indexOfDecimal != -1){
                    toTidy = toTidy.substring(0,indexOfDecimal);
                }
                int data = Integer.parseInt(toTidy);
                if (data <= 100) {
                    data = 100;
                } else if (data <= 1000) {
                    data = 1000;
                } else if (data <= 2000) {
                    data = 2000;
                } else if (data <= 10000) {
                    data = 10000;
                } else {
                    return toTidy;
                }
                toTidy = Integer.toString(data);
            } catch (NumberFormatException ex){
                //Do nothing - let toTidy be returned as is
            }
        }
        return toTidy;
    }
    
    private String getAttributes(List<String> headings, Map<String,String> data) throws JSONException {
        JSONObject toReturn = new JSONObject();
        for(String heading : headings){
            String value = Normalizer.normalize(data.get(heading).trim(), Normalizer.Form.NFD)
                .replaceAll("\\p{InCombiningDiacriticalMarks}+", "")
                .replaceAll("[^\\p{ASCII}]", "")
                .replaceAll("^\"|\"$", "");
            value = value.substring(0,Math.min(value.length(), 255));
            toReturn.put(heading.toUpperCase(),value);
        }
        return toReturn.toString();
    }
}
