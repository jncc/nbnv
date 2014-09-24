package uk.org.nbn.nbnv.importer.daemon.builders;

import java.io.File;

public class OptionBuilder {

    public boolean build(File file) {

//        String archivePath = file.getAbsolutePath().toString();
//        Enumeration.Value target = new Target().validate();
//        String logLevel = "ERROR";
//        String rootDir = null;
//        Resource rootResource = new ClassPathResource("importer.ui.properties");
//        try {
//            rootDir = rootResource.getFile().getParent();
//        } catch (IOException e) {
//
//        }
//
//        String logDir = rootDir+"/importer-logs";
//        String tempDir = rootDir+"/importer-temp";
//        checkExistCreateIfNot(logDir, tempDir);
//
//        //Int flush = Int.unbox(25);
//        Options options =  new Options(archivePath, target, logLevel, logDir, tempDir, null);
        return false;
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
}