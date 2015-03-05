package uk.gov.nbn.data.portal.controllers;

import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
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
