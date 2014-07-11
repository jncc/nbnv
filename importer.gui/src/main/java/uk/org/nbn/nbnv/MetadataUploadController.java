package uk.org.nbn.nbnv;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.xml.ws.Response;
import java.util.ArrayList;
import java.util.List;

/**
 * @author stephen batty
 *         Date: 30/06/14
 *         Time: 11:41
 */
@RestController
@RequestMapping(value = "/rest")     // every method return value is responsebody
public class MetadataUploadController {

    @RequestMapping(value= "/upload-metadata",  method = RequestMethod.POST)
    public Boolean uploadMetadata(@RequestBody MetadataForm metadataForm){
        Logger.info("uploading metadata : {0}", metadataForm.getTitle());
        return true;
    };

    @RequestMapping("/dataset-names")
    public List<String> getDatasetNames(){
        return new ArrayList<String>(10){{
            add("GA000001");
            add("GA000002");
            add("GA000003");
            add("GA000004");
            add("GA000005");
            add("GA000006");
            add("GA000007");
            add("GA000008");
            add("GA000009");
            add("GA000010");
        }};
    };

    @RequestMapping(value = "/dataset")
    public List<Object> getDataset(){
        return null;
    };



}
