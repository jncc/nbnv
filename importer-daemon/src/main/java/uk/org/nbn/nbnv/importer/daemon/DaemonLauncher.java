package uk.org.nbn.nbnv.importer.daemon;

import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import org.apache.commons.daemon.Daemon;
import org.apache.commons.daemon.DaemonContext;
import org.apache.commons.daemon.DaemonInitException;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import uk.org.nbn.nbnv.importer.daemon.config.ApplicationConfig;

/**
 * Hello world!
 *
 */
public class DaemonLauncher implements Daemon
{    
    private ImporterDaemon daemon;
    private ExecutorService executor;
    private Future<?> future;
    private AnnotationConfigApplicationContext context;
    
    public static void main(String[] args) throws Exception {        
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(ApplicationConfig.class);
        DaemonLauncher launcher = new DaemonLauncher(context);
        launcher.start();
    }
    
    public DaemonLauncher(AnnotationConfigApplicationContext context) {
        this.context = context;
    }

    @Override
    public void init(DaemonContext dc) throws DaemonInitException, Exception {
    }

    @Override
    public void start() throws Exception {        
        daemon = new ImporterDaemon((Properties) context.getBean("properties"));
        daemon.run();
//        executor = Executors.newSingleThreadExecutor();
//        future = executor.submit(daemon);
//        executor.
    }

    @Override
    public void stop() throws Exception {
        future.cancel(true);
        executor.shutdown();
    }

    @Override
    public void destroy() {
        future.cancel(true);
        executor.shutdown();
    }
}