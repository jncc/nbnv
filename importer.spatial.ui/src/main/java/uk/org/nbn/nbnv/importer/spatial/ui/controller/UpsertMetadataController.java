/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.org.nbn.nbnv.importer.spatial.ui.controller;

import javax.persistence.EntityManager;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;
import uk.org.nbn.nbnv.importer.spatial.ui.model.MetadataForm;
import uk.org.nbn.nbnv.importer.spatial.ui.model.SiteBoundaryForm;
import uk.org.nbn.nbnv.importer.spatial.ui.model.SiteBoundaryMetadata;
import uk.org.nbn.nbnv.importer.spatial.ui.util.DatabaseConnection;
import uk.org.nbn.nbnv.jpa.nbncore.Organisation;

/**
 *
 * @author Paul Gilbertson
 */
@Controller
@SessionAttributes({"metadataForm", "org"})
public class UpsertMetadataController {

    @RequestMapping(value="/upsertSiteBoundary.html", method = RequestMethod.GET)
    public ModelAndView upsertSB(@ModelAttribute("org") Organisation organisation, @ModelAttribute("metadataForm") MetadataForm metadataForm, BindingResult result) {
        EntityManager em = DatabaseConnection.getInstance().createEntityManager();
        SiteBoundaryForm form = new SiteBoundaryForm();
        form.setMetadata(new SiteBoundaryMetadata());
        form.fill();
        
        ModelAndView mv = new ModelAndView("siteBoundary", "siteBoundaryForm", form);
        
        if (result.hasErrors()) {
            mv = new ModelAndView("debug");
        }
        
        
        return mv;
    }

    @RequestMapping(value="/upsertHabitat.html", method = RequestMethod.GET)
    public ModelAndView upsertHL(@ModelAttribute("org") Organisation organisation, @ModelAttribute("metadataForm") MetadataForm metadataForm, BindingResult result) {
        EntityManager em = DatabaseConnection.getInstance().createEntityManager();
        ModelAndView mv = new ModelAndView("dump", "data", metadataForm.getMetadata());
        
        if (result.hasErrors()) {
            mv = new ModelAndView("debug");
        }
        
        
        return mv;
    }
}
