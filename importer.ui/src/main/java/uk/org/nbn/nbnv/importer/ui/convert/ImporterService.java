package uk.org.nbn.nbnv.importer.ui.convert;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uk.org.nbn.nbnv.importer.Importer;
import uk.org.nbn.nbnv.importer.Options;

import java.io.File;

/**
 * @author Stephen Batty
 *         Date: 30/07/14
 *         Time: 16:47
 */
@Service("importerService")
public class ImporterService {

    private static Logger logger = LoggerFactory.getLogger(ImporterService.class);
    @Autowired
    OptionBuilder optionBuilder;

    public boolean doImport(File file) {
        logger.info("Creating importer options");
        Options options = optionBuilder.build(file);

        Importer importer = Importer.createImporter(options);
        boolean noUnhandledImporterExceptions = true;
        try {
            logger.info("About to run importer");
            importer.run();
        } catch (Exception e) {
            // somethings gone very wrong, but we don't actually care
            // we still just send the log file to user
            // an import can fail and not throw and error here, I think, need to check with felix et al
            logger.error("The importer failed but we sent the log file to user");
            logger.error(e.getMessage().toString());
            noUnhandledImporterExceptions = false;
        }

        return noUnhandledImporterExceptions;
        // the return value is only useful for testing and is not bubbled up to user
        // they are are always sent a log file
    }
}
