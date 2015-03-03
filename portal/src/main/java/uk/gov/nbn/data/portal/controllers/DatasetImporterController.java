package uk.gov.nbn.data.portal.controllers;

import com.sun.jersey.api.client.WebResource;
import java.io.IOException;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ws.rs.core.MediaType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class DatasetImporterController {
    @Autowired WebResource resource;
    
    @RequestMapping(value="/replace", method = RequestMethod.POST)
//    public String replaceDataset(@RequestParam("file") MultipartFile file, @RequestParam("key") String datasetKey){
//    public @ResponseBody String replaceDataset(@RequestParam Map<String, String> requestData){
    public @ResponseBody String replaceDataset(@ModelAttribute ReplaceForm replaceForm){
//        try {
//            resource.path(String.format("taxonDatasets/%s/import", datasetKey))
//                    .accept(MediaType.APPLICATION_JSON)
//                    .put(file.getInputStream());
//        } catch (IOException ex) {
//            Logger.getLogger(DatasetImporterController.class.getName()).log(Level.SEVERE, null, ex);
//        }
//        return "did it: " + (replaceForm.getKey()==null) + " " + (replaceForm.getFile()==null);
        return "did it: " + replaceForm.getKey();
    }
    
    public static class ReplaceForm {
//        private MultipartFile file;
        private String key;

//        public MultipartFile getFile() {
//            return file;
//        }
//
//        public void setFile(MultipartFile file) {
//            this.file = file;
//        }

        public String getKey() {
            return key;
        }

        public void setKey(String key) {
            this.key = key;
        }
    }
    
    @RequestMapping(value="/replace", method = RequestMethod.GET)
    @ResponseBody
    public String replaceDataset(){
        return "get works";
    }
    
}
