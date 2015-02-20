package uk.org.nbn.nbnv.api.services;

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
import java.io.PrintWriter;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uk.org.nbn.nbnv.api.model.ValidationError;

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
public class DatasetImporterService {
    @Autowired Properties properties;
    
    private static final Pattern VALIDATION_LOG_LINE = Pattern.compile("[0-9]{4}-[A-z]{3}-[0-9]{2}\\s[0-9]{2}\\:[0-9]{2}\\:[0-9]{2}\\sERROR\\sValidation.*");
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
     * Scan over the completed directory for archived import log files for the given
     * datasetKey.
     * @param datasetKey to 
     * @return the history of all known previous imports for the given datasetKey
     *  mapped to the import timestamp
     * @throws java.io.IOException 
     */
    public Map<String, List<ValidationError>> getImportHistory(String datasetKey) throws IOException {
        Map<String, List<ValidationError>> history = new HashMap<>();
        CompletedLogFilter completedImports = new CompletedLogFilter(datasetKey);
        File[] completed = getImporterPath("completed").toFile().listFiles(completedImports);
        for(File importArchive : completed) {
            String timestamp = importArchive.getName().substring(datasetKey.length() + 1);
            history.put(timestamp, getValidationErrors(new File(importArchive, "ConsoleOutput.txt")));
        }
        return history;
    }
    
    /**
     * Grab an archived upload from the completed directory and process the NBN 
     * Exchange Format file to remove any lines which have been flagged as invalid.
     * 
     * Pop the resultant cleaned archive back onto the import queue
     * @param datasetKey
     * @param timestamp 
     */
    public void stripInvalidRecords(String datasetKey, String timestamp) throws IOException {
        List<ValidationError> errors = getImportHistory(datasetKey).get(timestamp);
        if(errors == null || errors.isEmpty()) {
            throw new IllegalArgumentException("There is no archive which previously failed to import with the given timestamp");
        }
        Path upload = getImporterPath("uploads", datasetKey + ".zip");
        Path archive = getImporterPath("completed", datasetKey + "-" + timestamp, datasetKey + ".zip");
        try (ZipOutputStream out = new ZipOutputStream(new FileOutputStream(upload.toFile()))) {
            try (ZipFile zipFile = new ZipFile(archive.toFile())) {
                for(ZipEntry entry : Collections.list(zipFile.entries())) {
                    out.putNextEntry(new ZipEntry(entry.getName()));
                    InputStream in = zipFile.getInputStream(entry);
                    if(entry.getName().equals("data.tab")) {
                        copyWithoutValidationErrors(in, out, errors);
                    }
                    else {
                       IOUtils.copy(in, out); 
                    }
                    out.closeEntry();
                }
            }
        }
        moveToImportQueue(datasetKey); //A new dataset.zip has been created, pop it onto the queue
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
    
    /**
     * Moves the prepared dataset import zip from the uploads directory to the 
     * importer queue directory. At this point the importer service can kick in
     * and begin the import process.
     * 
     * @param datasetKey
     * @throws NoSuchFileException if nothing has been uploaded with the given datasetKey
     * @throws FileAlreadyExistsException if an import for this dataset is already 
     *  in the queue
    */
    protected void moveToImportQueue(String datasetKey) throws IOException, NoSuchFileException, FileAlreadyExistsException {
        Path upload = getImporterPath("uploads", datasetKey + ".zip");
        Path queue = getImporterPath("queue", datasetKey + ".zip");
        Files.move(upload, queue);
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
            return pathname.isDirectory() && pathname.getName().startsWith(datasetKey + "-");
        }
    }
}
