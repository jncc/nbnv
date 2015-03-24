package uk.org.nbn.nbnv.api.services;

import freemarker.template.TemplateException;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileFilter;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.EnumSet;
import java.util.List;
import java.util.Properties;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uk.org.nbn.nbnv.api.model.ImportCleanup;
import uk.org.nbn.nbnv.api.model.ImporterResult;
import uk.org.nbn.nbnv.api.model.ImporterResult.State;
import static uk.org.nbn.nbnv.api.model.ImporterResult.State.BAD_FILE;
import static uk.org.nbn.nbnv.api.model.ImporterResult.State.MISSING_SENSITIVE_COLUMN;
import static uk.org.nbn.nbnv.api.model.ImporterResult.State.SUCCESSFUL;
import static uk.org.nbn.nbnv.api.model.ImporterResult.State.UNKNOWN_ERROR;
import static uk.org.nbn.nbnv.api.model.ImporterResult.State.VALIDATION_ERRORS;
import uk.org.nbn.nbnv.api.model.TaxonDataset;
import uk.org.nbn.nbnv.api.model.User;
import uk.org.nbn.nbnv.api.model.ValidationError;
import uk.org.nbn.nbnv.api.nxf.NXFDateCoverageTracker;
import uk.org.nbn.nbnv.api.nxf.NXFFieldMappingXMLWriter;
import uk.org.nbn.nbnv.api.nxf.NXFLine;
import uk.org.nbn.nbnv.api.nxf.NXFNormaliser;
import uk.org.nbn.nbnv.api.nxf.NXFReader;
import uk.org.nbn.nbnv.api.nxf.EMLWriter;
import uk.org.nbn.nbnv.api.nxf.NXFHeading;

/**
 * The following service manages an uploaded dataset file such that an Importer 
 * Daemon can process the file.
 * 
 * The service can also report back on errors which occurred during the importing
 * process
 * 
 * @author cjohn
 * @author jcoop
 */
@Service
public class TaxonDatasetImporterService {
    @Autowired Properties properties;
    
    private static final Pattern VALIDATION_LOG_LINE = Pattern.compile("[0-9]{4}-[A-z]{3}-[0-9]{2}\\s[0-9]{2}\\:[0-9]{2}\\:[0-9]{2}\\sERROR\\sValidation.*");
    private static final Pattern ISSUE_FILE_ARCHIVE = Pattern.compile(".*-[0-9]{18}-.*\\.zip");
    private static final EnumSet<State> ISSUE_STATES = EnumSet.of(MISSING_SENSITIVE_COLUMN);
    private static final EnumSet<State> COMPLETED_STATES = EnumSet.complementOf(ISSUE_STATES);
    private static final String EXCEPTION_ERROR = "Exception";
    private static final String VALIDATION_ERRORS_LOG_ERROR = "validation errors";
    private static final String BAD_FILE_LOG_ERROR = "invalid data file structure";
    /**
     * Looks in the processing path and determines the dataset which is 
     * currently being processed by the importer service
     * @return 
     */
    public String getCurrentlyProcessedDataset() {
        ProcessingLogFilter logFiles = new ProcessingLogFilter();
        //There should only ever be 0 or 1 files inside teh processing directory
        String[] processing = getImporterPath("processing").toFile().list(logFiles);
        
        if(processing.length != 0) {
            // Log files take the format:
            //   log-{importFile}.zip.{YYYY}.{MM}.{DD}.{hh}.{mm}.{ss}.log
            return processing[0].substring(4, processing[0].length()-28);
        }
        return null;
    }
    
    /**
     * Search the queue directory for a file named {datasetKey}.zip. If it 
     * exists then it is queued for import.
     * @param datasetKey to check if in the importer queue
     * @return true if the datasetKey is queued for import
     */
    public boolean isQueued(String datasetKey) {
        return getImporterPath("queue", datasetKey + ".zip").toFile().exists();
    }
    
    /**
     * Removes an archive from the queue. This method will return true if the 
     * operation was successful (e.g. there was a queued dataset to remove)
     * or false if it failed to delete.
     * @param datasetKey to remove from the queue
     * @return if successfully deleted a dataset from the queue
     * @throws java.io.IOException if an I/O error occurs
     */
    public boolean removeFromQueue(String datasetKey) throws IOException {
        return Files.deleteIfExists(getImporterPath("queue", datasetKey + ".zip"));
    }
    
