package uk.org.nbn.nbnv.importer.daemon;

import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import org.apache.commons.daemon.Daemon;
import org.apache.commons.daemon.DaemonContext;
import org.apache.commons.daemon.DaemonInitException;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Hello world!
 *
 */
public class DaemonLauncher implements Daemon
{    
    private ImporterDaemon daemon;
    private ExecutorService executor;
    private Future<?> future;
    private ClassPathXmlApplicationContext context;
    
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