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
import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.FileAppender;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import uk.org.nbn.nbnv.importer.Importer;
import uk.org.nbn.nbnv.importer.daemon.mail.TemplateMailer;
import uk.org.nbn.nbnv.importer.s1.utils.archive.ArchiveWriter;
import uk.org.nbn.nbnv.importer.s1.utils.convert.RunConversions;
import uk.org.nbn.nbnv.importer.s1.utils.database.DatabaseConnection;
import uk.org.nbn.nbnv.importer.s1.utils.model.Metadata;
import uk.org.nbn.nbnv.importer.s1.utils.xmlWriters.MetadataWriter;
import uk.org.nbn.nbnv.jpa.nbncore.Organisation;
import uk.org.nbn.nbnv.importer.logging.NewLogPerRunFileAppender;

/**
 *
 * @author Matt Debont
 */
public class ImporterDaemon implements Runnable {

    private Metadata defaultMetadata;
    private Organisation defaultOrganisation;
    private final Properties properties;
    private final String validatorQueue;
    private final Logger log = Logger.getLogger(ImporterDaemon.class);

    public ImporterDaemon(Properties properties, TemplateMailer mailer) {
        this.properties = properties;
        this.validatorQueue = properties.getProperty("outputRoot") + File.separator + "queue" + File.separator;
        // Setup Database connection
        DatabaseConnection.getInstance(properties);
    }

    @Override
    public void run() {
        checkForInput();
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

        map = mapper.readValue(mappingsIn, new TypeReference<HashMap<String, String>>() {
        });

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
            log.removeAllAppenders();
            
            PatternLayout pattern = new PatternLayout("%d{yyyy-MMM-dd HH:mm:ss} %-5p %m%n");

            FileAppender fa = new NewLogPerRunFileAppender();
            String name = new File(archive.getAbsolutePath()).getName().replace(' ', '-');
            String path = new File(logDir, "log-" + name + ".log").getAbsolutePath();
            fa.setName("LogPerRunFileLogger");
            fa.setFile(path);
            fa.setLayout(pattern);
            fa.setThreshold(Level.toLevel(properties.getProperty("importer.log.level")));
            fa.setAppend(true);
            fa.activateOptions();

            ConsoleAppender ca = new ConsoleAppender();
            ca.setName("ConsoleLogger");
            ca.setLayout(pattern);
            ca.setThreshold(Level.toLevel(properties.getProperty("importer.log.level")));
            ca.activateOptions();

            log.addAppender(ca);
            log.addAppender(fa);

            Importer.startImporterFromDaemon(archive.getAbsolutePath(),
                    properties.getProperty("importer.target"),
                    properties.getProperty("importer.log.level"),
                    logDir.getAbsolutePath(), tmpDir.getAbsolutePath(), log);
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
            EntityManager em
                    = DatabaseConnection.getInstance().createEntityManager();
            defaultOrganisation = em.find(Organisation.class,
                    Integer.parseInt(properties.getProperty("defaultOrganisationID")));
            em.close();
        }
        return defaultOrganisation;
    }
}
