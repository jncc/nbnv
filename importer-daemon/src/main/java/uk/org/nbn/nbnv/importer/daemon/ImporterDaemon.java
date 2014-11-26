package uk.org.nbn.nbnv.importer.daemon;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.attribute.BasicFileAttributes;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.persistence.EntityManager;
import org.apache.commons.compress.archivers.ArchiveOutputStream;
import org.apache.commons.compress.archivers.ArchiveStreamFactory;
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveOutputStream;
import org.apache.commons.compress.utils.IOUtils;
import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.FileAppender;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import org.springframework.beans.factory.annotation.Autowired;
import uk.org.nbn.nbnv.importer.Importer;
import uk.org.nbn.nbnv.importer.daemon.exceptions.MissingFileException;
import uk.org.nbn.nbnv.importer.daemon.mail.TemplateMailer;
import uk.org.nbn.nbnv.importer.s1.utils.archive.ArchiveWriter;
import uk.org.nbn.nbnv.importer.s1.utils.convert.RunConversions;
import uk.org.nbn.nbnv.importer.s1.utils.database.DatabaseConnection;
import uk.org.nbn.nbnv.importer.s1.utils.model.Metadata;
import uk.org.nbn.nbnv.importer.s1.utils.xmlWriters.MetadataWriter;
import uk.org.nbn.nbnv.jpa.nbncore.Organisation;
import uk.org.nbn.nbnv.importer.logging.NewLogPerRunFileAppender;
import uk.org.nbn.nbnv.jpa.nbncore.User;

/**
 *
 * @author Matt Debont
 */
public class ImporterDaemon implements Runnable {

    private Metadata defaultMetadata;
    private Organisation defaultOrganisation;
    private final String validatorQueue;
    private final Logger log = Logger.getLogger(ImporterDaemon.class);
        
    @Autowired private TemplateMailer mailer;
    @Autowired private Properties properties;

    public ImporterDaemon(Properties properties) {
        this.validatorQueue = properties.getProperty("outputRoot") + File.separator + "queue" + File.separator;
        // Setup Database connection
        DatabaseConnection.getInstance(properties);         
    }    
    
//    public ImporterDaemon(Properties properties, TemplateMailer mailer) {
//        this.properties = properties;
//        this.validatorQueue = properties.getProperty("outputRoot") + File.separator + "queue" + File.separator;
//        // Setup Database connection
//        DatabaseConnection.getInstance(properties);
//        this.mailer = mailer;
//    }

    /**
     * Start the thread by checking for new input, should be run by a scheduled
     * task executor
     */
    @Override
    public void run() {       
        checkForInput();
    }

    /**
     * Check for new input in the specified queue folder, will run through the
     * list then check again to see if any new input has appeared on the queue
     * when nothing is left the thread will go to sleep for the period specified
     * in the properties file
     *
     * @return Only returns false when there is no more input left to return
     */
    private void checkForInput() {
        File[] jobList = new File(validatorQueue).listFiles();
        if (jobList != null && jobList.length > 0) {
            for (File job : jobList) {
                // Get full path of job
                String jobFolder = validatorQueue + job.getName() + File.separator;
                
                try {
                    readInput(jobFolder);
                } catch (MissingFileException ex) {
                    sendFailureEmail(job, ex.getMessage());
                } finally {
                    deleteDirectory(new File(jobFolder));
                }
            }

            // Check for any new input that has come in since we started
            checkForInput();
        }
    }

    /**
     * For the specified job attempt to perform the S1 import and then the S2
     * importer running to the level specified in the importer_daemon.properties
     * file
     *
     * @param jobName The path to the folder containing the job
     */
    private void readInput(String jobFolder) throws MissingFileException {
        File upload = new File(jobFolder + "upload.tab");
        File mappings = new File(jobFolder + "mappings.out");
        File info = new File(jobFolder + "info.out");

        if (!upload.exists()) {
            throw new MissingFileException(upload.getName());
        }
        if (!mappings.exists()) {
            throw new MissingFileException(mappings.getName());
        }
        if (!info.exists()) {
            throw new MissingFileException(info.getName());
        }

        // Create a directory to store the log files and create it
        File logDir = new File(jobFolder + "logs");
        logDir.mkdirs();

        File archive = null;
        
        try {
            // Run the S1 Import and get the archive generated from it
            archive = s1Transform(jobFolder, logDir);
        } catch (IOException ex) {
            
        }

        try {
            // If the archive is null then the S1 transform failed, otherwise
            // proceed to the S2 Import
            if (archive != null) {
                s2validation(jobFolder, logDir);
                sendValidationCompleteEmail(jobFolder, logDir);
            }
        } catch (Exception ex) {
            
        }
    }

    private void sendValidationCompleteEmail(String jobFolder, File logDir) {
        try {
            File info = new File(jobFolder + "info.out");
            Map<String, Object> map = readJSONFile(info);

            File zip = new File(jobFolder + "output.zip");

            ZipArchiveOutputStream zipOut = new ZipArchiveOutputStream(zip);
            ArchiveOutputStream logical_zip = new ArchiveStreamFactory().createArchiveOutputStream(ArchiveStreamFactory.ZIP, zipOut);

            for (File file : logDir.listFiles()) {
                logical_zip.putArchiveEntry(new ZipArchiveEntry(file.getName()));

                FileInputStream input = new FileInputStream(file);
                IOUtils.copy(input, logical_zip);

                logical_zip.closeArchiveEntry();
                input.close();
            }
            logical_zip.finish();
            zipOut.close();

            map.put("attachment", zip);

            mailer.sendMime("validationDone.ftl", (String) map.get("email"), "NBN Dataset Validation has finished", map);
        } catch (Exception ex) {
            
        }
    }

