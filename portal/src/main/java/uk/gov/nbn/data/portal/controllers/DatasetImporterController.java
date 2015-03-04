package uk.gov.nbn.data.portal.controllers;

import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ws.rs.core.MediaType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Controller
public class DatasetImporterController {
    @Autowired WebResource resource;
    
    @RequestMapping(value="/replace", method = RequestMethod.POST)
    public Object replaceDataset(@RequestParam("file") MultipartFile file, @RequestParam("key") String datasetKey){
        try {
            ClientResponse response = resource.path(String.format("taxonDatasets/%s/import", datasetKey))
                    .accept(MediaType.APPLICATION_JSON)
                    .put(ClientResponse.class, file.getInputStream());
            
//            switch(response.getStatus()) {
//                case 200: return RedirectView("/Import");
//            }
        } catch (IOException ex) {
            Logger.getLogger(DatasetImporterController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return "did it: " + datasetKey;
    }
}
