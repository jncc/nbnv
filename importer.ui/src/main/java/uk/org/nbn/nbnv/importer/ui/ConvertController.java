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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import uk.org.nbn.nbnv.importer.ui.archive.ArchiveWriter;
import uk.org.nbn.nbnv.importer.ui.convert.ConverterStep;
import uk.org.nbn.nbnv.importer.ui.convert.RunConversions;
import uk.org.nbn.nbnv.importer.ui.meta.MetaWriter;
import uk.org.nbn.nbnv.importer.ui.model.ConvertResults;

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
            
            File in = new File(args.get("filename"));
            RunConversions rc = new RunConversions(in);
            
            File out = File.createTempFile("nbnimporter", "processed.tab");
            File meta = File.createTempFile("nbnimporter", "meta.xml");
            File archive = File.createTempFile("nbnimporter", "archive.zip");
            
            List<String> errors = rc.run(out, meta, args);
            
            ArchiveWriter aw = new ArchiveWriter();
            aw.createArchive(out, meta, archive);

            model.setArchive(archive.getAbsolutePath());
            
            if (errors.isEmpty()) {
                errors.add("None");
            }
            
            List<String> steps = new ArrayList<String>();
            for (ConverterStep cs : rc.getSteps()) {
                steps.add(cs.getName());
            }
            
            model.setErrors(errors);
            model.setSteps(steps);
            return new ModelAndView("compile", "model", model);
        } catch (IOException ex) {
            Logger.getLogger(ConvertController.class.getName()).log(Level.SEVERE, null, ex);
            return new ModelAndView("exception", "error", ex.getMessage());
        }
    }
}
