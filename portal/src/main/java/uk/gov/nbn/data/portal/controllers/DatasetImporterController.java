package uk.gov.nbn.data.portal.controllers;

import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.GenericType;
import com.sun.jersey.api.client.WebResource;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.fileupload.FileItemIterator;
import org.apache.commons.fileupload.FileItemStream;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.fileupload.util.Streams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import uk.org.nbn.nbnv.api.model.ImportCleanup;
import static uk.org.nbn.nbnv.api.model.ImportCleanup.Operation.SET_SENSITIVE_FALSE;
import static uk.org.nbn.nbnv.api.model.ImportCleanup.Operation.SET_SENSITIVE_TRUE;
import static uk.org.nbn.nbnv.api.model.ImportCleanup.Operation.STRIP_INVALID_RECORDS;
import uk.org.nbn.nbnv.api.model.TaxonDatasetWithImportStatus;

@Controller
public class DatasetImporterController {
    @Autowired WebResource resource;
    
    @RequestMapping(value    = "/Import/Existing", 
                    consumes = "multipart/form-data",
                    method   = RequestMethod.POST)
    public ModelAndView replaceDataset(
            HttpServletRequest request,
            HttpServletResponse response) throws IOException, FileUploadException{
        
        ServletFileUpload upload = new ServletFileUpload();
        FileItemIterator iter = upload.getItemIterator(request);
        
        boolean isReplace = Boolean.parseBoolean(getFormField("isReplace", iter));
        String datasetKey = getFormField("key", iter);
        Map<String, Object> data = new HashMap<>();
        data.put("isReplace", isReplace);
        try {
            WebResource importer = resource.path(String.format("taxonDatasets/%s/import", datasetKey));
            
            InputStream nbnExchangeStream = iter.next().openStream();
            ClientResponse restResponse = (isReplace) ?
                    importer.put(ClientResponse.class, nbnExchangeStream) :
                    importer.post(ClientResponse.class, nbnExchangeStream);
            
            switch(restResponse.getStatus()) {
                case 200: 
                    response.sendRedirect("/Import"); //Great stuff!! Jump back to the dashboard
                    return null;
                default:
                    //Get the error message from the response
                    data.put("status", restResponse.getEntity(Map.class).get("status"));
            }
        } catch (IOException ex) {
            data.put("status", "Failed to communicate with the NBN Rest API");
        }
        return new ModelAndView("importExistingDataset", data);
    }
    
    @RequestMapping(value="/Import/Existing", method = RequestMethod.GET)
    public ModelAndView getImportExistingPage(@RequestParam("isReplace") boolean isReplace) {
        Map<String,Object> data = new HashMap<>();
        data.put("isReplace", isReplace);
        return new ModelAndView("importExistingDataset", data);
    }
    
    @RequestMapping(value="/Import", method = RequestMethod.GET)
    public ModelAndView getImporterDashboard() {
        GenericType<List<TaxonDatasetWithImportStatus>> type = new GenericType<List<TaxonDatasetWithImportStatus>>() {};
        Map<String,Object> data = new HashMap<>();
        data.put("statuses", resource.path("/taxonDatasets/adminable/import").get(type));
        return new ModelAndView("importDashboard", data);
    }
    
    @RequestMapping(value="/Import", method = RequestMethod.POST, params="op=delete")
    public ModelAndView deleteQueuedDataset(@RequestParam("datasetKey") String datasetKey) {
        ClientResponse response = resource
                .path(String.format("taxonDatasets/%s/import", datasetKey))
                .delete(ClientResponse.class);
        
        ModelAndView model = getImporterDashboard();
        switch(response.getStatus()) {
            case 204: return model.addObject("message", "Dataset: " + datasetKey + " has been removed from the queue");
            case 404: return model.addObject("error", "Could not find " + datasetKey);
            default:  return model.addObject("error", "The NBN Gateway API failed. If this continues please contact us using the forums");
        }
    }
    
    @RequestMapping(value="/Import", method = RequestMethod.POST, params="op=importValid")
    public ModelAndView continueWithValidRecords(@RequestParam("datasetKey") String datasetKey, @RequestParam("timestamp") String timestamp) {
        return performImportCleanup(datasetKey, timestamp, STRIP_INVALID_RECORDS);
    }
    
    @RequestMapping(value="/Import", method = RequestMethod.POST, params="op=sensitiveTrue")
    public ModelAndView continueWithSensitiveSetToTrue(@RequestParam("datasetKey") String datasetKey, @RequestParam("timestamp") String timestamp) {
        return performImportCleanup(datasetKey, timestamp, SET_SENSITIVE_TRUE);
    }
    
    @RequestMapping(value="/Import", method = RequestMethod.POST, params="op=sensitiveFalse")
    public ModelAndView continueWithSensitiveSetToFalse(@RequestParam("datasetKey") String datasetKey, @RequestParam("timestamp") String timestamp) {
        return performImportCleanup(datasetKey, timestamp, SET_SENSITIVE_FALSE);
    }
    
    private ModelAndView performImportCleanup(String datasetKey, String timestamp, ImportCleanup.Operation operation) {
        ClientResponse response = resource
                .path(String.format("taxonDatasets/%s/import/%s", datasetKey, timestamp))
                .post(ClientResponse.class, new ImportCleanup(operation));
        
        ModelAndView model = getImporterDashboard();
        switch(response.getStatus()) {
            case 200: return model.addObject("message", "Dataset: " + datasetKey + " has been queued for processing");
            case 404: return model.addObject("error", "Could not find an existing import for " + datasetKey + " with the timestamp " + timestamp);
            case 409: return model.addObject("error", "The dataset " + datasetKey + " is already queued for import. Please delete and try again.");
            default:  
                String status = (String)response.getEntity(Map.class).get("status");
                return model.addObject("error", "The NBN Gateway API failed. If this continues please contact us using the forums. " + status);
        }
    }
    
    private String getFormField(String name, FileItemIterator iterator) throws FileUploadException, IOException {
        FileItemStream field = iterator.next();
        if(field.isFormField() && field.getFieldName().equals(name)) {
            return Streams.asString(field.openStream());
        }
        else {
            throw new IllegalArgumentException("Expected to find the form field: " + name);
        }
    }
}
