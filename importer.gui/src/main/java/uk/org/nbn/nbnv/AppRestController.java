package uk.org.nbn.nbnv;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.xml.ws.Response;
import java.util.List;

/**
 * @author stephen batty
 *         Date: 30/06/14
 *         Time: 11:41
 */
@RestController("/rest")
public class AppRestController {


    @RequestMapping("/dataset-names")
    public @ResponseBody List<String> getDatasetNames(){
        return null;
    };

    @RequestMapping("/dataset")
    public @ResponseBody List<Object> getDataset(){
        return null;
    };

}
