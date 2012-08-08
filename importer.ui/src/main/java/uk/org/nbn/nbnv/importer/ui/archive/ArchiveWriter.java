/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.org.nbn.nbnv.importer.ui.archive;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.Deflater;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 *
 * @author Paul Gilbertson
 */
public class ArchiveWriter {
    public List<String> createArchive(File data, File meta, File archive) throws IOException {
        List<String> errors = new ArrayList<String>();
        ZipOutputStream zout = null;
        try {
            zout = new ZipOutputStream(new FileOutputStream(archive));
            zout.setLevel(Deflater.DEFAULT_COMPRESSION);
            addFile(zout, "meta.xml", meta);
            addFile(zout, "data.tab", data);
        } catch (FileNotFoundException ex) {
            errors.add(ex.getMessage());
        } catch (IOException ex) {
            errors.add(ex.getMessage());
        } finally {
            if (zout != null) {
                zout.close();
            }
        }
        
        return errors;
    }
    
    private void addFile(ZipOutputStream zout, String filename, File file) throws IOException {
        byte[] buf = new byte[1024];
        
        zout.putNextEntry(new ZipEntry(filename));
        
        FileInputStream in = new FileInputStream(file);
        int len;
        while ((len = in.read(buf)) > 0) {
            zout.write(buf, 0, len);
        }
        
        zout.closeEntry();

    }
}
