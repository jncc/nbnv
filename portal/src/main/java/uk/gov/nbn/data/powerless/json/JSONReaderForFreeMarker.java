package uk.gov.nbn.data.powerless.json;

import freemarker.core.Environment;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModel;
import freemarker.template.TemplateModelException;
import java.io.IOException;
import java.io.InputStreamReader;
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
    private final CookiePassthrough passthrough;

    public JSONReaderForFreeMarker(CookiePassthrough passthrough) {
        this.passthrough = passthrough;
    }

    public TemplateModel readFile(String filename) throws TemplateException, IOException, JSONException {
        return readFile(filename, PROCESS_BY_DEFAULT);
    }
    
    public TemplateModel readFile(String filename, boolean process) throws TemplateException, IOException, JSONException {
        Reader jsonReader = FreeMarkerHelper.getReaderForTemplateFile(Environment.getCurrentEnvironment(), filename, process);
        return readAndClose(jsonReader);
    }  
    
    public TemplateModel readURL(String url) throws TemplateException, IOException, JSONException, JSONReaderStatusException {
        return readURL(url, new HashMap<String, Object>());
    }
    
    public TemplateModel readURL(String url, Map<String, Object> data) throws TemplateException, IOException, JSONException, JSONReaderStatusException {
        TraditionalHttpRequestParameterIterable wrappedData = new TraditionalHttpRequestParameterIterable(data);
        URL toCall = new URL(url + (data.isEmpty() ? "" : (((url.contains("?") ? '&' : '?') + wrappedData.getEncodedParameters() )))); //form url
        return readAndClose(passthrough.openConnection(toCall));
    }
    
    public boolean isNull(TemplateModel input) {
        return TemplateModel.NOTHING.equals(input);
    }
    
    private TemplateModel readAndClose(Reader in) throws IOException, TemplateModelException, JSONException {
        try {
            return WRAPPER.wrap(new JSONTokener(in).nextValue());
        }
        finally {
            in.close();
        }
    }
    
    private TemplateModel readAndClose(HttpURLConnection conn) throws IOException, TemplateModelException, JSONException, JSONReaderStatusException {
        if(conn.getResponseCode() == 204) { // NBNV-227 Check for 204 response and handle them as nothings
            conn.disconnect();
            return TemplateModel.NOTHING;
        }
        else {
            return readAndClose(new InputStreamReader(
                passthrough.getInputStream(conn), 
                CharsetReader.getCharsetFromContentType(conn.getContentType())
            ));
        }
    }
}
