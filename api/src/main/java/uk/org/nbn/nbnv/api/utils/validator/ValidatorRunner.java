/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.org.nbn.nbnv.api.utils.validator;

import java.io.File;
import java.io.FilenameFilter;
import java.util.List;
import java.util.Properties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import uk.org.nbn.nbnv.api.mail.TemplateMailer;
import uk.org.nbn.nbnv.importer.s1.utils.parser.ColumnMapping;

/**
 *
 * @author Matt Debont
 */
@Component
public class ValidatorRunner {
    
    @Autowired private Properties properties;
    @Autowired private TemplateMailer templateMailer;
    
    private boolean isRunning;
    
    /**
     * Check for new input in the queued jobs folder and run the next one in the
     * queue
     * 
     * @throws RuntimeException 
     */
    public synchronized void checkForInput() throws RuntimeException {
        String nextJob = getNextJob();
        
        if (nextJob != null && !isRunning) {
            isRunning = true;
            start(nextJob);
        } else {
            throw new RuntimeException("Validator is already running, waiting ...");
        }        
    }
    
    private String getNextJob() {
        File file = new File(properties.getProperty("validator_queue"));
        String[] directories = file.list(new FilenameFilter() {
            @Override
            public boolean accept(File current, String name) {
                return new File(current, name).isDirectory();
            }
        });
        
        if (directories != null && directories.length > 0) {    
            return directories[0];
        } else {
            return null;
        }
    }
    
    /**
     * After we have finished a job, check for new jobs in the queued folders 
     * and start processing the next one in the list
     */
    private void checkForInputPostRun() {
        String nextJob = getNextJob();
        
        if (nextJob == null) {
            isRunning = false;
        } else {
            start(nextJob);
        }
    }
    
    /**
     * Checks to see if a job is being run or not
     * 
     * @return If a job is being processed currently
     */
    public synchronized boolean isRunning() {
        return isRunning;
    }
    
    /**
     * Starts processing the specified job, relating to a folder in the Queued 
     * input folder, should be ordered by submitted date 
     * 
     * @param jobName The folder containing the job to be started of the format
     * yyyyMMdd_hhmmss_userID_abcde12345
     */
    private void start(String jobName) {
        File upload = new File(properties.getProperty("validator_queue") + File.separator + jobName + File.separator + "upload.tab");
        File mappingFile = new File(properties.getProperty("validator_queue") + File.separator + jobName + File.separator + "mappings.txt");
        
        if (!upload.exists() || !mappingFile.exists()) {
            // Email to user with error message
            // Delete Job Directory
        } else {
            // Stage 1 Processing
            List<ColumnMapping> mappings = getMappingsFromFile(mappingFile);

            // Stage 2 Processing

            // Email result

            // Delete Job
        }
        
        checkForInputPostRun();
    }
    
    private List<ColumnMapping> getMappingsFromFile(File file) {
        
        return null;
    }
}