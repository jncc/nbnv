/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.org.nbn.nbnv.importer.ui.util.wordImporter;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import uk.org.nbn.nbnv.importer.ui.util.POIImportError;

/**
 *
 * @author Matt Debont
 */
public class WordImporter_3_1 implements WordImporter {
    public static final int MAJOR = 3;
    public static final int MINOR = 1;
    
    // Limit for wandering up the string list looking for a valid input
    // descriptor contained in the valid stringSet
    public static final int WANDER_MAX = 3;
    
    private Map<String, Integer> longDescCutter;
    private HashSet<String> stringSet;
    
    public WordImporter_3_1() {
        longDescCutter = new HashMap<String, Integer>();
        longDescCutter.put(META_ACCESS_CONSTRAINT, 15);
        longDescCutter.put(META_DATA_CONFIDENCE, 1);
        longDescCutter.put(META_DESC, 1);
        longDescCutter.put(META_GEOCOVER, 3);
        longDescCutter.put(META_CAPTURE_METHOD, 1);
        longDescCutter.put(META_TEMPORAL, 5);
        longDescCutter.put(META_TITLE, 1);
        
        stringSet = new HashSet<String>(Arrays.asList(stringsHWPF));
    }
    
    
    @Override
    public Map<String, String> parseDocument(List<String> strList, ListIterator<String> strIt, Map<String, String> mappings) throws POIImportError {
        String desc, str = "", field;
        
        while (strIt.hasNext()) {
            // Store previous string as possible descriptor

            // Remove stars at end of descriptors if they are there
            desc = str.trim().replaceAll("\\*$", "");

            // Store cursor for descriptor
            int descCursor = strIt.previousIndex();

            // Get the next string in the iterator
            String origStr = strIt.next();
            // Keep original string for error processing and exception
            // handling
            str = origStr;

            // If the str is a FORMTEXT input then we have an input
            // field
            if (str.contains(INPUT_TEXT)) {                    
                // Store cursor index for next val
                int cursor = strIt.nextIndex();

                str = str.replaceAll("^.*" + INPUT_TEXT, "").trim();
                // Copy over to handle multi-line inputs
                field = str;

                // Grab multi-line inputs, might sometimes get, more
                // than we bargined for, so need to fix these as they
                // crop up
                boolean endOfField = false;
                while(!endOfField) {
                    str = strIt.next();
                    if (str.contains("\r\n")) {
                        field = field + "\n\n" + str.trim();
                    } else {
                        endOfField = true;
                    }
                }    

                if (!stringSet.contains(desc.trim())) {
                    // Search back a few entries to see if it just
                    // got lost somewhere
                    strIt = strList.listIterator(descCursor);
                    boolean foundDesc = false;
                    int count = 0;
                    while (count < WANDER_MAX && !foundDesc) {
                        desc = strIt.previous().trim().replaceAll("\\*$", "");
                        if (stringSet.contains(desc.trim())) {
                            foundDesc = true;
                        }
                        count++;
                    }

                    if (!foundDesc) {
                        // Haven't found a valid descriptor so we need
                        // to check our exceptions list
                        if (origStr.contains("I read and understood the NBN Gateway Data Provider Agreement and agree, on behalf of the data provider named in section A, to submit the dataset described in section D to the NBN Trust under this agreement.")) {
                            // Not invalid just the declaration, do some processing here
                        } else {
                            // No exceptions found We have an odity
                            throw new POIImportError("Found an input field with an unknown name input was: " + origStr);
                        }
                    }
                }

                // Deal with cases where the descriptions getted tagged
                // from the metadata 
                if (longDescCutter.get(desc.trim()) != null && longDescCutter.get(desc.trim()) > 0) {
                    for (int i = 0; i < longDescCutter.get(desc.trim()); i++) {
                        field = field.replaceAll("\n.*$", "");
                    }
                }

                mappings.put(desc, field);

                // Reset iterator to the correct place
                strIt = strList.listIterator(cursor);
            } if (str.contains(INPUT_CHECKBOX)) {
                str = str + "";
            }
        }
        
        return mappings;
    }

    @Override
    public boolean supports(int major, int minor) {
        if (major == MAJOR && minor == MINOR) {
            return true;
        }
        return false;
    }
    
}
