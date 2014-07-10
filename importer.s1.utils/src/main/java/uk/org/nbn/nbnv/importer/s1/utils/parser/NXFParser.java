/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.org.nbn.nbnv.importer.s1.utils.parser;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
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

        NXFtoDWCAMapping.put("RECORDKEY", DarwinCoreField.OCCURRENCEID);
        NXFtoDWCAMapping.put("TAXONVERSIONKEY", DarwinCoreField.TAXONID);
        NXFtoDWCAMapping.put("SITEKEY", DarwinCoreField.LOCATIONID);
        NXFtoDWCAMapping.put("SITENAME", DarwinCoreField.LOCALITY);
        NXFtoDWCAMapping.put("NORTH", DarwinCoreField.VERBATIMLATITUDE);
        NXFtoDWCAMapping.put("EAST", DarwinCoreField.VERBATIMLONGITUDE);
        NXFtoDWCAMapping.put("NORTHINGS", DarwinCoreField.VERBATIMLATITUDE);
        NXFtoDWCAMapping.put("EASTINGS", DarwinCoreField.VERBATIMLONGITUDE);
        NXFtoDWCAMapping.put("LAT", DarwinCoreField.VERBATIMLATITUDE);
        NXFtoDWCAMapping.put("LONG", DarwinCoreField.VERBATIMLONGITUDE);
        NXFtoDWCAMapping.put("LATITUDE", DarwinCoreField.VERBATIMLATITUDE);
        NXFtoDWCAMapping.put("LONGITUDE", DarwinCoreField.VERBATIMLONGITUDE);
        NXFtoDWCAMapping.put("SRS", DarwinCoreField.VERBATIMSRS);
        NXFtoDWCAMapping.put("PROJECTION", DarwinCoreField.GRIDREFERENCETYPE);
        NXFtoDWCAMapping.put("RECORDER", DarwinCoreField.RECORDEDBY);
        NXFtoDWCAMapping.put("DETERMINER", DarwinCoreField.IDENTIFIEDBY);
        NXFtoDWCAMapping.put("ZEROABUNDANCE", DarwinCoreField.OCCURRENCESTATUS);
        NXFtoDWCAMapping.put("SENSITIVE", DarwinCoreField.SENSITIVEOCCURRENCE);
        NXFtoDWCAMapping.put("GRIDREFERENCE", DarwinCoreField.GRIDREFERENCE);
        NXFtoDWCAMapping.put("PRECISION", DarwinCoreField.GRIDREFERENCEPRECISION);
        NXFtoDWCAMapping.put("SURVEYKEY", DarwinCoreField.COLLECTIONCODE);
        NXFtoDWCAMapping.put("SAMPLEKEY", DarwinCoreField.EVENTID);
        NXFtoDWCAMapping.put("DATETYPE", DarwinCoreField.EVENTDATETYPECODE);
        NXFtoDWCAMapping.put("STARTDATE", DarwinCoreField.EVENTDATESTART);
        NXFtoDWCAMapping.put("ENDDATE", DarwinCoreField.EVENTDATEEND);
        NXFtoDWCAMapping.put("NAME", DarwinCoreField.TAXONNAME);
        NXFtoDWCAMapping.put("SCIENTIFICNAME", DarwinCoreField.TAXONNAME);
        NXFtoDWCAMapping.put("COMMONNAME", DarwinCoreField.TAXONNAME);
        NXFtoDWCAMapping.put("TAXONNAME", DarwinCoreField.TAXONNAME);
        NXFtoDWCAMapping.put("DATE", DarwinCoreField.EVENTDATE);
        NXFtoDWCAMapping.put("FEATUREKEY", DarwinCoreField.SITEFEATUREKEY);
    }

    public NXFParser(File file) throws FileNotFoundException {
        r = new BufferedReader(new FileReader(file));
    }

    public List<ColumnMapping> parseHeaders() throws IOException {
        List<ColumnMapping> headers = new ArrayList<ColumnMapping>();

        String[] origHeaders = r.readLine().split("\t", -1);

        for (int i = 0; i < origHeaders.length; i++) {
            // Uppercase to put an end to anyone hand-crafting the data inputs getting it wrong, removing any non word characters
            DarwinCoreField dcf = NXFtoDWCAMapping.get(origHeaders[i].toUpperCase().replaceAll("\\W", ""));

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