    /**
     * Given an NXFReader of the nbn exchange format, we want to create a new
     * Zip Archive which can be passed to the importer for importing. An NBN 
     * Importer archive must contain three files:
     *  - data.tab - the original nbn exchange format
     *  - eml.xml  - a representation of the datasets metadata
     *  - meta.xml - data.tabs column mappings to darwin core fields
     * @param nxf
     * @param dataset
     * @param isUpsert if true then import will replace dataset, otherwise it will append
     * @throws IOException if the nxf is not valid, or there was a problem writing the zip
     * @throws TemplateException if there was a problem with an underlying template
     */
    public void importDataset(NXFReader nxf, TaxonDataset dataset, boolean isUpsert) throws IOException, TemplateException {
        Path upload = Files.createTempFile(getImporterPath("uploads"), "new", ".zip");
        try {
            NXFNormaliser normaliser = new NXFNormaliser(nxf.readLine());
            NXFLine header = normaliser.header();
            NXFDateCoverageTracker temporalCoverage = new NXFDateCoverageTracker(header);
            try (ZipOutputStream out = new ZipOutputStream(new FileOutputStream(upload.toFile()))) {
                PrintWriter writer = new PrintWriter(new OutputStreamWriter(out));
                out.putNextEntry(new ZipEntry("data.tab"));
                writer.println(header.toString());
                NXFLine nxfLine;
                while( (nxfLine = nxf.readLine()) != null ) {
                    nxfLine = normaliser.normalise(nxfLine); // Normalise the line
                    temporalCoverage.read(nxfLine);          //Update the temporal coverage of the nxf file
                    writer.println(nxfLine.toString());      //Write the original line to the new archive
                }
                writer.flush();
                out.putNextEntry(new ZipEntry("meta.xml"));
                new NXFFieldMappingXMLWriter(writer).write(header);
                writer.flush();
                out.putNextEntry(new ZipEntry("eml.xml"));
                new EMLWriter(writer).write(dataset, temporalCoverage.getEarliestDate(), temporalCoverage.getLatestDate(), isUpsert);
                writer.flush();
                out.closeEntry();
            }
            if(!header.getValues().contains(NXFHeading.SENSITIVE.name())) {
                Files.move(upload, getIssuePath(dataset.getKey(), getCurrentTimeStamp(), MISSING_SENSITIVE_COLUMN));
            }
            else {
                Files.move(upload, getImporterPath("queue", dataset.getKey() + ".zip")); //Success. Move to queue
            }
        }
        finally {
            Files.deleteIfExists(upload);
        }
    }
    
    /**
     * Scan over the completed directory for archived import log files for the given
     * datasetKey.
     * @param datasetKey to 
     * @return the history of all known previous imports for the given datasetKey
     *  mapped to the import timestamp
     * @throws java.io.IOException 
     */
    public List<ImporterResult> getImportHistory(String datasetKey) throws IOException {
        List<ImporterResult> history = new ArrayList<>();
        //Read in and process the history for all in the completed directory
        CompletedLogFilter completedImports = new CompletedLogFilter(datasetKey);
        File[] completed = getImporterPath("completed").toFile().listFiles(completedImports);
        for(File importArchive : completed) {
            String timestamp = importArchive.getName().substring(datasetKey.length() + 1);
            List<ValidationError> errors = getValidationErrors(new File(importArchive, "ConsoleOutput.txt"));
            ImporterResult.State state = getImporterResultState(new File(importArchive, "ConsoleErrors.txt"));
            history.add(new ImporterResult(errors, state, timestamp));
        }
        //Read in the history for the archives which have issues
        IssueArchiveFilter issueImports = new IssueArchiveFilter(datasetKey);
        File[] issues = getImporterPath("issues").toFile().listFiles(issueImports);
        for(File importArchive: issues) {
            String[] parts = importArchive.getName().split("-");
            String state = StringUtils.removeEnd(parts[2], ".zip"); //remove the extension from the state
            history.add(new ImporterResult(ImporterResult.State.valueOf(state), parts[1]));
        }        
        Collections.sort(history); //Sort into the natural history order (Most recent first)
        return history;
    }
    
