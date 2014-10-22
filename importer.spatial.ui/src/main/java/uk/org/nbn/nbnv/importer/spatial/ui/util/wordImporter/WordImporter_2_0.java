/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.org.nbn.nbnv.importer.spatial.ui.util.wordImporter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import uk.org.nbn.nbnv.importer.s1.utils.errors.POIImportError;

/**
 *
 * @author Matt Debont
 */
public class WordImporter_2_0 implements WordImporter {
    public static final int MAJOR = 2;
    public static final int MINOR = 0;
    
    // Limit for wandering up the string list looking for a valid input
    // descriptor contained in the valid stringSet
    public static final int WANDER_MAX = 3;
    
    private HashSet<String> stringSet;
    
    private List<String> defaultMessages;
    
    public WordImporter_2_0() {       
        stringSet = new HashSet<String>(Arrays.asList(stringsHWPF));
        defaultMessages = new ArrayList<String>();
    }
    
    
    @Override
    public Map<String, String> parseDocument(List<String> strList, ListIterator<String> strIt, Map<String, String> mappings, List<String> errors) {
        String desc, str = "", field;
        
        while (strIt.hasNext()) {
            try { 
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
                            desc = strIt.previous().replaceAll("\\*$", "").trim();
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
                                // Reset iterator to the correct place
                                strIt = strList.listIterator(cursor);                                
                                // No exceptions found We have an odity
                                throw new POIImportError("Found an input field with an unknown name input was: " + origStr);
                            }
                        }
                    }

                    if (!mappings.containsKey(desc)) {
                        mappings.put(desc, field.trim());
                    } else {
                        // Reset iterator to the correct place
                        strIt = strList.listIterator(cursor);
                        throw new POIImportError("Found a duplicate match for key : " + desc);
                    }

                    // Reset iterator to the correct place
                    strIt = strList.listIterator(cursor);
                }
            } catch (POIImportError ex) {
                errors.add("POIImporter Error: " + ex.getMessage());
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
    
    @Override
    public List<String> getDefaultMessages() {
        return defaultMessages;
    }
    
}
