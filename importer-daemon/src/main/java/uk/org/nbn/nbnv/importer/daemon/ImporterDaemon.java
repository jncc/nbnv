package uk.org.nbn.nbnv.importer.daemon;

import java.io.File;
import java.io.IOException;
import uk.org.nbn.nbnv.importer.s1.utils.archive.ArchiveWriter;
import uk.org.nbn.nbnv.importer.s1.utils.convert.RunConversions;
import uk.org.nbn.nbnv.importer.s1.utils.model.Metadata;
import uk.org.nbn.nbnv.importer.s1.utils.xmlWriters.MetadataWriter;
import uk.org.nbn.nbnv.jpa.nbncore.Organisation;

/**
 *
 * @author Matt Debont
 */
public class ImporterDaemon {
    
    private static final int ORGANISATION = 1;
    private Metadata defaultMetadata;
    private Organisation defaultOrganisation;
    
    
    public void start() {
        
    }
    
    private boolean s1Transform(String jobName) {
        File upload = new File("");
        File mappings = new File("");
        
        try {
            RunConversions rc = new RunConversions(upload, ORGANISATION);

            File output = new File("");
            File meta = new File("");

            rc.run(output, meta, null);

            
            File metadata = new File("");
            
            MetadataWriter metadataWriter = new MetadataWriter(metadata);
            try {
                metadataWriter.datasetToEML(getDefaultMetadata(), getDefaultOrganisation(), rc.getStartDate(), rc.getEndDate(), true);
            } catch (Exception ex) {
                return false;
            }
            
            File zip = new File("");

            ArchiveWriter aw = new ArchiveWriter();
            aw.createArchive(output, meta, metadata, zip);
        
        } catch (IOException ex) {
            
        }
        
        return false;
    }
    
    private boolean s2validation(String jobName) {
        
        return false;
    }
    
    private Metadata getDefaultMetadata() {
        if (defaultMetadata == null) {
            defaultMetadata = new Metadata();
            defaultMetadata.setAccess(null);
            defaultMetadata.setDatasetAdminEmail(null);
            defaultMetadata.setDatasetAdminID(ORGANISATION);
            defaultMetadata.setDatasetAdminName(null);
            defaultMetadata.setDatasetAdminPhone(null);
            defaultMetadata.setDatasetID(null);
            defaultMetadata.setDescription(null);
            defaultMetadata.setGeographic(null);
            defaultMetadata.setGeographicalRes(null);
            defaultMetadata.setInfo(null);
            defaultMetadata.setMethods(null);
            defaultMetadata.setOrganisationID(ORGANISATION);
            defaultMetadata.setPurpose(null);
            defaultMetadata.setQuality(null);
            defaultMetadata.setRecordAtts(null);
            defaultMetadata.setRecorderNames(null);
            defaultMetadata.setTemporal(null);
            defaultMetadata.setTitle(null);
            defaultMetadata.setUse(null);
        }
        return defaultMetadata;
    }
    
    private Organisation getDefaultOrganisation() {
        if (defaultOrganisation == null) {
                                
        }
        return defaultOrganisation;
    }
}
