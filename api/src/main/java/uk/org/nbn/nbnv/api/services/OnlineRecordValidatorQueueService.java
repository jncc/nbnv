package uk.org.nbn.nbnv.api.services;

import java.util.Properties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import uk.org.nbn.nbnv.api.mail.TemplateMailer;
import uk.org.nbn.nbnv.api.utils.validator.ValidatorRunner;

/**
 *
 * @author Matt Debont
 */
public class OnlineRecordValidatorQueueService {
    
    @Autowired private ValidatorRunner runner;
    @Autowired private Properties properties;
    
    private Logger logger = LoggerFactory.getLogger(OnlineRecordValidatorQueueService.class);
    
    @Scheduled(cron = "0 0 0 * * *")
    public void checkForNewInput() {
        if (!runner.isRunning()) {
            runner.checkForInput();
        }
    }
    
    @Scheduled(cron = "0 0 0 * * *")
    public void deleteOldInputs() {
        
    }
}