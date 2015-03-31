package uk.org.nbn.nbnv.api.model;

import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;
import org.junit.Test;

/**
 *
 * @author cjohn
 */
public class DatasetTest {
    @Test
    public void checkThatCanDetermineIfADatasetIsNotAPlaceholder() {
        //Given
        Dataset dataset = new Dataset();
        dataset.setKey("GA000466");
        
        //When
        boolean placeholder = dataset.isPlaceholder();
        
        //Then
        assertFalse("Expected the dataset with the supplied key to not be considered a placeholder", placeholder);
    }

    @Test
    public void checkThatCanDetermineIfADatasetIsAPlaceholder() {
        //Given
        Dataset dataset = new Dataset();
        dataset.setKey("273d2f7e-3c4c-47c9-b57d-88321789326d");
        
        //When
        boolean placeholder = dataset.isPlaceholder();
        
        //Then
        assertTrue("Expected the dataset with the supplied key to be considered a placeholder", placeholder);
    }
}
