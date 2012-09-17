/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.org.nbn.nbnv.importer.ui.controller;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.validation.Valid;
import org.apache.poi.hwpf.HWPFDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import uk.org.nbn.nbnv.importer.ui.metadata.MetadataWriter;
import uk.org.nbn.nbnv.importer.ui.model.Metadata;
import uk.org.nbn.nbnv.importer.ui.model.MetadataForm;
import uk.org.nbn.nbnv.importer.ui.model.SessionData;
import uk.org.nbn.nbnv.importer.ui.model.UploadItem;
import uk.org.nbn.nbnv.importer.ui.util.DatabaseConnection;
import uk.org.nbn.nbnv.importer.ui.validators.MetadataFormValidator;
import uk.org.nbn.nbnv.importer.ui.validators.MetadataValidator;
import uk.org.nbn.nbnv.jpa.nbncore.Organisation;

/**
 *
 * @author Paul Gilbertson
 */
@Controller
public class MetadataController {
    @Autowired SessionData session;
    
    @RequestMapping(value="/metadata.html", method = RequestMethod.GET)
    public ModelAndView metadata() {
        MetadataForm model = new MetadataForm();
        model.setOrganisationList(getOrgList());
        return new ModelAndView("metadataForm", "model", model);
    }

    @RequestMapping(value="/metadataProcess.html", method = RequestMethod.POST, params="addOrganisation")
    public ModelAndView addOrganisation(Metadata metadata, BindingResult result) {
        return new ModelAndView("redirect:/addOrganisation.html");
    }    
    
    @RequestMapping(value="/metadataProcess.html", method = RequestMethod.POST, params="submit")
    public ModelAndView uploadFile(@ModelAttribute("model") @Valid MetadataForm model, BindingResult result, @RequestParam("organisationID") String organisationID) {

        // Quick fix to grab the ID of the organisation and push it in to the 
        // model for processing or return to user to ensure correct option is 
        // re-selected
        model.getMetadata().setOrganisationID(Integer.parseInt(organisationID));
        
        if (result.hasErrors()) {
            for (ObjectError error : result.getAllErrors()) {
                Logger.getLogger(UploadController.class.getName()).log(Level.WARNING, "Error ({0}): {1}", new Object[]{error.getCode(), error.getDefaultMessage()});
                model.getErrors().add(error.getDefaultMessage());
            }
            
            model.setOrganisationList(getOrgList());
            return new ModelAndView("metadataForm", "model", model);
        }


        try {
            File metadataFile = File.createTempFile("nbnimporter", "metadata.xml");
            MetadataWriter mw = new MetadataWriter(metadataFile);
            mw.datasetToEML(model.getMetadata());
            
            session.setMetadata(metadataFile.getAbsolutePath());
            
            return new ModelAndView("upload");
        } catch (Exception ex) {
            Logger.getLogger(MetadataController.class.getName()).log(Level.SEVERE, null, ex);
            model.getErrors().add(ex.getMessage());
        }

        return new ModelAndView("metadataForm", "model", model);
    }

    @RequestMapping(value="/metadata.html", method = RequestMethod.POST)
    public ModelAndView uploadFile(UploadItem uploadItem, BindingResult result) {
        MetadataForm model = new MetadataForm();
        model.setOrganisationList(getOrgList());

        if (result.hasErrors()) {
            for (ObjectError error : result.getAllErrors()) {
                Logger.getLogger(UploadController.class.getName()).log(Level.WARNING, "Error ({0}): {1}", new Object[]{error.getCode(), error.getDefaultMessage()});
                model.getErrors().add(error.getDefaultMessage());
            }
            return new ModelAndView("metadataForm", "model", model);
        }

        List<String> messages = new ArrayList<String>();
        messages.add("Original File name: " + uploadItem.getFileData().getOriginalFilename());
        messages.add("File size: " + Long.toString(uploadItem.getFileData().getSize()));
        messages.add("Content Type: " + uploadItem.getFileData().getContentType());
        messages.add("Storage description: " + uploadItem.getFileData().getStorageDescription());

        
        try {
            //File dFile = File.createTempFile("nbnimporter", "metadata.doc");
            //messages.add("Storage location: " + dFile.getAbsolutePath());
            //uploadItem.getFileData().transferTo(dFile);
            
            HWPFDocument doc = new HWPFDocument(uploadItem.getFileData().getInputStream());
            messages.add("Word document parsing not implemented yet");
            
        } catch (IOException ex) {
            messages.add("EXCEPTION: Parse exception: " + ex.getMessage());
        }

        return new ModelAndView("debug", "messages", messages);
    }
    
    @InitBinder("model")
    protected void initBinder(WebDataBinder binder) {
        binder.setValidator(new MetadataFormValidator(new MetadataValidator()));
    }    
    
    private List<Organisation> getOrgList() {
        EntityManager em = DatabaseConnection.getInstance().createEntityManager();
        
        Query q = em.createNamedQuery("Organisation.findAll");
        return q.getResultList();
    }
}
