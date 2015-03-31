package uk.org.nbn.nbnv.api.services;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.zip.ZipFile;
import org.apache.commons.lang.StringUtils;
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
    private static final List<String> VALID_RESOLUTIONS = Arrays.asList("10km", "2km", "1km", "100m", "No Access");
    
    @Autowired TaxonDatasetMetadataImportService wordReader;
    
    // If the supplied input stream can not be fully transformed into a 
    // TaxonDataset we need to throw an exception.
    public TaxonDataset readWordDocument(File input) throws IOException {
        try (ZipFile zipFile = new ZipFile(input)) {
            //No solution has been found to extract checkboxes from a Word document
            //into Java.  Poi is the library used to work with Word documents in Java
            //and it does not have an implementation for this.  Therefore, we will
            //store these values as a json object in a supplied zip file.
            TaxonDataset taxonDataset = wordReader.getTaxonDataset(zipFile.getInputStream(zipFile.getEntry("form.doc")));
            
            TaxonDatasetAdditions additions = new ObjectMapper().readValue(
                    zipFile.getInputStream(zipFile.getEntry("additions.json")), TaxonDatasetAdditions.class);
            
            if(!VALID_RESOLUTIONS.contains(additions.getResolution())) {
                throw new IllegalArgumentException("The resolution must be one of " + StringUtils.join(VALID_RESOLUTIONS, ", "));
            }
            
            if((additions.isRecordAttributes() || additions.isRecorderNames()) && !additions.getResolution().equals("100m")) {
                throw new IllegalArgumentException("You must grant 100m public access if you wish to allow recorder names or attributes to be public");
            }
            
            taxonDataset.setPublicResolution(additions.getResolution());
            taxonDataset.setPublicAttribute(additions.isRecordAttributes());
            taxonDataset.setPublicRecorder(additions.isRecorderNames());
                    
            return taxonDataset;
        } catch (MetadataValidationException ex) {
            throw new IllegalArgumentException(ex);
        }
    }
}
