/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.org.nbn.nbnv.importer.ui;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

/**
 *
 * @author Paul Gilbertson
 */
@Controller
@RequestMapping("/compile.html")
public class ConvertController {
    @RequestMapping(method= RequestMethod.POST)
    public ModelAndView compile(@RequestParam Map<String, String> args) {
        List<String> messages = new ArrayList<String>();
        
        messages.add("Filename: " + args.get("filename"));
        
        for (String key : args.keySet()) {
            messages.add("Key: " + key + " - Value: " + args.get(key));
        }
        
        return new ModelAndView("compile", "messages", messages);
    }
}
