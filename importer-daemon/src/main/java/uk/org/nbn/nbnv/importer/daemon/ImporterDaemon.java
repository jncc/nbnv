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
import org.springframework.stereotype.Component;
import uk.org.nbn.nbnv.importer.Importer;
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
@Component
public class ImporterDaemon implements Runnable {
    
    private Metadata defaultMetadata;
    private Organisation defaultOrganisation;
    private Properties properties;
    private boolean running;
    private String validatorQueue;

    public ImporterDaemon(Properties properties) {
        this.properties = properties;
        this.validatorQueue = properties.getProperty("outputRoot") + File.separator + "queue" + File.separator;
        // Setup Database connection
        DatabaseConnection.getInstance(properties);
        running = true;
    }
    
    @Override
    public void run() {
        //while (running) {
        //    try {
                checkForInput();
        //        Thread.sleep(Integer.parseInt(properties.getProperty("defaultSleep")));
        //    } catch (InterruptedException ex) {
                // Nothing should intterupt us except for a call to end
        //        running = false;
        //    }
        //}
    }

    private boolean checkForInput() {
        File[] jobList = new File(validatorQueue).listFiles();
        if (jobList != null && jobList.length > 0) {
            for (File job : jobList) {
                readInput(job.getName());
            }
            
            // Check for any new input that has come in since we started
            //return checkForInput();
        }
        
        // No new Input detected
        return false;
    }

    private void readInput(String jobName) {
        try {
            File archive = s1Transform(jobName);
            if (archive != null) {
                s2validation(jobName);
            } else {
                
            }
        } catch (IOException ex) {
        }
    }

    private File s1Transform(String jobName) throws IOException {
        String jobFolder = validatorQueue + jobName + File.separator;
        
        File upload = new File(jobFolder + "upload.tab");
        File mappings = new File(jobFolder + "mappings.out");

        String mappingsIn = new BufferedReader(new FileReader(mappings)).readLine();
        Map<String, String> map;
        ObjectMapper mapper = new ObjectMapper();
        
        map = mapper.readValue(mappingsIn, new TypeReference<HashMap<String,String>>(){});

        RunConversions rc = new RunConversions(upload, Integer.parseInt(properties.getProperty("defaultOrganisationID")));

        File output = new File(jobFolder + "data.tab");
        File meta = new File(jobFolder + "meta.xml");

        Map<String, List<String>> conversionOutput = rc.run(output, meta, map);
        
        if (conversionOutput.containsKey("errors") && conversionOutput.get("errors").size() > 0) {
            
        } else {
            File metadata = new File(jobFolder + "metadata.xml");

            try {
                MetadataWriter metadataWriter = new MetadataWriter(metadata);
                metadataWriter.datasetToEML(getDefaultMetadata(), getDefaultOrganisation(), rc.getStartDate(), rc.getEndDate(), true);
            } catch (Exception ex) {
                throw new IOException(ex);
            }

            File zip = new File(jobFolder + "archive.zip");

            ArchiveWriter aw = new ArchiveWriter();
            aw.createArchive(output, meta, metadata, zip);
            
            return zip;
        }
        
        return null;
    }

    private boolean s2validation(String jobName) {
        String jobFolder = validatorQueue + jobName + File.separator;
        
        File archive = new File(jobFolder + "archive.zip");
        
        File logDir = new File(jobFolder + "logs");
        File tmpDir = new File(jobFolder + "tmp");
        
        logDir.mkdirs();
        tmpDir.mkdirs();

        boolean noUnhandledImporterExceptions = true;
        
        try {
            String args = archive.getAbsolutePath() 
                    + " -target " + properties.getProperty("importer.target")
                    + " -logLevel " + properties.getProperty("importer.log.level") 
                    + " -logDir " + logDir.getAbsolutePath() 
                    + " -tempDir " + tmpDir.getAbsolutePath();            
            Importer.startImporterWithCommandLineTargets(args);
        } catch (Exception ex) {
            noUnhandledImporterExceptions = false;
        }
        
        return noUnhandledImporterExceptions;
    }

    private Metadata getDefaultMetadata() {
        if (defaultMetadata == null) {
            defaultMetadata = new Metadata();
            defaultMetadata.setAccess("TEST");
            defaultMetadata.setDatasetAdminEmail("noreply@nbn.org.uk");
            defaultMetadata.setDatasetAdminID(Integer.parseInt(properties.getProperty("defaultUserID")));
            defaultMetadata.setDatasetAdminName("NBN SysAdmin");
            defaultMetadata.setDatasetAdminPhone("Nope");
            defaultMetadata.setDatasetID("");
            defaultMetadata.setDescription("TEST");
            defaultMetadata.setGeographic("TEST");
            defaultMetadata.setGeographicalRes(null);
            defaultMetadata.setInfo("TEST");
            defaultMetadata.setMethods("TEST");
            defaultMetadata.setOrganisationID(Integer.parseInt(properties.getProperty("defaultOrganisationID")));
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
            EntityManager em = 
                    DatabaseConnection.getInstance().createEntityManager();
            defaultOrganisation = em.find(Organisation.class, 
                    Integer.parseInt(properties.getProperty("defaultOrganisationID")));
        }
        return defaultOrganisation;
    }
}