    /**
     * Obtains an importer result for the given dataset key which occurred at
     * the given timestamp and is in an expected state. If an importer result
     * can not be located, then throw a NoSuchFileException
     * @param datasetKey of the dataset which was imported
     * @param timestamp of when the import occurred
     * @param state which the result should be in
     * @return The importer history which occurred at "timestamp" for "datasetkey"
     * @throws IOException if there was a problem reading the import history
     * @throws NoSuchFileException if no importer result exists
     */
    protected ImporterResult getImportHistory(String datasetKey, String timestamp, ImporterResult.State state) throws IOException {
        for(ImporterResult result: getImportHistory(datasetKey)) {
            if(result.getState() == state && timestamp.equals(result.getTimestamp())) {
                return result;
            }
        }
        throw new NoSuchFileException("Could not find an importer result for " + datasetKey + " in the " + state + " state at " + timestamp);
    }
    
    /**
     * Remove the importer result as specified by the given datasetkey, timestamp
     * and state. This method will actually delete the result from disk in all
     * cases other than when that result was successful. In which case it will
     * be moved to the archive directory.
     * @param datasetKey to find the import result of
     * @param timestamp when the import result occurred
     * @param state that the import result is expected to be in
     * @throws IOException if there was a problem removing the result
     * @throws NoSuchFileException if no importer result exists
     */
    public void removeImporterResult(String datasetKey, String timestamp, ImporterResult.State state) throws IOException {
        //Make sure that the requested importer result actually exists
        ImporterResult result = getImportHistory(datasetKey, timestamp, state);
        String archive = datasetKey + "-" + result.getTimestamp();
        if(state == SUCCESSFUL) {
            // We want to keep the successful imports, but hide them from the
            // end user. Just move them in to the archived directory to be moved
            // at a later time
            Files.move(getImporterPath("completed", archive), getImporterPath("archived", archive));
        }
        else if(COMPLETED_STATES.contains(state)) {
            // Anything else in the completed directory should just be deleted.
            // We move the directory into the workspace as there is a slight 
            // chance one of the files in there may be locked. This should allow
            // us atomically hide the directory from the other methods on this
            // class. If successful, just delete
            Path tmp = getImporterPath("uploads", archive);
            Files.move(getImporterPath("completed", archive), tmp);
            FileUtils.deleteDirectory(tmp.toFile());    
        }
        else if(ISSUE_STATES.contains(state)) {
            // Issue states have not been processed by the importer and as such
            // exist as a single file. Just delete
            Files.delete(getIssuePath(datasetKey, timestamp, state));
        }
    }
    
    /**
     * Grab an archived upload from the completed directory and process the NBN 
     * Exchange Format file to remove any lines which have been flagged as invalid.
     * 
     * Pop the resultant cleaned archive back onto the import queue
     * @param datasetKey
     * @param timestamp
     * @throws java.nio.file.NoSuchFileException if no archive is saved for the 
     *  given timestamp and datasetkey
     * @throws java.nio.file.FileAlreadyExistsException if a file is already on 
     *  the queue
     */
    public void stripInvalidRecords(String datasetKey, String timestamp) throws IOException, NoSuchFileException, FileAlreadyExistsException {
        ImporterResult status = getImportHistory(datasetKey, timestamp, VALIDATION_ERRORS);
        Path upload = Files.createTempFile(getImporterPath("uploads"), "reimport", ".zip");
        Path archive = getImporterPath("completed", datasetKey + "-" + timestamp, datasetKey + ".zip");
        try {
            try (ZipOutputStream out = new ZipOutputStream(new FileOutputStream(upload.toFile()))) {
                try (ZipFile zipFile = new ZipFile(archive.toFile())) {
                    copyEntries(zipFile, out, Arrays.asList("data.tab")); //Copy all files other than data.tab
                    InputStream in = zipFile.getInputStream(zipFile.getEntry("data.tab"));
                    out.putNextEntry(new ZipEntry("data.tab"));
                    copyWithoutValidationErrors(in, out, status.getValidationErrors());
                    out.closeEntry();
                }
            }
            Files.move(upload, getImporterPath("queue", datasetKey + ".zip")); //Success. Move to queue
        }
        finally {
            Files.deleteIfExists(upload); //Clear up the upload document
        }
    }
    
