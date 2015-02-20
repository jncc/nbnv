package uk.org.nbn.nbnv.api.services;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileFilter;
import java.io.FileReader;
import java.io.FilenameFilter;
import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.FileSystems;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.regex.Pattern;
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
        int lineNoStart = 39;
        int lineNoEnd   = line.indexOf(']', lineNoStart);
        int ruleEnd     = line.indexOf(':', lineNoEnd);
        
        ValidationError toReturn = new ValidationError();
        toReturn.setLineNumber(Long.parseLong(line.substring(lineNoStart, lineNoEnd)));
        toReturn.setRule(line.substring(lineNoEnd + 2, ruleEnd));
        toReturn.setMessage(line.substring(ruleEnd + 2));
        return toReturn;
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
    
    public void moveToImportQueue(String datasetKey) throws IOException, NoSuchFileException, FileAlreadyExistsException {
        Path upload = getImporterPath("uploads", datasetKey + ".zip");
        Path queue = getImporterPath("queue", datasetKey + ".zip");
        Files.move(upload, queue);
    }
    */
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
