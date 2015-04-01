package uk.org.nbn.nbnv.api.nxf;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
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
    private final static List<String> ATTRS_TO_IGNORE = Arrays.asList("TAXONNAME", "TAXONGROUP", "COMMONNAME");
    private final List<String> origHeaders, nxfHeaders, attrHeaders;
    private long recordKey = 1;

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
        String cleanedHeader = header.toString().toUpperCase().replaceAll(" |_|-|\"", "");
        origHeaders = new ArrayList<>(Arrays.asList(cleanedHeader.split("\t")));
        if(origHeaders.contains("SENSTIVE")){
            origHeaders.set(origHeaders.indexOf("SENSTIVE"), "SENSITIVE");
        }
        if(origHeaders.contains("SENITIVE")){
            origHeaders.set(origHeaders.indexOf("SENITIVE"), "SENSITIVE");
        }
        
        // Make sure that the headers supplied does not contain any duplicate
        // columns. These will cause confusion.
        if(new HashSet<>(origHeaders).size() != origHeaders.size()) {
            throw new IllegalArgumentException("Duplicate columns are not allowed");
        }
        // We need to remove the DynamicProperties heading to create a set of 
        // standard NXF Headings. We use DynamicProperties during the normaliser
        // process to concat any none NXF heading value into one column.
        List<String> standardNXFHeadings = NXFHeading.stringValues();
        standardNXFHeadings.remove(NXFHeading.DYNAMICPROPERTIES.name());
        
        nxfHeaders = new ArrayList<>(origHeaders);
        nxfHeaders.retainAll(standardNXFHeadings);
        attrHeaders = new ArrayList<>(origHeaders);
        attrHeaders.removeAll(standardNXFHeadings);
        attrHeaders.removeAll(ATTRS_TO_IGNORE);
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
        if(!nxfHeaders.contains(NXFHeading.RECORDKEY.name())) {
            toReturn.add(NXFHeading.RECORDKEY.name());
        }
        if(!attrHeaders.isEmpty()) {
            toReturn.add(NXFHeading.DYNAMICPROPERTIES.name());
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
        // Normalise the data
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
        
        //Construct a line to return
        List<String> toReturn = new ArrayList<>(getHeadings(nxfHeaders, data));
        if(addSRSColumn()) {
            toReturn.add(data.get(NXFHeading.SRS.name()));
        }
        if(!nxfHeaders.contains(NXFHeading.RECORDKEY.name())) {
            toReturn.add(Long.toString(recordKey++));
        }
        if(!attrHeaders.isEmpty()){
            toReturn.add(getAttributes(attrHeaders, data));
        }
        return new NXFLine(toReturn);
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
            data.put(origHeaders.get(i), values.get(i)
                                               .replaceAll("\"", "")
                                               .replaceAll("\n", " "));
        }
        return data;
    }
    
    private boolean addSRSColumn() {
        return nxfHeaders.contains("PROJECTION") && !nxfHeaders.contains("SRS");
    }
    
    private String getSensitive(String toTidy){
        if(Arrays.asList("t", "true", "yes").contains(toTidy.toLowerCase())) {
            return "true";            
        }
        else if(Arrays.asList("f", "false", "no").contains(toTidy.toLowerCase())) {
            return "false";
        }
        return toTidy;
    }
    
    private String getDateTypePToOO(String toTidy){
        return toTidy.endsWith("P") ? "OO" : toTidy;
    }
    
    private String getGridRef(String toTidy){
        return toTidy.replaceAll(" ", "").replaceAll("-", "");
    }
    
    private String getSRSValue(Map<String,String> data){
        String toReturn = data.containsKey(NXFHeading.SRS.name()) ? data.get(NXFHeading.SRS.name()) : "";
        if(data.containsKey(NXFHeading.PROJECTION.name())){
            if(data.get(NXFHeading.PROJECTION.name()).equals("WGS84")){
                return "4326";
            }
        }
        return toReturn;
    }
    
    private String getZeroAbundance(String toTidy) {
        if(Arrays.asList("t", "true", "yes", "absent", "absence").contains(toTidy.toLowerCase())) {
            return "absence";            
        }
        else if(Arrays.asList("f", "false", "no", "present", "presence").contains(toTidy.toLowerCase())) {
            return "presence";
        }
        return toTidy;
    }
    
    private String getPrecision(String toTidy) {
        toTidy = toTidy.trim();
        if (!toTidy.isEmpty()) {
            try{
                Double value = Double.valueOf(toTidy);
                if (value.isInfinite() || value.isNaN()) {
                    return toTidy;
                }
                int data = value.intValue();
                if (data <= 100) {
                    return "100";
                } else if (data <= 1000) {
                    return "1000";
                } else if (data <= 2000) {
                    return "2000";
                } else if (data <= 10000) {
                    return "10000";
                }
            } catch (NumberFormatException ex){
                //Do nothing - let toTidy be returned as is
            }
        }
        return toTidy;
    }
    
    private String getAttributes(List<String> headings, Map<String,String> data)  {
        JSONObject toReturn = new JSONObject();
        for(String heading : headings){
            try {
                String value = Normalizer.normalize(data.get(heading).trim(), Normalizer.Form.NFD)
                        .replaceAll("\\p{InCombiningDiacriticalMarks}+", "")
                        .replaceAll("[^\\p{ASCII}]", "")
                        .replaceAll("^\"|\"$", "");
                value = value.substring(0,Math.min(value.length(), 255));
                toReturn.put(heading.toUpperCase(),value);
            } catch (JSONException ex) {
                return "Logic Error: Failed to build attributes";
            }
        }
        return toReturn.toString();
    }
}