    private void sendFailureEmail(File job, String errorMessage) {
        String jobFolder = validatorQueue + job.getName() + File.separator;
        File info = new File(jobFolder + "info.out");

        try {
            if (info.exists()) {
                Map<String, Object> map = readJSONFile(info);

                SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yy HH:mm:SS");
                
                BasicFileAttributes attr = Files.readAttributes(job.toPath(), BasicFileAttributes.class);
                map.put("time", sdf.format(new Date(attr.creationTime().toMillis())));
                map.put("failMsg", errorMessage);
                
                mailer.sendMime("processFailure.ftl", (String) map.get("email"), "NBN Gateway Online Validator Failed", (Map<String, Object>) map);
            } else {
                Matcher matcher = Pattern.compile("^(\\d{8})_(\\d{6})_(\\d+)_[A-Za-z0-9]{10}$").matcher(job.getName());
                if (matcher.matches()) {
                    EntityManager em
                            = DatabaseConnection.getInstance().createEntityManager();
                    User user = em.find(User.class,
                            Integer.parseInt(matcher.group(3)));
                    em.close();
                    
                    if (user != null) {
                        Map<String, Object> map = new HashMap<String, Object>();
                        
                        String date = matcher.group(1).substring(6) + ":" + matcher.group(1).substring(4,6) + ":" + matcher.group(1).substring(0,4);
                        String time = matcher.group(2).substring(0,2) + ":" + matcher.group(2).substring(2,4) + ":" + matcher.group(2).substring(4);
                        map.put("time", date + " " + time);
                        
                        map.put("failMsg", errorMessage);                        
                        mailer.sendMime("processFailure.ftl", (String) user.getEmail(), "NBN Gateway Online Validator Failed", map);
                    }
                }
            }
        } catch (Exception ex) {
            log.error("An exception has occurred while sending a failure email (email contains: " + errorMessage + " / job: " + job.getName() + ")", ex);
        } finally {
            // TODO: Move to failure folder
        }
    }

    private Map<String, Object> readJSONFile(File input) throws FileNotFoundException, IOException {
        BufferedReader reader = new BufferedReader(new FileReader(input));
        String mappingsIn = reader.readLine();
        reader.close();

        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(mappingsIn, new TypeReference<HashMap<String, Object>>() {
        });
    }

    private void deleteDirectory(File folder) {
        for (File file : folder.listFiles()) {
            if (file.isDirectory()) {
                deleteDirectory(file);
            }
            file.delete();
        }
        folder.delete();
    }

    /**
     * Perform the Stage 1 Import transformations to the job in the supplied
     * folder
     *
     * @param jobFolder The full path to the directory containing this job
     * @param logDir The file representation of the directory containing the
     * logs for this job
     * @return The output of the S1 importer a Darwin Core Archive file to feed
     * into the S2 Importer
     * @throws IOException There was an error writing out one or more of the
     * files needed by this step
     */
    private File s1Transform(String jobFolder, File logDir) throws IOException {
        File upload = new File(jobFolder + "upload.tab");
        File mappings = new File(jobFolder + "mappings.out");

        String mappingsIn = new BufferedReader(new FileReader(mappings)).readLine();
        Map<String, String> map;
        ObjectMapper mapper = new ObjectMapper();

        map = mapper.readValue(mappingsIn, new TypeReference<HashMap<String, String>>() {});

        RunConversions rc = new RunConversions(upload, Integer.parseInt(properties.getProperty("defaultOrganisationID")));

        File output = new File(jobFolder + "data.tab");
        File meta = new File(jobFolder + "meta.xml");

        Map<String, List<String>> conversionOutput = rc.run(output, meta, map);

        if (conversionOutput.containsKey("errors") && !conversionOutput.get("errors").isEmpty()) {
            PrintWriter errorWriter = new PrintWriter(logDir.getAbsolutePath() + File.separator + "Stage1_Errors.txt", "UTF-8");
            for (String warning : conversionOutput.get("errors")) {
                errorWriter.write(warning + "\n");
            }
            errorWriter.close();

            writeStage1Warnings(conversionOutput, logDir);

            return null;
        } else {
            writeStage1Warnings(conversionOutput, logDir);

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
    }

    /**
     * Writes out the warnings output from the stage 1 importer if there have
     * been any warnings produced
     *
     * @param conversionOutput A Map containing the warnings (output by S1
     * import)
     * @param logDir The log directory containing the logs for this importer run
     * @throws IOException There was an error writing out one or more of the
     * files needed by this step
     */
    private void writeStage1Warnings(Map<String, List<String>> conversionOutput, File logDir) throws IOException {
        if (conversionOutput.containsKey("warnings") && !conversionOutput.get("warnings").isEmpty()) {
            PrintWriter warningWriter = new PrintWriter(logDir.getAbsolutePath() + File.separator + "Stage1_Warnings.txt", "UTF-8");
            for (String warning : conversionOutput.get("warnings")) {
                warningWriter.write(warning + "\n");
            }
            warningWriter.close();
        }
    }

    /**
     * Start the stage 2 (Scala Importer), which will run to the stage specified
     * in the properties file (default
     *
     * @param jobFolder
     * @param logDir
     * @throws Exception
     */
    private void s2validation(String jobFolder, File logDir) throws Exception {
        File archive = new File(jobFolder + "archive.zip");

        File tmpDir = new File(jobFolder + "tmp");
        tmpDir.mkdirs();

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
