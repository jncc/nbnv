package uk.org.nbn.nbnv;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * @author stephen batty
 *         Date: 30/06/14
 *         Time: 11:41
 */
@RestController
public class MetadataUploadController {

    @RequestMapping(value= Url.uploadMetadata,  method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE,  produces = MediaType.APPLICATION_JSON_VALUE)
    public Boolean uploadMetadata(@RequestBody MetadataForm metadataForm){
        Logger.info("uploading metadata : {0}", metadataForm.getTitle());
        return true;
    };
    @RequestMapping(value= Url.downloadMetadataSample,  method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public MetadataForm downloadSample(){
        Logger.info("downloading test sample data");
        return new MetadataForm().SampleTestData();
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
