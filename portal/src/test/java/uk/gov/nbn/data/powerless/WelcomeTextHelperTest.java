package uk.gov.nbn.data.powerless;

import java.io.File;
import java.io.IOException;
import static junit.framework.Assert.assertEquals;
import org.apache.commons.io.FileUtils;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

/**
 *
 * @author cjohn
 */
public class WelcomeTextHelperTest {
    @Rule public TemporaryFolder folder = new TemporaryFolder();;
    
    @Test
    public void checkThatCanReadContentFromFileWhichExists() throws IOException {
        //Given
        String myContent = "content";
        File testFile = folder.newFile("testFile");
        FileUtils.writeStringToFile(testFile, myContent, "UTF-8");
        
        WelcomeTextHelper helper = new WelcomeTextHelper(testFile);
        
        //When
        String readContent = helper.read();
        
        //Then
        assertEquals("Expected to be able to read the content", myContent, readContent);        
    }
    
    @Test
    public void checkThatMissingFileReturnsEmptyString() throws IOException {
        //Given
        File testFile = folder.newFile("toDelete");
        testFile.delete();
        WelcomeTextHelper helper = new WelcomeTextHelper(testFile);
        
        //When
        String readContent = helper.read();
        
        //Then
        assertEquals("Expected to be able no content to be read", "", readContent); 
    }
}
