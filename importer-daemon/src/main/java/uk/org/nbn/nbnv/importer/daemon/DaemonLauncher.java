package uk.org.nbn.nbnv.importer.daemon;

import org.apache.commons.daemon.Daemon;
import org.apache.commons.daemon.DaemonContext;
import org.apache.commons.daemon.DaemonInitException;

/**
 * Hello world!
 *
 */
public class DaemonLauncher implements Daemon
{    
    private ImporterDaemon daemon;
    
    public static void main( String[] args )
    {
        
    }

    @Override
    public void init(DaemonContext dc) throws DaemonInitException, Exception {
        daemon = new ImporterDaemon();
    }

    @Override
    public void start() throws Exception {
        daemon.start();
    }

    @Override
    public void stop() throws Exception {
        
    }

    @Override
    public void destroy() {
        
    }
}
