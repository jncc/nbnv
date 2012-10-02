/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.org.nbn.nbnv.importer.ui.controller;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;
import uk.org.nbn.nbnv.importer.ui.archive.ArchiveWriter;
import uk.org.nbn.nbnv.importer.ui.convert.ConverterStep;
import uk.org.nbn.nbnv.importer.ui.convert.RunConversions;
import uk.org.nbn.nbnv.importer.ui.metadata.MetadataWriter;
import uk.org.nbn.nbnv.importer.ui.model.ConvertResults;
import uk.org.nbn.nbnv.importer.ui.model.MetadataForm;
import uk.org.nbn.nbnv.importer.ui.util.DatabaseConnection;
import uk.org.nbn.nbnv.jpa.nbncore.Organisation;

/**
 *
 * @author Paul Gilbertson
 */
@Controller
@SessionAttributes({"model", "org"})
@RequestMapping("/compile.html")
public class ConvertController {   
    @RequestMapping(method= RequestMethod.POST)
    public ModelAndView compile(@RequestParam Map<String, String> args, @ModelAttribute("model") MetadataForm metadataForm, @ModelAttribute("org") Organisation organisation) {
        try {
            ConvertResults model = new ConvertResults();
            
            File in = new File(args.get("filename"));
            RunConversions rc = new RunConversions(in, organisation.getId(), metadataForm);
            
            File out = File.createTempFile("nbnimporter", "processed.tab");
            File meta = File.createTempFile("nbnimporter", "meta.xml");
            File archive = File.createTempFile("nbnimporter", "archive.zip");
            File metadata = File.createTempFile("nbnimporter", "metadata.xml");
                        
            List<String> errors = rc.run(out, meta, args);
            
            EntityManager em = DatabaseConnection.getInstance().createEntityManager();
            Query q = em.createNamedQuery("Organisation.findById");
            q.setParameter("organisationID", organisation.getId());
            Organisation org = (Organisation) q.getSingleResult();
            
            MetadataWriter mw = new MetadataWriter(metadata);
            mw.datasetToEML(metadataForm.getMetadata(), org, rc.getStartDate(), rc.getEndDate());
            
            ArchiveWriter aw = new ArchiveWriter();
            errors.addAll(aw.createArchive(out, meta, metadata, archive));

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
        } catch (Exception ex) {
            Logger.getLogger(ConvertController.class.getName()).log(Level.SEVERE, null, ex);
            return new ModelAndView("exception", "error", ex.getMessage());
        }
    }
}
