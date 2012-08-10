package uk.gov.nbn.data.powerless.json;

import freemarker.core.Environment;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModel;
import freemarker.template.TemplateModelException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import org.json.JSONException;
import org.json.JSONTokener;
import uk.gov.nbn.data.powerless.FreeMarkerHelper;
import uk.gov.nbn.data.powerless.request.TraditionalHttpRequestParameterIterable;

/**
 * The following bean enables reading of JSON data from either the file system
 * or from URLs.
 * 
 * In the case of URLs data can be posted as a Traditionally encoded url 
 * parameters @see TraditionalHttpRequestParameterIterable.
 * @author Christopher Johnson
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
    
    public TemplateModel readURL(String url, Map<String, Object> data) throws TemplateException, IOException, JSONException {
        return readURL(url, "GET", data);
    }
    
    public TemplateModel readURL(String url, String requestType, Map<String,Object> data) throws TemplateException, IOException, JSONException {
        TraditionalHttpRequestParameterIterable wrappedData = new TraditionalHttpRequestParameterIterable(data);
        if(requestType.equals("GET")) {
            URL toCall = new URL(url + (data.isEmpty() ? "" : (((url.contains("?") ? '&' : '?') + wrappedData.getEncodedParameters() )))); //form url
            return readAndClose(new InputStreamReader(toCall.openStream()));
        }
        else { //assuming post for now
            HttpURLConnection conn = (HttpURLConnection)new URL(url).openConnection();
            conn.setRequestMethod(requestType);
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded" );
            conn.setDoOutput(true);
            OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
            try {
                wrappedData.writeEncodedParameters(wr); //write the map in url encoded form
                wr.flush();
                return readAndClose(new InputStreamReader(conn.getInputStream()));
            }
            finally {
                wr.close();
            }
        }
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
