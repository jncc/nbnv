package uk.org.nbn.nbnv.api.services;

import java.io.File;
import java.io.IOException;
import java.util.zip.ZipFile;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uk.org.nbn.nbnv.api.model.TaxonDataset;
import uk.org.nbn.nbnv.api.model.TaxonDatasetAdditions;
import uk.org.nbn.nbnv.api.nxf.metadata.MetadataValidationException;

/**
 * The following service is capable of processing the NBN 'Metadata From for
 * Species Datasets' along with an additions.json from a ZipFile and 
 * transforming the data into a TaxonDataset object
 * @author cjohn
 */
@Service
public class TaxonDatasetMetadataArchiveService {
    @Autowired TaxonDatasetMetadataImportService wordReader;
    
    // If the supplied input stream can not be fully transformed into a 
    // TaxonDataset we need to throw an exception.
    public TaxonDataset readWordDocument(File input) throws IOException {
        try {
            //No solution has been found to extract checkboxes from a Word document
            //into Java.  Poi is the library used to work with Word documents in Java
            //and it does not have an implementation for this.  Therefore, we will
            //store these values as a json object in a supplied zip file.
            ZipFile zipFile = new ZipFile(input);
            TaxonDataset taxonDataset = wordReader.getTaxonDataset(zipFile.getInputStream(zipFile.getEntry("form.doc")));
            
            TaxonDatasetAdditions additions = new ObjectMapper().readValue(
                    zipFile.getInputStream(zipFile.getEntry("additions.json")), TaxonDatasetAdditions.class);
            
            taxonDataset.setPublicResolution(additions.getResolution());
            taxonDataset.setPublicAttribute(additions.isRecordAttributes());
            taxonDataset.setPublicRecorder(additions.isRecorderNames());
                    
            return taxonDataset;
        } catch (MetadataValidationException ex) {
            throw new IllegalArgumentException(ex);
        }
    }
}
