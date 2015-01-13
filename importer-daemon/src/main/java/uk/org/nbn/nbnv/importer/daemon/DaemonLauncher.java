package uk.org.nbn.nbnv.importer.daemon;

import java.io.File;
import java.util.Properties;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.DailyRollingFileAppender;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Launches the importer daemon app on a defined schedule, every X minutes where
 * X is defined in importer-daemon.properties
 */
public class DaemonLauncher
{    
    private ImporterDaemon daemon;
    private ScheduledExecutorService sExecutor;
    private final ClassPathXmlApplicationContext context;
    
    /**
     * Main entry point to set up file logging and then start the scheduled 
     * executor
     * 
     * @param args No arguments necessary here
     * @throws Exception 
     */
    public static void main(String[] args) throws Exception {                
        DaemonLauncher launcher = new DaemonLauncher();
        launcher.start();
    }
    
    public DaemonLauncher() throws Exception {
        this.context = new ClassPathXmlApplicationContext("applicationContext.xml");     
    }

    /**
     * Kick off a scheduled task executor to run every X minutes where X is 
     * defined by the properties file for this application 
     * (importer-daemon.properties Global override in /nbnv-settings/) 
     * 
     * @throws Exception 
     */
    public void start() throws Exception {        
        Properties properties = (Properties) context.getBean("properties");
        
        // Setup log file appender and 
        PatternLayout layout = new PatternLayout("[%p] %d %c %M - %m%n");
        
        DailyRollingFileAppender rollingAppender = new DailyRollingFileAppender();
        rollingAppender.setFile(properties.getProperty("logDirectory") + File.separator + "daemon.log");
        rollingAppender.setDatePattern("'.'yyyy-MM-dd");
        rollingAppender.setLayout(layout);
        rollingAppender.activateOptions();
        
        ConsoleAppender consoleAppender = new ConsoleAppender();
        consoleAppender.setLayout(layout);
        consoleAppender.activateOptions();
        
        Logger rootLogger = Logger.getRootLogger();
        rootLogger.setLevel(Level.INFO);
        rootLogger.addAppender(rollingAppender);
        rootLogger.addAppender(consoleAppender);        
        
        daemon = new ImporterDaemon(properties);
        context.getAutowireCapableBeanFactory().autowireBeanProperties(daemon, AutowireCapableBeanFactory.AUTOWIRE_BY_NAME, true);
        
        sExecutor = Executors.newSingleThreadScheduledExecutor();
        sExecutor.scheduleAtFixedRate(daemon, 0, 
                        Integer.parseInt(properties.getProperty("sleepTime")), 
                        TimeUnit.MINUTES);
    }
}