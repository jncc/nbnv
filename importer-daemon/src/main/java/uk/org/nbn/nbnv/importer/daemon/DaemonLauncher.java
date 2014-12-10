package uk.org.nbn.nbnv.importer.daemon;

import java.util.Properties;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import org.apache.commons.daemon.Daemon;
import org.apache.commons.daemon.DaemonContext;
import org.apache.commons.daemon.DaemonInitException;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Hello world!
 *
 */
public class DaemonLauncher implements Daemon
{    
    private ImporterDaemon daemon;
    private ScheduledExecutorService sExecutor;
    private ScheduledFuture scheduledFuture;
    private ClassPathXmlApplicationContext context;
    
    public static void main(String[] args) throws Exception {        
        DaemonLauncher launcher = new DaemonLauncher();
        launcher.start();
    }
    
    public DaemonLauncher() throws Exception {
        this.context = new ClassPathXmlApplicationContext("applicationContext.xml");     
    }

    @Override
    public void init(DaemonContext dc) throws DaemonInitException, Exception {
        this.context = new ClassPathXmlApplicationContext("applicationContext.xml");
    }

    @Override
    public void start() throws Exception {        
        Properties properties = (Properties) context.getBean("properties");
        daemon = new ImporterDaemon(properties);
        context.getAutowireCapableBeanFactory().autowireBeanProperties(daemon, AutowireCapableBeanFactory.AUTOWIRE_BY_NAME, true);
        
        sExecutor = Executors.newSingleThreadScheduledExecutor();
        scheduledFuture = sExecutor.scheduleAtFixedRate(
                        daemon, 0, 
                        30, 
                        TimeUnit.MINUTES);
    }

    @Override
    public void stop() throws Exception {
        scheduledFuture.cancel(true);
        sExecutor.shutdownNow();
    }

    @Override
    public void destroy() {
        scheduledFuture.cancel(true);
        sExecutor.shutdownNow();
    }
}