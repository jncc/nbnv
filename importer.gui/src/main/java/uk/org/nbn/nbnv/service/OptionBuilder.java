package uk.org.nbn.nbnv.service;

import org.springframework.stereotype.Component;
import scala.Enumeration;
import uk.org.nbn.nbnv.Logger;
import uk.org.nbn.nbnv.importer.Options;
import uk.org.nbn.nbnv.importer.Target;

import java.io.File;
import java.util.List;
import java.util.Map;

/**
 * @author Stephen Batty
 *         Date: 31/07/14
 *         Time: 11:45
 */
@Component("optionBuiler")
public class OptionBuilder {

    public Options build(File file ) {

        String archivePath = file.getAbsolutePath().toString();
        Logger.info("file is : " + archivePath);
        Enumeration.Value target = Target.validate();
        String logLevel = "ERROR";
        String logDir = "/importer-logs";
        String tempDir = "/";
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
