package uk.gov.nbn.data.portal.controllers;

import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class DatasetImporterController {
    @Autowired WebResource resource;
    
    @RequestMapping(value="/Import/Existing", method = RequestMethod.POST)
    public ModelAndView replaceDataset(
            @RequestParam("file") MultipartFile file, 
            @RequestParam("key") String datasetKey,
            @RequestParam("isReplace") boolean isReplace,
            HttpServletResponse response){
        Map<String, Object> data = new HashMap<>();
        data.put("isReplace", isReplace);
        try {
            WebResource importer = resource.path(String.format("taxonDatasets/%s/import", datasetKey));
            
            ClientResponse restResponse = (isReplace) ?
                    importer.put(ClientResponse.class, file.getInputStream()) :
                    importer.post(ClientResponse.class, file.getInputStream());
            
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
}