    /**
     * Gets a dataset from the issues directory which has failed due to a missing
     * sensitive column. Reads in the data from the archive and append the sensitive
     * column with the supplied value set
     * @param datasetKey of the dataset which previously failed due to missing column
     * @param timestamp when this dataset failed
     * @param sensitive the new value for the sensitive column
     * @throws java.io.IOException if there is an issue with writing files
     * @throws freemarker.template.TemplateException if there was a problem with an underlying template
     */
    public void queueDatasetWithSensitiveColumnSet(String datasetKey, String timestamp, boolean sensitive) throws IOException, TemplateException {
        Path upload = Files.createTempFile(getImporterPath("uploads"), "new", ".zip");
        try {
            try (ZipOutputStream out = new ZipOutputStream(new FileOutputStream(upload.toFile()))) {
                Path failedArchivePath = getIssuePath(datasetKey, timestamp, MISSING_SENSITIVE_COLUMN);
                try (ZipFile failedArchive = new ZipFile(failedArchivePath.toFile())) {
                    //Copy all the files other than the ones we need to modify
                    copyEntries(failedArchive, out, Arrays.asList("data.tab", "meta.xml"));
                    
                    //Process the original data.tab and append the sensitive column
                    ZipEntry failedDataTab = failedArchive.getEntry("data.tab");
                    NXFReader failedData = new NXFReader(new InputStreamReader(failedArchive.getInputStream(failedDataTab)));
                    NXFLine newHeader = failedData.readLine().append(NXFHeading.SENSITIVE.name()); //Add the sensitive column

                    PrintWriter writer = new PrintWriter(new OutputStreamWriter(out));
                    out.putNextEntry(new ZipEntry("data.tab"));
                    writer.println(newHeader.toString());
                    NXFLine line;
                    while( (line = failedData.readLine()) != null ) {
                        //Append the supplied sensitive value to each line
                        writer.println(line.append(Boolean.toString(sensitive)).toString());
                    }
                    writer.flush();
                    
                    //The header will have changed so we need to recreate the meta.xml.
                    out.putNextEntry(new ZipEntry("meta.xml"));
                    new NXFFieldMappingXMLWriter(writer).write(newHeader);
                    writer.flush();
                    out.closeEntry();
                }
            }
            //Put the processed file back on to the queue
            Files.move(upload, getImporterPath("queue", datasetKey + ".zip")); //Success. Move to queue   
        }
        finally {
            Files.deleteIfExists(upload);
        }
    }
    
    /**
     * Scan the given ConsoleErrors log file for any occurrences of the word
     * 'Exception'. If present, we assume that the import was a failure
     * @param consoleErrors 
     * @return the state which can be determined by reading from the consoleErrors log
     * @throws IOException 
     */
    protected ImporterResult.State getImporterResultState(File consoleErrors) throws IOException {
        try (BufferedReader in = new BufferedReader(new FileReader(consoleErrors))) {
            String line;
            while((line = in.readLine()) != null) {
                if(line.contains(VALIDATION_ERRORS_LOG_ERROR)) {
                    return VALIDATION_ERRORS;
                }
                else if(line.contains(BAD_FILE_LOG_ERROR)) {
                    return BAD_FILE;
                }
                else if(line.startsWith(EXCEPTION_ERROR)) {
                    //The importer failed, for some unknown reason. This requires
                    //the someone to manually review the logs for this import
                    return UNKNOWN_ERROR;
                }
            }
        }
        return SUCCESSFUL;
    }
    
    /**
     * Scan the given ConsoleOutput log file for validation errors.
     * @param consoleOutput log file to read
     * @return a list of validation errors which are present in that file
     * @throws java.io.IOException if there was a problem reading the file
     */
    protected List<ValidationError> getValidationErrors(File consoleOutput) throws IOException {
        List<ValidationError> errors = new ArrayList<>();
        try (BufferedReader in = new BufferedReader(new FileReader(consoleOutput))) {
            while(in.ready()) {
                String line = in.readLine();
                if(VALIDATION_LOG_LINE.matcher(line).matches()) {
                    errors.add(parseValidationError(line));
                }
            }
        }
        return errors;
    }
    
