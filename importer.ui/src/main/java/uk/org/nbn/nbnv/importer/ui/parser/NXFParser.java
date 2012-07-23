/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.org.nbn.nbnv.importer.ui.parser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.apache.commons.fileupload.FileItem;

/**
 *
 * @author Administrator
 */
public class NXFParser {

    public List<String> parseHeaders(FileItem file) throws IOException {
        BufferedReader r = null;

        try {
            List<String> headers = new ArrayList<String>();

            r = new BufferedReader(new InputStreamReader(file.getInputStream()));
            String[] origHeaders = r.readLine().split("\t");
            headers.addAll(Arrays.asList(origHeaders));

            return headers;
        } finally {
            if (r != null)
                r.close();
        }
    }
}
