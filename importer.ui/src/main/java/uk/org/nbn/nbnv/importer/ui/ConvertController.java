/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.org.nbn.nbnv.importer.ui;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import uk.org.nbn.nbnv.importer.ui.convert.BadDataException;
import uk.org.nbn.nbnv.importer.ui.convert.ConverterStep;
import uk.org.nbn.nbnv.importer.ui.convert.RunConversions;
import uk.org.nbn.nbnv.importer.ui.model.ConvertResults;
import uk.org.nbn.nbnv.importer.ui.parser.ColumnMapping;
import uk.org.nbn.nbnv.importer.ui.parser.DarwinCoreField;
import uk.org.nbn.nbnv.importer.ui.parser.NXFParser;

/**
 *
 * @author Paul Gilbertson
 */
@Controller
@RequestMapping("/compile.html")
public class ConvertController {
    @RequestMapping(method= RequestMethod.POST)
    public ModelAndView compile(@RequestParam Map<String, String> args) {
        try {
            ConvertResults model = new ConvertResults();
            RunConversions rc = new RunConversions();
            
            List<String> messages = new ArrayList<String>();
            messages.add("Filename: " + args.get("filename"));

            File in = new File(args.get("filename"));
            File out = File.createTempFile("nbnimporter", "processed.tab");
            
            messages.add("Outfile: " + out.getAbsolutePath());
            
            List<String> errors = rc.run(in, out, args);
            
            model.setMessages(messages);
            model.setErrors(errors);
            model.setSteps(new ArrayList<String>());
            return new ModelAndView("compile", "model", model);
        } catch (IOException ex) {
            Logger.getLogger(ConvertController.class.getName()).log(Level.SEVERE, null, ex);
            return new ModelAndView("exception", "error", ex.getMessage());
        }
    }
}
