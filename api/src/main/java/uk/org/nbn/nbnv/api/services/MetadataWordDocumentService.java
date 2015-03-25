package uk.org.nbn.nbnv.api.services;

import java.io.InputStream;
import org.springframework.stereotype.Service;
import uk.org.nbn.nbnv.api.model.TaxonDataset;

/**
 * The following service is capable of processing the NBN 'Metadata From for
 * Species Datasets' and transforming the data into a TaxonDataset object
 * @author cjohn
 */
@Service
public class MetadataWordDocumentService {
    // Currently just a place holder for the work @jcoop is going to do.
    // This method should attach the organisation to the taxon dataset as 
    // required.
    //
    // If the supplied input stream can not be fully transformed into a 
    // TaxonDataset we need to throw an exception.
    public TaxonDataset readWordDocument(int organisationId, InputStream input) {
        return new TaxonDataset();
    }
}
