package uk.org.nbn.nbnv.api.services;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * The following service manages an uploaded dataset file such that an Importer 
 * Daemon can process the file.
 * 
 * The service can also report back on errors which occurred during the importing
 * process
 * 
 * @author cjohn
 * @author jcoop
 */
@Service
public class DatasetImporterService {
   @Autowired Properties properties;
   
//   public ImportStatus getImportStatusOfDataset(String id) {
//       
//   }
   
   public void submitImport(String datasetKey, InputStream nxf) throws IOException {
       File startingFile = getStartingFile(datasetKey);
       
       FileOutputStream out = new FileOutputStream(startingFile);
       try {
           IOUtils.copyLarge(nxf, out);
           //File is now on disk, check if it is nbn exchange valid. If it is 
           //valid, move to the queue, else delete
       }
       finally {
           out.close();
       }
   }
   
   private File getStartingFile(String datasetKey) {
       File importer = new File(properties.getProperty("importer_location"));
       
       return new File(importer, "start" + File.pathSeparator + datasetKey);
   }
}
