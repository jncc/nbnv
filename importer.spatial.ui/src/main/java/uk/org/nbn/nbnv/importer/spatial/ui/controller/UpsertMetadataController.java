/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.org.nbn.nbnv.importer.spatial.ui.controller;

import java.util.Date;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Query;
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
import uk.org.nbn.nbnv.jpa.nbncore.Dataset;
import uk.org.nbn.nbnv.jpa.nbncore.DatasetType;
import uk.org.nbn.nbnv.jpa.nbncore.DatasetUpdateFrequency;
import uk.org.nbn.nbnv.jpa.nbncore.Organisation;
import uk.org.nbn.nbnv.jpa.nbncore.SiteBoundaryCategory;
import uk.org.nbn.nbnv.jpa.nbncore.SiteBoundaryDataset;
import uk.org.nbn.nbnv.jpa.nbncore.SiteBoundaryType;

/**
 *
 * @author Paul Gilbertson
 */
@Controller
@SessionAttributes({"metadataForm", "org", "siteBoundaryForm"})
public class UpsertMetadataController {

    @RequestMapping(value="/upsertSiteBoundary.html", method = RequestMethod.GET)
    public ModelAndView upsertSB(@ModelAttribute("org") Organisation organisation, @ModelAttribute("metadataForm") MetadataForm metadataForm, BindingResult result) {
        SiteBoundaryForm form = new SiteBoundaryForm();
        form.setMetadata(new SiteBoundaryMetadata());
        form.fill();
        
        ModelAndView mv = new ModelAndView("siteBoundary", "siteBoundaryForm", form);
        
        if (result.hasErrors()) {
            mv = new ModelAndView("debug");
        }
        
        return mv;
    }

    @RequestMapping(value="/siteBoundaryProcess.html", method = RequestMethod.POST)
    public ModelAndView processSB(@ModelAttribute("org") Organisation organisation, @ModelAttribute("metadataForm") MetadataForm metadataForm, @ModelAttribute("siteBoundaryForm") SiteBoundaryForm sbForm, BindingResult result) {
        EntityManager em = DatabaseConnection.getInstance().createEntityManager();
        em.getTransaction().begin();
        
        Dataset d = new Dataset();
        if (metadataForm.getMetadata().getDatasetID().isEmpty()) {
            d.setKey(generateSBKey(em));
        } else {
            d.setKey(metadataForm.getMetadata().getDatasetID());
        }
        
        d.setTitle(metadataForm.getMetadata().getTitle());
        d.setDescription(metadataForm.getMetadata().getDescription());
        d.setPurpose(metadataForm.getMetadata().getPurpose());
        d.setDataCaptureMethod(metadataForm.getMetadata().getMethods());
        d.setDataQuality(metadataForm.getMetadata().getQuality());
        d.setGeographicalCoverage(metadataForm.getMetadata().getGeographic());
        d.setTemporalCoverage(metadataForm.getMetadata().getTemporal());
        d.setAdditionalInformation(metadataForm.getMetadata().getInfo());
        d.setOrganisation(em.find(Organisation.class, metadataForm.getMetadata().getOrganisationID()));
        d.setDatasetType(em.find(DatasetType.class, 'A'));
        d.setDatasetUpdateFrequency(em.find(DatasetUpdateFrequency.class, "012"));
        d.setDateUploaded(new Date());
        d.setMetadataLastEdited(new Date());
        
        SiteBoundaryDataset sd = new SiteBoundaryDataset();
        sd.setNameField(sbForm.getMetadata().getName());
        sd.setSiteBoundaryCategory(em.find(SiteBoundaryCategory.class, sbForm.getMetadata().getCategory()));
        sd.setSiteBoundaryType(em.find(SiteBoundaryType.class, sbForm.getMetadata().getType()));
        sd.setDataset(d);
        
        em.persist(d);
        em.persist(sd);
        
        em.getTransaction().commit();
        
        return new ModelAndView("dump", "data", "Dataset : " + d.getKey() + " metadata uploaded");
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
    
    private String generateSBKey(EntityManager em) {
        int id = 1;
        
        Query q = em.createNamedQuery("SiteBoundaryDataset.findAll");
        List<SiteBoundaryDataset> sbds = q.getResultList();
        
        for (SiteBoundaryDataset sbd : sbds) {
            if (sbd.getDatasetKey().startsWith("SB")) {
                if (Integer.parseInt(sbd.getDatasetKey().substring(3, 8)) >= id) {
                    id = Integer.parseInt(sbd.getDatasetKey().substring(3, 8)) + 1;
                }
            }
        }
        
        return "SB" + String.format("%06d", id);
    }
}
