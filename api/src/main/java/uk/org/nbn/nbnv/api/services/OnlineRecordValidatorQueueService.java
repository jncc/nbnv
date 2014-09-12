package uk.org.nbn.nbnv.api.services;

import java.io.File;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.Properties;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.AgeFileFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import uk.org.nbn.nbnv.api.utils.validator.ValidatorRunner;
import static org.apache.commons.io.filefilter.TrueFileFilter.TRUE;

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
        logger.info("Checking for new uploads");
//        if (!runner.isRunning()) {
//            runner.checkForInput();
//        }
    }
    
    @Scheduled(cron = "0 0 0 * * *")
    public void deleteOldInputs() {
        logger.info("Cleaning up old uploads that have not been acted on");
        
//        File dir = new File(properties.getProperty("temp"));
//        
//        Calendar cal = Calendar.getInstance();
//        cal.setTime(new Date());
//        cal.add(Calendar.HOUR_OF_DAY, -2);
//        
//        Iterator<File> foldersToDelete = FileUtils.iterateFiles(dir, new AgeFileFilter(cal.getTime()), TRUE);
//        while (foldersToDelete.hasNext()) {
//            foldersToDelete.next().delete();
//        }
    }
}