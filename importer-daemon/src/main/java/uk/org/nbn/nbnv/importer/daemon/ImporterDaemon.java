package uk.org.nbn.nbnv.importer.daemon;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import javax.persistence.EntityManager;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import uk.org.nbn.nbnv.importer.s1.utils.archive.ArchiveWriter;
import uk.org.nbn.nbnv.importer.s1.utils.convert.RunConversions;
import uk.org.nbn.nbnv.importer.s1.utils.database.DatabaseConnection;
import uk.org.nbn.nbnv.importer.s1.utils.model.Metadata;
import uk.org.nbn.nbnv.importer.s1.utils.xmlWriters.MetadataWriter;
import uk.org.nbn.nbnv.jpa.nbncore.Organisation;

/**
 *
 * @author Matt Debont
 */
public class ImporterDaemon implements Runnable {

    private static final int ORGANISATION = 189;
    private static final int DATASETADMINID = 15848;
    private Metadata defaultMetadata;
    private Organisation defaultOrganisation;
    private Properties properties;

    public ImporterDaemon(Properties properties) {
        this.properties = properties;
        // Setup Database connection
        DatabaseConnection.getInstance(properties);
    }
    
    @Override
    public void run() {
        checkForInput();
    }

    private void checkForInput() {
        File[] jobList = new File(properties.getProperty("validatorRoot") + File.separator + "queue").listFiles();
        if (jobList != null && jobList.length > 0) {
            for (File job : jobList) {
                readInput(job.getName());
            }
        }
    }

    private void readInput(String jobName) {
        try {
            File archive = s1Transform(jobName);
            if (archive != null) {
                //s2validation(jobName);
            } else {
                
            }
        } catch (IOException ex) {
        }
    }

    private File s1Transform(String jobName) throws IOException {
        File upload = new File(properties.getProperty("validatorRoot")
                + File.separator + "queue" + File.separator + jobName
                + File.separator + "upload.tab");
        File mappings = new File(properties.getProperty("validatorRoot")
                + File.separator + "queue" + File.separator + jobName
                + File.separator + "mappings.out");

        String mappingsIn = new BufferedReader(new FileReader(mappings)).readLine();
        Map<String, String> map;
        ObjectMapper mapper = new ObjectMapper();
        
        map = mapper.readValue(mappingsIn, new TypeReference<HashMap<String,String>>(){});

        RunConversions rc = new RunConversions(upload, ORGANISATION);

        File output = new File(properties.getProperty("validatorRoot")
                + File.separator + "queue" + File.separator + jobName
                + File.separator + "data.tab");
        File meta = new File(properties.getProperty("validatorRoot")
                + File.separator + "queue" + File.separator + jobName
                + File.separator + "meta.xml");

        Map<String, List<String>> conversionOutput = rc.run(output, meta, map);
        
        if (conversionOutput.containsKey("errors") && conversionOutput.get("errors").size() > 0) {
            
        } else {
            File metadata = new File(properties.getProperty("validatorRoot")
                    + File.separator + "queue" + File.separator + jobName
                    + File.separator + "metadata.xml");

            try {
                MetadataWriter metadataWriter = new MetadataWriter(metadata);
                metadataWriter.datasetToEML(getDefaultMetadata(), getDefaultOrganisation(), rc.getStartDate(), rc.getEndDate(), true);
            } catch (Exception ex) {
                throw new IOException(ex);
            }

            File zip = new File(properties.getProperty("validatorRoot")
                    + File.separator + "queue" + File.separator + jobName
                    + File.separator + "archive.zip");

            ArchiveWriter aw = new ArchiveWriter();
            aw.createArchive(output, meta, metadata, zip);
            
            return zip;
        }
        
        return null;
    }

    private boolean s2validation(String jobName) {
        File archive = new File(
                properties.getProperty("validatorRoot") + File.separator
                + "queue" + File.separator + jobName + File.separator
                + "archive.zip");

//        Options options = optionBuilder.build(archive);
//
//        Importer importer = Importer.createImporter(options);
//        boolean noUnhandledImporterExceptions = true;
//        try {
//            //logger.info("About to run importer");
//            importer.run();
//        } catch (Exception e) {
//            // somethings gone very wrong, but we don't actually care
//            // we still just send the log file to user
//            // an import can fail and not throw and error here, I think, need to check with felix et al
//            //logger.error("The importer failed but we sent the log file to user");
//            //logger.error(e.getMessage().toString());
//            noUnhandledImporterExceptions = false;
//        }
//
//        return noUnhandledImporterExceptions;
//        // the return value is only useful for testing and is not bubbled up to user
//        // they are are always sent a log file
        
        return false;
    }

    private Metadata getDefaultMetadata() {
        if (defaultMetadata == null) {
            defaultMetadata = new Metadata();
            defaultMetadata.setAccess("TEST");
            defaultMetadata.setDatasetAdminEmail("noreply@nbn.org.uk");
            defaultMetadata.setDatasetAdminID(DATASETADMINID);
            defaultMetadata.setDatasetAdminName("NBN SysAdmin");
            defaultMetadata.setDatasetAdminPhone("Nope");
            defaultMetadata.setDatasetID("");
            defaultMetadata.setDescription("TEST");
            defaultMetadata.setGeographic("TEST");
            defaultMetadata.setGeographicalRes(null);
            defaultMetadata.setInfo("TEST");
            defaultMetadata.setMethods("TEST");
            defaultMetadata.setOrganisationID(ORGANISATION);
            defaultMetadata.setPurpose("TEST");
            defaultMetadata.setQuality("TEST");
            defaultMetadata.setRecordAtts("false");
            defaultMetadata.setRecorderNames("false");
            defaultMetadata.setTemporal("TEST");
            defaultMetadata.setTitle("TEST");
            defaultMetadata.setUse("TEST");
        }
        return defaultMetadata;
    }

    private Organisation getDefaultOrganisation() {
        if (defaultOrganisation == null) {
            EntityManager em = DatabaseConnection.getInstance().createEntityManager();
            defaultOrganisation = em.find(Organisation.class, ORGANISATION);
        }
        return defaultOrganisation;
    }
}