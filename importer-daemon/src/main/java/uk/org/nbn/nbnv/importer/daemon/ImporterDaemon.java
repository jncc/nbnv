package uk.org.nbn.nbnv.importer.daemon;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import javax.persistence.EntityManager;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import scala.Array;
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
public class ImporterDaemon implements Runnable {
    
    private Metadata defaultMetadata;
    private Organisation defaultOrganisation;
    private Properties properties;
    private boolean running;

    public ImporterDaemon(Properties properties) {
        this.properties = properties;
        // Setup Database connection
        DatabaseConnection.getInstance(properties);
        running = true;
    }
    
    @Override
    public void run() {
        while (running) {
            try {
                checkForInput();
                Thread.sleep(Integer.parseInt(properties.getProperty("defaultSleep")));
            } catch (InterruptedException ex) {
                // Nothing should intterupt us except for a call to end
                running = false;
            }
        }
    }

    private boolean checkForInput() {
        File[] jobList = new File(properties.getProperty("validatorRoot") + File.separator + "queue").listFiles();
        if (jobList != null && jobList.length > 0) {
            for (File job : jobList) {
                readInput(job.getName());
            }
            
            // Check for any new input that has come in since we started
            return checkForInput();
        }
        
        // No new Input detected
        return false;
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

        RunConversions rc = new RunConversions(upload, Integer.parseInt(properties.getProperty("defaultOrganisationID")));

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
        
        File logDir = new File(properties.getProperty("validatorRoot") + File.separator
                + "queue" + File.separator + jobName + File.separator
                + "logs");
        File tmpDir = new File(properties.getProperty("validatorRoot") + File.separator
                + "queue" + File.separator + jobName + File.separator
                + "tmp");
        
        logDir.mkdirs();
        tmpDir.mkdirs();
        
        
//        
//        Options options = new Options(archive.getAbsolutePath(), 
//                null, "ERROR", logDir.getAbsolutePath(), 
//                tmpDir.getAbsolutePath(), null);
//        
//        Importer importer = Importer.createImporter(options);

        boolean noUnhandledImporterExceptions = true;
        
        try {
            Process tr = Runtime.getRuntime().exec(properties.getProperty("importerLocation") + " " + archive.getAbsolutePath() + " -target validate -logLevel ERROR -logDir " + logDir.getAbsolutePath() + " -tmpDir " + tmpDir.getAbsolutePath());
            BufferedReader br = new BufferedReader(new InputStreamReader(tr.getInputStream()));
            String s;
            while((s = br.readLine()) != null) {
                // Do nothing waiting for output to end
            }
            
//            Array<String> args = new Array<String>(9);
//            args.update(0, archive.getAbsolutePath());
//            args.update(1, "-target");
//            args.update(2, "validate");
//            args.update(3, "-logLevel");
//            args.update(4, "ERROR");
//            args.update(5, "-logDir");
//            args.update(6, logDir.getAbsolutePath());
//            args.update(7, "-tmpDir");
//            args.update(8, tmpDir.getAbsolutePath());
//            
//            Importer.main(args);
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