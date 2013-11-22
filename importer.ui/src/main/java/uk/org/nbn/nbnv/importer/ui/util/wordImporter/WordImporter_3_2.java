/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.org.nbn.nbnv.importer.ui.util.wordImporter;

import java.util.List;
import java.util.ListIterator;
import java.util.Map;

/**
 *
 * @author Matt Debont
 */
public class WordImporter_3_2 implements WordImporter {
    public static final int MAJOR = 3;
    public static final int MINOR = 2;
    
    private WordImporter importer = new WordImporter_3_0();
    
    public WordImporter_3_2() {
        
    }
    
       @Override
    public Map<String, String> parseDocument(List<String> strList, ListIterator<String> strIt, Map<String, String> mappings, List<String> errors) {
        return importer.parseDocument(strList, strIt, mappings, errors);
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
        return importer.getDefaultMessages();
    }
    
}
