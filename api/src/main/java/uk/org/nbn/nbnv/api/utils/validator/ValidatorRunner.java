/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.org.nbn.nbnv.api.utils.validator;

import java.util.Properties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *
 * @author Matt Debont
 */
@Component
public class ValidatorRunner {
    
    @Autowired private Properties properties;
    
    private boolean isRunning;
    
    public synchronized void checkForInput() throws RuntimeException {
        if (isRunning) {
            throw new RuntimeException("Validator is already running, waiting ...");
        }
        
        start(null);
    }
    
    private void checkForInputPostRun() {
        
    }
    
    public synchronized boolean isRunning() {
        return isRunning;
    }
    
    private void start(String jobName) {
        // Stage 1 Processing
        
        // Stage 2 Processing
        
        checkForInputPostRun();
    }
}