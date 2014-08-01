package uk.org.nbn.nbnv.importer.ui.convert;

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

    @Autowired
    OptionBuilder optionBuilder;

    public boolean doImport(File file) {
        Options options = optionBuilder.build(file);
        Importer importer = Importer.createImporter(options);
        try {
            importer.run();
        } catch (Exception e) {
            // somethings gone very wrong, but we don't actually care
            // just send the log file to user, as per success
            return false;
        }
        return true;
    }
}
