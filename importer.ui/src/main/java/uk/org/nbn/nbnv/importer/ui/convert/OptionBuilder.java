package uk.org.nbn.nbnv.importer.ui.convert;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import scala.Enumeration;
import uk.org.nbn.nbnv.importer.Options;
import uk.org.nbn.nbnv.importer.Target;

import java.io.File;
import java.io.IOException;
import java.net.URL;

/**
 * @author Stephen Batty
 *         Date: 31/07/14
 *         Time: 11:45
 */
@Component("optionBuilder")
public class OptionBuilder {

    public Options build(File file ) {

        String archivePath = file.getAbsolutePath().toString();
        Enumeration.Value target = Target.validate();
        String logLevel = "ERROR";
        String rootDir = null;
        Resource rootResource = new ClassPathResource("importer.ui.properties");
        try {
            rootDir = rootResource.getFile().getParent();
        } catch (IOException e) {
            // do soemthign
            e.printStackTrace();
        }

        String logDir = rootDir+"/importer-logs";
        String tempDir = rootDir+"/importer-temp";
        checkExistCreateIfNot(logDir, tempDir);

        int flush = 25;
        Options options =  new Options(archivePath, target, logLevel, logDir, tempDir, flush);
        return options;
        /*archivePath:   "specify-an-archive-path.zip",
                target:Target.Value = Target.commit,
                logLevel:String = "INFO",
                logDir:String = ".",
                tempDir:String = "./temp",
                flush:Int = 25*/

    }

    private void checkExistCreateIfNot(String logDir, String tempDir) {
        File temp = new File(tempDir);
        if(!temp.canRead()){
            temp.mkdir();
        }
        File log = new File(logDir);
        if(!log.canRead()){
            log.mkdir();
        }
    }

    public enum Keys {

        ARCHIVE_PATH(String.class),
        TARGET(String.class),
        LOG_LEVEL(String.class),
        LOG_DIR(String.class),
        TEMP_DIR(String.class),
        FLUSH(int.class);

        private Class clazz;

        Keys(Class clazz) {
            this.clazz = clazz;
        }

        /** Not used for anything yet, but might be handy for validating */
        public Class getParameterType(){
            return this.clazz;
        }
    }
}