    // Parse the validation error line and turn it into a Validation Error object
    private ValidationError parseValidationError(String line) {
        int recKeyStart = 39;
        int recKeyEnd   = line.indexOf(']', recKeyStart);
        int ruleEnd     = line.indexOf(':', recKeyEnd);
        
        ValidationError toReturn = new ValidationError();
        toReturn.setRecordKey(line.substring(recKeyStart, recKeyEnd));
        toReturn.setRule(line.substring(recKeyEnd + 2, ruleEnd));
        toReturn.setMessage(line.substring(ruleEnd + 2));
        return toReturn;
    }
    
    //Process a dataTab input stream and remove any record keys which have failed validation
    private void copyWithoutValidationErrors(InputStream dataTab, OutputStream zip, List<ValidationError> errors) throws IOException {
        NXFReader in = new NXFReader(new InputStreamReader(dataTab));
        PrintWriter out = new PrintWriter(zip);
        
        // Create a list of the recordKeys which need to be skipped
        List<String> keysToSkip = new ArrayList<>();
        for(ValidationError error: errors) {
            keysToSkip.add(error.getRecordKey());
        }
        
        NXFLine header = in.readLine(); //Read the header from the original data.tab        
        int recordKeyColumn = header.getValues().indexOf(NXFHeading.RECORDKEY.name()); //Locate the record key column
        out.println(header.toString()); //Write the original header to the new file
        
        //Copy all lines from the input file except ones with invalid record keys
        NXFLine line;
        while( (line = in.readLine()) != null ) {
            if(!keysToSkip.contains(line.getValues().get(recordKeyColumn))) {
                out.println(line.toString());
            }
            out.flush();
        }
    }
    
    private Path getImporterPath(String... parts) {
        String location = properties.getProperty("importer_location");
        return FileSystems.getDefault().getPath(location, parts);
    }
    
    private Path getIssuePath(String datasetKey, String timestamp, ImporterResult.State issue) {
        return getImporterPath("issues", datasetKey + "-" + timestamp + "-" + issue.name() + ".zip");
    }
    
    private String getCurrentTimeStamp() {
        DateFormat dateFormat = new SimpleDateFormat("yyyyMMddhhmmssSSS");
        return dateFormat.format(Calendar.getInstance().getTime()) + "0";
    }
    
    private void copyEntries(ZipFile in, ZipOutputStream out, List<String> omit) throws IOException {
        for(ZipEntry entry : Collections.list(in.entries())) {
            String name = entry.getName();
            if(!omit.contains(name)) {
                out.putNextEntry(new ZipEntry(name));
                IOUtils.copy(in.getInputStream(entry), out);
                out.closeEntry();
            }
        }
    }

    public void reprocessHistoricalImport(User user, String datasetKey, String timestamp, ImportCleanup cleanup) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    private static class ProcessingLogFilter implements FilenameFilter {
        @Override
        public boolean accept(File dir, String name) {
            return name.startsWith("log-") && name.endsWith(".log");
        }
    }
    
    private static class CompletedLogFilter implements FileFilter {
        private final String datasetKey;
        
        public CompletedLogFilter(String datasetKey) {
            this.datasetKey = datasetKey;
        }
        
        @Override
        public boolean accept(File pathname) {
            return pathname.isDirectory() && pathname.getName().toLowerCase().startsWith(datasetKey.toLowerCase() + "-");
        }
    }
    
    private static class IssueArchiveFilter implements FileFilter {
        private final String datasetKey;
        
        public IssueArchiveFilter(String datasetKey) {
            this.datasetKey = datasetKey;
        }
        
        @Override
        public boolean accept(File pathname) {
            String name = pathname.getName();
            return pathname.isFile()
                   && ISSUE_FILE_ARCHIVE.matcher(name).matches() 
                   && name.toLowerCase().startsWith(datasetKey.toLowerCase());
        }
    }
}
