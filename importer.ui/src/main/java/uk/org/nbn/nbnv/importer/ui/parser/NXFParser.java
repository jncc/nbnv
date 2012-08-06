/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.org.nbn.nbnv.importer.ui.parser;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Administrator
 */
public class NXFParser {

    private BufferedReader r = null;
    private static final Map<String, DarwinCoreField> NXFtoDWCAMapping;

    static {
        NXFtoDWCAMapping = new HashMap<String, DarwinCoreField>();

        NXFtoDWCAMapping.put("RecordKey", DarwinCoreField.OCCURRENCEID);
        NXFtoDWCAMapping.put("TaxonVersionKey", DarwinCoreField.TAXONID);
        NXFtoDWCAMapping.put("SiteKey", DarwinCoreField.LOCATIONID);
        NXFtoDWCAMapping.put("SiteName", DarwinCoreField.LOCALITY);
        NXFtoDWCAMapping.put("North", DarwinCoreField.VERBATIMLATITUDE);
        NXFtoDWCAMapping.put("East", DarwinCoreField.VERBATIMLONGITUDE);
        NXFtoDWCAMapping.put("Projection", DarwinCoreField.GRIDREFERENCETYPE);
        NXFtoDWCAMapping.put("Recorder", DarwinCoreField.RECORDEDBY);
        NXFtoDWCAMapping.put("Determiner", DarwinCoreField.IDENTIFIEDBY);
        NXFtoDWCAMapping.put("ZeroAbundance", DarwinCoreField.OCCURRENCESTATUS);
        NXFtoDWCAMapping.put("Sensitive", DarwinCoreField.SENSITIVEOCCURRENCE);
        NXFtoDWCAMapping.put("GridReference", DarwinCoreField.GRIDREFERENCE);
        NXFtoDWCAMapping.put("Precision", DarwinCoreField.GRIDREFERENCEPRECISION);
        NXFtoDWCAMapping.put("SurveyKey", DarwinCoreField.COLLECTIONCODE);
        NXFtoDWCAMapping.put("SampleKey", DarwinCoreField.EVENTID);
        NXFtoDWCAMapping.put("DateType", DarwinCoreField.EVENTDATETYPECODE);
        NXFtoDWCAMapping.put("StartDate", DarwinCoreField.EVENTDATESTART);
        NXFtoDWCAMapping.put("EndDate", DarwinCoreField.EVENTDATEEND);
    }

    public NXFParser(File file) throws FileNotFoundException {
        r = new BufferedReader(new FileReader(file));
    }

    public List<ColumnMapping> parseHeaders() throws IOException {
        List<ColumnMapping> headers = new ArrayList<ColumnMapping>();

        String[] origHeaders = r.readLine().split("\t", -1);

        for (int i = 0; i < origHeaders.length; i++) {
            DarwinCoreField dcf = NXFtoDWCAMapping.get(origHeaders[i]);

            if (dcf == null) {
                dcf = DarwinCoreField.ATTRIBUTE;
            }

            ColumnMapping cm = new ColumnMapping(i, origHeaders[i], dcf);
            headers.add(cm);
        }

        return headers;
    }

    public List<String> readDataLine() throws IOException {
        if (!r.ready()) {
            return null;
        }
        
        return new ArrayList<String>(Arrays.asList(r.readLine().split("\t", -1)));
    }
}
