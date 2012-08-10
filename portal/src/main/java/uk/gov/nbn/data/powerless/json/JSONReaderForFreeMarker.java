package uk.gov.nbn.data.powerless.json;

import freemarker.core.Environment;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModel;
import freemarker.template.TemplateModelException;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;
import uk.gov.nbn.data.powerless.FreeMarkerHelper;

/**
 *
 * @author Chris Johnson
 */
public class JSONReaderForFreeMarker {
    private static final boolean PROCESS_BY_DEFAULT = false;
    private static final JSONObjectWrapper WRAPPER = new JSONObjectWrapper();

    public TemplateModel readFile(String filename) throws TemplateException, IOException, JSONException {
        return readFile(filename, PROCESS_BY_DEFAULT);
    }
    
    public TemplateModel readFile(String filename, boolean process) throws TemplateException, IOException, JSONException {
        Reader jsonReader = FreeMarkerHelper.getReaderForTemplateFile(Environment.getCurrentEnvironment(), filename, process);
        return readAndClose(jsonReader);
    }  
    
    public TemplateModel readURL(String url) throws TemplateException, IOException, JSONException {
        return readURL(url, new HashMap<String, Object>());
    }
    
    public TemplateModel readURL(String url, Map data) throws TemplateException, IOException, JSONException {
        return readURL(url, "GET", data);
    }
    
    public TemplateModel readURL(String url, String requestType, Map<String,Object> data) throws TemplateException, IOException, JSONException {
        if(requestType.equals("GET")) {
            return readAndClose(new InputStreamReader(new URL(url + "?" + encodeMap(data)).openStream()));
        }
        else { //assuming post for now
            HttpURLConnection conn = (HttpURLConnection)new URL(url).openConnection();
            conn.setRequestMethod(requestType);
            conn.setRequestProperty("Content-Type", "application/json" );
            conn.setDoOutput(true);
            OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
            try {
                new JSONObject(data).write(wr); //write the map as a json object
                wr.flush();
                return readAndClose(new InputStreamReader(conn.getInputStream()));
            }
            finally {
                wr.close();
            }
        }
    }
    
    private static String encodeMap(Map<String, Object> map) throws IOException {
        StringWriter toReturn = new StringWriter();
        writeMap(map, toReturn);
        return toReturn.toString();
    }
    
    private static void writeMap(Map<String, Object> map, Writer toWriteTo) throws IOException {
        Iterator<Entry<String, Object>> iterator = map.entrySet().iterator();
        if(iterator.hasNext()) { //is there any entries in map?
            writeEntry(iterator.next(), toWriteTo); //write first
            while(iterator.hasNext()) { 
                toWriteTo.write("&"); //seperator others
                writeEntry(iterator.next(),toWriteTo); //write others
            }
        }
    }
    
    private static void writeEntry(Entry<String, Object> entry, Writer toWriteTo) throws IOException {
        Object value = entry.getValue();
        if(value instanceof List) {
            for(String currValue : (List<String>)value) {
                writeParameter(entry.getKey(), currValue, toWriteTo);
            }
        }
        else {
            writeParameter(entry.getKey(), value.toString(), toWriteTo);
        }
    }
    
    private static void writeParameter(String key, String value, Writer toWriteTo) throws IOException {
        toWriteTo.write(URLEncoder.encode(key, "UTF-8"));
        toWriteTo.write("=");
        toWriteTo.write(URLEncoder.encode(value, "UTF-8"));
    }
    
    private TemplateModel readAndClose(Reader in) throws IOException, TemplateModelException, JSONException {
        try {
            return WRAPPER.wrap(new JSONTokener(in).nextValue());
        }
        finally {
            in.close();
        }
    }
}
