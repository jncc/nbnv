package uk.org.nbn.nbnv.importer.daemon;

import java.util.Properties;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import org.apache.commons.daemon.Daemon;
import org.apache.commons.daemon.DaemonContext;
import org.apache.commons.daemon.DaemonInitException;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import uk.org.nbn.nbnv.importer.daemon.mail.TemplateMailer;

/**
 * Hello world!
 *
 */
public class DaemonLauncher implements Daemon
{    
    private ImporterDaemon daemon;
    private ScheduledExecutorService sExecutor;
    private ScheduledFuture scheduledFuture;
    private final ClassPathXmlApplicationContext context;
    
    public static void main(String[] args) throws Exception {        
        ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext("applicationContext.xml");
        DaemonLauncher launcher = new DaemonLauncher(ctx);
        launcher.start();
    }
    
    public DaemonLauncher(ClassPathXmlApplicationContext context) {
        this.context = context;
    }

    @Override
    public void init(DaemonContext dc) throws DaemonInitException, Exception {
    }

    @Override
    public void start() throws Exception {        
        Properties properties = (Properties) context.getBean("properties");
        daemon = new ImporterDaemon(properties, null);
        //daemon.run();
        
        sExecutor = Executors.newSingleThreadScheduledExecutor();
        scheduledFuture = sExecutor.scheduleAtFixedRate(
                        daemon, 0, 
                        Integer.getInteger(properties.getProperty("sleepTime")), 
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