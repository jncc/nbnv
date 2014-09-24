package uk.gov.nbn.data.powerless;

import java.io.File;
import java.io.IOException;
import org.apache.commons.io.FileUtils;

/**
 *
 * @author cjohn
 */
public class WelcomeTextHelper {
    private final File welcomeText;
    
    public WelcomeTextHelper(File filename) {
        this.welcomeText = filename;
    }
    
    /**
     * Reads the contents of the file specified in welcome text file passed into.
     * The constructor
     * @return The contents of welcomeText if the file exists and is readable. 
     * Else ""
     */
    public String read() {
        try {
            if(welcomeText.canRead()) {
                return FileUtils.readFileToString(welcomeText, "UTF-8");
            }
            else {
                return "";
            }
        }
        catch(IOException io) {
            return "";
        }
    }
}
