/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.org.nbn.nbnv.importer.ui.controller;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;
import uk.org.nbn.nbnv.importer.s1.utils.convert.ConverterStep;
import uk.org.nbn.nbnv.importer.s1.utils.database.DatabaseConnection;
import uk.org.nbn.nbnv.importer.s1.utils.model.MetadataForm;
import uk.org.nbn.nbnv.importer.s1.utils.archive.ArchiveWriter;
import uk.org.nbn.nbnv.importer.ui.convert.ImporterService;
import uk.org.nbn.nbnv.importer.ui.convert.RunConversions;
import uk.org.nbn.nbnv.importer.s1.utils.xmlWriters.MetadataWriter;
import uk.org.nbn.nbnv.importer.ui.model.ConvertResults;
import uk.org.nbn.nbnv.jpa.nbncore.Organisation;

/**
 *
 * @author Paul Gilbertson
 */
@Controller
@SessionAttributes({"metadataForm", "org"})
@RequestMapping("/compile.html")
public class ConvertController {

    @Autowired
    ImporterService importerService;

    @RequestMapping(method= RequestMethod.POST)
    public ModelAndView compile(@RequestParam Map<String, String> args, HttpServletRequest request,  
                                @ModelAttribute("metadataForm") MetadataForm metadataForm, 
                                @ModelAttribute("org") Organisation organisation) {
        try {
            ConvertResults model = new ConvertResults();
            
            // Check if we are an update or not
            if (!metadataForm.getDatasetUpdate()) {
                metadataForm.getMetadata().setDatasetID("");
            }
            
            File in = new File(args.get("filename"));
            RunConversions rc = new RunConversions(in, organisation.getId());
            
            File out = File.createTempFile("nbnimporter", "processed.tab");
            File meta = File.createTempFile("nbnimporter", "meta.xml");
            File metadata = File.createTempFile("nbnimporter", "metadata.xml");
            
            String archiveName;
            
            if (metadataForm.getDatasetUpdate()) {
                                archiveName = String.format("archive_%s_%s_%s.zip", 
                        ((organisation.getAbbreviation() == null || 
                         organisation.getAbbreviation().trim().isEmpty()) ? 
                         organisation.getName().substring(0, Math.min(organisation.getName().length(), 10)) : 
                         organisation.getAbbreviation()),
                        metadataForm.getMetadata().getDatasetID(),
                        new SimpleDateFormat("ddMMyyyy_hhmmss").format(new Date())).replaceAll(" ", "_");
            } else {
                archiveName = String.format("archive_%s_%s.zip", 
                        ((organisation.getAbbreviation() == null || 
                         organisation.getAbbreviation().trim().isEmpty()) ? 
                         organisation.getName().substring(0, Math.min(organisation.getName().length(), 10)).replace(" ", "") : 
                         organisation.getAbbreviation()),
                        new SimpleDateFormat("ddMMyyyy_hhmmss").format(new Date()));
            }
            File archive = File.createTempFile("nbnimporter", archiveName);

            Map<String, List<String>> results = rc.run(out, meta, args);
            
            EntityManager em = DatabaseConnection.getInstance().createEntityManager();
            Query q = em.createNamedQuery("Organisation.findById");
            q.setParameter("id", organisation.getId());
            Organisation org = (Organisation) q.getSingleResult();
            em.close();
            
            MetadataWriter mw = new MetadataWriter(metadata);
            mw.datasetToEML(metadataForm.getMetadata(), org, rc.getStartDate(), rc.getEndDate(), 
                    !metadataForm.getInsertType().equals("append"));
            
            ArchiveWriter aw = new ArchiveWriter();
            results.get("errors").addAll(aw.createArchive(out, meta, metadata, archive));

            model.setArchive(archive.getAbsolutePath());
            
            if (results.get("errors").isEmpty()) {
                results.get("errors").add("None");
                importerService.doImport(archive);
            }
            
            List<String> steps = new ArrayList<String>();
            for (ConverterStep cs : rc.getSteps()) {
                steps.add(cs.getName());
            }
            
            if (results.get("warnings").isEmpty()) {
                results.get("warnings").add("None");
            }
            
            model.setErrors(results.get("errors"));
            model.setWarnings(results.get("warnings"));
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
