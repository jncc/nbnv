package uk.org.nbn.nbnv.service;

import java.io.File;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uk.org.nbn.nbnv.importer.Importer;
import uk.org.nbn.nbnv.importer.Options;

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
        importer.run();

        // check logs and output
        // return success/fail


    /*    Options.parse(args.toList) match {
            case OptionsSuccess(options) => {
                val importer = createImporter(options)
                importer.run()
            }
            case OptionsFailure(message) => {
                println(message)
                sys.exit(1)
            }*/

        return true;
    }
}
