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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Properties;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uk.org.nbn.nbnv.api.model.ImporterResult;
import uk.org.nbn.nbnv.api.model.TaxonDataset;
import uk.org.nbn.nbnv.api.model.ValidationError;
import uk.org.nbn.nbnv.api.nxf.NXFDateCoverageTracker;
import uk.org.nbn.nbnv.api.nxf.NXFFieldMappingXMLWriter;
import uk.org.nbn.nbnv.api.nxf.NXFLine;
import uk.org.nbn.nbnv.api.nxf.NXFNormaliser;
import uk.org.nbn.nbnv.api.nxf.NXFReader;
import uk.org.nbn.nbnv.api.utils.EMLWriter;

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
    private static final Pattern EXCEPTION_LOG_ERROR = Pattern.compile("Exception.*");
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
            }
            Files.move(upload, getImporterPath("queue", dataset.getKey() + ".zip")); //Success. Move to queue
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
        CompletedLogFilter completedImports = new CompletedLogFilter(datasetKey);
        File[] completed = getImporterPath("completed").toFile().listFiles(completedImports);
        for(File importArchive : completed) {
            String timestamp = importArchive.getName().substring(datasetKey.length() + 1);
            List<ValidationError> errors = getValidationErrors(new File(importArchive, "ConsoleOutput.txt"));
            boolean success = isSuccessfulImport(new File(importArchive, "ConsoleErrors.txt"));
            history.add(new ImporterResult(errors, success, timestamp));
        }
        Collections.sort(history); //Sort into the natural history order (Most recent first)
        return history;
    }
    
    /**
     * Obtains an importer result for the given dataset key. The one obtained 
     * will have occurred at the supplied timestamp. If no result importer result
     * exists an IllegalArgumentException will be thrown
     * @param datasetKey of the dataset which was imported
     * @param timestamp of when the import occurred
     * @return The importer history which occurred at "timestamp" for "datasetkey"
     * @throws IOException if there was a problem reading the import history
     * @throws NoSuchFileException if no importer result exists
     */
    public ImporterResult getImportHistory(String datasetKey, String timestamp) throws IOException {
        for(ImporterResult result: getImportHistory(datasetKey)) {
            if(!result.isSuccess() && timestamp.equals(result.getTimestamp())) {
                return result;
            }
        }
        throw new NoSuchFileException("There is no archive which previously failed to import with the given timestamp");
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
        ImporterResult status = getImportHistory(datasetKey, timestamp);
        Path upload = Files.createTempFile(getImporterPath("uploads"), "reimport", ".zip");
        Path archive = getImporterPath("completed", datasetKey + "-" + timestamp, datasetKey + ".zip");
        try {
            try (ZipOutputStream out = new ZipOutputStream(new FileOutputStream(upload.toFile()))) {
                try (ZipFile zipFile = new ZipFile(archive.toFile())) {
                    for(ZipEntry entry : Collections.list(zipFile.entries())) {
                        out.putNextEntry(new ZipEntry(entry.getName()));
                        InputStream in = zipFile.getInputStream(entry);
                        if(entry.getName().equals("data.tab")) {
                            copyWithoutValidationErrors(in, out, status.getValidationErrors());
                        }
                        else {
                           IOUtils.copy(in, out); 
                        }
                        out.closeEntry();
                    }
                }
            }
            Files.move(upload, getImporterPath("queue", datasetKey + ".zip")); //Success. Move to queue
        }
        finally {
            Files.deleteIfExists(upload); //Clear up the upload document
        }
    }
    
    /**
     * Scan the given ConsoleErrors log file for any occurrances of the word
     * 'Exception'. If present, we assume that the import was a failure
     * @param consoleErrors 
     * @return if the consoleErrors log does not have a line which starts with 'Exception'
     * @throws IOException 
     */
    protected boolean isSuccessfulImport(File consoleErrors) throws IOException {
        try (BufferedReader in = new BufferedReader(new FileReader(consoleErrors))) {
            while(in.ready()) {
                if(EXCEPTION_LOG_ERROR.matcher(in.readLine()).matches()) {
                    return false;
                }
            }
        }
        return true;
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
        BufferedReader in = new BufferedReader(new InputStreamReader(dataTab));
        PrintWriter out = new PrintWriter(zip);
        
        // Create a list of the recordKeys which need to be skipped
        List<String> keysToSkip = new ArrayList<>();
        for(ValidationError error: errors) {
            keysToSkip.add(error.getRecordKey());
        }
        
        String header = in.readLine(); //Read the header from the original data.tab        
        int recordKeyColumn = Arrays.asList(header.toUpperCase().split("\t")).indexOf("RECORDKEY"); //Locate the record key column
        out.println(header);           //Write the original header to the new file
        
        //Copy all non-empty lines from the input file except ones with invalid record keys
        while(in.ready()) {
            String line = in.readLine();
            if(line.trim().isEmpty() || !keysToSkip.contains(line.split("\t")[recordKeyColumn])) {
                out.println(line);
            }
            out.flush();
        }
    }
    
    private Path getImporterPath(String... parts) {
        String location = properties.getProperty("importer_location");
        return FileSystems.getDefault().getPath(location, parts);
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
}