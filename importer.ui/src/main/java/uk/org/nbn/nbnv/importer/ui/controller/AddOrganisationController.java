/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.org.nbn.nbnv.importer.ui.controller;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import org.apache.commons.codec.binary.Base64;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import uk.org.nbn.nbnv.importer.ui.model.AddOrganisationForm;
import uk.org.nbn.nbnv.importer.ui.model.MetadataForm;
import uk.org.nbn.nbnv.importer.ui.util.DatabaseConnection;
import uk.org.nbn.nbnv.importer.ui.validators.AddOrganisationFormValidator;
import uk.org.nbn.nbnv.importer.ui.validators.OrganisationValidator;
import org.imgscalr.Scalr;
import org.springframework.web.bind.annotation.SessionAttributes;
import uk.org.nbn.nbnv.jpa.nbncore.Organisation;

/**
 *
 * @author Matt Debont
 */
@Controller
@SessionAttributes({"model", "org", "logo", "logoSmall"})
public class AddOrganisationController {
    
    private static final int maxLogoWidth = 150;
    private static final int maxLogoSmallWidth = 32;
    private static final String outputType = "png";
    
    @RequestMapping(value = "/organisation.html", method = RequestMethod.GET)
    public ModelAndView addOrganisation(@ModelAttribute("model") MetadataForm metadataForm, @ModelAttribute("org") Organisation org) {       
        AddOrganisationForm orgForm = new AddOrganisationForm();
        if (metadataForm.hasStoredOrg()) {
            orgForm.setOrgagnisation(org);
        }
        return new ModelAndView("addOrganisation", "orgForm", orgForm);
    }
    
    @RequestMapping(value="/organisationProcess.html", method=RequestMethod.POST, params="addImage") 
    public ModelAndView addImage(HttpServletRequest request, AddOrganisationForm orgForm, BindingResult result, @RequestParam("imageData") CommonsMultipartFile imageData, @ModelAttribute("logo") String logo, @ModelAttribute("logoSmall") String logoSmall) {
        try {
            if (imageData != null && !imageData.isEmpty()) {
                
                Base64 b64 = new Base64(true);
                
                ByteArrayInputStream is = new ByteArrayInputStream(imageData.getBytes());
                ByteArrayOutputStream out = new ByteArrayOutputStream();
                
                BufferedImage bi = ImageIO.read(is);
                Scalr.resize(bi, maxLogoWidth, Scalr.OP_ANTIALIAS);
                ImageIO.write(bi, outputType, out);
                
                orgForm.setLogoBase64(b64.encodeToString(out.toByteArray()));
                orgForm.setLogo("/imageBase/" + orgForm.getLogoBase64() + ".html");
                orgForm.getOrganisation().setLogo(out.toByteArray());
                
                out.flush();
                
                Scalr.resize(bi, maxLogoSmallWidth, Scalr.OP_ANTIALIAS);
                ImageIO.write(bi, outputType, out);
                
                orgForm.setLogoSmallBase64(b64.encodeToString(out.toByteArray()));
                orgForm.setLogoSmall("/imageBase/" + orgForm.getLogoSmallBase64() + ".html");
                orgForm.getOrganisation().setLogoSmall(out.toByteArray());
                
                out.flush();
                out.close();
            } else {
                orgForm.setImageError("No Valid Image Selected");
            }
        } catch (IOException ex) {
            Logger.getLogger(AddOrganisationController.class.getName()).log(Level.SEVERE, "Error Processing image", ex);
            orgForm.setImageError("Error Processing image");
        }

        return new ModelAndView("addOrganisation", "orgForm", orgForm);
    }
    
    @RequestMapping(value="/organisationProcess.html", method=RequestMethod.POST, params="submit")
    public ModelAndView processNewOrganisation(@ModelAttribute("model") MetadataForm metadataForm, @ModelAttribute("org") Organisation org, HttpServletRequest request, @ModelAttribute("orgForm") @Valid AddOrganisationForm orgForm, BindingResult result) {    
        // If we have errors, pass them back to the user, keeping any inputs in 
        // place, need to improve so that errors are displayed next to the
        // correct inputs
        if (result.hasErrors()) {
            for (ObjectError error : result.getAllErrors()) {
                Logger.getLogger(UploadController.class.getName()).log(Level.WARNING, "Error ({0}): {1}", new Object[]{error.getCode(), error.getDefaultMessage()});
            }
            
            orgForm.setLogo("/imageBase/" + orgForm.getLogoBase64() + ".html");
            orgForm.setLogoSmall("/imageBase/" + orgForm.getLogoSmallBase64() + ".html");
            
            return new ModelAndView("addOrganisation", "orgForm", orgForm);
        }
        
        org = new Organisation();
        metadataForm.setStoredOrg(false);

        // Write validated organisation to the database
        EntityManager em = DatabaseConnection.getInstance().createEntityManager();
        em.getTransaction().begin();
        em.persist(orgForm.getOrganisation());
        em.getTransaction().commit();      
        
        metadataForm.getMetadata().setOrganisationID(orgForm.getOrganisation().getId());
        metadataForm.updateOrganisationList();

        return new ModelAndView("redirect:/metadataView.html", "model", metadataForm);
    }
    
    @InitBinder("orgForm")
    protected void initBinder(WebDataBinder binder) {
        binder.setValidator(new AddOrganisationFormValidator(new OrganisationValidator()));
    }
}